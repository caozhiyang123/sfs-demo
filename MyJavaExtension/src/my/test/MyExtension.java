/**
 * 
 */
package my.test;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.components.login.LoginAssistantComponent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * @author 12502
 *
 */
public class MyExtension  extends SFSExtension{
	
	private SmartFoxServer smartFoxServer;
	
	private LoginAssistantComponent lac = null;
	
	public static final String DATABASE_ID = "dbID";

	@Override
	public void init() {
		smartFoxServer = SmartFoxServer.getInstance();
		trace("Hello Word! ... this is my first Extension");
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addRequestHandler("add", AddRequestHandlder.class);
		addRequestHandler("balance",TurnBalanceHandler.class);
		/*User user = smartFoxServer.getUserManager().getUserByName("");
		if(user.isSuperUser()){
			trace("current user is superUser");
		}*/
		//TODO create room
		/*CreateRoomSettings cfg = new CreateRoomSettings();
		cfg.setName("myNewRoom");
		cfg.setMaxUsers(500);
		cfg.setDynamic(true);
		try {
			super.getApi().createRoom(super.getParentZone(), cfg, null);
		} catch (SFSCreateRoomException e) {
			e.printStackTrace();
		}*/
		/*List<RoomVariable> vars = Arrays.asList
				(
						(RoomVariable) new SFSRoomVariable("integer", 12345),
				    new SFSRoomVariable("bool", false),
				    new SFSRoomVariable("string", "abcdefg")
				);*/
				 
		
	}
	
	private void destory() {
		super.destroy();
		trace("I have beend destoried");
	}
	
	
}
