SFS
===

SmartFoxServer Java Extension samples including both server and client side code

====================================================================================================
JAVA Extension Remote Debugging:
In SmartFoxServer Admin site
1- Go to "Server Configuration" module -> JVM settings ->
Add following
-Xdebug
-Xnoagent
-Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n

As separate(3) JVM options

2. In Eclipse IDE
Add a "Remote Java Application"
- Browse to the current Java Extension project in current workspace.
- Select "Connection Type" as "Standard(Socket Attach"
- Connection Properties -> Host -> 192.168.9.88 (same IP which is used to connect via Client side to call java SFS extension)
Port -> 8787

====================================================================================================
