package com.exist.service2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.exist.model2.Table;
import com.exist.util2.FileValidatorUtil;
import com.exist.util2.ScannerUtil;
import com.exist.util2.StringGeneratorUtil;

public class TableServiceImpl implements TableService {
	
	private FileValidatorUtil fileValidatorUtil = new FileValidatorUtil();
	
	private List<String> arrayListOfLines = new ArrayList<String>();
	private List<String> extractedKeyValueArrayList = new ArrayList<String>();
	private List<String> keyArrayList = new ArrayList<String>();
	private List<String> valueArrayList = new ArrayList<String>();
	
	private int keyLength;
	private int valueLength;
	private boolean match = false;
	private String textEntry;
	
	private Table table = new Table();
	
	public void runTableService(String[] args) {
		
		table.setFilePath(fileValidatorUtil.runFileValidator(args));		
		checkExistingOrNewFile();
	}
	
	public void checkExistingOrNewFile() {		
		
		boolean existence = fileValidatorUtil.getExistence();
		
		if (existence == true) {
			System.out.println("File: " + table.getFilePath());
			System.out.print("File exists");
			
			//extract arraylist of lines from file
			File file = new File(table.getFilePath());
			extractArrayListOfLinesFromFile(file);
				
			//breaks all lines into arraylist of key:values and gets dim per row			
			extractKeyValueArrayListAndDim();
			
			//extract key arraylist
			extractKeyArrayList();
		
			//extract value arraylist	
			extractValueArrayList();

			//create map from key and value arraylists	
			createMapFromKeyAndValueArrayLists();
			
			//generates new table if map size is not equal to the elements counted by dim else print the correct table
			if (table.getMap().size()!=table.getDim().stream().mapToInt(Integer::intValue).sum()) {
				//with three of more colons per "pair"
				invalidFileContentsStartNewTableCreation();
			} else {
				System.out.println(". Below are the contents:");
				printTable();
			}
		} 
		
		else {
			System.out.println("File does not exist. We will instead use the default one.");
			File tempFile = null;
			try (InputStream inputStream = TableServiceImpl.class.getResourceAsStream("/default_file.txt")) {
	            if (inputStream != null) {
	                // create a temporary file to read the resource into
	                tempFile = File.createTempFile("default_file", ".tmp");
	                tempFile.deleteOnExit();

	                // copy the inputStream to the temporary file
	                try (OutputStream outputStream = new FileOutputStream(tempFile)) {
	                    IOUtils.copy(inputStream, outputStream);
	                }

	                // read lines from the temporary file using FileUtils.readLines
	                extractArrayListOfLinesFromFile(tempFile);
	            } else {
	                System.err.println("Resource not found.");
	                System.exit(0);
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading the resource: " + tempFile.getAbsolutePath());
	            e.printStackTrace();
	            System.exit(0);
	        }
			extractKeyValueArrayListAndDim();
			extractKeyArrayList();
			extractValueArrayList();
			createMapFromKeyAndValueArrayLists();
			saveNewTable("NEW TABLE\n(Saved in " + table.getFilePath() + ")");
		}
		
	}
	
	public void extractArrayListOfLinesFromFile(File file) {
		
		try {
			this.arrayListOfLines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Error reading the file: " + file.getAbsolutePath());
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Error reading the file: " + file.getAbsolutePath());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void extractKeyValueArrayListAndDim() {
		this.extractedKeyValueArrayList.clear();
		List<Integer> dim = new ArrayList<Integer>();
		for (int i = 0; i < arrayListOfLines.size(); i++) {
			int tokenCounterPerRow = 0;
			StringTokenizer row = new StringTokenizer(arrayListOfLines.get(i), " ");
			while (row.hasMoreTokens()) {
				this.extractedKeyValueArrayList.add(row.nextToken());
				tokenCounterPerRow++; 
			}
			dim.add(tokenCounterPerRow);
		}
		table.setDim(dim);
	}
	
	public void extractKeyArrayList() {
		this.keyArrayList.clear();
		try {
			for (int i = 0; i < extractedKeyValueArrayList.size(); i++) {
				StringTokenizer cell = new StringTokenizer(extractedKeyValueArrayList.get(i), ":");
				while (cell.hasMoreTokens()) {
					this.keyArrayList.add(cell.nextToken());
					cell.nextToken();
				}
			}	
		} catch (NoSuchElementException e) {
			//there is a missing colon or space
			invalidFileContentsStartNewTableCreation();
		} 		
	}

	public void extractValueArrayList() {
		this.valueArrayList.clear();
		for (int i = 0; i < extractedKeyValueArrayList.size(); i++) {
			StringTokenizer cell = new StringTokenizer(extractedKeyValueArrayList.get(i), ":");
			while (cell.hasMoreTokens()) {
				cell.nextToken();
				this.valueArrayList.add(cell.nextToken());
			}
		}				
	}
	
	public void createMapFromKeyAndValueArrayLists() {
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		for (int i = 0; i < keyArrayList.size(); i++) {
			String key = keyArrayList.get(i);
			String value = valueArrayList.get(i);
			map.put(key, new ArrayList<String>());
			map.get(key).add(key+value);
			map.get(key).add(value);
		}
		table.setMap(map);
	}
	
	private void invalidFileContentsStartNewTableCreation() {
				
		String option = "a";
		
		System.out.println(" but there are problems in the contents of the file. "
						   + "\nMake sure the keys and values are delimited by colons (:), key:value pairs are separated by spaces, "
						   + "and there are no duplicate keys.");
		System.out.print("Replace the faulty table with the default table in the same file? (Yes = y or No = n): ");
	
		while (!option.equals("y") && !option.equals("n")) {
			option = ScannerUtil.scanNextLine();
			if (option.equals("y")) {	
				File tempFile = null;
				try (InputStream inputStream = TableServiceImpl.class.getResourceAsStream("/default_file.txt")) {
		            if (inputStream != null) {
		                // create a temporary file to read the resource into
		                tempFile = File.createTempFile("default_file", ".tmp");
		                tempFile.deleteOnExit();

		                // copy the inputStream to the temporary file
		                try (OutputStream outputStream = new FileOutputStream(tempFile)) {
		                    IOUtils.copy(inputStream, outputStream);
		                }

		                // read lines from the temporary file using FileUtils.readLines
		                extractArrayListOfLinesFromFile(tempFile);
		            } else {
		                System.err.println("Resource not found.");
		                System.exit(0);
		            }
		        } catch (IOException e) {
		            System.err.println("Error reading the resource: " + tempFile.getAbsolutePath());
		            e.printStackTrace();
		            System.exit(0);
		        }			
				extractKeyValueArrayListAndDim();				
				extractKeyArrayList();				
				extractValueArrayList();
				createMapFromKeyAndValueArrayLists();
				saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nNEW TABLE");
				System.out.println("Run the program again to access the menu. Exiting the program...");
				ScannerUtil.scanClose();
				System.exit(0);
			} else if (option.equals("n")) {
				System.out.println("Try editing the file and try again. Exiting the program...");
				ScannerUtil.scanClose();
				System.exit(0);
			} else {
				System.out.print("Invalid key entered, choose between \"y\" or \"n\" only: ");
			}
		}
	}
	
	//stores generated random key-keyvalue-value pairs to map
	public void createRandomMap() {
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {				
				String key = StringGeneratorUtil.getString(keyLength);
				while (map.containsKey(key)) {
					key = StringGeneratorUtil.getString(keyLength); //generates new key if there is duplicate
				}
				String value = StringGeneratorUtil.getString(valueLength);
				map.put(key, new ArrayList<String>());
				map.get(key).add(key+value);
				map.get(key).add(value);
			}
		}
		table.setMap(map);
	}
	
	//creates arraylist of keys from map
	public void createKeyArrayListFromMap() {
		this.keyArrayList.clear();
			for (String key : table.getMap().keySet()) {
				this.keyArrayList.add(key);
			}
	}
	
	//creates arraylist of values from map
	public void createValueArrayListFromMap() {
		//flattens the arraylist values to create a single arraylist of value pairs arraylist 	
		List<String> valueArrayListPairs = new ArrayList<String>();
		for (List<String> innerList : table.getMap().values()) {
			for (String value : innerList) {
				valueArrayListPairs.add(value);
			}
		}
		//creates arraylist of values from value pairs arraylist
        this.valueArrayList.clear();
        for (int i = 1; i < valueArrayListPairs.size(); i = i + 2) {
        	this.valueArrayList.add(valueArrayListPairs.get(i));
        }
	}
	
	public void search() {
		
		createKeyArrayListFromMap(); 
		createValueArrayListFromMap();
		
		String find = ScannerUtil.scanNextLine("\nEnter the string to be searched: ");
		System.out.println("Search String: "+find);
		
		int counter = 0;
		
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				String source = keyArrayList.get(counter);
				int index = 0;
				int occur = 0;
				while ((index = source.indexOf(find, index)) != -1) {
					index++;
					occur++;
				}
				if (occur == 1 ) {
					System.out.println("Found "+find+" on ("+i+", "+j+") key with "+occur+" instance.");
					this.match = true;
				}
				else if (occur > 1) {
					System.out.println("Found "+find+" on ("+i+", "+j+") key with "+occur+" instances.");
					this.match = true;
				}
				counter++;
			}	
		}
		
		counter = 0;
		
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				String source = valueArrayList.get(counter);
				int index = 0;
				int occur = 0;
				while ((index = source.indexOf(find, index)) != -1) {
					index++;
					occur++;
				}
				if (occur == 1 ) {
					System.out.println("Found "+find+" on ("+i+", "+j+") value with "+occur+" instance.");
					this.match = true;
				}
				else if (occur > 1) {
					System.out.println("Found "+find+" on ("+i+", "+j+") value with "+occur+" instances.");
					this.match = true;
				}
				counter++;
			}	
		}
		
		if (match == false) {
			System.out.println("No match found!");
		}
		
	}

	public void edit() {
		
		createKeyArrayListFromMap(); 
		createValueArrayListFromMap();
		
		String edit = ScannerUtil.scanNextLine("Enter the key of the key:value pair to edit: ");
			
		boolean match = false;
		int counter = 0;
		int index = 0;
		
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				if (keyArrayList.get(counter).equals(edit)) {
					System.out.println("Found the key "+edit+" in ("+i+", "+j+") cell.");
					System.out.println("Key:Value = "+keyArrayList.get(counter)+":"+valueArrayList.get(counter));
					match = true;
					index = counter;
					}
					counter++;
				}	
			}
		
		String option;
		
		if (match == true) {
			outerloop:
			do {
				option = ScannerUtil.scanNextLine("Type \"k\" to edit the key, type \"v\" to edit the value or type \"x\""
												  + " to exit the editor: ");
				String replacement;
				if (option.equals("k")) {
					replacement = ScannerUtil.scanNextLine("Enter the new key containing no space or \":\". Type \"x\""
														   + " to exit editor: ");
					if (replacement.equals("x")) {
						System.out.println("Exiting editor...");
						break outerloop;
					} 
					while (replacement.contains(" ") || replacement.contains(":")) {
						replacement = ScannerUtil.scanNextLine("Enter the new key containing no space or \":\". Type \"x\""
															   + " to exit editor: ");
						if (replacement.equals("x")) {
							System.out.println("Exiting editor...");
							break outerloop;
						}
					}
					do {	
						int otherIndex = -1;
						counter = 0;
						for (int i = 0; i < table.getDim().size(); i++) {
							for (int j = 0; j < table.getDim().get(i); j++) {
								if (counter == index) {
									//do nothing;
								} else if (keyArrayList.get(counter).equals(replacement)) {
									otherIndex = counter;
								}
								counter++;
							}	
						}
						if (otherIndex == -1) {
							match = false;
						} else if (keyArrayList.get(otherIndex).equals(replacement)) {
							replacement = ScannerUtil.scanNextLine("Key already exists. Enter a unique key containing no space "
																   + "or \":\". Type \"x\" to exit editor: ");
							while (replacement.contains(" ") || replacement.contains(":")) {
								replacement = ScannerUtil.scanNextLine("Enter the new key containing no space or \":\". Type "
																	   + "\"x\" to exit editor: ");
								if (replacement.equals("x")) {
									System.out.println("Exiting editor...");
									break outerloop;
								}
							}
							if (replacement.equals("x")) {
								System.out.println("Exiting editor...");
								break outerloop;
							} else {
								match = true;	
							}
						}
					} while (match == true);
					this.keyArrayList.set(index, replacement);
					createMapFromKeyAndValueArrayLists();
					saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nNEW TABLE");
				} else if (option.equals("v")) {
					replacement = ScannerUtil.scanNextLine("Enter the new value containing no space or \":\". Type \"x\" to exit "
														   + "editor: ");
					while (replacement.contains(" ") || replacement.contains(":")) {
						replacement = ScannerUtil.scanNextLine("Enter the new value containing no space or \":\". Type \"x\""
															   + " to exit editor: ");
						if (replacement.equals("x")) {
							System.out.println("Exiting editor...");
							break outerloop;
						}
					}
					if (replacement.equals("x")) {
						System.out.println("Exiting editor...");
						break;
					} else {
						this.valueArrayList.set(index, replacement);
						createMapFromKeyAndValueArrayLists(); 
						saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nNEW TABLE");
					}
				} else if (option.equals("x")){
					System.out.println("Exiting editor...");
					break;
				} else {
					System.out.println("Invalid string entered.");
				}	
			} while (!option.equals("k") && !option.equals("v"));
		} else {
			System.out.println("Key not found!");
		}
		
	}
	
	public void printTable() {
		
		createKeyArrayListFromMap(); 
		createValueArrayListFromMap();
		
		int counter = 0;
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				System.out.print(keyArrayList.get(counter) + ":" + valueArrayList.get(counter) + " ");
				counter++;	
			}
			System.out.println();
		}		
		
	}
	
	public void resetDimAndTable(int rows, int columns, int keyLength, int valueLength) {
		List<Integer> dim = new ArrayList<Integer>();
		//creates arraylist of columns per row for a square table
		for (int i = 0; i < rows; i++) {
			dim.add(columns);
		}
		table.setDim(dim);
		this.keyLength = keyLength;
		this.valueLength = valueLength;
		createRandomMap(); 
		saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nNEW TABLE");
	}
	
	public void saveNewTable(String writeMessage) {
		
		prepareTextEntry();
		writeStringToFile(writeMessage);	
		printTable();
		
	}
	
	public void prepareTextEntry() {
		
		createKeyArrayListFromMap(); 
		createValueArrayListFromMap();

		int counter = 0;
		String textEntry = "";
		
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				textEntry = textEntry + keyArrayList.get(counter) + ":" + valueArrayList.get(counter) + " ";
				counter++;	
			}
			textEntry = textEntry + "\r\n";
		}		
		this.textEntry = textEntry;
		
	}
	
	public void writeStringToFile(String writeMessage) {
		File file = new File(table.getFilePath());
		
		try {	
			FileUtils.writeStringToFile(file, textEntry, StandardCharsets.UTF_8);
		} catch (Exception e) {
	        System.err.println("Error writing to the file: " + table.getFilePath());
	        e.printStackTrace();
	 	}
		System.out.println(writeMessage);
	}
	
	public void sortTableAscending() {
		
		//sort by key
		List<String> sortedKeys = new ArrayList<String>(table.getMap().keySet());
		Collections.sort(sortedKeys);
		
		//store into mapSortedByKey
		Map<String, List<String>> mapSortedByKey = new LinkedHashMap<String, List<String>>();
		for (String key : sortedKeys) {
			mapSortedByKey.put(key, new ArrayList<String>());
			mapSortedByKey.get(key).add(table.getMap().get(key).get(0));
			mapSortedByKey.get(key).add(table.getMap().get(key).get(1));
		}
		
		//generates a mapKey:KeyValue
		Map<String, String> mapKeyKeyValue = new LinkedHashMap<String, String>();
		for (Map.Entry<String, List<String>> pair : mapSortedByKey.entrySet()) {
			mapKeyKeyValue.put(pair.getKey(), pair.getValue().get(0));
		}
		
		//sort by KeyValue
		Map<String, String> mapSortedByKeyValue = mapKeyKeyValue
			.entrySet()		
			.stream()
			.sorted(Map.Entry.comparingByValue())
			.collect(LinkedHashMap::new,
				(col, e) -> col.put(e.getKey(), e.getValue()),
				HashMap::putAll);
		
		//store into mapSortedByKeyKeyValue
		Map<String, List<String>> mapSortedByKeyKeyValue = new LinkedHashMap<String, List<String>>();
		for (String key : mapSortedByKeyValue.keySet()) {
			mapSortedByKeyKeyValue.put(key, new ArrayList<String>());
			mapSortedByKeyKeyValue.get(key).add(mapSortedByKeyValue.get(key));
			mapSortedByKeyKeyValue.get(key).add(mapSortedByKey.get(key).get(1));
		}
		
		//creates arraylist of sortedKeys
		List<String> sortedKeyArrayList = createTempKeyArrayListFromMap(mapSortedByKeyKeyValue); 
		//creates arraylist of sorted values
		List<String> sortedValueArrayList = createTempValueArrayListFromMap(mapSortedByKeyKeyValue);
		
		System.out.println("\nSORTED BY KEYVALUE");
		int counter = 0;
		for (int i = 0; i < table.getDim().size(); i++) {
			for (int j = 0; j < table.getDim().get(i); j++) {
				System.out.print(sortedKeyArrayList.get(counter) + sortedValueArrayList.get(counter) + " ");
				counter++;	
			}
			System.out.println();
		}
		
		String option = null;
		do {
			option = ScannerUtil.scanNextLine("Do you want to save the sorted table? Yes(y)/No(n): ");
			if (option.equals("y")) {
				
				table.setMap(mapSortedByKeyKeyValue);
				saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nSORTED TABLE");
				
            } else if (option.equals("n")) {
            	System.out.println("Sorted table is not saved into the text file.\n\nORIGINAL UNSORTED TABLE"); 
            	printTable();
            } else {
                System.out.println("You have entered an invalid string."); 
            }
		} while (!option.equals("y") && !option.equals("n"));
	
	}
	
	//creates temporary arraylist of keys from map
	public List<String> createTempKeyArrayListFromMap(Map<String, List<String>> mapSortedByKeyKeyValue) {
		List<String> tempKeyArrayList = new ArrayList<String>();
		for (String key : mapSortedByKeyKeyValue.keySet()) {
			tempKeyArrayList.add(key);
		}
		return tempKeyArrayList;
	}
	
	//creates temporary arraylist of values from map
	public List<String> createTempValueArrayListFromMap(Map<String, List<String>> mapSortedByKeyKeyValue) {
		//flattens the arraylist values to create a single arraylist of value pairs arraylist 	
		List<String> valueArrayListPairs = new ArrayList<String>();
		for (List<String> innerList : mapSortedByKeyKeyValue.values()) {
			for (String value : innerList) {
				valueArrayListPairs.add(value);
			}
		}
		//creates temporary arraylist of values from value pairs arraylist
		List<String> tempValueArrayList = new ArrayList<String>();
        for (int i = 1; i < valueArrayListPairs.size(); i = i + 2) {
        	tempValueArrayList.add(valueArrayListPairs.get(i));
        }
        return tempValueArrayList;
	}
	
	public void addRow(int columns, int keyLength, int valueLength) {
		
		List<Integer> dim = table.getDim();
		dim.add(columns);
		table.setDim(dim);
		
		Map<String, List<String>> map = table.getMap();
		for (int j = 0; j < table.getDim().get(dim.size()-1); j++) {				
			String key = StringGeneratorUtil.getString(keyLength);
			while (table.getMap().containsKey(key)) {
				key = StringGeneratorUtil.getString(keyLength); //generates new key if there is duplicate
			}
			String value = StringGeneratorUtil.getString(valueLength);
			map.put(key, new ArrayList<String>());
			map.get(key).add(key+value);
			map.get(key).add(value);
		}
		table.setMap(map);
		saveNewTable("Text file " + table.getFilePath() + " is now updated with new key:value pairs.\n\nNEW TABLE");
		
	}
	
	public void setFileValidatorUtil(FileValidatorUtil fileValidatorUtil) {
		this.fileValidatorUtil = fileValidatorUtil;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}

	public List<String> getArrayListOfLines() {
		return arrayListOfLines;
	}
	
	public void setArrayListOfLines(List<String> arrayListOfLines) {
		this.arrayListOfLines = arrayListOfLines;
	}

	public List<String> getExtractedKeyValueArrayList() {
		return extractedKeyValueArrayList;
	}
	
	public void setExtractedKeyValueArrayList(List<String> extractedKeyValueArrayList) {
		this.extractedKeyValueArrayList = extractedKeyValueArrayList;
	}
	
	public List<String> getKeyArrayList() {
		return keyArrayList;
	}
	
	public void setKeyArrayList(List<String> keyArrayList) {
		this.keyArrayList = keyArrayList;
	}
	
	public List<String> getValueArrayList() {
		return valueArrayList;
	}
	
	public void setValueArrayList(List<String> valueArrayList) {
		this.valueArrayList = valueArrayList;
	}
	
	public int getKeyLength() {
		return keyLength;
	}
	
	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}
	
	public int getValueLength() {
		return valueLength;
	}
	
	public void setValueLength(int valueLength) {
		this.valueLength = valueLength;
	}
	
	public boolean withMatch() {
		return match;
	}
	
	public String getTextEntry() {
		return textEntry;
	}
		
}
