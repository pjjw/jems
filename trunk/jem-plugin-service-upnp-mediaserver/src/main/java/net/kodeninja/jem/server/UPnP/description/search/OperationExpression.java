package net.kodeninja.jem.server.UPnP.description.search;

import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;
import net.kodeninja.jem.server.UPnP.description.internal.MediaTreeAttribute;

public class OperationExpression implements Terminal {

	private enum OperationTypes {
		EXISTS, CONTAINS, DOES_NOT_CONTAIN, DERIVED_FROM,
		EQUAL, NOT_EQUAL, LESS_THAN, LESS_EQUAL_THAN,
		GREATER_THAN, GREATER_EQUAL_THAN, UNKNOWN
	}

	private String prop;
	private OperationTypes op;
	private String value;

	public OperationExpression(String prop, String opStr, String value) {
		this.prop = prop;
		this.value = value;

		opStr = opStr.toLowerCase();
		if (opStr.equals("exists"))
			op = OperationTypes.EXISTS;
		else if (opStr.equals("contains"))
			op = OperationTypes.CONTAINS;
		else if (opStr.equals("doesnotcontain"))
			op = OperationTypes.DOES_NOT_CONTAIN;
		else if (opStr.equals("derivedfrom"))
			op = OperationTypes.DERIVED_FROM;
		else if (opStr.equals("="))
			op = OperationTypes.EQUAL;
		else if (opStr.equals("!="))
			op = OperationTypes.NOT_EQUAL;
		else if (opStr.equals("<"))
			op = OperationTypes.LESS_THAN;
		else if (opStr.equals("<="))
			op = OperationTypes.LESS_EQUAL_THAN;
		else if (opStr.equals(">"))
			op = OperationTypes.GREATER_THAN;
		else if (opStr.equals(">="))
			op = OperationTypes.GREATER_EQUAL_THAN;
		else
			op = OperationTypes.UNKNOWN;
	}

	public boolean evaluate(MediaTree mt) {
		switch (op) {
		case EXISTS:
			boolean exists = false;
			for (MediaTreeAttribute mtAttr: mt.getAttributes())
				if (mtAttr.getName().equalsIgnoreCase(prop)) {
					exists = true;
					break;
				}
			return (value.equals("true") ? exists : !exists);
		case CONTAINS:
		case DOES_NOT_CONTAIN:
		case DERIVED_FROM:
			for (MediaTreeAttribute mtAttr: mt.getAttributes())
				if (mtAttr.getName().equalsIgnoreCase(prop)) {
					switch (op) {
					case CONTAINS:
						if (mtAttr.getValue().toString().contains(value))
							return true;
						break;
					case DOES_NOT_CONTAIN:
						if (mtAttr.getValue().toString().contains(value))
							return false;
						break;
					case DERIVED_FROM:
						if (mtAttr.getValue().toString().startsWith(value))
							return true;
						break;
					}
				}
			return (op == OperationTypes.DOES_NOT_CONTAIN ? true : false);
		case EQUAL:
		case NOT_EQUAL:
		case LESS_THAN:
		case LESS_EQUAL_THAN:
		case GREATER_THAN:
		case GREATER_EQUAL_THAN:
			float right;
			boolean nonNumber = false;
			try {
				right = Float.parseFloat(value);
			}
			catch (NumberFormatException e) {
				nonNumber = true;
				right = 0;
			}

			for (MediaTreeAttribute mtAttr: mt.getAttributes())
				if (mtAttr.getName().equalsIgnoreCase(prop)) {
					float left;

					if (nonNumber == false) {
						try {
							left = Float.parseFloat(mtAttr.getValue().toString());
						} catch (NumberFormatException e) {
							continue;
						}
					}
					else {
						// Allow for strings to be compared via =,!=,<,<=,>,>=
						left = mtAttr.getValue().toString().compareTo(value);
					}

					switch (op) {
					case EQUAL:
						if (left == right)
							return true;
						break;
					case NOT_EQUAL:
						if (left != right)
							return true;
						break;
					case LESS_THAN:
						if (left < right)
							return true;
						break;
					case LESS_EQUAL_THAN:
						if (left <= right)
							return true;
						break;
					case GREATER_THAN:
						if (left > right)
							return true;
						break;
					case GREATER_EQUAL_THAN:
						if (left >= right)
							return true;
						break;
					}
				}
			return false;
		case UNKNOWN: 
		default:
			return false;
		}
	}

	public String toString() {
		return "(" + prop + " " + op + " \"" + value + "\")";
	}
}
