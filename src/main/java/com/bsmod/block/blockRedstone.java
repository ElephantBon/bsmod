package com.bsmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;

import com.bsmod.lib.Strings;
import com.bsmod.tile_entity.TileFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class blockRedstone extends BlockDirectional {	
	private final boolean isPowered;	
	private final String textureName;	

	@SideOnly(Side.CLIENT)
	private IIcon top_s;
	@SideOnly(Side.CLIENT)
	private IIcon top_w;
	@SideOnly(Side.CLIENT)
	private IIcon top_n;
	@SideOnly(Side.CLIENT)
	private IIcon top_e;
	
	public blockRedstone(String name, boolean powered) {
		super(Material.circuits);
		this.disableStats();
		isPowered = powered;
		textureName = name;
		this.setHardness(1.0F);
	}
	
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(getBlockUnpowered());
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(getBlockUnpowered());
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int direction) {
		if (side == 1) {
			if (direction == 0)
				return this.top_n;
			if (direction == 1)
				return this.top_e;
			if (direction == 2)
				return this.top_s;
			if (direction == 3)
				return this.top_w;
		}

		return this.blockIcon;
	}
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(Strings.MODID + ":" + this.textureName + "_side");
		this.top_s = p_149651_1_.registerIcon(Strings.MODID + ":" + this.textureName + "_s");
		this.top_w = p_149651_1_.registerIcon(Strings.MODID + ":" + this.textureName + "_w");
		this.top_n = p_149651_1_.registerIcon(Strings.MODID + ":" + this.textureName + "_n");
		this.top_e = p_149651_1_.registerIcon(Strings.MODID + ":" + this.textureName + "_e");
	}
	
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = ((MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 3);
		boolean flag = this.isGettingInput(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, l);

		if (flag) {
			p_149689_1_.scheduleBlockUpdate(p_149689_2_, p_149689_3_, p_149689_4_, this, 1);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		int l = getDirection(world.getBlockMetadata(x, y, z));

		if (l == 1) {
			world.notifyBlockOfNeighborChange(x + 1, y, z, this);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this, 4);
		}

		if (l == 3) {
			world.notifyBlockOfNeighborChange(x - 1, y, z, this);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this, 5);
		}

		if (l == 2) {
			world.notifyBlockOfNeighborChange(x, y, z + 1, this);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this, 2);
		}

		if (l == 0) {
			world.notifyBlockOfNeighborChange(x, y, z - 1, this);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this, 3);
		}
	}

	protected boolean isGettingInput(World p_149900_1_, int p_149900_2_, int p_149900_3_, int p_149900_4_,
			int p_149900_5_) {
		return this.getInputStrength(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_) > 0;
	}

	protected int getInputStrength(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_,
			int p_149903_5_) {
		int i1 = getDirection(p_149903_5_);
		int j1 = p_149903_2_ + Direction.offsetX[i1];
		int k1 = p_149903_4_ + Direction.offsetZ[i1];
		int l1 = p_149903_1_.getIndirectPowerLevelTo(j1, p_149903_3_, k1, Direction.directionToFacing[i1]);
		return l1 >= 15 ? l1
				: Math.max(l1, p_149903_1_.getBlock(j1, p_149903_3_, k1) == Blocks.redstone_wire
						? p_149903_1_.getBlockMetadata(j1, p_149903_3_, k1) : 0);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
		return true;
		//return !World.doesBlockHaveSolidTopSurface(p_149718_1_, p_149718_2_, p_149718_3_ - 1, p_149718_4_) ? false : super.canBlockStay(p_149718_1_, p_149718_2_, p_149718_3_, p_149718_4_);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World world, int x, int y, int z, Random random) {
		int l = world.getBlockMetadata(x, y, z);

		if (!this.func_149910_g(world, x, y, z, l)) {
			boolean flag = this.isGettingInput(world, x, y, z, l);

			if (this.isPowered && !flag) {
				world.setBlock(x, y, z, this.getBlockUnpowered(), l, 2);
			} else if (!this.isPowered) {
				world.setBlock(x, y, z, this.getBlockPowered(), l, 2);

				if (!flag) {
					world.scheduleBlockUpdateWithPriority(x, y, z, this.getBlockPowered(), 1, -1);
				}
			}
		}
	}

	protected boolean func_149905_c(int p_149905_1_) {
		return this.isPowered;
	}

	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
	}

	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		int i1 = p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);

		if (!this.func_149905_c(i1)) {
			return 0;
		} else {
			int j1 = getDirection(i1);
			return j1 == 0 && p_149709_5_ == 3
					? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, i1)
					: (j1 == 1 && p_149709_5_ == 4
							? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, i1)
							: (j1 == 2 && p_149709_5_ == 2
									? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, i1)
									: (j1 == 3 && p_149709_5_ == 5
											? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, i1)
											: 0)));
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z,
					world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
		} else {
			this.func_149897_b(world, x, y, z, block);
		}
	}

	protected void func_149897_b(World world, int x, int y, int z, Block block) {
		int l = world.getBlockMetadata(x, y, z);

		if (!this.func_149910_g(world, x, y, z, l)) {
			boolean flag = this.isGettingInput(world, x, y, z, l);

			if ((this.isPowered && !flag || !this.isPowered && flag)
					&& !world.isBlockTickScheduledThisTick(x, y, z, this)) {
				byte b0 = -1;

				if (this.func_149912_i(world, x, y, z, l)) {
					b0 = -3;
				} else if (this.isPowered) {
					b0 = -2;
				}

				world.scheduleBlockUpdateWithPriority(x, y, z, this, 1, b0);
			}
		}
	}
	public boolean func_149912_i(World p_149912_1_, int p_149912_2_, int p_149912_3_, int p_149912_4_, int p_149912_5_)
    {
        int i1 = getDirection(p_149912_5_);

        if (isRedstoneRepeaterBlockID(p_149912_1_.getBlock(p_149912_2_ - Direction.offsetX[i1], p_149912_3_, p_149912_4_ - Direction.offsetZ[i1])))
        {
            int j1 = p_149912_1_.getBlockMetadata(p_149912_2_ - Direction.offsetX[i1], p_149912_3_, p_149912_4_ - Direction.offsetZ[i1]);
            int k1 = getDirection(j1);
            return k1 != i1;
        }
        else
        {
            return false;
        }
    }

    public boolean isRedstoneRepeaterBlockID(Block p_149909_0_)
    {
        return func_149907_e(p_149909_0_) || func_149907_e(p_149909_0_);
    }

	public boolean func_149910_g(IBlockAccess blockAccess, int p_149910_2_, int p_149910_3_, int p_149910_4_,
			int p_149910_5_) {
		return false;
	}

	protected int func_149902_h(IBlockAccess p_149902_1_, int p_149902_2_, int p_149902_3_, int p_149902_4_,
			int p_149902_5_) {
		int i1 = getDirection(p_149902_5_);

		switch (i1) {
		case 0:
		case 2:
			return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_ - 1, p_149902_3_, p_149902_4_, 4),
					this.func_149913_i(p_149902_1_, p_149902_2_ + 1, p_149902_3_, p_149902_4_, 5));
		case 1:
		case 3:
			return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ + 1, 3),
					this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ - 1, 2));
		default:
			return 0;
		}
	}

	protected int func_149913_i(IBlockAccess p_149913_1_, int p_149913_2_, int p_149913_3_, int p_149913_4_,
			int p_149913_5_) {
		Block block = p_149913_1_.getBlock(p_149913_2_, p_149913_3_, p_149913_4_);
		return this.func_149908_a(block)
				? (block == Blocks.redstone_wire ? p_149913_1_.getBlockMetadata(p_149913_2_, p_149913_3_, p_149913_4_)
						: p_149913_1_.isBlockProvidingPowerTo(p_149913_2_, p_149913_3_, p_149913_4_, p_149913_5_))
				: 0;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this
	 * change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Called right before the block is destroyed by a player. Args: world, x,
	 * y, z, metaData
	 */
	public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_,
			int p_149664_5_) {
		if (this.isPowered) {
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_ + 1, p_149664_3_, p_149664_4_, this);
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_ - 1, p_149664_3_, p_149664_4_, this);
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_, p_149664_4_ + 1, this);
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_, p_149664_4_ - 1, this);
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_ - 1, p_149664_4_, this);
			p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_ + 1, p_149664_4_, this);
		}

		super.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_);
	}

	protected boolean func_149908_a(Block p_149908_1_) {
		return p_149908_1_.canProvidePower();
	}

	protected int func_149904_f(IBlockAccess p_149904_1_, int p_149904_2_, int p_149904_3_, int p_149904_4_,
			int p_149904_5_) {
		return 15;
	}

	public boolean func_149907_e(Block p_149907_1_) {
		return this == getBlockPowered() || this == getBlockUnpowered();
	}

	protected abstract Block getBlockPowered();

	protected abstract Block getBlockUnpowered();

	public boolean isAssociatedBlock(Block p_149667_1_) {
		return this.func_149907_e(p_149667_1_);
	}
}