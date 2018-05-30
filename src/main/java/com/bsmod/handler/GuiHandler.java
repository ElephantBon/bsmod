package com.bsmod.handler;

import com.bsmod.gui.guiCampfire;
import com.bsmod.gui.guiFireplace;
import com.bsmod.gui.guiFurnace;
import com.bsmod.inventory.containerCampfire;
import com.bsmod.inventory.containerFireplace;
import com.bsmod.inventory.containerFurnace;
import com.bsmod.tile_entity.TileCampfire;
import com.bsmod.tile_entity.TileFireplace;
import com.bsmod.tile_entity.TileFurnace;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile.getClass() == TileCampfire.class) {
				TileCampfire t = (TileCampfire) tile;
				return new containerCampfire(player.inventory, t);
			} else if (tile.getClass() == TileFurnace.class) {
				TileFurnace t = (TileFurnace) tile;
				return new containerFurnace(player.inventory, t);
			} else if (tile.getClass() == TileFireplace.class) {
				TileFireplace t = (TileFireplace) tile;
				return new containerFireplace(player.inventory, t);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if (tile.getClass() == TileCampfire.class) {
				TileCampfire t = (TileCampfire) tile;
				return new guiCampfire(player.inventory, t);
			} else if (tile.getClass() == TileFurnace.class) {
				TileFurnace t = (TileFurnace) tile;
				return new guiFurnace(player.inventory, t);
			} else if (tile.getClass() == TileFireplace.class) {
				TileFireplace t = (TileFireplace) tile;
				return new guiFireplace(player.inventory, t);
			}
		}
		return null;
	}

}
