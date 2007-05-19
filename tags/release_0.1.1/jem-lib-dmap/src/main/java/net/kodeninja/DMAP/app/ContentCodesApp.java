package net.kodeninja.DMAP.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import net.kodeninja.DMAP.ParameterFactory;
import net.kodeninja.DMAP.ParameterLinkedList;
import net.kodeninja.DMAP.DataTypes.DMAPParameter;
import net.kodeninja.DMAP.parameters.dmap.mccr;
import net.kodeninja.DMAP.parameters.dmap.mcna;
import net.kodeninja.DMAP.parameters.dmap.mcnm;
import net.kodeninja.DMAP.parameters.dmap.mcty;
import net.kodeninja.DMAP.parameters.dmap.mdcl;

/**
 * This program takes a content-codes file downloaded off of a DMAP based
 * server and creates java classes off of the entries.
 * @author Charles Ikeson
 *
 */
class ContentCodesApp {
	/**
	 * Prints the specified tag to stdout.
	 * @param paramTag The 4 char name of the tag, encoded as an integer.
	 * @param paramName The human readable tag.
	 * @param paramType The data type of the tag.
	 */
	private static void printTag(mcnm paramTag, mcna paramName, mcty paramType) {
		System.out.println("[" + paramTag + "] (" + paramType + ") "
		                   + paramName);
	}

	/**
	 * Returns the class name equivalent for the passed data type code.
	 * @param type The data type code.
	 * @return The class name of the passed type.
	 */
	private static String getDMAPType(int type) {
		switch (type) {
			case DMAPParameter.DATATYPE_BYTE:
				return "DMAPByteParameter";
			case DMAPParameter.DATATYPE_DATE:
				return "DMAPDateParameter";
			case DMAPParameter.DATATYPE_INT:
				return "DMAPIntParameter";
			case DMAPParameter.DATATYPE_LIST:
				return "DMAPListParameter";
			case DMAPParameter.DATATYPE_LONG:
				return "DMAPLongParameter";
			case DMAPParameter.DATATYPE_SHORT:
				return "DMAPShortParameter";
			case DMAPParameter.DATATYPE_STRING:
				return "DMAPStringParameter";
			case DMAPParameter.DATATYPE_UBYTE:
				return "DMAPUByteParameter";
			case DMAPParameter.DATATYPE_UINT:
				return "DMAPUIntParameter";
			case DMAPParameter.DATATYPE_ULONG:
				return "DMAPULongParameter";
			case DMAPParameter.DATATYPE_USHORT:
				return "DMAPUShortParameter";
			case DMAPParameter.DATATYPE_VERSION:
				return "DMAPVersionParameter";
		}
		System.err.println("Type: Unknown type: " + type);
		return "DMAPParameter";
	}

	/**
	 * Returns the passed parameter types based on the provide type code. 
	 * @param type The type code.
	 * @return The string represent the procedure's passed type.
	 */
	private static String getDMAPParams(int type) {
		switch (type) {
			case DMAPParameter.DATATYPE_UBYTE:
			case DMAPParameter.DATATYPE_BYTE:
				return "byte value";
			case DMAPParameter.DATATYPE_UINT:
			case DMAPParameter.DATATYPE_INT:
				return "int value";
			case DMAPParameter.DATATYPE_ULONG:
			case DMAPParameter.DATATYPE_LONG:
				return "long value";
			case DMAPParameter.DATATYPE_USHORT:
			case DMAPParameter.DATATYPE_SHORT:
				return "short value";
			case DMAPParameter.DATATYPE_DATE:
				return "Date value";
			case DMAPParameter.DATATYPE_STRING:
				return "String value";
			case DMAPParameter.DATATYPE_VERSION:
				return "short major, short minor";
		}
		System.err.println("Param: Unknown type: " + type);
		return "";
	}

	/**
	 * Returns the argument variables based on the passed data type.
	 * @param type The data type.
	 * @return Argument variables based on data type.
	 */
	private static String getDMAPArgs(int type) {
		if (type == DMAPParameter.DATATYPE_VERSION)
			return "major, minor";
		else
			return "value";
	}

	/**
	 * Generate code based off of the passed tag.
	 * @param paramTag The 4 char name of the tag, encoded as an integer.
	 * @param paramName The human readable tag.
	 * @param paramType The data type of the tag.
	 */
	private static void generateCode(mcnm paramTag, mcna paramName,
			mcty paramType) {
		String pack = "net.kodeninja.DMAP.parameters." + paramName.toString();
		pack = pack.substring(0, pack.lastIndexOf("."));
		String path = pack.replace('.', '/') + "/" + paramTag.toString()
		+ ".java";
		File f = new File(path);

		try {
			if (f.exists() == true)
				return;

			f.getParentFile().mkdirs();

			OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
			PrintStream p = new PrintStream(os);

			p.println("package " + pack + ";");
			p.println();
			p.println("import net.kodeninja.DMAP.DataTypes." + getDMAPType(paramType.getValue()) + ";");
			if (paramType.getValue() == DMAPParameter.DATATYPE_DATE)
				p.println("import java.util.Date;");
			p.println();
			p.println("/**");
			p.println(" * Generated class for parameter: " + paramName);
			p.println(" */");
			p.println("public class " + paramTag + " extends " + getDMAPType(paramType.getValue()) + " {");
			p.println("\t/**");
			p.println("\t * Parameter Type");
			p.println("\t */");
			p.println("\tprivate static final String PARAM_NAME = \"" + paramName + "\";");
			p.println();
			p.println("\t/**");
			p.println("\t * Default constructor used when reading tags from a stream.");
			p.println("\t */");
			p.println("\tpublic " + paramTag + "() {");
			p.println("\t\tsuper(PARAM_NAME);");
			p.println("\t}");
			if (paramType.getValue() != DMAPParameter.DATATYPE_LIST) {
				p.println();
				p.println("\t/**");
				p.println("\t * Constructor used to create the tag.");
				if (paramType.getValue() == DMAPParameter.DATATYPE_VERSION) {
					p.println("\t * @param major The major number of the version number");
					p.println("\t * @param minor The minor number of the version number");
				}
				else
					p.println("\t * @param value The value of the parameter.");
				p.println("\t */");
				p.println("\tpublic " + paramTag + "(" + getDMAPParams(paramType.getValue()) + ") {");
				p.println("\t\tsuper(PARAM_NAME, " + getDMAPArgs(paramType.getValue()) + ");");
				p.println("\t}");
			}
			p.println("}");
			p.println();
			p.flush();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Private variable to store the temporary OutputStream for
	 * building the content-codes response java class.
	 */
	private static OutputStream ccOS = null;

	/**
	 * Private variable to store the temporary PrintStream for
	 * building the content codes response java class. 
	 */
	private static PrintStream ccP = null;

	/**
	 * Prepares the content codes response java class.
	 */
	private static void startCC() {
		try {
			ccOS = new BufferedOutputStream(new FileOutputStream(
			"ContentCodesURI.java"));
			ccP = new PrintStream(ccOS);
			ccP.println("package net.kodeninja.jem.server.DMAP.responses;");
			ccP.println();
			ccP.println("import net.kodeninja.DMAP.DataTypes.DMAPParameter;");
			ccP.println("import net.kodeninja.DMAP.parameters.dmap.mccr;");
			ccP.println("import net.kodeninja.DMAP.parameters.dmap.mcna;");
			ccP.println("import net.kodeninja.DMAP.parameters.dmap.mcnm;");
			ccP.println("import net.kodeninja.DMAP.parameters.dmap.mcty;");
			ccP.println("import net.kodeninja.DMAP.parameters.dmap.mdcl;");
			ccP.println("import net.kodeninja.jem.server.DMAP.DMAPService;");
			ccP.println();
			ccP.println("public class ContentCodesURI extends DMAPURI {");
			ccP.println();
			ccP.println("\tpublic ContentCodesURI(DMAPService s) {");
			ccP.println("\t\tsuper(s, \"/content-codes\");");
			ccP.println();
			ccP.println("\t\tmccr root = new mccr();");
			ccP.println("\t\tbody.addParameter(root);");
			ccP.println();
			ccP.println("\t\t// Status Tag");
			ccP.println("\t\troot.addParameter(statusCode);");
			ccP.println();
			ccP.println("\t\t// Temporary var");
			ccP.println("\t\tmdcl dict = null;");
			ccP.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the enumeration data type based on the passed parameter type.
	 * @param type The parameter type.
	 * @return The enumaration data type.
	 */
	private static String getEnumType(int type) {
		switch (type) {
			case DMAPParameter.DATATYPE_BYTE:
				return "DMAPParameter.DATATYPE_BYTE";
			case DMAPParameter.DATATYPE_DATE:
				return "DMAPParameter.DATATYPE_DATE";
			case DMAPParameter.DATATYPE_INT:
				return "DMAPParameter.DATATYPE_INT";
			case DMAPParameter.DATATYPE_LIST:
				return "DMAPParameter.DATATYPE_LIST";
			case DMAPParameter.DATATYPE_LONG:
				return "DMAPParameter.DATATYPE_LONG";
			case DMAPParameter.DATATYPE_SHORT:
				return "DMAPParameter.DATATYPE_SHORT";
			case DMAPParameter.DATATYPE_STRING:
				return "DMAPParameter.DATATYPE_STRING";
			case DMAPParameter.DATATYPE_UBYTE:
				return "DMAPParameter.DATATYPE_UBYTE";
			case DMAPParameter.DATATYPE_UINT:
				return "DMAPParameter.DATATYPE_UINT";
			case DMAPParameter.DATATYPE_ULONG:
				return "DMAPParameter.DATATYPE_ULONG";
			case DMAPParameter.DATATYPE_USHORT:
				return "DMAPParameter.DATATYPE_USHORT";
			case DMAPParameter.DATATYPE_VERSION:
				return "DMAPParameter.DATATYPE_VERSION";
		}
		return "DMAPParameter.DATATYPE_UNKNOWN";
	}

	/**
	 * Adds new entry to the content codes files.
	 * @param paramTag The 4 char name of the tag, encoded as an integer.
	 * @param paramName The human readable tag.
	 * @param paramType The data type of the tag.
	 */
	private static void appendCC(mcnm paramTag, mcna paramName, mcty paramType) {
		ccP.println("\t\t");
		ccP.println("\t\t// Dictonary Entry for " + paramName.toString());
		ccP.println("\t\tdict = new mdcl();");
		ccP.println("\t\troot.addParameter(dict);");
		ccP.println("\t\tdict.addParameter(new mcna(\"" + paramName.toString()
		            + "\"));");
		ccP.println("\t\tdict.addParameter(new mcnm(\"" + paramTag.toString()
		            + "\"));");
		ccP.println("\t\tdict.addParameter(new mcty("
		            + getEnumType(paramType.getValue()) + "));");
	}

	/**
	 * Finishes the content codes java class.
	 */
	private static void endCC() {
		try {
			ccP.println("\t}");
			ccP.println();
			ccP.println("}");
			ccP.println();
			ccP.flush();
			ccOS.flush();
			ccOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Makes sure the tag is valid and can be translated into a java class.
	 * @param paramTag The tag.
	 * @return True if the tag is valid.
	 */
	private static boolean isValidTag(mcnm paramTag) {
		for (char c : paramTag.toString().toCharArray())
			if ((((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) == false)
				return false;
		return true;
	}

	/**
	 * The program.
	 *   Arguments:
	 *     [--print] [--generateCode] [--generateCC] content-codes
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream is;
		ParameterLinkedList paramList = new ParameterLinkedList();

		boolean optionPrint = false;
		boolean optionGenerateCodes = false;
		boolean optionGenerateCC = false;

		if (args.length == 0) {
			System.err.println("Error: Expected path or URL to content-codes file.");
			System.exit(-1);
		}

		String path = "";
		boolean noMoreOptions = false;
		for (String arg : args)
			if (noMoreOptions)
				path += " " + arg;
			else if (arg.equals("-"))
				noMoreOptions = true;
			else if (arg.startsWith("--")) {
				if (arg.equals("--print"))
					optionPrint = true;
				else if (arg.equals("--generateCode"))
					optionGenerateCodes = true;
				else if (arg.equals("--generateCC"))
					optionGenerateCC = true;
				else
					System.err.println("Warning: Unknown option " + arg);
			} else if (arg.startsWith("-")) {
				for (char c : arg.toCharArray())
					if (c == 'p')
						optionPrint = true;
					else if (c == 'g')
						optionGenerateCodes = true;
					else if (c == 'c')
						optionGenerateCC = true;
					else if (c != '-')
						System.err.println("Warning: Unknown option -" + c);
			} else {
				path = arg;
				noMoreOptions = true;
			}
		if (path.equals(""))
			path = args[args.length - 1];

		try {
			try {
				URL tmpURL = new URL(path);
				is = new BufferedInputStream(tmpURL.openStream());
			} catch (MalformedURLException e) {
				File tmpFile = new File(path);
				if (tmpFile.exists() == false) {
					System.err.println("Error: Invalid path: " + path);
					System.exit(-1);
				}
				is = new BufferedInputStream(new FileInputStream(tmpFile));
			}

			ParameterFactory.readFromStream(paramList, is, -1);

			mccr base = (mccr) paramList.findParamByTag("mccr", true);
			if (base == null)
				throw new IOException("Error: Invalid content-codes file.");

			if (optionGenerateCC)
				startCC();

			Iterator<DMAPParameter> it = base.getParameters();
			while (it.hasNext()) {
				DMAPParameter baseParam = it.next();
				if (baseParam instanceof mdcl) {
					mdcl dictonary = (mdcl) baseParam;
					mcnm paramTag = null;
					mcna paramName = null;
					mcty paramType = null;
					Iterator<DMAPParameter> dictIt = dictonary.getParameters();
					while (dictIt.hasNext()) {
						DMAPParameter param = dictIt.next();
						if (param instanceof mcnm)
							paramTag = (mcnm) param;
						else if (param instanceof mcna)
							paramName = (mcna) param;
						else if (param instanceof mcty)
							paramType = (mcty) param;
						else
							throw new IOException(
							"Error: Unexpected tag found.");

						if ((paramTag != null) && (paramName != null)
								&& (paramType != null)) {

							if (isValidTag(paramTag)) {
								if (optionPrint)
									printTag(paramTag, paramName, paramType);

								if (optionGenerateCodes)
									generateCode(paramTag, paramName, paramType);

								if (optionGenerateCC)
									appendCC(paramTag, paramName, paramType);
							}

							paramTag = null;
							paramName = null;
							paramType = null;
						}
					}
				}
			}

			if (optionGenerateCC)
				endCC();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
