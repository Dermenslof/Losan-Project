package Losan.TileEntity;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class TileEntityPoubelle extends TileEntity implements IInventory //ajout
{
    private ItemStack[] poubelleContents = new ItemStack[27];


//    /** The current angle of the lid (between 0 and 1) */
//    public float lidAngle;
//
//    /** The angle of the lid last tick */
//    public float prevLidAngle;

    /** The number of players currently using this poubelle */
    public int numUsingPlayers;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.poubelleContents[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.poubelleContents[par1] != null)
        {
            ItemStack var3;

            if (this.poubelleContents[par1].stackSize <= par2)
            {
                var3 = this.poubelleContents[par1];
                this.poubelleContents[par1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.poubelleContents[par1].splitStack(par2);

                if (this.poubelleContents[par1].stackSize == 0)
                {
                    this.poubelleContents[par1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.poubelleContents[par1] != null)
        {
            ItemStack var2 = this.poubelleContents[par1];
            this.poubelleContents[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.poubelleContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }
    
    
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setContents( ItemStack par2ItemStack)
    {
    	for(int x = 0; x < 27; x++){
    		if(this.poubelleContents[x] != null && this.poubelleContents[x].getItem().shiftedIndex == par2ItemStack.getItem().shiftedIndex){
    			int max = this.poubelleContents[x].stackSize + par2ItemStack.stackSize;
    			if(par2ItemStack.getItem().getHasSubtypes()){
    				
    			}else if(max >64){
    				this.poubelleContents[x].stackSize = 64;
    				par2ItemStack.stackSize = max-64;
    				this.onInventoryChanged();
    			}else{
    				this.poubelleContents[x].stackSize += par2ItemStack.stackSize;
    				this.onInventoryChanged();
        			break;
    			}
    		}
    		else if(this.poubelleContents[x] == null){
    			this.poubelleContents[x] = par2ItemStack;

    			if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
    			{
    				par2ItemStack.stackSize = this.getInventoryStackLimit();
    			}
   			
    			
    			this.onInventoryChanged();
    			break;
    		}
    	}
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.poubelle";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.poubelleContents = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.poubelleContents.length)
            {
                this.poubelleContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.poubelleContents.length; ++var3)
        {
            if (this.poubelleContents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.poubelleContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * Causes the TileEntity to reset all it's cached values for it's container block, blockID, metaData and in the case
     * of poubelles, the adjcacent poubelle check
     */
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
    }

    

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (++this.ticksSinceSync % 20 * 4 == 0)
        {
            ;
        }

//        this.prevLidAngle = this.lidAngle;
        float var1 = 0.1F;
        double var4;

        
    }

    /**
     * Called when a client event is received with the event number and argument, see World.sendClientEvent
     */
    public void receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
        }
    }

    public void openChest()
    {
        ++this.numUsingPlayers;
//        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Block.poubelle.blockID, 1, this.numUsingPlayers);
    }

    public void closeChest()
    {
        --this.numUsingPlayers;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, Block.poubelle.blockID, 1, this.numUsingPlayers);
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

}
