package sfs2x.extension.test.dblogin;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomSettings;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ZoneJoinEventHandler extends BaseServerEventHandler
{
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{
		User theUser = (User) event.getParameter(SFSEventParam.USER);
		
		// dbid is a hidden UserVariable, available only server side
		UserVariable uv_dbId = new SFSUserVariable("dbid", theUser.getSession().getProperty(DBLogin.DATABASE_ID));
		uv_dbId.setHidden(true);
		
		// The avatar UserVariable is a regular UserVariable
		UserVariable uv_avatar = new SFSUserVariable("avatar", "avatar_" + theUser.getName() + ".jpg");
		
		// Set the variables
		List<UserVariable> vars = Arrays.asList(uv_dbId, uv_avatar);
		getApi().setUserVariables(theUser, vars);
		
		// Join the user
		/*Room lobby = getParentExtension().getParentZone().getRoomByName("The Lobby");
		
		if (lobby == null)
			throw new SFSException("The Lobby Room was not found! Make sure a Room called 'The Lobby' exists in the Zone to make this example work correctly.");
		*/
		//create a room and join the new room  in the specifical zone
		CreateRoomSettings cfg = new CreateRoomSettings();
		cfg.setName("newMyRoom02");//room name
//		cfg.setGame(true);
		cfg.setMaxUsers(50);//max players
		cfg.setMaxSpectators(5);//max spectators
		cfg.setDynamic(true);// remove users when appropriate
//		cfg.setPassword("--==StrongPassword123==--");
		//set roomSettings
		/*cfg.setRoomSettings(EnumSet.of(
				SFSRoomSettings.ROOM_NAME_CHANGE, 
				SFSRoomSettings.PASSWORD_STATE_CHANGE,
				SFSRoomSettings.PUBLIC_MESSAGES,
				SFSRoomSettings.CAPACITY_CHANGE));
		//set roomVariable
		List<RoomVariable> var = Arrays.asList(
				(RoomVariable)new SFSRoomVariable("integer", 123456),
				new SFSRoomVariable("bool", true),
				new SFSRoomVariable("String", "abcdefg")
				);
		cfg.setRoomVariables(var);*/
		//in a specific room group
//		cfg.setGroupId("chat");
		
		Room newMyRoom02 = null;
		try
		{
			newMyRoom02 = getApi().createRoom(this.getParentExtension().getParentZone(), cfg, null);
		}
		catch (SFSCreateRoomException e)
		{
			throw new SFSException("create room failed ");
		}
		getApi().joinRoom(theUser, newMyRoom02);
	}
}
