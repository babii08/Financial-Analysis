package findCompanyFillings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenURLStream {
	
public String ExtractLink(OpenURLStream url, String companyLink)
{
	String urlString = url.getStringForNewSearch(companyLink);
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
	return siteAccess;
}
	
public String getStringForNewSearch(String companyName)	{
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
}