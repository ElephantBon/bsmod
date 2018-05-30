package com.bsmod.main;

import com.bsmod.block.ModBlocks;
import com.bsmod.lib.Strings;
import com.bsmod.tile_entity.ModTiles;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Strings.MODID, name = Strings.name, version = Strings.version)

public class Main {
	
	@SidedProxy(clientSide = "com.bsmod.main.ClientProxy", serverSide = "com.bsmod.main.ServerProxy") 
	public static ServerProxy proxy;
	
	
	@Instance(Strings.MODID)
	public static Main modInstance;
	
	@EventHandler
	public static void PreLoad(FMLPreInitializationEvent PreEvent){
		ModBlocks.mainRegistry();
		ModTiles.mainRegistry();
		
		proxy.registerRenderThings();
	}
	
	@EventHandler
	public static void load(FMLInitializationEvent event){
		proxy.registerNetworkStuff();
	}
	
	@EventHandler
	public static void PostLoad(FMLPostInitializationEvent PostEvent){
		
	}
	
}
