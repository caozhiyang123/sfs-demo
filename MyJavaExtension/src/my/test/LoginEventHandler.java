package my.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		
		User user = (User) event.getParameter(SFSEventParam.USER);
		
		String name = (String)event.getParameter(SFSEventParam.LOGIN_NAME);
		String cryptedPass = (String)event.getParameter(SFSEventParam.LOGIN_PASSWORD);
		ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);
		
		IDBManager dbManager = this.getParentExtension().getParentZone().getDBManager();
		Connection con = null;
		PreparedStatement psp = null;
		ResultSet userSet = null;
		try {
			con = dbManager.getConnection();
			psp = con.prepareStatement("select password,id from users where name = ? limit 1");
			psp.setString(1, name);
			userSet = psp.executeQuery();
			if(!userSet.next()) {
				SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				passErrorData.addParameter(name);
				throw new SFSException("login failed becauseOf bad username", passErrorData);
			}
			String pass = userSet.getString("password");
			int dbId = userSet.getInt("id");
			while(userSet.next()) {
				if(cryptedPass!=null) { 								//数据库，前台
					if(!getApi().checkSecurePassword(user.getSession(), pass, cryptedPass)) {
						SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
						passErrorData.addParameter(name);
						throw new SFSException("login failed becauseOf bad password", passErrorData);
					}
					session.setProperty(MyExtension.DATABASE_ID, dbId);
					/*if(password.equals(pass)) {
						trace("login success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					}else {
						SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
						passErrorData.addParameter(name);
						throw new SFSException("login failed becauseOf bad password", passErrorData);
					}*/
				}
			}
			
		} catch (SQLException e) {
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
        	errData.addParameter("SQL Error: " + e.getMessage());
        	
        	throw new SFSLoginException("A SQL Error occurred: " + e.getMessage(), errData);
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
		
		
//		boolean flag = getApi().checkSecurePassword(session, password, encryptedPass);
		
//		SFSObject sfsObject = (SFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
		
		if("steve".equals(name)||"wang".equals(name)){
			trace("steve an wang are not allowed in the zone");
			// Create the error code to send to the client
	        SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
//	        SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
	        errData.addParameter(name);
			throw new SFSLoginException("steve an wang are not allowed in the zone",errData);
		}
		
//		String newName = "user-"+name;
//		sfsObject.putUtfString(SFSConstants.NEW_LOGIN_NAME, newName);
	}

}
