package code;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class CellLineAncestry {
	//TWO-PARENT CELL LINE ANCESTRY TEST
	//By Harmony Smith
	//January 17, 2023, Western Canada High School
	
	//main program loop: gathers variables needed to run methods
	public static void main(String[] args)throws Exception {	
		String run = "y";
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to the Cell Line Ancestry Test!");
		System.out.println("============================");
		System.out.println("Place your query and reference .csv files into the folder, select \nwhether to include amelogenin in the calculations, then check the results.");
		System.out.println("\n-Name the query and database files 'query' and 'database' (case sensitive) \nthen drop them straight into the program folder (click 'replace' if prompted).");
		System.out.println("\n-Answer 'y' for yes, or 'n' for no to choose whether to include amelogenin \nwhen checking possible matches.");
		System.out.println("\n-Wait for the program to finish running, then open the 'results.txt' file \nto see each query's possible combinations.");
		System.out.println("============================\n");
		
		while (run.equals("y")) {
			String[][]database = new String[findLines("database")][1];
			database = toArray(database, "database");
			
			String[][]query = new String[findLines("query")][1];
			query = toArray(query, "query");
			
			System.out.println("Two-Parent Cell Line Ancestry Test:");
			String amelo = "";
			do {
				System.out.println("Include Amelogenin(y/n)?");
				amelo = input.next();
			} while (!amelo.equals("y") && !amelo.equals("n"));
			
			System.out.println("Running...");
			compare(query, database, amelo);
			System.out.println("Complete. Check 'Results' file.\n");
			System.out.println("============================");
			
			do {
				System.out.println("Run again(y/n)?");
				run = input.next();
			} while (!run.equals("y") && !run.equals("n"));
			System.out.println("============================\n");
		}
		input.close();
	}
	
	//checks query against possible combinations of cell lines from the database
	//a query is first compared to a database reference, then to the rest of the 
	//database references to see if a pair has the correct markers to make up the query
	//if a match is found, prints to results file
	//
	//QUERY-DB is the query database, which will remain unchanged for future reference
	//DATABASE holds the reference database to be passed onto checkMarkers
	//AMELO gets passed on to checkMarkers and isMatch
	public static void compare(String[][]queryDB, String[][]database, String amelo)throws Exception {
		PrintWriter pw = new PrintWriter(new File("results.txt"));
		
		for (int x = 1; x < queryDB.length; x++) {
			boolean hasMatch = false;
			pw.println("Matches for Cell Line '" + queryDB[x][0] + "':");
			
			for (int y = 1; y < database.length; y++) {
				String[]queryLine = new String[queryDB[0].length];
				System.arraycopy(queryDB[x], 0, queryLine, 0, queryDB[0].length);
				checkMarkers(queryDB, database, queryLine, amelo, y);
				
				if (isMatch(queryLine, amelo)) {
					hasMatch = true;
					pw.println("\tDirect Match: cell line '" + database[y][0] + "'");	
				} else if (anyMatch(queryLine, amelo)){
					
					for (int z = y + 1; z < database.length; z++) {
						String[]temp = new String[queryLine.length];
						System.arraycopy(queryLine, 0, temp, 0, queryLine.length);
						
						checkMarkers(queryDB, database, temp, amelo, z);
						if (isMatch(temp, amelo)) {
							hasMatch = true;
							pw.println("\tMatch: cell line '" + database[y][0] + "' and cell line '" + database[z][0] + "'");
						}
					}
				}
			}
			if (!hasMatch)
				pw.println("\tNo matches found.");
			pw.println("");
		}	
		pw.close();
	}
	
	//check each markers in query against markers from database, if a match, change query marker to ~
	//also checks marker names in columns to ensure they are the same & haven't been scrambled in csv sheets
	//
	//QUERY is a placeholder so that original query database doesn't get changed
	//X and Y determine which query and reference, respectively, data lines to use
	//AMELO tells method whether or not to include amelogenin data
	//QUERY-DB and DATABASE are used as references for data and marker names and are not changed
	//'q' is short for 'query' and 'db' is short for database
	public static void checkMarkers(String[][]queryDB, String [][]database, String[]query, String amelo, int y) {
		for (int column = 1; column < query.length; column++) {
			char[]qMarkers = query[column].toCharArray();
			int databaseColumn = 0;					
			for (databaseColumn = 0; !database[0][databaseColumn].equals(queryDB[0][column]); databaseColumn++);
			char[]dbMarkers = database[y][databaseColumn].toCharArray();
			
			if (amelo.equals("y") || !(queryDB[0][column].equals("AMELO") || queryDB[0][column].equals("amelo") || queryDB[0][column].equals("amelogenin") || queryDB[0][column].equals("Amelogenin") || queryDB[0][column].equals("AM") || queryDB[0][column].equals("am"))) {
				for (int z = 0; z < qMarkers.length; z++) {
					for (int w = 0; w < dbMarkers.length; w++) {
						if (qMarkers[z] == dbMarkers[w] && dbMarkers[w] != '^')
							qMarkers[z] = '~';
					}
				}
				query[column] = String.copyValueOf(qMarkers);
			}
		}
	}
	
	//checks query if there are STR markers left over after matching data was changed to '~'
	//
	//QUERY supplies the line to check
	//AMELO decides whether or not amelogenin in considered in process
	public static boolean isMatch(String[]query, String amelo) {
		for (int x = 1; x < query.length; x++) {
			char[]content = query[x].toCharArray();
			for (int y = 0; y < content.length; y++) {
				if (content[y] != '^' && content[y] != '~') {
					if (!(content[y] == 'X' || content[y] == 'Y' || content[y] == 'y' || content[y] == 'x'))
						return false;
					if (amelo.equals("y")) 
						return false;
				}
			}
		}
		return true;
	}
	
	//checks if there was any matching data changed to '~'
	//if none were changed, then the only possible match would a later direct match to a cell line
	//in other words: lets the program know if it should skip some unneeded pairings
	//
	//QUERY gives the data line to check
	//AMELO is whether or not to consider amelogenin in process
	public static boolean anyMatch(String[]query, String amelo) {
		for (int x = 1; x < query.length; x++) {
			char[]content = query[x].toCharArray();
			for (int y = 0; y < content.length; y++) {
				if (content[y] == '~') 
					return true;
			}
		}
		return false;
	}
	
	//reformats .csv file into array for use in program. returns an array equivalent of .csv file given
	//
	//ARRAY: gives array address for method to use
	//ARRAY TYPE: tells method which file to use
	public static String[][] toArray(String[][]array, String arrayType)throws Exception {
		Scanner input = new Scanner(new File(arrayType + ".csv"));
		boolean withinQuotes = false;
		
		for (int x = 0; input.hasNext(); x++) {
			array[x][0] = input.next();
			char[] temp = array[x][0].toCharArray();
			int commaPairs = 0, place = 0;
			
			//change ',' within cells to '^' to avoid later splitting
			for (int y = 0; y < temp.length; y++) {
				if (temp[y] == '"' && withinQuotes == false) {
					withinQuotes = true;
					commaPairs++;
				} else if (temp[y] == ',' && withinQuotes == true) {
					temp[y] = '^';
				} else if (temp[y] == '"' && withinQuotes == true) {
					withinQuotes = false;
				}
			}
			
			//remove " previously used to denote cells with multiple values
			char[] transfer = new char[temp.length - 2*commaPairs];
			for (int y = 0; y < temp.length; y++) {
				if (temp[y] != '"') {
					transfer[place] = temp[y];
					place++;
				}
			}
			
			array[x] = String.valueOf(transfer).split(",");
		}
		
		//System.out.println(array[3][4]);
		return array;
	}

	
	//finds how many cell lines are in file for array creation
	//
	//ARRAY TYPE: tells scanner which file to read
	public static int findLines(String arrayType)throws Exception {
		Scanner input = new Scanner(new File(arrayType + ".csv"));
		int lines = 0;
		for (lines = 0; input.hasNext(); lines++) {
			input.next();
		}
		return lines;
	}
}
