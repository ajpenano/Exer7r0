package com.exist.util2;

import java.util.Scanner;

public class ScannerUtil {
	private static Scanner scanner = new Scanner(System.in);
	public static String scanNextLine() {
		return scanner.nextLine();
	}
	public static String scanNextLine(String message) {
		System.out.print(message);
		return scanner.nextLine();
	}	
	public static int scanNextInt() {
		return scanner.nextInt();
	}
	public static boolean scanHasNextInt() {
		return scanner.hasNextInt();
	}
	public static void scanClose() {
		scanner.close();
	}
}
