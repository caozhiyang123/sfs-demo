package my.test;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSConstants;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		String name = (String)event.getParameter(SFSEventParam.LOGIN_NAME);
		String password = (String)event.getParameter(SFSEventParam.LOGIN_PASSWORD);
		
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
