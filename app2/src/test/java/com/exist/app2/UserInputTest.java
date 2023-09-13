package com.exist.app2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//For Mocking a Static Method
import mockit.Expectations;
import mockit.Mocked;

//For Disabling Printing of Texts in the Console
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.exist.util2.ScannerUtil;

public class UserInputTest {
	
	private UserInput userInput;

	private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@BeforeEach
	void setUpBeforeEach() {
		userInput = new UserInput();
	}
	
	@BeforeEach
	void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}
	
    @AfterEach
    public void restoreStreams() {
    	System.setOut(originalOut);
//		String output = outContent.toString();
//		System.out.println(output); 
    }
	
    @Mocked
	private ScannerUtil scannerUtil;
    
//1
	@Test
	void testInputRows() {
		new Expectations() {{
			ScannerUtil.scanNextInt();
			result = 2;
			ScannerUtil.scanHasNextInt();
	        result = true;
		}};		
		assertEquals(2, userInput.inputRows());
	}
//2
	@Test
	void testInputColumns() {
		new Expectations() {{
			ScannerUtil.scanNextInt();
			result = 3;
			ScannerUtil.scanHasNextInt();
	        result = true;			
		}};
		assertEquals(3, userInput.inputColumns());
	}
//3
	@Test
	void testInputKeyLength() {
		new Expectations() {{
			ScannerUtil.scanNextInt();
			result = 2;
			ScannerUtil.scanHasNextInt();
	        result = true;			
		}};
		assertEquals(2, userInput.inputKeyLength());
	}
//4
	@Test
	void testInputValueLength() {
		new Expectations() {{
			ScannerUtil.scanNextInt();
			result = 3;
			ScannerUtil.scanHasNextInt();
	        result = true;			
		}};
		assertEquals(3, userInput.inputValueLength());
	}	
	
}
