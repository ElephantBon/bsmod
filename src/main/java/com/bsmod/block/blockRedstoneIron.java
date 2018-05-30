package com.bsmod.block;

import java.util.Random;

import com.bsmod.lib.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class blockRedstoneIron extends blockRedstone{
	public blockRedstoneIron(String name, boolean powered) {
		super(name, powered);
		this.setBlockName(name);		
	}

	@Override
	protected Block getBlockPowered() {		
		return ModBlocks.redstoneiron_powered;
	}

	@Override
	protected Block getBlockUnpowered() {
		return ModBlocks.redstoneiron_unpowered;
	}
}
