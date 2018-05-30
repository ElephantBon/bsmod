package com.bsmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.RegistryNamespaced;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static Block campfire_lit;
	public static Block campfire_unlit;
	public static Block campfire_smoldering;
	public static Block fireplace_lit;
	public static Block fireplace_unlit;
	public static Block furnace_lit;
	public static Block furnace_unlit;
	public static Block redstoneiron_powered;
	public static Block redstoneiron_unpowered;
	
	public static void mainRegistry() {		
		GameRegistry.registerBlock(fireplace_lit = new blockFireplace("fireplace_lit", true), "fireplace_lit");
		GameRegistry.registerBlock(fireplace_unlit = new blockFireplace("fireplace_unlit", false).setCreativeTab(CreativeTabs.tabDecorations), "fireplace_unlit");
		GameRegistry.registerBlock(furnace_lit = new blockFurnace("furnace_lit", true), "furnace_lit");
		GameRegistry.registerBlock(furnace_unlit = new blockFurnace("furnace_unlit", false).setCreativeTab(CreativeTabs.tabDecorations), "furnace_unlit");
		GameRegistry.registerBlock(campfire_lit = new blockCampfire("campfire_lit", 1.0F), "campfire_lit");
		GameRegistry.registerBlock(campfire_smoldering = new blockCampfire("campfire_smoldering", 0.5F), "campfire_smoldering");
		GameRegistry.registerBlock(campfire_unlit = new blockCampfire("campfire_unlit", 0.0F).setCreativeTab(CreativeTabs.tabDecorations), "campfire_unlit");				
		GameRegistry.registerBlock(redstoneiron_powered = new blockRedstoneIron("redstoneiron", true), "redstoneiron_powered");
		GameRegistry.registerBlock(redstoneiron_unpowered = new blockRedstoneIron("redstoneiron", false).setCreativeTab(CreativeTabs.tabRedstone), "redstoneiron_unpowered");
	}
}
