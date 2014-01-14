package com.sfs.test;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;

import java.awt.Event;
import java.util.Map;

public class MyTest
{
    private static SmartFox sfs;
    
    public static void main(String args[]) {
    	
    	sfs = new SmartFox(true);
        /*
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
		});*/    	
    	
        // Add event handler for connection
        sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener()
        {
            public void dispatch(BaseEvent evt) throws SFSException {
                
                // Retrieve event parameters
                Map params = evt.getArguments();
                
                if ((Boolean)params.get("success")) {
                    System.out.println("Connection established");
    				LoginRequest request = new LoginRequest("madhusmita1", "mindfire", "TestZone");
    				System.out.println("Logging in");
    				sfs.send(request);
    				
    				sfs.send(new JoinRoomRequest("MyLobby"));
    				/*
    		        // Send two integers to the Zone extension and get their sum in return
    		        ISFSObject params1 = new SFSObject();
    		        params1.putInt("n1", 26);
    		        params1.putInt("n2", 16);
    		        
    		        sfs.send(new ExtensionRequest("math", params1));
    		        */
                }
                else
                    System.out.println("Connection failed");
            }
        });
        

        sfs.addEventListener(SFSEvent.LOGIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				//LoginRequest request = new LoginRequest("madhusmita", "", "TestZone");
				System.out.println("Login Success :)");
			}
		});
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("Login falied :(");
			}
		});
        
        sfs.connect("192.168.9.88", 9933);
        sfs.disconnect();
    }
}