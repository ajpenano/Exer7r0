package com.exist.service2;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.exist.model2.Table;
import com.exist.util2.FileValidatorUtil;

public interface TableService {
	public void runTableService(String[] args);
	void checkExistingOrNewFile();
	void extractArrayListOfLinesFromFile(File file);
	void extractKeyValueArrayListAndDim();
	void extractKeyArrayList();
	void extractValueArrayList();
	void createMapFromKeyAndValueArrayLists();
	void createRandomMap();
	void createKeyArrayListFromMap();
	void createValueArrayListFromMap();
	public void search();
	public void edit();
	void saveNewTable(String writeMessage);
	public void printTable();
	public void resetDimAndTable(int rows, int columns, int keyLength, int valueLength);
	void prepareTextEntry();
	void writeStringToFile(String writeMessage);
	public void sortTableAscending();
	List<String> createTempKeyArrayListFromMap(Map<String, List<String>> mapSortedByKeyKeyValue);
	List<String> createTempValueArrayListFromMap(Map<String, List<String>> mapSortedByKeyKeyValue);
	public void addRow(int columns, int keyLength, int valueLength);
	void setFileValidatorUtil(FileValidatorUtil fileValidatorUtil);
	void setTable (Table table);
	List<String> getArrayListOfLines();
	void setArrayListOfLines(List<String> arrayListOfLines);
	List<String> getExtractedKeyValueArrayList();
	void setExtractedKeyValueArrayList(List<String> extractedKeyValueArrayList);
	List<String> getKeyArrayList();
	void setKeyArrayList(List<String> keyArrayList);
	List<String> getValueArrayList();
	void setValueArrayList(List<String> valueArrayList);
	int getKeyLength();
	void setKeyLength(int keyLength);
	int getValueLength();
	void setValueLength(int valueLength);
	boolean withMatch();
	String getTextEntry();
}