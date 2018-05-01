package sfs2x.extension.test.dblogin;

import java.util.Map;

import org.w3c.dom.events.Event;

import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.User;
import sfs2x.client.requests.LoginRequest;

@SuppressWarnings("all")
public class CustomLogin implements IEventListener{	
	
		private SmartFox sfs;
	
		public SmartFox getSfs(){
			return sfs;
		}
		
		//default status
		private enum Status {
			DISCONNECTED, CONNECTED, CONNECTING, CONNECTION_ERROR, CONNECTION_LOST, LOGGED, IN_A_ROOM
		}
		
		
		public  CustomLogin()
		{
		 	sfs = new SmartFox();
			sfs.setDebug(true);
			
			sfs.addEventListener(SFSEvent.CONNECTION, this);
			sfs.addEventListener(SFSEvent.CONNECTION_LOST, this);
			
			
			sfs.addEventListener(SFSEvent.LOGIN, this);
			sfs.addEventListener(SFSEvent.LOGIN_ERROR, this);
			
			sfs.addEventListener(SFSEvent.ROOM_JOIN, this);
			sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR, this);
			
			
			System.out.println("SmartFox API: "+ sfs.getVersion());
			System.out.println("Click CONNECT to start...");
			
			sfs.connect("127.0.0.1", 9933);
			
		}	
	

		@Override
		public void dispatch(BaseEvent event) throws SFSException {
			//retrieve event parameters
			Map params = event.getArguments();
			if(SFSEvent.CONNECTION.equalsIgnoreCase(event.getType())){
				if((Boolean)params.get("success")){
					this.getSfs().send(new LoginRequest("Fozzie","bear","BasicExamples"));
				}else{
					System.out.println("connection failed");
				}
				
			}else if(SFSEvent.CONNECTION_LOST.equalsIgnoreCase(event.getType())){
				System.out.println("connection lost");
				
			}else if(SFSEvent.LOGIN.equalsIgnoreCase(event.getType())){
				User u = (User) event.getArguments().get("user");
				if(u!=null) {
					System.out.println("login success:"+u);
				}
				
				User user = sfs.getUserManager().getUserByName("Fozzie");
				if(user!=null) {
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
				}
				
			}else if(SFSEvent.LOGIN_ERROR.equalsIgnoreCase(event.getType())){
				System.out.println("login fail");
			}else if(SFSEvent.EXTENSION_RESPONSE.equalsIgnoreCase(event.getType())){
//				SFSObject sfsObject = (SFSObject)event.getArguments().get("params");
//				System.out.println("the response is:"+sfsObject.getInt("sum"));
//				System.out.println("the response is:"+sfsObject.getUtfString("res").toString());
//				this.getSfs().send(new JoinRoomRequest("newMyRoom01"));
				
			}else if(SFSEvent.ROOM_JOIN.equalsIgnoreCase(event.getType())){
				System.out.println("join room success");
				
			}else if(SFSEvent.ROOM_JOIN_ERROR.equalsIgnoreCase(event.getType())){
				System.out.println("join room fail");
			}
		}
		
		public static void main(String[] args) {
			CustomLogin customLogin = new CustomLogin();
			
		}
}