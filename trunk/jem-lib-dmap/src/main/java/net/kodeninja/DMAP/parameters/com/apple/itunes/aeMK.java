package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: com.apple.itunes.mediakind
 */
public class aeMK extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.mediakind";

	/**
	 * Constant value denoting the media as audio.
	 */
	public static final byte MEDIA_KIND_AUDIO = 1;
	
	/**
	 * Constant value denoting the media as video.
	 */
	public static final byte MEDIA_KIND_VIDEO = 2;
	
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeMK() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeMK(byte value) {
		super(PARAM_NAME, value);
	}
}

