package qrcode;

public class Main {

	public static final String INPUT =  "This quick refactoring seems to work...";

	/*
	 * Parameters
	 */
	public static final int VERSION = 4;
	public static final int MASK = 7;
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
