package com.bsmod.tile_entity;


import com.bsmod.block.blockCampfire;
import com.bsmod.block.blockFurnace;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileCampfire extends TileEntity implements ISidedInventory {
	private static final int[] slotsTop = new int[] { 0 };
	private static final int[] slotsBottom = new int[] { 2, 1 };
	private static final int[] slotsSides = new int[] { 1 };
	/**
	 * The ItemStacks that hold the items currently being used in the furnace
	 */
	private ItemStack[] itemStacks = new ItemStack[3];
	/** The number of ticks that the furnace will keep burning */
	public int burnTime;
	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the furnace burning for
	 */
	public int currentItemBurnTime;
	/** The number of ticks that the current item has been cooking for */
	public int cookTime;
	private String field_145958_o;
	private int updateScale = 5;
	public int fuelTime;
	public int state = 3;
	private int smolderingThreshold = 180;	// sec
	public boolean setFire = false;
	public boolean putOutFire = false;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	public String getRemainingTime()
	{
		int time = Math.max(this.burnTime, this.fuelTime) / 20;		
		int hour = time / 3600;
		int minute = (time - hour * 3600) / 60;
		int second = time - hour * 3600 - minute * 60;
		return String.format("%d : %d : %d", hour, minute, second);		
	}
	
	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int p_70301_1_) {
		return this.itemStacks[p_70301_1_];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (this.itemStacks[p_70298_1_] != null) {
			ItemStack itemstack;

			if (this.itemStacks[p_70298_1_].stackSize <= p_70298_2_) {
				itemstack = this.itemStacks[p_70298_1_];
				this.itemStacks[p_70298_1_] = null;
				return itemstack;
			} else {
				itemstack = this.itemStacks[p_70298_1_].splitStack(p_70298_2_);

				if (this.itemStacks[p_70298_1_].stackSize == 0) {
					this.itemStacks[p_70298_1_] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (this.itemStacks[p_70304_1_] != null) {
			ItemStack itemstack = this.itemStacks[p_70304_1_];
			this.itemStacks[p_70304_1_] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		this.itemStacks[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
			p_70299_2_.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory
	 */
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.field_145958_o : "container.campfire";
	}

	/**
	 * Returns if the inventory is named
	 */
	public boolean hasCustomInventoryName() {
		return this.field_145958_o != null && this.field_145958_o.length() > 0;
	}

	public void func_145951_a(String p_145951_1_) {
		this.field_145958_o = p_145951_1_;
	}

	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
		this.itemStacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.itemStacks.length) {
				this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.burnTime = p_145839_1_.getShort("BurnTime");
		this.cookTime = p_145839_1_.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.itemStacks[1]);

		if (p_145839_1_.hasKey("CustomName", 8)) {
			this.field_145958_o = p_145839_1_.getString("CustomName");
		}
	}

	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setShort("BurnTime", (short) this.burnTime);
		p_145841_1_.setShort("CookTime", (short) this.cookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (this.itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		p_145841_1_.setTag("Items", nbttaglist);
		
		if (this.hasCustomInventoryName()) {
			p_145841_1_.setString("CustomName", this.field_145958_o);
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns an integer between 0 and the passed value representing how close
	 * the current item is to being completely cooked
	 */
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int p_145953_1_) {
		return this.cookTime * p_145953_1_ / (200*this.updateScale);
	}

	/**
	 * Returns an integer between 0 and the passed value representing how much
	 * burn time is left on the current fuel item, where 0 means that the item
	 * is exhausted and the passed value means that the item is fresh
	 */
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int p_145955_1_) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * p_145955_1_ / this.currentItemBurnTime;
	}

	public boolean hasFuel() {
		return (this.itemStacks[1] != null && getItemBurnTime(this.itemStacks[1]) > 0) || this.fuelTime > 0;
	}
	
	public boolean lessFuel(){
		return this.burnTime < this.smolderingThreshold * 20;
	}


	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	public void updateEntity() {	
		boolean isDirty = false;

		if (this.isBurning()) {
			--this.burnTime;
		}		

		if (!this.worldObj.isRemote) {
			if(this.setFire){
				this.setFire = false;
				this.burnTime += 1;
			}
			else if(this.putOutFire){
				this.putOutFire = false;
				this.fuelTime = this.burnTime;
				this.burnTime = 0;
				this.cookTime = 0;
				this.currentItemBurnTime = 0;
			}
			else{				
				// New Fuel
				if(this.state == 1 || this.state == 2){
					if(this.fuelTime > 0){
						this.burnTime += this.fuelTime;
						this.fuelTime = 0;
					}
				
					if(this.itemStacks[1] != null){
						int newFuel = getItemBurnTime(this.itemStacks[1]) * this.itemStacks[1].stackSize * updateScale;
						if(newFuel > 0){
							isDirty = true;
							
							this.burnTime += newFuel;					
							this.currentItemBurnTime = this.burnTime;   
	
							if (this.itemStacks[1] != null) {
								this.itemStacks[1].stackSize = 0;
	
								if (this.itemStacks[1].stackSize == 0) {
									this.itemStacks[1] = itemStacks[1].getItem().getContainerItem(itemStacks[1]);
								}
							}						
						}
					}
	
					// Update Cook Time
					if (this.isBurning() && this.canSmelt()) {
						++this.cookTime;
		
						if (this.cookTime == 200*this.updateScale) {
							this.cookTime = 0;
							this.smeltItem();
							isDirty = true;
						}
					} else {
						this.cookTime = 0;
					}			
				}
			}// endof_newfuel

			// Update State
			int newState = 0;
			if(this.isBurning()){
				if(this.lessFuel()){
					if(this.state != 2){
						newState = 2;
					}
				}else{
					if(this.state != 1){
						newState = 1;
					}
				}
			}else{
				if(this.state != 3){
					newState = 3;
				}
			}
			
			if(newState != 0){
				this.state = newState;
				isDirty = true;
				blockCampfire.updateBlockState(this.state, this.worldObj, this.xCoord, this.yCoord,
						this.zCoord);
			}
		}

		if (isDirty) {
			this.markDirty();
		}
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item,
	 * destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.itemStacks[0] == null) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);
			if (itemstack == null)
				return false;
			if (this.itemStacks[2] == null)
				return true;
			if (!this.itemStacks[2].isItemEqual(itemstack))
				return false;
			int result = itemStacks[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.itemStacks[2].getMaxStackSize(); // Forge
																													// BugFix:
																													// Make
																													// it
																													// respect
																													// stack
																													// sizes
																													// properly.
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted
	 * item in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);

			if (this.itemStacks[2] == null) {
				this.itemStacks[2] = itemstack.copy();
			} else if (this.itemStacks[2].getItem() == itemstack.getItem()) {
				this.itemStacks[2].stackSize += itemstack.stackSize; // Forge
																				// BugFix:
																				// Results
																				// may
																				// have
																				// multiple
																				// items
			}

			--this.itemStacks[0].stackSize;

			if (this.itemStacks[0].stackSize <= 0) {
				this.itemStacks[0] = null;
			}
		}
	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 */
	public static int getItemBurnTime(ItemStack p_145952_0_) {
		if (p_145952_0_ == null) {
			return 0;
		} else {
			int moddedBurnTime = net.minecraftforge.event.ForgeEventFactory.getFuelBurnTime(p_145952_0_);
			if (moddedBurnTime >= 0)
				return moddedBurnTime;

			Item item = p_145952_0_.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab) {
					return 150;
				}

				if (block.getMaterial() == Material.wood) {
					return 300;
				}

				if (block == Blocks.coal_block) {
					return 16000;
				}
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item == Items.stick)
				return 100;
			if (item == Items.coal)
				return 1600;
			if (item == Items.lava_bucket)
				return 20000;
			if (item == Item.getItemFromBlock(Blocks.sapling))
				return 100;
			if (item == Items.blaze_rod)
				return 2400;
			return GameRegistry.getFuelValue(p_145952_0_);
		}
	}

	public static boolean isItemFuel(ItemStack p_145954_0_) {
		/**
		 * Returns the number of ticks that the supplied fuel item will keep the
		 * furnace burning, or 0 if the item isn't fuel
		 */
		return getItemBurnTime(p_145954_0_) > 0;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
				: p_70300_1_.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
						(double) this.zCoord + 0.5D) <= 64.0D;
	}

	public void openInventory() {
	}

	public void closeInventory() {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 2 ? false : (p_94041_1_ == 1 ? isItemFuel(p_94041_2_) : true);
	}

	/**
	 * Returns an array containing the indices of the slots that can be accessed
	 * by automation on the given side of this block.
	 */
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 0 ? slotsBottom : (p_94128_1_ == 1 ? slotsTop : slotsSides);
	}

	/**
	 * Returns true if automation can insert the given item in the given slot
	 * from the given side. Args: Slot, item, side
	 */
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot
	 * from the given side. Args: Slot, item, side
	 */
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return p_102008_3_ != 0 || p_102008_1_ != 1 || p_102008_2_.getItem() == Items.bucket;
	}
}