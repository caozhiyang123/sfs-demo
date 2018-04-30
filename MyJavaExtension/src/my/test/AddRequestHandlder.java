package my.test;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddRequestHandlder extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		Integer numberOne = params.getInt("n1");
		Integer numberTwo = params.getInt("n2");
		trace("user name is:"+sender.getName());
		ISFSObject isfsObject = SFSObject.newInstance();
		isfsObject.putInt("sum", numberOne+numberTwo);
		//send it back
		super.send("add",isfsObject, sender);
		
	}

}
