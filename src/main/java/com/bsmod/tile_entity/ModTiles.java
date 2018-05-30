package com.bsmod.tile_entity;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModTiles {

	public static void mainRegistry(){
		registerTileEntity();
	}

	private static void registerTileEntity() {
		GameRegistry.registerTileEntity(TileCampfire.class, "TileCampfire");
		GameRegistry.registerTileEntity(TileFireplace.class, "TileFireplace");
		GameRegistry.registerTileEntity(TileFurnace.class, "TileFurnace");
	}
	
}
