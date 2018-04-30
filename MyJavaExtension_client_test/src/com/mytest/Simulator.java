package com.mytest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			while(userSet.next()) {
				System.out.println(userSet.getInt("id"));		
							}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
}
