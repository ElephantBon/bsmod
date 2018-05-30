package com.bsmod.main;

import com.bsmod.render.tile_entity.CampfireRenderer;
import com.bsmod.tile_entity.TileCampfire;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy{
	
	public void registerRenderThings(){		
		ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new CampfireRenderer());		
	}
	
	public int addArmor(String armor){
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}
}
