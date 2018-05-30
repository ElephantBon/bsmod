package com.bsmod.block;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.bsmod.block.ModBlocks;
import com.bsmod.lib.Strings;
import com.bsmod.main.Main;
import com.bsmod.tile_entity.TileCampfire;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class blockCampfire extends BlockContainer {
	private final Random random = new Random();
	private static boolean isBurning;
	private float lightLevel;
	public int renderId = -1;

	public blockCampfire(String unlocalizedName, float ll) {
		super(Material.rock);
		this.setBlockName(unlocalizedName);
		this.setHardness(2.0F);
		this.setResistance(2.0F);
		this.setBlockTextureName(Strings.MODID + ":stone");
		if (ll > 0) {
			this.setLightLevel(ll);
		}
		this.lightLevel = ll;
	}

	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.direction(world, x, y, z);
	}

	private void direction(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		ItemStack item = player.inventory.getCurrentItem();
		if (item != null) {
			TileCampfire tile = (TileCampfire) world.getTileEntity(x, y, z);

			if (this.isUnlit()) {
				if (tile.hasFuel()) {
					boolean setlit = true;
					if (item.getItem() == Items.flint_and_steel)
						item.damageItem(1, player);
					else if (item.getItem() == Items.flint){
						--item.stackSize;
				         if(item.stackSize <= 0) 
				            player.destroyCurrentEquippedItem();				         
					}
					else
						setlit = false;

					if(setlit){
						tile.setFire = true;
						//updateBlockState(1, world, x, y, z);
						return true;
					}
				}

			} else {
				if (item.getItem() == Item.getItemFromBlock(Blocks.sand) && (this.isLit() || this.isSmoldering())) {
					//updateBlockState(3, world, x, y, z);
					tile.putOutFire = true;
					return true;
				}
			}
		}

		// Open GUI
		player.openGui(Main.modInstance, 0, world, x, y, z);
		return true;
	}

	/**
	 * Update which block the furnace is using depending on whether or not it is
	 * burning
	 */
	public static void updateBlockState(int newState, World world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z);
		//TileCampfire tile = (TileCampfire)world.getTileEntity(x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		
		isBurning = true;
		switch(newState){
		case 1:
			world.setBlock(x, y, z, ModBlocks.campfire_lit);
			break;
		case 2:
			world.setBlock(x, y, z, ModBlocks.campfire_smoldering);
			break;
		case 3:
			world.setBlock(x, y, z, ModBlocks.campfire_unlit);
			break;
		default:
			break;
		}
		isBurning = false;
		
		world.setBlockMetadataWithNotify(x, y, z, l, 2);

		if (tile != null) {			
			tile.validate();
			//tile.state = newState;
			world.setTileEntity(x, y, z, tile);
		}		
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileCampfire();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack itemstack) {
		int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		if (itemstack.hasDisplayName()) {
			((TileCampfire) world.getTileEntity(x, y, z)).func_145951_a(itemstack.getDisplayName());
		}
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
		if (!isBurning) {
			TileCampfire tile = (TileCampfire) world.getTileEntity(x, y, z);

			if (tile != null) {
				for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
					ItemStack itemstack = tile.getStackInSlot(i1);

					if (itemstack != null) {
						float f = this.random.nextFloat() * 0.8F + 0.1F;
						float f1 = this.random.nextFloat() * 0.8F + 0.1F;
						float f2 = this.random.nextFloat() * 0.8F + 0.1F;

						while (itemstack.stackSize > 0) {
							int j1 = this.random.nextInt(21) + 10;

							if (j1 > itemstack.stackSize) {
								j1 = itemstack.stackSize;
							}

							itemstack.stackSize -= j1;
							EntityItem entityitem = new EntityItem(world, (double) ((float) x + f),
									(double) ((float) y + f1), (double) ((float) z + f2),
									new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

							if (itemstack.hasTagCompound()) {
								entityitem.getEntityItem()
										.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
							}

							float f3 = 0.05F;
							entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
							entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
							entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
							world.spawnEntityInWorld(entityitem);
						}
					}
				}

				world.func_147453_f(x, y, z, block);
			}
		}

		super.breakBlock(world, x, y, z, block, p_149749_6_);
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {		
		if (this.isLit() || this.isSmoldering()) {
			if (random.nextInt(36) == 0) {
				world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
						"fire.fire", 1.0F + random.nextFloat(), 0.3F + random.nextFloat() * 0.7F, false);
			}

			float xOffset = 0.5F;
			float yOffset = 0.7F;
			float zOffset = 0.5F;
			double d0 = (double) ((float) x + xOffset);
			double d1 = (double) ((float) y + yOffset);
			double d2 = (double) ((float) z + zOffset);
			GL11.glPushMatrix();
			Main.proxy.spawnParticle("largesmoke", d0, d1, d2, 0.0D, 0.0D, 0.0D, 2.0F * this.lightLevel);
			Main.proxy.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D, 6.0F * this.lightLevel);
			GL11.glPopMatrix();
		}
	}

	/**
	 * If this returns true, then comparators facing away from this block will
	 * use the value from getComparatorInputOverride instead of the actual
	 * redstone signal strength.
	 */
	public boolean hasComparatorInputOverride() {
		return true;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		return Container.calcRedstoneFromInventory(
				(IInventory) p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(ModBlocks.campfire_unlit);
	}
	
	public boolean isLit()
	{
		return this == ModBlocks.campfire_lit;
	}
	public boolean isUnlit()
	{
		return this == ModBlocks.campfire_unlit;
	}
	public boolean isSmoldering()
	{
		return this == ModBlocks.campfire_smoldering;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return this.renderId;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
	}
}