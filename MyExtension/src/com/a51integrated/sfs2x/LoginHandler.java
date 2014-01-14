package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class LoginHandler extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		trace("Login Handler entry");
        String username = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
        String password = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);

        ISession session = (ISession)event.getParameter(SFSEventParam.SESSION);

        try {
            //get a connection to the database
            Connection conn = getParentExtension().getParentZone().getDBManager().getConnection();

            //This will strip potential SQL injections
            PreparedStatement sql = conn.prepareStatement("SELECT user_id, pwd FROM users WHERE name = ?");
            sql.setString(1, username);

            trace("LOGIN EXTENSION SQL: " + sql);
            
            // Obtain ResultSet
            ResultSet result = sql.executeQuery();

            //Put the result into an SFSobject array
            SFSArray row = SFSArray.newFromResultSet(result);

            //make sure there is a password before you try to use the checkSecurePassword function
            if (password.equals(""))
            {
                SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
                data.addParameter(username);
                throw new SFSLoginException("You must enter a password.", data);
            }

           //SFS always encrypts passwords before sending them so you need to decrypt the password
           //received from the database and compare that to what they entered in flash
           /*if (!getApi().checkSecurePassword(session, row.getSFSObject(0).getUtfString("password"), password))
           {
                SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);

                data.addParameter(username);

                throw new SFSLoginException("Login failed for user: "  + username, data);
            }*/

            //this was in one of the SFS examples so I left it in there for testing purposes
            if (username.equals("Gonzo") || username.equals("Kermit"))
            {

                // Create the error code to send to the client
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
                errData.addParameter(username);

                // Fire a Login exception
                throw new SFSLoginException("Gonzo and Kermit are not allowed in this Zone!", errData);
            }

            //make sure you close the database connection when you're done with it, especially if you've
            //set a low number of maximum connections
            conn.close();

            //at this point you could trigger an joinRoom request if you wanted to, otherwise
            //this will return success to your LOGIN event listener
            trace("Login successful, joining room!");

        } catch (SQLException e) {
            trace(ExtensionLogLevel.WARN, " SQL Failed: " + e.toString());
        }
	}

}
