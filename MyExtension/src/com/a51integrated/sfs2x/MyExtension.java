package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExtension extends SFSExtension {

	@Override
	public void init() {
		trace("Login extension starting.");
		
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
		
		trace("Math handler extension starting.");
		this.addRequestHandler("math", MathHandler.class);
	}

	@Override
	public void destroy() {
		trace("Login extension stopped.");
		super.destroy();
	}
	
}
