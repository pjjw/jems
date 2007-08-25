package net.kodeninja.jem.server.UPnP.description.search;

import java.util.LinkedList;

import net.kodeninja.UPnP.internal.control.ControlException;
import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;

public class SearchExpression implements Terminal {

	private static final int MODE_INITIAL = -1;
	private static final int MODE_PROPERTY = 0;
	private static final int MODE_OPERATOR = 1;
	private static final int MODE_VALUE = 2;
	private static final int MODE_LOGIC_OPERATOR = 3; 

	private Terminal topLevelOp = null;
	private int start;
	private int end;
	
	public SearchExpression(String expression) throws ControlException {
		this(expression, 0);
	}

	private SearchExpression(String expression, int offset) throws ControlException {
		int pos = offset;
		int mode = MODE_INITIAL;

		String property = "", op = "", value = "";
		LinkedList<Terminal> terminals = new LinkedList<Terminal>();

		start = offset;
		
		//Step 1: Tokenize & Compound operations
		tokenLoop:
			while (pos < expression.length()) {
				if (isSpace(expression.charAt(pos))) {
					pos++;
					continue;
				}
				mode++;

				StringBuffer buf = new StringBuffer();
				switch (mode) {
				case MODE_PROPERTY:
					if (expression.charAt(pos) == '(') {
						SearchExpression exp;
						terminals.add(exp = new SearchExpression(expression, pos + 1));
						pos = exp.rangeEnd() + 1;
						mode = MODE_VALUE;
					}
					else {
						while ((pos < expression.length()) && (isSpace(expression.charAt(pos)) == false)) {
							buf.append(expression.charAt(pos));
							pos++;
						}
						property = buf.toString();
						pos++;
					}
					break;
				case MODE_OPERATOR:
					while ((pos < expression.length()) && (isSpace(expression.charAt(pos)) == false)) {
						buf.append(expression.charAt(pos));
						pos++;
					}
					op = buf.toString().toLowerCase();
					pos++;
					break;
				case MODE_VALUE:
					if (expression.charAt(pos) == '"') {
						char curChar = expression.charAt(pos);
						char lastChar;
						do {
							if (pos >= expression.length())
								break;
							pos++;
							lastChar = curChar;
							curChar = expression.charAt(pos);
							buf.append(curChar);
						} while ((curChar != '"') || (lastChar == '\\'));
						buf.deleteCharAt(buf.length() - 1);
						value = buf.toString();
						pos++;
					}
					else if ((pos + 4 <= expression.length()) && (expression.substring(pos, pos + 4).equalsIgnoreCase("true"))) {
						value = "true";
						pos += 4;
					}
					else if ((pos + 5 <= expression.length()) && (expression.substring(pos, pos + 5).equalsIgnoreCase("false"))) {
						value = "false";
						pos += 5;
					}
					else 
						throw new ControlException(708, "Unsupported or invalid search critera");
					terminals.add(new OperationExpression(property, op, value));
					property = "";
					op = "";
					value = "";
					break;
				case MODE_LOGIC_OPERATOR:
					if (expression.charAt(pos) == ')')
						break tokenLoop;

					while ((pos < expression.length()) && (isSpace(expression.charAt(pos)) == false)) {
						buf.append(expression.charAt(pos));
						pos++;
					}
					terminals.add(new TemporaryExpression(buf.toString()));
					mode = MODE_INITIAL;
					pos++;
					break;
				}
			}

		end = pos;
		
		//Step 2: Compound ANDs
		Terminal op1 = null, logicOp = null;
		Terminal termArray[] = new Terminal[terminals.size()];
		terminals.toArray(termArray);
		terminals.clear();
		for (int i = 0; i < termArray.length; i++) {
			if (op1 == null)
				op1 = termArray[i];
			else if (logicOp == null) {
				if (((TemporaryExpression)termArray[i]).getValue().equalsIgnoreCase("and"))
					logicOp = termArray[i];
				else { // Its an OR op so add the previous 
					terminals.add(op1);
					terminals.add(termArray[i]);
					op1 = null;
				}
			}
			else {
				op1 = new AndOperationExpression(op1, termArray[i]);
				logicOp = null;
			}
		}
		terminals.add(op1);

		//Step 3: Compound ORs
		op1 = null;
		logicOp = null;
		termArray = new Terminal[terminals.size()];
		terminals.toArray(termArray);
		terminals.clear();
		for (int i = 0; i < termArray.length; i++) {
			if (op1 == null)
				op1 = termArray[i];
			else if (logicOp == null) {
				logicOp = termArray[i];
			}
			else {
				op1 = new OrOperationExpression(op1, termArray[i]);
			}
		}
		topLevelOp = op1;
	}

	private boolean isSpace(char c) {
		return (c == 0x20) || ((c >= 0x09) && (c <= 0x0D));
	}

	public int rangeStart() {
		return start;
	}
	
	public int rangeEnd() {
		return end;
	}
	
	public boolean evaluate(MediaTree mt) {
		if (topLevelOp == null)
			return false;
		else
			return topLevelOp.evaluate(mt);
	}

	public String toString() {
		return "(" + topLevelOp + ")";
	}
	
}
