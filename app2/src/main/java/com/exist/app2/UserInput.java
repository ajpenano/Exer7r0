package com.exist.app2;

import com.exist.util2.ScannerUtil;

public class UserInput {
	
	public int inputRows() {
		int rows = 0;
		System.out.print("Input number of rows greater than 0: ");
		
		do {
			if (!ScannerUtil.scanHasNextInt()) {
                System.out.print("You have entered an invalid string, input a number greater than 0 for the rows: ");
            } else {
                rows = ScannerUtil.scanNextInt();
                if (rows <= 0) {
                    System.out.print("Please enter a number greater than 0 only: ");
                } 
            }
			ScannerUtil.scanNextLine();
		} while (rows <= 0);
		return rows;
	}
	
	public int inputColumns() {
		int columns = 0;
		System.out.print("Input number of columns greater than 0: ");
		
		do {
			if (!ScannerUtil.scanHasNextInt()) {
                System.out.print("You have entered an invalid string, input a number greater than 0 for the columns: ");
            } else {
                columns = ScannerUtil.scanNextInt();
                if (columns <= 0) {
                    System.out.print("Please enter a number greater than 0 only: ");
                }
            }
			ScannerUtil.scanNextLine();
		} while (columns <= 0);
		return columns;
	}
	
	public int inputKeyLength() {
		int keyLength = 0;
		System.out.print("Enter desired length of each key in the cells (1-5): ");
		do {
			if (!ScannerUtil.scanHasNextInt()) {
                System.out.print("You have entered an invalid string, input a number between 1 to 5: ");
            }
			else {
                keyLength = ScannerUtil.scanNextInt();
                if ((keyLength <= 0) || (keyLength > 5)) {
                    System.out.print("Please enter a number between 1 to 5 only: ");
                }
            }
			ScannerUtil.scanNextLine();
		} while ((keyLength <= 0) || (keyLength > 5));
		return keyLength;
	}
	
	public int inputValueLength() {
		int valueLength = 0;
		System.out.print("Enter desired length of each value in the cells (1-5): ");
		do {
			if (!ScannerUtil.scanHasNextInt()) {
                System.out.print("You have entered an invalid string, input a number between 1 to 5: ");
            }
			else {
                valueLength = ScannerUtil.scanNextInt();
                if ((valueLength <= 0) || (valueLength > 5)) {
                    System.out.print("Please enter a number between 1 to 5 only: ");
                }
            }
			ScannerUtil.scanNextLine();
		} while ((valueLength <= 0) || (valueLength > 5));
		return valueLength;
	}
	
}
