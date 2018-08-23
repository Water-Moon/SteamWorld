package zaexides.steamworld.te;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import zaexides.steamworld.utility.capability.ItemStackHandlerInput;
import zaexides.steamworld.utility.capability.ItemStackHandlerOutput;
import zaexides.steamworld.utility.capability.ItemStackInputOutputHandler;

public class TileEntitySWFurnace extends TileEntityMachine implements ITickable
{
	public ItemStackHandlerInput inputStack = new ItemStackHandlerInput(1);
			
	public ItemStackHandlerOutput outputStack = new ItemStackHandlerOutput(1);
			
	private int efficiency = 1;
	public final int MAX_PROGRESSION = 200;
	
	private static final int BASE_COST_PER_TICK = 4;
			
	public void SetStats(int efficiency)
	{
		this.efficiency = efficiency;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(steamTank);
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackInputOutputHandler(inputStack, outputStack));
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		steamTank.writeToNBT(compound);
		compound.setTag("items_in", inputStack.serializeNBT());
		compound.setTag("items_out", outputStack.serializeNBT());
		
		compound.setInteger("progress", progression);
		compound.setInteger("efficiency", efficiency);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		steamTank.readFromNBT(compound);
		if(compound.hasKey("items_in"))
			inputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_in"));
		if(compound.hasKey("items_out"))
			outputStack.deserializeNBT((NBTTagCompound) compound.getTag("items_out"));
		
		progression = compound.getInteger("progress");
		efficiency = compound.getInteger("efficiency");
	}

	@Override
	public boolean Execute()
	{
		ItemStack itemStack = FurnaceRecipes.instance().getSmeltingResult(inputStack.getStackInSlot(0));
		if(itemStack != ItemStack.EMPTY)
		{
			ItemStack targetStack = outputStack.getStackInSlot(0);
			
			if(targetStack.getItem() == Items.AIR)
				targetStack = ItemStack.EMPTY;
			
			if(targetStack != ItemStack.EMPTY)
			{
				if(targetStack.getItem() != itemStack.getItem() || targetStack.getItemDamage() != itemStack.getItemDamage() || targetStack.getMetadata() != itemStack.getMetadata() || (targetStack.getCount() + itemStack.getCount()) > targetStack.getMaxStackSize())
				{
					SetActive(false);
					return false;
				}
			}
			
			if(efficiency == 0)
				efficiency = 1;
			
			if(steamTank.getFluidAmount() < (BASE_COST_PER_TICK*efficiency))
				return false;
			
			SetActive(true);
			progression += efficiency;
			steamTank.drain(BASE_COST_PER_TICK * efficiency, true);
			
			if(progression >= MAX_PROGRESSION)
			{
				ItemStack inStack = inputStack.getStack(0);
				inStack.shrink(1);
				inputStack.setStackInSlot(0, inStack);
				targetStack = new ItemStack(itemStack.getItem(), targetStack.getCount() + itemStack.getCount(), itemStack.getMetadata());
				outputStack.setStackInSlot(0, targetStack);
				progression = 0;
			}
			return true;
		}
		return false;
	}
}
