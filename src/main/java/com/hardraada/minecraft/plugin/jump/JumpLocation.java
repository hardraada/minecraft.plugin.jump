package com.hardraada.minecraft.plugin.jump;

import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;

public class JumpLocation extends DataAccess {
	@Column( columnName = "player_name", columnType = ColumnType.PRIMARY, dataType = DataType.STRING )
	public String playerName;
	
	@Column( columnName = "world_name", columnType = ColumnType.PRIMARY, dataType = DataType.STRING )
	public String worldName;

	@Column( columnName = "location_name", columnType = ColumnType.PRIMARY, dataType = DataType.STRING )
	public String locationName;
	
	@Column( columnName = "x", dataType = DataType.DOUBLE )
	public double x;
	
	@Column( columnName = "y", dataType = DataType.DOUBLE )
	public double y;
	
	@Column( columnName = "z", dataType = DataType.DOUBLE )
	public double z;
	
	public JumpLocation( ) {
		super( "jump_location" );
	}

	@Override
	public DataAccess getInstance( ) {
		return new JumpLocation( );
	}
}
