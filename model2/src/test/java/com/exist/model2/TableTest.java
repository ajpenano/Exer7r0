package com.exist.model2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TableTest {
	
	private Table table;
	
	@BeforeEach
	void setUpBeforeEach() {
		table = new Table();
	}
	
//1
	@Test
	void testSetFilePath() {
		table.setFilePath("file_path.txt");
		assertEquals("file_path.txt", table.getFilePath());
	}
//2
	@Test
	void testSetMap() {
		List<String> keyArrayList = new ArrayList<String>(Arrays.asList("abc","ABC","123"));
		List<String> valueArrayList = new ArrayList<String>(Arrays.asList("def","DEF","456"));
		LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		for (int i = 0; i < keyArrayList.size(); i++) {
			String key = keyArrayList.get(i);
			String value = valueArrayList.get(i);
			map.put(key, new ArrayList<String>());
			map.get(key).add(key+value);
			map.get(key).add(value);
		}
		table.setMap(map);
		assertEquals(map, table.getMap());
	}
//3
	@Test
	void testSetDim() {
		List<Integer> dim = new ArrayList<Integer>(Arrays.asList(2, 1));
		table.setDim(dim);
		assertEquals(dim, table.getDim());
	}
}
