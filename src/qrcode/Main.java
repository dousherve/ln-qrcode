package qrcode;

public class Main {

	public static final String INPUT =  "0xYolo";

	/*
	 * Parameters
	 */
	public static final int VERSION = 1;
	public static final int MASK = 1;
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
		
		//QRCodeFrame frame = new QRCodeFrame();
	}

}
