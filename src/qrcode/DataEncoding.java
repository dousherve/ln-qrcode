package qrcode;

import java.nio.charset.StandardCharsets;

import reedsolomon.ErrorCorrectionEncoding;

import static qrcode.Util.toInt;

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
		
	    final int MAX_CHAR_COUNT = QRCodeInfos.getMaxInputLength(version);
	    final int MAX_BYTE_COUNT = QRCodeInfos.getCodeWordsLength(version);
	    final int ECC_COUNT = QRCodeInfos.getECCLength(version);
	    
	    final int[] ENCODED_STRING = encodeString(input, MAX_CHAR_COUNT);
	    final int[] ENCODED_WITH_INFOS = addInformations(ENCODED_STRING);
	    final int[] ENCODED_PADDED = fillSequence(ENCODED_WITH_INFOS, MAX_BYTE_COUNT);
	    final int[] ENCODED_PADDED_ECC = addErrorCorrection(ENCODED_PADDED, ECC_COUNT);
	 
		return bytesToBinaryArray(ENCODED_PADDED_ECC);
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return An array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	public static int[] encodeString(String input, int maxLength) {
		
		byte[] tabByte = input.getBytes(StandardCharsets.ISO_8859_1);
		
		// The length of the "encoded" array is the smallest value
		// between tabByte's length and the maxLength parameter.
		final int ENCODED_LENGTH = Math.min(tabByte.length, maxLength);
		int[] encoded = new int[ENCODED_LENGTH];
		
		for (int i = 0; i < ENCODED_LENGTH; ++i) {
			encoded[i] = toInt(tabByte[i]);
		}
		
		return encoded;
	}

	/**
	 * Add the 16 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {
	 
		final int BYTE_MODE_FLAG = 0b0100;
		final int INPUT_BYTES_LENGTH = inputBytes.length;
		final int DATA_LENGTH = INPUT_BYTES_LENGTH + 2;
		
		int[] data = new int[DATA_LENGTH];
		
		// Fill the first byte (byte mode flag + 4 MSB of the length of the data)
		data[0] = (BYTE_MODE_FLAG << 4) | ((INPUT_BYTES_LENGTH & 0xF0) >> 4);
		// Fill the second byte (4 LSB of the length of the data + 4 MSB of the first data byte)
		data[1] = ((INPUT_BYTES_LENGTH & 0x0F) << 4) | ((inputBytes[0] & 0xF0) >> 4);
		// Fill the data from the third byte to the (DATA_LENGTH - 1)-th byte
        for (int i = 2; i < DATA_LENGTH - 1; ++i) {
            // We offset i by -2 and -1 because of the header which consists of ~2 bytes
            data[i] = ((inputBytes[i - 2] & 0x0F) << 4) | ((inputBytes[i - 1] & 0xF0) >> 4);
        }
        // Fill the last byte and shift left to add the '0000' end sequence
        data[DATA_LENGTH - 1] = (inputBytes[INPUT_BYTES_LENGTH - 1] & 0x0F) << 4;
		
		return data;
	}

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		
	    final int ENCODED_LENGTH = encodedData.length;
	    
	    if (finalLength <= ENCODED_LENGTH) {
	        return encodedData;
        }
	    
	    int[] paddedData = new int[finalLength];
	    System.arraycopy(encodedData, 0, paddedData, 0, ENCODED_LENGTH);
	    
        for (int i = 0; i < finalLength - ENCODED_LENGTH; ++i) {
            int index = i + ENCODED_LENGTH;
            paddedData[index] = (i % 2 == 0) ? 236 : 17;
        }
	    
		return paddedData;
	}

	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		
	    final int ENCODED_LENGTH = encodedData.length;
	    
	    int[] dataWithCorrection = new int[ENCODED_LENGTH + eccLength];
	    System.arraycopy(encodedData, 0, dataWithCorrection, 0, ENCODED_LENGTH);
	    
	    int[] correctionBytes = ErrorCorrectionEncoding.encode(encodedData, eccLength);
        
        for (int i = ENCODED_LENGTH; i < dataWithCorrection.length; ++i) {
            dataWithCorrection[i] = correctionBytes[i - ENCODED_LENGTH];
        }
	 
		return dataWithCorrection;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) {
        
        boolean[] binaryArray = new boolean[data.length * 8];
        
        for (int i = 0; i < data.length; ++i) {
            int mask = 0b1000_0000;
            for (int j = 0; j < 8; ++j) {
                binaryArray[8 * i + j] = (data[i] & mask) == mask;
                mask >>= 1;
            }
        }
	 
		return binaryArray;
	}

}
