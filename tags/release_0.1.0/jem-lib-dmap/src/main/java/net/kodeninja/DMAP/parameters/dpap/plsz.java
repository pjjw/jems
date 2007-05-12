package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPLongParameter;

/**
 * DPAP.ImageLargeFileSize
 * The filesize of the image as a long.
 * Note: Not sure how this is viable since the pfdt tag can only
 * be as large as an integer allows?
 * @author Charles Ikeson
 *
 */
public class plsz extends DMAPLongParameter {
	private static final String PARAM_NAME = "DPAP.ImageLargeFileSize";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public plsz() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter using the passed value.
	 * @param Filesize The filesize of the image, as a long.
	 */
	public plsz(long Filesize) {
		super(PARAM_NAME, Filesize);
	}

}
