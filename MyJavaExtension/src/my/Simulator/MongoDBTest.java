package my.Simulator;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class MongoDBTest {
	public static void main(String[] args) {
		Mongo mongo = null;
		try {
			mongo = new Mongo("127.0.0.1", 27017);
			DB db = mongo.getDB("game");
			DBCollection collection = db.getCollection("users");
//			DBCursor cursor = collection.find();
//			for (DBObject dbObject : cursor) {
//				System.out.println(dbObject.toString());
//			}
			DBObject userObj = collection.findOne(new BasicDBObject("id","143972028"));
			System.out.println("=======================================");
			if(userObj!=null) {
//				System.out.println(userObj.toString());			
				if(userObj.get("coins") instanceof Integer) {
					System.out.println(userObj.get("coins"));					
				}else if(userObj.get("coins") instanceof Double) {
					System.out.println(userObj.get("coins"));
				}
			}else {
				System.out.println("user is not available");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
