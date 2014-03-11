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

public class ChapaTest
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
    				LoginRequest request = new LoginRequest("", "", "Chapatiz");
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
				
		        // Send two integers to the Zone extension and get their sum in return
		        ISFSObject params1 = new SFSObject();
		        params1.putInt("n1", 26);
		        params1.putInt("n2", 16);
		        
		        sfs.send(new ExtensionRequest("Game", params1));
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
				if ("Game".equals(evt.getArguments().get("cmd"))) {
					 ISFSObject responseParams = (SFSObject)evt.getArguments().get("params");
					 System.out.println("responseParams: " + (SFSObject)evt.getArguments().get("params"));
					 // We expect an int parameter called "sum"
					 System.out.println("The sum is: " + responseParams.getInt("sum"));
				}
			}
		});        
        
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}