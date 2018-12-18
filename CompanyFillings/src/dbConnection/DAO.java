package dbConnection;

import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

public interface DAO {
	Optional<FinancialActivity> get(String activityName, int year, String companyName, String tableName);
	Hashtable<String,List<Object>> getAll(String tableName, int year, String companyName);
	void save(FinancialActivity financialActivity, String tableName);
	void update(FinancialActivity financialActivity, String tableName);
	void delete(FinancialActivity financialActivity, String tableName);	
}
