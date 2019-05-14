/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [25/11/2015, 19:59:06 (GMT)]
 */
package vazkii.botania.client.gui.box;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.gui.SlotLocked;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerBaubleBox extends Container {

	private final InventoryBaubleBox baubleBoxInv;
	public IItemHandlerModifiable baubles;

	public ContainerBaubleBox(EntityPlayer player, InventoryBaubleBox boxInv) {
		int i;
		int j;

		IInventory playerInv = player.inventory;
		baubleBoxInv = boxInv;

        /* todo 1.13
        baubles = BaublesApi.getBaublesHandler(player);

		addSlotToContainer(new SlotBauble(player, baubles, 0, 8, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 1, 8, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 2, 8, 8 + 2 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 3, 8, 8 + 3 * 18));
		
		addSlotToContainer(new SlotBauble(player, baubles, 4, 27, 8 + 0 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 5, 27, 8 + 1 * 18));
		addSlotToContainer(new SlotBauble(player, baubles, 6, 27, 8 + 2 * 18));

		for(i = 0; i < 4; ++i)
			for(j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlotToContainer(new SlotItemHandler(baubleBoxInv, k, 62 + j * 18, 8 + i * 18));
			}
		*/

		for(i = 0; i < 3; ++i)
			for(j = 0; j < 9; ++j)
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for(i = 0; i < 9; ++i) {
			if(playerInv.getStackInSlot(i) == baubleBoxInv.box)
				addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			else addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return player.getHeldItemMainhand() == baubleBoxInv.box
				|| player.getHeldItemOffhand() == baubleBoxInv.box;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void setAll(List<ItemStack> l) {
		// todo 1.13 baubles.setEventBlock(true);
		super.setAll(l);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int boxStart = 7;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;
			
			if(slotIndex < boxEnd) {
				if(!mergeItemStack(itemstack1, boxEnd, invEnd, true))
					return ItemStack.EMPTY;
			} else {
				// todo 1.13 if(!itemstack1.isEmpty() && (itemstack1.getItem() instanceof IBauble || itemstack1.getItem() instanceof IManaItem) && !mergeItemStack(itemstack1, boxStart, boxEnd, false))
					return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
