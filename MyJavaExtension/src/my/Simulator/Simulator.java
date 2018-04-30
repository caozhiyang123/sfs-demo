package my.Simulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;


public class Simulator {
	
	
	public static Options options = null;
	public static Connection con = null;
	
	public static void main(String[] args) {
		options = new Options();
		con = getConnection(con);
		PreparedStatement psp = null;
		ResultSet userSet = null;
		try {
			psp = con.prepareStatement("select * from users where name = ?");
			psp.setString(1, "Steve Wang");
			userSet = psp.executeQuery();
			if(userSet.next()) {
				String password = userSet.getString("password");
				if("Steve Wang".equals(password)) {
					System.out.println(userSet.getInt("id"));							
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//=========================================================
		int id = conSql("Steve Wang","Steve Wang");
		System.out.println(id);
	}
	
	static private Connection getConnection(Connection paramCon) {
		
		

		try {
			if(paramCon!=null&&!paramCon.isClosed()){
			
				return paramCon;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection con;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			con = DriverManager.getConnection("jdbc:" + options.host, options.user, options.password);
			con.setAutoCommit(false);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	static private int conSql(String name, String password) {
		Connection con = null;
		PreparedStatement psp = null;
		ResultSet userSet = null;
		int id = 0;
		try {
//			con = dbManager.getConnection();
			con = getConnection(con);
			psp = con.prepareStatement("select * from users where name = ? limit 1");
			psp.setString(1, name);
			userSet = psp.executeQuery();
			
			/*if(!userSet.next()) {
				SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				passErrorData.addParameter(password);
				throw new SFSException("login failed becauseOf bad username", passErrorData);
			}*/
			while(userSet.next()) {
				String pass = userSet.getString("password");
				if(password!=null) {
					if(password.equals(pass)) {
						System.out.println("login success");
						return  userSet.getInt("id");
					}else {
						SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
						passErrorData.addParameter(password);
						throw new SFSException("login failed becauseOf bad password", passErrorData);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SFSException e) {
			e.printStackTrace();
		}finally {
			if(userSet!=null) {
				try {
					userSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(psp!=null) {
				try {
					psp.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return id;
	}
}
