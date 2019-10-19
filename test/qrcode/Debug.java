package qrcode;

public class Debug {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		/*
		 * 		MatrixConstructionTest01

		 */
		//MatrixConstructionTest01 test1 = new MatrixConstructionTest01();

		//test1.testInitializePatternsV1();
		
		//test1.testInitializePatternsV4();
		
		//test1.testAddFormatInformationV1M0();
		
		//test1.testAddFormatInformationV4M5();
		
		//test1.testConstructMatrix();
		
		/*
		 * 		MatrixConstructionTest02
		 */
		
		//MatrixConstructionTest02 test2 = new MatrixConstructionTest02();
		
		//test2.testRenderQRCodeMatrixV1();
		
		//test2.testAddDataInformationOnEmptyMat();
		
		//test2.testAddDataInformationWithNoData();
		
		//test2.testAddDataInformation();
		
		int[] data = {34, 13, 52, 2, 9, 201, 10, 0, 59, 100};
		int[] output = DataEncoding.fillSequence(data, 19);
		
		for (int i = 0; i < output.length; i++) {
			System.out.print(output[i]);
			if (i < output.length - 1) {
				System.out.print(", ");
			} else {
				System.out.print('\n');
			}
		}
		
	}

}
