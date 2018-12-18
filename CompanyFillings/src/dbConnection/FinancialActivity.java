package dbConnection;

import java.sql.Date;

public class FinancialActivity {

	private String activityName;
	private long activityValue;
	private int activityDate;
	private String companyName;
	
	public FinancialActivity(String name, long value, int date, String company){
		activityName = name;
		activityValue = value;
		activityDate = date;
		companyName = company;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getActivityValue() {
		return activityValue;
	}

	public void setActivityValue(long activityValue) {
		this.activityValue = activityValue;
	}

	public int getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(int activityDate) {
		this.activityDate = activityDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
