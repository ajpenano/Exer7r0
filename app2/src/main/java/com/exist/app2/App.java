package com.exist.app2;

import com.exist.service2.TableService;
import com.exist.service2.TableServiceImpl;
import com.exist.util2.ScannerUtil;

public class App {
	
	TableService tableService = new TableServiceImpl();
	UserInput userInput = new UserInput();
	
	void start(String[] args) {
		
		tableService.runTableService(args);
		
		while(true) {
			String option = ScannerUtil.scanNextLine("\nEnter the small letter representing the action you like:"
											  + "\nSearch(s), Edit(d), Print(p), Reset(r), Sort(o), Add Row(w), Exit(x)"
											  + "\nYour choice: ");
			
			switch (option) {
			
			case "s":
				tableService.search();
				break;
				
			case "d":
				tableService.edit();	
				break;
				
			case "p":
				tableService.printTable();
				break;
				
			case "r":				
				confirmReset();
				break;
			
			case "o":
				tableService.sortTableAscending();
				break;
				
			case "w":
				addRow();
				break;
				
			case "x":
				System.out.println("Exiting the program...");
				ScannerUtil.scanClose();
				System.exit(0);
				break;
				
				
			default:
				System.out.println("This is not a valid menu option. Please choose again.");
				break;
				
			}
			
		}
		
	}
	
	private void confirmReset() {
		String option;
		do {
			option = ScannerUtil.scanNextLine("You are to set the new dimensions, reset the values of the table, and save it to file. "
											  + "Continue? Yes(y)/No(n): ");
			if (option.equals("y")) {
				int rows = userInput.inputRows();
				int columns = userInput.inputColumns();
				int keyLength = userInput.inputKeyLength();
				int valueLength = userInput.inputValueLength();
				tableService.resetDimAndTable(rows, columns, keyLength, valueLength);
            } else if (option.equals("n")) {
            	System.out.println("Dimensions and values were not reset and saved.");
            } else {
                System.out.println("You have entered an invalid string."); 
            }
		} while (!option.equals("y") && !option.equals("n"));
	}
	
	private void addRow() {
		int columns = userInput.inputColumns();
		int keyLength = userInput.inputKeyLength();
		int valueLength = userInput.inputValueLength();
		tableService.addRow(columns, keyLength, valueLength);
	}
	
}
