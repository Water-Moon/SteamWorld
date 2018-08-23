package zaexides.steamworld.gui.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.containers.ContainerSimple;
import zaexides.steamworld.te.energy.TileEntitySteamGeneratorElectric;
import zaexides.steamworld.utility.SWGuiUtil;
import zaexides.steamworld.utility.interfaces.IGuiContainerUtil;

public class GuiSteamGeneratorElectric extends GuiContainer implements IGuiContainerUtil
{
	private TileEntitySteamGeneratorElectric tileEntity;
	private IInventory playerInv;
	
	public GuiSteamGeneratorElectric(EntityPlayer player, IInventory playerInv, TileEntitySteamGeneratorElectric tileEntity) 
	{
		super(new ContainerSimple(player, playerInv, tileEntity));
		
		xSize = 176;
		ySize = 165;
		
		this.tileEntity = tileEntity;
		this.playerInv = playerInv;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/machine/gui_generator_electric.png"));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		float progression = (float) tileEntity.energy.getEnergyStored() / (float) tileEntity.energy.getMaxEnergyStored();
		float yOff = 41 * progression;
		
		drawTexturedModalRect(guiLeft + 116, guiTop + 23 + (41 - yOff), 177, 15, 8, (int)yOff);
		
		SWGuiUtil.DrawFluid(this, tileEntity.fluidIn, mouseX, mouseY, 80, 23, 16, 41);
		SWGuiUtil.DrawFluid(this, tileEntity.fluidOut, mouseX, mouseY, 44, 23, 16, 41);
		
		tileEntity = (TileEntitySteamGeneratorElectric) tileEntity.getWorld().getTileEntity(tileEntity.getPos());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String fgString = I18n.format("container.steamworld.steam_generator");
		
		int x = xSize / 2 - mc.fontRenderer.getStringWidth(fgString) / 2;
		mc.fontRenderer.drawString(fgString, x, 6, 4210752);
		mc.fontRenderer.drawString(playerInv.getDisplayName().getFormattedText(), 8, 72, 4210752);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.fluidIn, mouseX, mouseY, 80, 23, 16, 41);
		SWGuiUtil.DrawFluidHoverText(this, tileEntity.fluidOut, mouseX, mouseY, 44, 23, 16, 41);
		
		List<String> text = new ArrayList<String>();
		text.add(new TextComponentTranslation("energy.name").getFormattedText());
		text.add(tileEntity.energy.getEnergyStored() + "/" + tileEntity.energy.getMaxEnergyStored() + " RF");
		SWGuiUtil.DrawHoverText(this, text, mouseX, mouseY, 116, 23, 8, 41);
	}

	@Override
	public boolean isInRegion(int x, int y, int w, int h, int mX, int mY) 
	{
		return isPointInRegion(x, y, w, h, mX, mY);
	}
}
