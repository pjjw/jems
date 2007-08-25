package net.kodeninja.UPnP.internal.control;

public class ControlException extends Exception {

	private static final long serialVersionUID = -780282128705529523L;

	private int code;
	private String desc;
	
	public ControlException(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public ControlException(int code, String desc, String message) {
		super(message);
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return desc;
	}

}
