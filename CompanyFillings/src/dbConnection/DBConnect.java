package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {

	static final String JDBC_Driver = "com.mysql.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://localhost:3306/companiesfillings";
	
	private Connection con = null;
	private PreparedStatement pstm = null;
	private Statement stm = null;
	private ResultSet rst = null;
	
	public void connect(){
		try {
			con = DriverManager.getConnection(DATABASE_URL, "root", "");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
			try {
				if (rst != null)
				rst.close();
				if (stm != null)
					stm.close();
				if (pstm != null)
					pstm.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public PreparedStatement getPstm() {
		return pstm;
	}

	public void setPstm(PreparedStatement pstm) {
		this.pstm = pstm;
	}

	public Statement getStm() {
		return stm;
	}

	public void setStm(Statement stm) {
		this.stm = stm;
	}

	public ResultSet getRst() {
		return rst;
	}

	public void setRst(ResultSet rst) {
		this.rst = rst;
	}
}
