package qrcode;

import static qrcode.Util.*;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = false integer for ARGB
	 * 
	 * B = true integer for ARGB
	 * 
	 * both need their alpha component set to 255
	 */
	
	public static final int B = 0xFF_00_00_00;
	public static final int W = 0xFF_FF_FF_FF;

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
		addDarkModule(matrix);
		addFormatInformation(matrix, mask);
		
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
	 * Add a certain pattern to the matrix.
	 *
	 * @param matrix
	 * The matrix on which the modifications will be made
	 * @param pattern
	 * The pattern to add (see enum Pattern)
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
						final int X = i + x;
						final int Y = j + y;
						
						if (i == 0 || i == 6 || j == 0 || j == 6) {
							// Outer square (true)
							matrix[X][Y] = B;
						} else if (i >= 2 && i <= 4 && j >= 2 && j <= 4) {
							// Inner square (true)
							matrix[X][Y] = B;
						} else {
							// false otherwise
							matrix[X][Y] = W;
						}
					}
				}
				break;
			
			case ALIGNMENT:
				for (int i = 0; i < 5; ++i) {
					for (int j = 0; j < 5; ++j) {
						final int X = i + x;
						final int Y = j + y;
						
						final boolean IS_CENTER = (i == 2 && j == 2);
						if (i == 0 || i == 4 || j == 0 || j == 4 || IS_CENTER) {
							matrix[X][Y] = B;
						} else {
							matrix[X][Y] = W;
						}
					}
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
	 * Add all finder patterns to the given matrix with a border of false modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		
		final int PATTERN_SIZE = 7;
		final int PATTERN_OFFSET = matrix.length - PATTERN_SIZE;
		final int SEPARATOR_OFFSET = PATTERN_OFFSET - 1;
		
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
		
		final int STOP_INDEX = matrix.length - 8;
		// Horizontal line on the 6th row
		// Starts at 8th column, ends 8 cols before the end
		for (int i = 8; i < STOP_INDEX; ++i) {
			matrix[i][6] = (i % 2 == 0) ? B : W;
		}
		
		// Vertical line on the 6th column
		// Starts at 8th row, ends 8 rows before the end
		for (int j = 8; j < STOP_INDEX; ++j) {
			matrix[6][j] = (j % 2 == 0) ? B : W;
		}
	}

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		matrix[8][matrix.length - 8] = B;
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
		
		boolean[] formatSequence = QRCodeInfos.getFormatSequence(mask);
		
		for (int i = 0; i < 6; ++i) {
			// Fill the bottom of the top left Finder Pattern
			matrix[i][8] = booleanToColor(formatSequence[i]);
		}
		for (int i = 14; i > 8; --i) {
			// Fill the right side of the top left Finder Pattern
			matrix[8][14 - i] = booleanToColor(formatSequence[i]);
		}
		// Fill the missing modules arround the bottom right corner of
		// the top left Finder Pattern
		matrix[7][8] = booleanToColor(formatSequence[6]);
		matrix[8][8] = booleanToColor(formatSequence[7]);
		matrix[8][7] = booleanToColor(formatSequence[8]);
		
		for (int i = 7; i < 15; ++i) {
			// Fill the bottom of the top right Finder Pattern
			final int OFFSET = matrix.length - 8 - 7;
			matrix[i + OFFSET][8] = booleanToColor(formatSequence[i]);
		}
		for (int i = 0; i < 7; ++i) {
			// Fill the right side of the bottom left Finder Pattern
			final int OFFSET = matrix.length - 1;
			matrix[8][OFFSET - i] = booleanToColor(formatSequence[i]);
		}
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
	 * @param dataBit
	 *            The data bit
	 * @param masking
	 * 			  The masking value
	 * @return The color with the maskingh
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		
		final int MASKED_COLOR = booleanToColor(!dataBit);
		final int CONDITION_PART_5_6 = ((col * row) % 2) + ((col * row) % 3);
		
		switch (masking) {
			case 0:
				if ((col + row) % 2 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			case 1:
				if (row % 2 == 0) {
					return MASKED_COLOR;
				}
				break;
			
			case 2:
				if (col % 3 == 0) {
					return MASKED_COLOR;
				}
				break;
			
			case 3:
				if ((col + row) % 3 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			case 4:
				final int FLOORED_A = (int) Math.floor(row / 2.0);
				final int FLOORED_B = (int) Math.floor(col / 3.0);
				
				if ((FLOORED_A + FLOORED_B) % 2 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			case 5:
				if (CONDITION_PART_5_6 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			case 6:
				if (CONDITION_PART_5_6 % 2 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			case 7:
				if ((((col + row) % 2) + ((col * row) % 3)) % 2 == 0) {
					return MASKED_COLOR;
				}
				break;
				
			default:
				break;
		}
		
		return booleanToColor(dataBit);
	}
    
    /**
     * Write a bit at [col][row].
     *
     * @param matrix
     * The matrix to write in.
     * @param col
     * @param row
     * @param bit
     * @param mask
     * The number of the mask to apply to the bit
     */
    public static void writeBit(int[][] matrix, int col, int row, boolean bit, int mask) {
        matrix[col][row] = maskColor(col, row, bit, mask);
    }
	
	/**
	 * Fill a row of a 2-module column, and keep track of the data index.
	 *
	 * @param matrix
	 * @param data
	 * @param mask
	 * @param dataIndex
	 * @param col
	 * @param row
	 * @return The new data index
	 */
	public static int fillModuleRow(int[][] matrix, boolean[] data, int mask, int dataIndex, int col, int row) {
		
		final int RIGHT = matrix[col][row];
		final int LEFT = matrix[col - 1][row];
		
		if (isWritable(RIGHT)) {
			if (dataIndex < data.length) {
				writeBit(matrix, col, row, data[dataIndex], mask);
				dataIndex++;
			} else {
				writeBit(matrix, col, row, false, mask);
			}
		}
		
		if (isWritable(LEFT)) {
			if (dataIndex < data.length) {
				writeBit(matrix, col - 1, row, data[dataIndex], mask);
				dataIndex++;
			} else {
				writeBit(matrix, col - 1, row, false, mask);
			}
		}
		
		return dataIndex;
	}
	
	/**
	 * Fill a whole 2-module column
	 *
	 * @param matrix
	 * @param data
	 * @param mask
	 * @param dataIndex
	 * @param dir
	 * @param col
	 * @return
	 */
	public static int fillModuleColumn(int[][] matrix, boolean[] data, int mask, int dataIndex, Direction dir, int col) {
	 
		final int LAST_INDEX = matrix.length - 1;
		
		if (dir == Direction.UP) {
			// i represents the line index relative to the matrix
			for (int i = LAST_INDEX; i >= 0; --i) {
				// Fill the row and update the dataIndex
				dataIndex = fillModuleRow(matrix, data, mask, dataIndex, col, i);
			}
			
		} else {
		    // DOWN direction
			for (int i = 0; i < matrix.length; ++i) {
				dataIndex = fillModuleRow(matrix, data, mask, dataIndex, col, i);
			}
			
		}
		
		return dataIndex;
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
	 
		// Up and Down, Right to Left, always right module and then left module
		// Here, we refer to 'col' as the index of the current 2-module column
		// 0 being the closest column to the Timing Pattern.
  
		final int LAST_INDEX = matrix.length - 1;
		final int MODULE_COLUMNS_COUNT = LAST_INDEX / 2;
		final int RIGHT_COLUMNS_COUNT = MODULE_COLUMNS_COUNT - 3;
		
		int dataIndex = 0;
        
        // Filling the path to the right of the Timing Pattern
		for (int col = RIGHT_COLUMNS_COUNT - 1; col >= 0; --col) {
			Direction dir = (col % 2 == 0) ? Direction.UP : Direction.DOWN;
			
			// The index of the right column of the 2-module column relative to 'matrix'
			final int MATRIX_COL = (col + 3) * 2 + 2;
			dataIndex = fillModuleColumn(matrix, data, mask, dataIndex, dir, MATRIX_COL);
		}
		
		// Filling the path to the left of the Timing Pattern (3 remaining columns)
		for (int col = 2; col >= 0; --col) {
			// Invert the direction condition, because we skipped a column -> the parity changed
			Direction dir = (col % 2 == 0) ? Direction.DOWN : Direction.UP;
			
			final int MATRIX_COL = col * 2 + 1;
			dataIndex = fillModuleColumn(matrix, data, mask, dataIndex, dir, MATRIX_COL);
		}
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
		
		final boolean[] PATTERN = {false, false, false, false, true, false, true, true, true, false, true};
		
		int penalty = 0;
		
		// Test all the columns to find the pattern
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < matrix.length; ++j) {
				
				boolean patternFound = false;
				
				for (int k = 0; k < PATTERN.length; ++k) {
					final boolean BIT = (matrix[i][j + k] == B);
					if (PATTERN[k] != BIT){
						break;
					} else if (k == PATTERN.length - 1) {
						patternFound = true;
					}
				}
				
				if (patternFound) {
					penalty += 40;
				}
				
			}
		}
		
		return 0;
	}

}
