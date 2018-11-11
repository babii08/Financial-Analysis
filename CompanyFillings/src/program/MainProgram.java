package program;

import java.util.Scanner;

import findCompanyFillings.OpenURLStream;

public class MainProgram {

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter the thicker symbol of the company: ");
		String companyName = reader.nextLine();
		OpenURLStream urlStream = new OpenURLStream(companyName);
		System.out.println(urlStream.consolidatedBalanceSheetData());
		System.out.println(urlStream.getDocumentYear());
	}

}
