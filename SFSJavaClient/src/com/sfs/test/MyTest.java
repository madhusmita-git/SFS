package com.sfs.test;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.requests.CreateRoomRequest;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.RoomSettings;

import java.util.Map;

public class MyTest
{
    private static SmartFox sfs;
    
    public static void main(String args[]) {
    	
    	sfs = new SmartFox(true);
        
        // Add event handler for connection
        sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener()
        {
            public void dispatch(BaseEvent evt) throws SFSException {
                
                // Retrieve event parameters
                Map<String, Object> params = evt.getArguments();
                System.out.println(params);
                if ((Boolean)params.get("success")) {
                    System.out.println("Connection established");
    				LoginRequest request = new LoginRequest("madhusmita", "mindfire", "TestZone");
    				System.out.println("Logging in");
    				sfs.send(request); 				
                }
                else {
                    System.out.println("Connection failed");
                }
            }
        });
        
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Connection LOST :(");
			}
		});
        
        // Listener for LOGIN, get called just after "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Login Success :)");				
				//System.out.println(evt.getArguments());
				
				Map<String, Object> mLoginOutputData = evt.getArguments();
				System.out.println("DATA: " + mLoginOutputData.get("data"));
				
				ISFSObject oLoginData = (ISFSObject) mLoginOutputData.get("data");
				System.out.println("DatabaseId: " + oLoginData.getInt("DatabaseID"));
				System.out.println("Test param coming from login: " + oLoginData.getUtfString("test1"));

				// Create new dynamic room from Client side
				/*RoomSettings roomSetting = new RoomSettings("TestRoom2 Settings");
		        roomSetting.setName("Room2Name");
		        roomSetting.setPassword("mindfire");
		        
		        sfs.send(new CreateRoomRequest(roomSetting));  */
				
		        // Send two integers to the Zone extension and get their sum in return
		        ISFSObject params1 = new SFSObject();
		        params1.putInt("n1", 26);
		        params1.putInt("n2", 16);
		        
		        sfs.send(new ExtensionRequest("math", params1));
		        System.out.println("After login: " + sfs.getRoomList());
		        
		        sfs.send(new JoinRoomRequest("TestRoom", "mindfire"));
			}
		});
        
        // Listener for LOGIN error if any during "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("Login falied :(");
			}
		});
        
    	// Listener for handling extension response
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("extension call");
				System.out.println("evt.getArguments().get: " + evt.getArguments().get("cmd"));
				if ("math".equals(evt.getArguments().get("cmd"))) {
					 ISFSObject responseParams = (SFSObject)evt.getArguments().get("params");
					 System.out.println("responseParams: " + (SFSObject)evt.getArguments().get("params"));
					 // We expect an int parameter called "sum"
					 System.out.println("The sum is: " + responseParams.getInt("sum"));
				}
			}
		});        
        
        // Listener for handle after successfully join the room
        sfs.addEventListener(SFSEvent.ROOM_JOIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				//System.out.println("Joined Room: " + evt.getArguments().get("room"));
				Map<String, Object> mRoomList = evt.getArguments();
				Room oRoomDetail = (Room) mRoomList.get("room");
				System.out.println("Room Details-> Id: " + oRoomDetail.getId()
						+ " , GroupId: " + oRoomDetail.getGroupId() 
						+ " , Capacity: " + oRoomDetail.getCapacity());
				System.out.println("After new room join successfully: " + sfs.getRoomList());
				Room testRoom = sfs.getRoomByName("TestRoom");
				if (! (oRoomDetail.getGroupId()).equalsIgnoreCase("Group 2")) {
					 sfs.send(new JoinRoomRequest("Room2Name", "mindfire", sfs.getRoomList().get(1).getId(), true));
				}
			}
		});
        
        // Listener for handle after error while join the room
        sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println(evt.getArguments());
				System.out.println("Couldn't able to join the room :( \n ERROR: " + evt.getArguments().get("errorMessage"));
				System.out.println("After new room join unsuccessfully: " + sfs.getRoomList());
			}
		});
        
        
        sfs.addEventListener(SFSEvent.ROOM_ADD, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent paramBaseEvent) throws SFSException {
				System.out.println("New room has been created: " + paramBaseEvent.getArguments());
				sfs.send(new JoinRoomRequest("Room2Name", "mindfire"));
			}
		});
        
        sfs.addEventListener(SFSEvent.ROOM_CREATION_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent paramBaseEvent) throws SFSException {
				System.out.println("Error while creating dynamic room :(" + paramBaseEvent.getArguments());
			}
		});
        
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("192.168.9.88", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}