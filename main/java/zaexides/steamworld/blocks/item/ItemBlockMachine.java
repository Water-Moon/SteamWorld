package zaexides.steamworld.blocks.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.machines.BlockMachine;
import zaexides.steamworld.utility.interfaces.IMetaName;

public class ItemBlockMachine extends ItemBlock
{
	private List<String> tooltips;
	
	public ItemBlockMachine(Block block)
	{
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	public ItemBlockMachine AddToolTip(String tooltipUnlocalized)
	{
		if(tooltips == null)
			tooltips = new ArrayList<String>();
		tooltips.add(I18n.format(tooltipUnlocalized));
		return this;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(tooltips != null)
			tooltip.addAll(tooltips);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		String unlocalizedName = super.getUnlocalizedName();
		if((stack.getMetadata() & 4) == 4)
			unlocalizedName += "_ht";
		return unlocalizedName;
	}
	
	@Override
	public int getMetadata(int damage) 
	{
		return damage;
	}
}
