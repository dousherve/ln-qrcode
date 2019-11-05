package qrcode;

public class Main {

	public static final String INPUT =  "EPFL is <3";

	/*
	 * Parameters
	 */
	public static final int VERSION = 1;
	public static final int MASK = 1;
	public static final int SCALING = 20;

	public static void main(String[] args) {

		/*
		 * Encoding
		 */
		boolean[] encodedData = DataEncoding.byteModeEncoding(INPUT, VERSION);
		
		/*
		 * image
		 */
		
		// Hardcoded mask selection
		int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData, MASK);
		
		// Automatic mask
		// int[][] qrCode = MatrixConstruction.renderQRCodeMatrix(VERSION, encodedData);

		/*
		 * Visualization
		 */
		
		Helpers.show(qrCode, SCALING);
		
		// Small GUI
		// QRCodeFrame frame = new QRCodeFrame();
	}

}
