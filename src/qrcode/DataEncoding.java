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
		// TODO Implementer
		return null;
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
		
		for (int i = 0; i < ENCODED_LENGTH; i++) {
			encoded[i] = toInt(tabByte[i]);
		}
		
		return encoded;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {
		
		final int BYTE_MODE_FLAG = 0b0100 << 4;
		final int INPUT_BYTES_LENGTH = inputBytes.length;
		
		final int DATA_LENGTH = INPUT_BYTES_LENGTH + 2;
		int[] data = new int[DATA_LENGTH];
		
		// First byte
		data[0] = BYTE_MODE_FLAG | (INPUT_BYTES_LENGTH & 0xF0);
		// Second byte
		data[1] = ((INPUT_BYTES_LENGTH & 0x0F) << 4) | ((inputBytes[0] & 0xF0) >> 4);
		// Fill the data from the third byte to the (DATA_LENGTH - 1)-th byte
        for (int i = 2; i < DATA_LENGTH - 1; i++) {
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
		// TODO Implementer
		return null;
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
		// TODO Implementer
		return null;
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
		// TODO Implementer
		return null;
	}

}
