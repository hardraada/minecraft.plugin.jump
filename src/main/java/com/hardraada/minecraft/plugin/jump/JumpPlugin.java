package com.hardraada.minecraft.plugin.jump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.effects.SoundEffect;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import com.hardraada.minecraft.commons.PluginBase;

public class JumpPlugin extends PluginBase {
	@Command(
		aliases = { "jump" }, 
		description = "Add, remove and jump to named locations.", 
		permissions = { "jump.allow" }, 
		min = 2, 
		toolTip = "/jump add|remove [name]; /jump [name]"
	)
	public void jumpCommand( MessageReceiver caller, String[ ] args ) {
		if( !( caller instanceof Player ) ) return;
		Player player = ( Player )caller;
		
		if( args == null || args.length < 2 ) { this.usage( player ); return; }
		
		String action = args[ 1 ].toLowerCase( );
		if( ( "add".equals( action ) || "remove".equals( action ) ) && args.length < 3 ) { this.usage( player ); return; }
		
		if( "add".equals( action ) ) {
			this.add( player, args[ 2 ] );
		} else if( "remove".equals( action ) ) {
			this.remove( player, args[ 2 ] );
		} else if( "list".equals( action ) ) {
			this.list( player );
		} else {
			this.jump( player, action );
		}
	}
	
	private void add( Player player, String name ) {
		JumpLocation loc = new JumpLocation( );
		loc.playerName = player.getName( );
		loc.worldName = player.getWorld( ).getName( );
		loc.locationName = name;
		loc.x = player.getLocation( ).getX( );
		loc.y = player.getLocation( ).getY( );
		loc.z = player.getLocation( ).getZ( );
		
		Map<String, Object> search = new HashMap<String, Object>( );
		search.put( "player_name", loc.playerName );
		search.put( "world_name", loc.worldName );
		search.put( "location_name", loc.locationName );
		
		try {
			Database.get( ).update( loc, search );
		} catch( DatabaseWriteException e ) {
			JumpPlugin.logger.error( e );
			JumpPlugin.logger.info( "An error occurred saving the jump record \"" + loc.playerName + "\" \"" + loc.locationName + "\":  " + e.getMessage( ) );
		}
		
		player.chat( "Location \"" + name + "\" added at (" + loc.x + ", " + loc.y + ", " + loc.z + ")" );
	}
	
	private void jump( Player player, String name ) {
		JumpLocation loc = new JumpLocation( );		
		Map<String, Object> search = new HashMap<String, Object>( );
		search.put( "player_name", player.getName( ) );
		search.put( "world_name", player.getWorld( ).getName( ) );
		search.put( "location_name", name );
		
		try {
			Database.get( ).load( loc, search );
			if( loc.playerName == null || loc.playerName.equals( "" ) ) {
				player.chat( "The location \"" + name + "\" does not exist for player \"" + player.getName( ) + "\"." );
				return;
			}
		} catch( DatabaseReadException e ) {
			JumpPlugin.logger.error( e );
			JumpPlugin.logger.info( "An error occurred reading the jump record \"" + loc.playerName + "\" \"" + loc.locationName + "\":  " + e.getMessage( ) );
		}

		player.chat( "Jumping to location \"" + name + "\" at (" + loc.x + ", " + loc.y + ", " + loc.z + ")" );
		player.setX( loc.x );
		player.setY( loc.y );
		player.setZ( loc.z );
		this.playSound( player.getLocation( ), SoundEffect.Type.DOOR_OPEN );
	}
	
	private void list( Player player ) {
		JumpLocation loc = new JumpLocation( );
		List<DataAccess> locs = new ArrayList<DataAccess>( );
		Map<String, Object> search = new HashMap<String, Object>( );
		search.put( "player_name", player.getName( ) );
		
		player.chat( "Jump locations for \"" + player.getName( ) + "\":" );
		
		try {
			Database.get( ).loadAll( loc, locs, search );
			for( DataAccess da : locs ) {
				if( !( da instanceof JumpLocation ) ) continue;
				
				loc = ( JumpLocation )da;
				player.chat( loc.locationName + " (" + loc.worldName + ")" );
			}
		} catch( DatabaseReadException e ) {
			JumpPlugin.logger.error( e );
			JumpPlugin.logger.info( "An error occurred reading all jump records for \"" + loc.playerName + "\":  " + e.getMessage( ) );
		}
	}
	
	private void remove( Player player, String name ) {
		JumpLocation loc = new JumpLocation( );		
		HashMap<String, Object> search = new HashMap<String, Object>( );
		search.put( "player_name", player.getName( ) );
		search.put( "world_name", player.getWorld( ).getName( ) );
		search.put( "location_name", name );
		
		try {
			Database.get( ).remove( loc, search );
		} catch( DatabaseWriteException e ) {
			JumpPlugin.logger.error( e );
			JumpPlugin.logger.info( "An error occurred deleting the jump record \"" + loc.playerName + "\" \"" + loc.locationName + "\":  " + e.getMessage( ) );
		}
		
		player.chat( "Location \"" + name + "\" removed." );
	}
	
	private void usage( Player player ) {
		player.chat( "Jump Usage:" );
		player.chat( "--------------------" );
		player.chat( "/jump add [name]" );
		player.chat( "/jump remove [name]" );
		player.chat( "/jump list" );
		player.chat( "/jump [name]" );
	}
}
