package program;

import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import findCompanyFillings.OpenURLStream;
import dbConnection.*;

public class MainProgram {

	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter the thicker symbol of the company: ");
		String companyName = reader.nextLine();
		String activityName = new String();
		long activityValue;
		Hashtable<String, Long> nameAndValue = new Hashtable<String,Long>();
		OpenURLStream urlStream = new OpenURLStream(companyName);
		nameAndValue = urlStream.consolidatedBalanceSheetData();
		FinancialActivityDAO objectConnection = new FinancialActivityDAO();
		
		int year = urlStream.getDocumentYear();
		
		System.out.println(nameAndValue.keySet());
		Set<String> keys = nameAndValue.keySet();
		for(String s : keys) {
			activityName = s;
			activityValue = nameAndValue.get(s);
			FinancialActivity financialActivity = new FinancialActivity(activityName, activityValue, year, companyName);
			objectConnection.save(financialActivity, "consolidated_balance_sheets");
		}
		
		
		
	}

}
