package com.exist.util2;

import java.io.File;
import java.io.IOException;

public class FileValidatorUtil {
	
	private boolean existence;
	
	public String runFileValidator(String[] args) {
		
		String filePath = "file.txt";
		File currDir = new File(".");
		String currDirPath = currDir.getAbsolutePath();
		currDirPath = currDirPath.substring(0,currDirPath.length()-1);
		
		if (args.length == 0) {
			filePath = currDirPath + "file.txt";
			File file = new File(filePath);
			this.existence = file.exists();
		} else if (args[0].endsWith(".txt")) {
			filePath = currDirPath + args[0];
			File file = new File(filePath);
			this.existence = file.exists();
			if (existence == false) {
				try {
					validateStringFilenameUsingIO(filePath);
				} catch (IOException ioe) {
					//ioe.printStackTrace();
					System.out.println("Invalid file name. Try using file.txt instead.");
					System.exit(0);
				}	
			}
		} else {
			System.out.println("Invalid file name. Try using file.txt instead.");
			System.exit(0);
		}			
		return filePath;
	}
	
	private void validateStringFilenameUsingIO(String filename) throws IOException {
	    File file = new File(filename);
	    boolean created = false;
			try {
				created = file.createNewFile();
			} finally {
				if (created) {
					file.delete();
			}
		}
	}
	
	public boolean getExistence() {
		return existence;
	}
	
}