package Losan.Container;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerPoubelle extends Container //ajout
{
    private IInventory lowerPoubelleInventory;
    private int numRows;

    public ContainerPoubelle(IInventory par1IInventory, IInventory par2IInventory)
    {
        this.lowerPoubelleInventory = par2IInventory;
        this.numRows = par2IInventory.getSizeInventory() / 9;
        par2IInventory.openChest();
        int var3 = (this.numRows - 4) * 18;
        int var4;
        int var5;

        for (var4 = 0; var4 < this.numRows; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(par2IInventory, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
            }
        }
        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(par1IInventory, var5 + var4 * 9 + 9, 8 + var5 * 18, 103 + var4 * 18 + var3));
            }
        }
        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(par1IInventory, var4, 8 + var4 * 18, 161 + var3));
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.lowerPoubelleInventory.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
//    	System.out.print(par2+"\n");
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par2);
        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par2 < this.numRows * 9 )
            {
                if (!this.mergeItemStack(var4, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 0, this.numRows * 9, false))
            {
                return null;
            }
            if (var4.stackSize == 0) 
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }          
        }
        return var2;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);
        this.lowerPoubelleInventory.closeChest();
    }
    
    public IInventory func_85151_d()
    {
        return this.lowerPoubelleInventory;
    }
}
