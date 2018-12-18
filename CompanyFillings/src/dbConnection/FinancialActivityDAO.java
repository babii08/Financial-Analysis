package dbConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

public class FinancialActivityDAO implements DAO{

	private DBConnect database;
	
	public FinancialActivityDAO(){
		database = new DBConnect();
	}

	@Override
	public Optional<FinancialActivity> get(String activityName, int year, String companyName, String tableName) {
		database.connect();
		try {
			database.setStm(database.getCon().createStatement());
			database.setRst(database.getStm().executeQuery(
					"SELECT * FROM " + tableName + " WHERE financial_activity = '" + activityName +
					"' AND date = '" + year + "' AND company_name = '" + companyName + "';"));
			
			long financialActivityValue = database.getRst().getLong(3);
			
			FinancialActivity financialActivityObject = new FinancialActivity(activityName,financialActivityValue, year, companyName);
			database.getRst().close();
			database.getStm().close();
			database.close();
			return Optional.ofNullable(financialActivityObject);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
}

	@Override
	public Hashtable<String, List<Object>> getAll(String tableName, int year, String companyName) {
		database.connect();
		Hashtable<String, List<Object>> filling = new Hashtable<String, List<Object>>();
		try {
			database.setStm(database.getCon().createStatement());
			database.setRst(database.getStm().executeQuery(
					"SELECT * FROM " + tableName + " WHERE date = '" + year + "' AND company_name = '" + companyName + "';"));
			
			List<Object> activity;
			
			
			while(database.getRst().next()) {
				String financialActivityName = database.getRst().getString(1);
				long financialActivityValue = database.getRst().getLong(3);
				activity = new ArrayList<Object>();
				activity.add(financialActivityValue);
				activity.add(year);
				activity.add(companyName);
				filling.put(financialActivityName, activity);
			}
			database.getRst().close();
			database.getStm().close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		database.close();
		return filling;
	}

	@Override
	public void save(FinancialActivity financialActivity, String tableName) {
		database.connect();
		try {
			database.setPstm(database.getCon().prepareStatement(
					"INSERT INTO " + tableName + " VALUES (default, ?, ?, ?, ?)"));
			database.getPstm().setString(1, financialActivity.getActivityName());
			database.getPstm().setInt(2, financialActivity.getActivityDate());
			database.getPstm().setLong(3, financialActivity.getActivityValue());
			database.getPstm().setString(4, financialActivity.getCompanyName());
			
			database.getPstm().executeUpdate();
			database.getPstm().close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		database.close();
	}

	@Override
	public void update(FinancialActivity financialActivity, String tableName) {
		
		database.connect();
		try {
			database.setPstm(database.getCon().prepareStatement("UPDATE " + tableName + " SET value = '" +
							financialActivity.getActivityValue() + "' WHERE financial_activity = '" + financialActivity.getActivityName() +
							"' AND company_name = '" + financialActivity.getCompanyName() + 
							"' AND date = '" + financialActivity.getActivityDate() + "';"));
			
			database.getPstm().setLong(3, financialActivity.getActivityValue());
			
			database.getPstm().executeUpdate();
			database.getPstm().close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		database.close();
		
	}

	@Override
	public void delete(FinancialActivity financialActivity, String tableName) {
		
		database.connect();
		try {
			database.setPstm(database.getCon().prepareStatement("DELETE FROM " + tableName + 
										" WHERE financial_activity = '" + financialActivity.getActivityName() + 
										"' AND date = 	'" + financialActivity.getActivityDate() +
										"' AND company_name = '" + financialActivity.getCompanyName() + "';"));
			
			database.getPstm().executeUpdate();
			database.getPstm().close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		database.close();
	}
	
	
	
}
