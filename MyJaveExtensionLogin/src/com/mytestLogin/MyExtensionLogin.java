package com.mytestLogin;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.components.login.ILoginAssistantPlugin;
import com.smartfoxserver.v2.components.login.LoginAssistantComponent;
import com.smartfoxserver.v2.components.login.LoginData;
import com.smartfoxserver.v2.components.login.PasswordCheckException;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.smartfoxserver.v2.security.DefaultPermissionProfile;

public class MyExtensionLogin  extends SFSExtension{

	private SmartFoxServer smartFoxServer;
	private LoginAssistantComponent lac;
	@Override
	public void init() {
		lac = new LoginAssistantComponent(this);
		
		//configure the component
		lac.getConfig().loginTable = "users";
		lac.getConfig().userNameField = "Steve Wang";
		lac.getConfig().passwordField = "Steve Wang";
		
		lac.getConfig().nickNameField = "name";
		lac.getConfig().useCaseSensitiveNameChecks = true;
		
		lac.getConfig().postProcessPlugin = new ILoginAssistantPlugin() {
			@Override
			public void execute(LoginData loginData) {
				ISFSObject fields = loginData.extraFields;
				String avatarPic = fields.getUtfString("avatar");
				boolean isMod = fields.getUtfString("isMod").equalsIgnoreCase("Y");
				//store avatar in session object
				loginData.session.setProperty("avatar", avatarPic);
				if(isMod) {
					loginData.session.setProperty("$permission", DefaultPermissionProfile.MODERATOR);
				}
			}
		};
		
//		lac.getConfig().customPasswordCheck = true;
		lac.getConfig().preProcessPlugin = new ILoginAssistantPlugin(){
		    @Override
		    public void execute(LoginData ld)
		    {
		        String clientPass = ld.clientIncomingData.getUtfString("passwd");		         
		        // Let's see if the password from the DB matches that of the user 
		        if (!ld.password.equals(clientPass))
		            throw new PasswordCheckException();
		             
		        // Success!
		    }
		};
		
	}
	
	@Override
	public void destroy() {
		lac.destroy();
	}
	
}
