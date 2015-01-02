minecraft.plugin.jump
=====================

Overview:
Plug-in like warp to allow players to save locations and teleport 
to them.  The locations, however, are private to each player.

Dependencies:
You may either download the project and build your own JAR or 
you may download the JAR directly from 
https://github.com/hardraada/minecraft.plugin.jump/blob/master/target/minecraft.plugin.jump-1.0.jar.  
This project relies on minecraft.commons which can be found 
at https://github.com/hardraada/minecraft.commons.  You may 
download that JAR directly from 
https://github.com/hardraada/minecraft.commons/blob/master/target/minecraft.commons-1.0.jar

Permissions:
None.

Configuration:
None.

Usage:
/jump add [name] - adds the current location as a jump point with the given name for the player.
/jump remove [name] - removes the location with the given name for the player.
/jump list - lists all saved locations for the player.
/jump [name] - teleports the player to the location with the given name.