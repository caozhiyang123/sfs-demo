package my.test;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class TurnBalanceHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User player, ISFSObject params) {

		String name = params.getUtfString("name");
		String password = params.getUtfString("password");
		
		IDBManager dbManager = this.getParentExtension().getParentZone().getDBManager();
		int id = conSql(name, password, dbManager);
//		SFSObject sfsObject = new SFSObject();
//		sfsObject.putUtfString("res", String.valueOf(id));
//		super.send("balance", sfsObject, player);
		
		/*ISession session = player.getSession();
		 String dbId = String.valueOf(session.getProperty(MyExtension.DATABASE_ID));
		 SFSObject obj2 = new SFSObject();
		obj2.putUtfString("res",name);
		super.send("coins", obj2, player);		*/
		if(id!=0) {
			DBObject object = conMongo(String.valueOf(id));
			if(object == null) {
				SFSObject obj = new SFSObject();
				obj.putUtfString("res", "user is not available");
				super.send("coins", obj, player);
			}else {
				if(object.get("coins")!=null) {
					SFSObject obj2 = new SFSObject();
					obj2.putUtfString("res",String.valueOf(object.get("coins")));
					super.send("coins", obj2, player);				
				}
			}
		}
	}
	
	private int conSql(String name, String password, IDBManager dbManager) {
		Connection con = null;
		PreparedStatement psp = null;
		ResultSet userSet = null;
		int id = 0;
		try {
			con = dbManager.getConnection();
			psp = con.prepareStatement("select * from users where name = ? limit 1");
			psp.setString(1, name);
			userSet = psp.executeQuery();
			
			if(!userSet.next()) {
				SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				passErrorData.addParameter(password);
				throw new SFSException("login failed becauseOf bad username", passErrorData);
			}
				String pass = userSet.getString("password");
				if(password!=null) {
					if(password.equals(pass)) {
						trace("login success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						return  userSet.getInt("id");
					}else {
						SFSErrorData passErrorData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
						passErrorData.addParameter(password);
						throw new SFSException("login failed becauseOf bad password", passErrorData);
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
	
	private DBObject conMongo(String id) {
		Mongo mongo = null;
		try {
			mongo = new Mongo("127.0.0.1", 27017);
			DB db = mongo.getDB("game");
			DBCollection collection = db.getCollection("users");
			DBObject userObj = collection.findOne(new BasicDBObject("id",id));
			if(userObj!=null) {
				trace("user is available"+userObj.toString());		
				return userObj;
			}else {
				trace("user is not available");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
