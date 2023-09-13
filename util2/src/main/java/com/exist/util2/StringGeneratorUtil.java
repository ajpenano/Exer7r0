package com.exist.util2;

public class StringGeneratorUtil {
	
	private static RandomIntGenerator randomIntGenerator = new RandomIntGenerator();
	
	public static String getString(int n) {
		StringBuilder stringBuilder = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int asciiCode;
			do {
				asciiCode = (int) (127*randomIntGenerator.getRandomInt());
			} while ((asciiCode < 33) || (asciiCode == 58));
			stringBuilder.append((char) asciiCode);
		}
		return stringBuilder.toString();
	}	
	
	public static void setRandomIntGenerator(RandomIntGenerator generator) {
		randomIntGenerator = generator;
	}
	
}