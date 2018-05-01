package sfs2x.extension.test.dblogin;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class DBLogin extends SFSExtension
{
	public static final String DATABASE_ID = "dbID";
	
	@Override
	public void init()
	{
		trace("Database Login Extension -- started");
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
	}
	
	@Override
	public void destroy()
	{
	    super.destroy();
	    trace("Database Login Extension -- stopped");
	}
	
}
