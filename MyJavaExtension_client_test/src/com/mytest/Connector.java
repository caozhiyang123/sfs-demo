package com.mytest;

import java.util.Map;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.User;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;

public class Connector implements IEventListener {

	private SmartFox sfs;
	
	public SmartFox getSfs(){
		return sfs;
	}
	//default status
	private enum Status {
		DISCONNECTED, CONNECTED, CONNECTING, CONNECTION_ERROR, CONNECTION_LOST, LOGGED, IN_A_ROOM
	}
	
	//create a SmartFox instance
	public Connector(){
		sfs = new SmartFox();
		//add sfs default handler for connection 
		sfs.addEventListener(SFSEvent.CONNECTION, this);
		sfs.addEventListener(SFSEvent.LOGIN, this);
		sfs.addEventListener(SFSEvent.LOGIN_ERROR, this);
		sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, this);
		sfs.addEventListener(SFSEvent.ROOM_JOIN, this);
		sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR, this);
		//connect to server
		sfs.connect("127.0.0.1", 9933);
	}
	
	/* (non-Javadoc)
	 * @see sfs2x.client.core.IEventListener#dispatch(sfs2x.client.core.BaseEvent)
	 */
	@Override
	public void dispatch(BaseEvent event) throws SFSException {
		//retrieve event parameters
		Map params = event.getArguments();
		if(SFSEvent.CONNECTION.equalsIgnoreCase(event.getType())){
			if((Boolean)params.get("success")){
				this.getSfs().send(new LoginRequest("michael","michael","BasicExamples"));
			}else{
				System.out.println("connection failed");
			}
			
		}else if(SFSEvent.CONNECTION_LOST.equalsIgnoreCase(event.getType())){
			System.out.println("connection lost");
			
		}else if(SFSEvent.LOGIN.equalsIgnoreCase(event.getType())){
			SFSObject reqParams = new SFSObject();
			reqParams.putInt("n1", 26);
			reqParams.putInt("n2", 16);
			ExtensionRequest extensionRequest = new ExtensionRequest("add",reqParams);
			this.getSfs().send(extensionRequest);
			User u = (User) event.getArguments().get("user");
			System.out.println("login success:"+u);
			
			User user = sfs.getUserManager().getUserByName("michael");
			System.out.println(user);
			  if(user.isGuest()){
			   System.out.println("current user is guest");
			  }else if(user.isPlayer()){
			   System.out.println("current user is player");
			  }else if(user.isModerator()){
			   System.out.println("current user is moderator");
			  }else if(user.isAdmin()){
			   System.out.println("current user is admin");
			  }
			
		}else if(SFSEvent.LOGIN_ERROR.equalsIgnoreCase(event.getType())){
			System.out.println("login fail");
			
		}else if(SFSEvent.EXTENSION_RESPONSE.equalsIgnoreCase(event.getType())){
			SFSObject sfsObject = (SFSObject)event.getArguments().get("params");
			System.out.println("the response is:"+sfsObject.getInt("sum"));
			this.getSfs().send(new JoinRoomRequest("Room#1"));
			
		}else if(SFSEvent.ROOM_JOIN.equalsIgnoreCase(event.getType())){
			System.out.println("join room success");
			
		}else if(SFSEvent.ROOM_JOIN_ERROR.equalsIgnoreCase(event.getType())){
			System.out.println("join room fail");
		}
		
	}
	
	public static void main(String[] args) {
		Connector connector = new Connector();
	}

}
