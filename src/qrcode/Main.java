package qrcode;

public class Main {

	public static final String INPUT =  "EPFL is <3";

	/*
	 * Parameters
	 */
	public static final int VERSION = 2;
	public static final int MASK = 5;
	public static final int SCALING = 40;

	public static void main(String[] args) {

		/*
		 * Encoding
		 */
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);
		
		/*
		 * image
		 */
		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData, MASK);

		/*
		 * Visualization
		 */
		Helpers.show(qrCode, SCALING);
	}

}
