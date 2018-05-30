package com.bsmod.main;

import com.bsmod.handler.GuiHandler;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.RenderGlobal;

public class ServerProxy {

	public void registerRenderThings() {

	}

	public int addArmor(String armor) {
		return 0;
	}
	
	public void registerNetworkStuff(){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.modInstance, new GuiHandler());
	}
	
	public void spawnParticle(String particle, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		RenderGlobal render = Minecraft.getMinecraft().renderGlobal;
		EntityFX fx = render.doSpawnParticle(particle, x, y, z, motionX, motionY, motionZ);
		if(fx != null) {
			if(particle.equals("flame")) {
				ObfuscationReflectionHelper.setPrivateValue(EntityFlameFX.class, (EntityFlameFX)fx, Float.valueOf(scale), 0);
			} else if(particle.equals("smoke")) {
				ObfuscationReflectionHelper.setPrivateValue(EntitySmokeFX.class, (EntitySmokeFX)fx, Float.valueOf(scale), 0);
			}
		}
	}
}
