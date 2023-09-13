package com.exist.model2;

import java.util.List;
import java.util.Map;

public class Table {
	private String filePath;
	private Map<String, List<String>> map;
	private List<Integer> dim;
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public Map<String, List<String>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}
	
	public List<Integer> getDim() {
		return dim;
	}
	
	public void setDim(List<Integer> dim) {
		this.dim = dim;
	}
}
