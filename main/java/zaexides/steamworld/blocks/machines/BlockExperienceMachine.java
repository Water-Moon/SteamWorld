package zaexides.steamworld.blocks.machines;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntityExperienceMachine;

public class BlockExperienceMachine extends BlockMachine implements ITileEntityProvider
{
	public int efficiency = 1;
	
	public BlockExperienceMachine(String name, float hardness, int efficiency) 
	{
		super(name, Material.IRON, hardness);
		this.efficiency = efficiency;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityExperienceMachine experienceMachine = new TileEntityExperienceMachine();
		setMachineStats(experienceMachine);
		return experienceMachine;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity) 
	{
		((TileEntityExperienceMachine)tileEntity).SetStats(efficiency);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntityExperienceMachine))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.EXPERIENCE_MACHINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
