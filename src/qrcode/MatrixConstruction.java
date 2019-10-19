package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both need their alpha component set to 255
	 */
	
	public static final int B = 0xFF_00_00_00;
	public static final int W = 0xFF_FF_FF_FF;

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes
	
	/**
	 * Add a certain pattern to the matrix.
	 *
	 * @param matrix
	 * The matrix on which the modifications will be made
	 * @param pattern
	 * The pattern to add (see enum Patttern)
	 * @param x
	 * x-coordinate of the starting point of the pattern
	 * @param y
	 * y-coordinate of the starting point of the pattern
	 */
	public static void addPattern(int[][] matrix, Pattern pattern, int x, int y) {
		
		switch (pattern) {
			case FINDER:
				for (int i = 0; i < 7; ++i) {
					for (int j = 0; j < 7; ++j) {
						
						if (i == 0 || i == 6 || j == 0 || j == 6) {
							// Outer square (black)
							matrix[i + x][j + y] = B;
						} else if (i >= 2 && i <= 4 && j >= 2 && j <= 4) {
							// Inner square (black)
							matrix[i + x][j + y] = B;
						} else {
							// White otherwise
							matrix[i + x][j + y] = W;
						}
						
					}
				}
				break;
				
			case ALIGNMENT:
				for (int i = 0; i < 5; ++i) {
					for (int j = 0; j < 5; ++j) {
						
						final boolean IS_CENTER = (i == 2 && j == 2);
						if (i == 0 || i == 4 || j == 0 || j == 4 || IS_CENTER) {
							matrix[i + x][j + y] = B;
						} else {
							matrix[i + x][j + y] = W;
						}
						
					}
				}
				break;
			case TIMING:
				for (int i = 8; i < matrix.length - 8; ++i) {
					matrix[i][6] = (i % 2 == 0) ? B : W;
				}
				
				for (int i = 8; i < matrix.length - 8; ++i) {
					matrix[6][i] = (i % 2 == 0) ? B : W;
				}
				break;
				
			default:
				break;
		}
		
	}
	
	/**
	 * Add a certain pattern to the matrix.
	 *
	 * @param matrix
	 * The matrix on which the modifications will be made
	 * @param pattern
	 * The pattern to add
	 * @param coord
	 * The coordinate on both axes of the starting point of the pattern
	 */
	public static void addPattern(int[][] matrix, Pattern pattern, int coord) {
		addPattern(matrix, pattern, coord, coord);
	}
	
	/**
	 * Add a horizontal separator for the Finder patterns.
	 *
	 * @param matrix
	 * The matrix on which the modifications will be made
	 * @param x
	 * x-coordinate of the starting point of the separator
	 * @param y
	 * y-coordinate of the starting point of the separator
	 */
	public static void addHorizontalSeparator(int[][] matrix, int x, int y) {
		for (int i = 0; i < 8; ++i) {
			matrix[x + i][y] = W;
		}
	}
	
	/**
	 * Add a vertical separator for the Finder patterns.
	 *
	 * @param matrix
	 * The matrix on which the modifications will be made
	 * @param x
	 * x-coordinate of the starting point of the separator
	 * @param y
	 * y-coordinate of the starting point of the separator
	 */
	public static void addVerticalSeparator(int[][] matrix, int x, int y) {
		for (int i = 0; i < 8; ++i) {
			matrix[x][y + i] = W;
		}
	}

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		int[][] matrix = initializeMatrix(version);
		addFinderPatterns(matrix);
		addAlignmentPatterns(matrix, version);
		addTimingPatterns(matrix);
		
		return matrix;
	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {
		
		final int SIZE = QRCodeInfos.getMatrixSize(version);
		int[][] matrix = new int[SIZE][SIZE];
		
		for (int i = 0; i < SIZE; ++i) {
			for (int j = 0; j < SIZE; ++j) {
				matrix[i][j] = 0;
			}
		}
		
		return matrix;
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		
		final int PATTERN_SIZE = 7;
		final int PATTERN_OFFSET = matrix.length - 7;
		final int SEPARATOR_OFFSET = matrix.length - 8;
		
		// Top Left
		addPattern(matrix, Pattern.FINDER, 0);
		addHorizontalSeparator(matrix, 0, PATTERN_SIZE);
		addVerticalSeparator(matrix, PATTERN_SIZE, 0);
		
		// Bottom Left
		addPattern(matrix, Pattern.FINDER, 0, PATTERN_OFFSET);
		addHorizontalSeparator(matrix, 0, SEPARATOR_OFFSET);
		addVerticalSeparator(matrix, PATTERN_SIZE, SEPARATOR_OFFSET);
		
		// Top Right
		addPattern(matrix, Pattern.FINDER, PATTERN_OFFSET, 0);
		addHorizontalSeparator(matrix, SEPARATOR_OFFSET, PATTERN_SIZE);
		addVerticalSeparator(matrix, SEPARATOR_OFFSET, 0);
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		
		if (version > 1) {
			final int START_COORD = matrix.length - 9;
			addPattern(matrix, Pattern.ALIGNMENT, START_COORD);
		}
		
	}

	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
		addPattern(matrix, Pattern.TIMING, 0);
	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		// TODO Implementer
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
		// TODO Implementer
	}

	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		// TODO Implementer
		return 0;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		// TODO Implementer

	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
	
		return 0;
	}

}
