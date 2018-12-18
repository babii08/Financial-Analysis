package findCompanyFillings;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dbConnection.*;

public class OpenURLStream {
	
	private String companyName;
	private String companyInformation;
	
public OpenURLStream(String companyName)
{
	this.companyName = companyName;
	companyInformation = "";
}
	
public Hashtable<String,Long> consolidatedBalanceSheetData()
{
	Hashtable<String,Long> financeActivity = new Hashtable<String,Long>();
	try {
		URL consolidatedBalanceSheetURL = getConsolidatedBalanceSheet();
		BufferedReader balanceSheetHTML = new BufferedReader(new InputStreamReader(
													consolidatedBalanceSheetURL.openStream()));
		String documentLine = new String();
		Pattern p = Pattern.compile("window \\);\">([\\w\\s\\p{Punct}]+)</a></td>");
		Pattern p1 = Pattern.compile("(>[0-9]+(\\p{Punct}[0-9]+)*)|( [0-9]+(\\p{Punct}[0-9]+)*)");
		boolean lineWithValue = false;
		int multiplier = 1;
		int activityCount = 0;
		int valueCount = 0;
		String[] typeOfActivity = new String[1000];
		long[] value = new long[1000];
		
		while((documentLine = balanceSheetHTML.readLine()) != null)
		{
			
			if (documentLine.contains("colspan=\"1\" rowspan=\"1\""))
				multiplier = getMultiplier(documentLine);
			
			if(lineWithValue == true && documentLine.contains("nump"))
			{
				Matcher m1 = p1.matcher(documentLine);
				if (m1.find()) {
					String replacePunct = documentLine.substring(m1.start()+1,m1.end()).replaceAll(",","");
					replacePunct = replacePunct.replace(".", "");
					value[valueCount] = Long.parseLong(replacePunct) * multiplier;
					System.out.println(value[valueCount]);
					companyInformation = companyInformation + " = " + value[valueCount] + "; \n";
					valueCount++;

				}
				lineWithValue = false;
			}
			
			Matcher m = p.matcher(documentLine);
			if (m.find())
			{
				lineWithValue = true;
				typeOfActivity[activityCount] = documentLine.substring(m.start()+11,m.end()-9);
				if (typeOfActivity[activityCount].contains("strong") == true)
					typeOfActivity[activityCount] = typeOfActivity[activityCount].substring(8, typeOfActivity[activityCount].length()-9);
				System.out.println(typeOfActivity[activityCount]);
				companyInformation = companyInformation + typeOfActivity[activityCount] + "   ";
				activityCount++;
				
			}
		}
		
		for(int i=0;i<activityCount;i++)
			financeActivity.put(typeOfActivity[i], value[i]);
			
	} catch (IOException e) {
		e.printStackTrace();
	}
	return financeActivity;
}
	
private int getMultiplier(String documentLine) {
	
	if (documentLine.contains("Thousands"))
		return 1000;
	else if (documentLine.contains("Millions"))
		return 1000000;
	return 1;
}


public URL getConsolidatedBalanceSheet()
{
	try {
	URL viewFillingData = extractLink();
	if (viewFillingData == null)
		throw new NullPointerException("Error in finding this company");
	BufferedReader fillingDataHTML = new BufferedReader(new InputStreamReader (viewFillingData.openStream()));
	String tmp = new String();
	Pattern pat = Pattern.compile("reports\\[\\p{Digit}\\+\\p{Digit}\\]");
	while ((tmp = fillingDataHTML.readLine()) != null){
		Matcher mat = pat.matcher(tmp);
		if(mat.find()){
			Pattern p = Pattern.compile("\"([^\"]*)\"");
			Matcher m = p.matcher(tmp);
			if(m.find())
			{
				String linkExtension = tmp.substring(m.start()+1,m.end()-1);
				System.out.println(linkExtension);
				String balanceSheetURL = "https://www.sec.gov" + linkExtension;
				if (checkForBalanceSheet(balanceSheetURL) == true)
					return new URL(balanceSheetURL);
			}
		}
	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
	
private boolean checkForBalanceSheet(String balanceSheetURL) {
	
	try {
	URL link = new URL(balanceSheetURL);
	BufferedReader linkForActivity = new BufferedReader(
							new InputStreamReader(link.openStream()));
	String tmp = new String();
	while ((tmp = linkForActivity.readLine()) != null){
		tmp = tmp.toUpperCase();
		if (tmp.contains("CONSOLIDATED BALANCE SHEETS - USD") || tmp.contains("BALANCE SHEETS - USD"))
			return true;
	}
	}catch(IOException e) {
		e.printStackTrace();
	}
	return false;
}

public URL extractLink()
{
	String urlString = getStringForNewSearch();
	Pattern p = Pattern.compile("href=\"(.*?)\"");
	System.out.println(urlString);
	Matcher m = p.matcher(urlString);
	String urlThatYouNeed = null;
	int count = 0;
	while (m.find())
	{
		count++;
		if (count == 2) {
			System.out.println(m.start());
			System.out.println(m.end());
			urlThatYouNeed = urlString.substring(m.start()+6,m.end()-1);
			System.out.println(urlThatYouNeed);
			break;
		}
	}
	String siteAccess = new String();
	siteAccess = "https://www.sec.gov" + urlThatYouNeed ;
	System.out.println(siteAccess);
	try {
		return new URL(siteAccess);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
	
public String getStringForNewSearch()	{
		try {
			String linkForCompany = "https://www.sec.gov/cgi-bin/browse-edgar?CIK="+companyName+"&owner=exclude&action=getcompany&Find=Search";
			URL link = new URL(linkForCompany);
			//BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\babii\\OneDrive\\Desktop\\out.txt"));
			BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
			String tmp = null;
			boolean lineWithLink = false;
			String linkLine = new String();
			while ((tmp = in.readLine()) != null)
			{
				if (lineWithLink == true)
				{
					linkLine = tmp;
					return linkLine;
				}
					
				if (tmp.contains("10-K") && !tmp.contains("Form 10-K"))
				{
					lineWithLink = true;
				}
			}
			in.close();
		}
		catch(MalformedURLException e)
		{
			System.out.println("MalformedURLException: " + e);
		} catch(IOException e) {
			System.out.println("IOException " + e);
		}
		return null;
	}

public int getDocumentYear()
{
	int year = 1;
	try {
		URL consolidatedBalanceSheetURL = getConsolidatedBalanceSheet();
		BufferedReader balanceSheetHTML;
		balanceSheetHTML = new BufferedReader(new InputStreamReader(
													consolidatedBalanceSheetURL.openStream()));
		String documentLine = new String();
		Pattern p = Pattern.compile("<div>([\\w\\s\\p{Punct}]+)</div>");
		boolean lineWithYear = false;
		while((documentLine = balanceSheetHTML.readLine()) != null)
		{
			if (lineWithYear == true)
				break;
			
			if(documentLine.contains("colspan=\"1\" rowspan=\"1\"") || documentLine.contains("colspan=\"2\" rowspan=\"1\""))
				lineWithYear = true;
		}
		Matcher m = p.matcher(documentLine);
		if (m.find()) {
			String date = documentLine.substring(m.start()+5,m.end()-6);
			year = Integer.parseInt(date.substring(date.length()-4, date.length()));
		}
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	System.out.println(year);
	return year;
}

public String getCompanyInformation() {
	return companyInformation;
}

}

