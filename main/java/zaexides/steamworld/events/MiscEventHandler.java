package zaexides.steamworld.events;

import java.time.LocalDate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.items.ItemTreasure;
import zaexides.steamworld.utility.interfaces.IBlockBreakInterruptor;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class MiscEventHandler 
{
	private final String MANUAL_TAG = "sw_manual_got";
	private final String BDAY_TAG = "sw_bday_year";
	
	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent loadEvent)
	{
		ResourceLocation eventName = loadEvent.getName();
		final int FISH_TREASURE_METADATA = ItemTreasure.EnumTreasure.FISH.getMeta();
		final int END_TREASURE_METADATA = ItemTreasure.EnumTreasure.END.getMeta();
		
		//Modified here and not in LootTableInitializer since that'd throw a ConcurrentModificationException
		if(eventName.equals(LootTableList.GAMEPLAY_FISHING_TREASURE))
		{
			LootEntry lootEntry = new LootEntryItem(ItemInitializer.TREASURE, 1, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(FISH_TREASURE_METADATA)) }, new LootCondition[0], "steamworld_treasure");
			loadEvent.getTable().getPool("main").addEntry(lootEntry);
		}
		else if(eventName.equals(LootTableList.CHESTS_END_CITY_TREASURE))
		{
			LootEntry lootEntry = new LootEntryItem(ItemInitializer.TREASURE, 10, 0, new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(END_TREASURE_METADATA)) }, new LootCondition[0], "steamworld_treasure");
			loadEvent.getTable().getPool("main").addEntry(lootEntry);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent loggedInEvent)
	{
		GiveManualOnSpawn(loggedInEvent);
		GiveBdayTreasureOnSpawn(loggedInEvent);
	}
	
	private void GiveManualOnSpawn(PlayerEvent.PlayerLoggedInEvent loggedInEvent)
	{
		if(ConfigHandler.spawnWithManual)
		{
			NBTTagCompound tagCompound = loggedInEvent.player.getEntityData();
			if(!tagCompound.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				tagCompound.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			NBTTagCompound persistingData = tagCompound.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			
			if(!persistingData.getBoolean(MANUAL_TAG))
			{
				ItemHandlerHelper.giveItemToPlayer(loggedInEvent.player, new ItemStack(ItemInitializer.BOOK_MANUAL));
				persistingData.setBoolean(MANUAL_TAG, true);
			}
		}
	}
	
	private void GiveBdayTreasureOnSpawn(PlayerEvent.PlayerLoggedInEvent loggedInEvent)
	{
		LocalDate localDate = LocalDate.now();
		
		if(localDate.getDayOfMonth() == 14 && localDate.getMonthValue() == 7)
		{
			NBTTagCompound tagCompound = loggedInEvent.player.getEntityData();
			if(!tagCompound.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				tagCompound.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			NBTTagCompound persistingData = tagCompound.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			
			if(!persistingData.hasKey(BDAY_TAG) || persistingData.getInteger(BDAY_TAG) != localDate.getYear())
			{
				ItemHandlerHelper.giveItemToPlayer(loggedInEvent.player, new ItemStack(ItemInitializer.TREASURE, 1, ItemTreasure.EnumTreasure.BIRTHDAY.getMeta()));
				persistingData.setInteger(BDAY_TAG, localDate.getYear());
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBroken(BlockEvent.BreakEvent event)
	{
		if(event.getState().getBlock() instanceof IBlockBreakInterruptor && !event.getPlayer().isCreative())
			event.setCanceled(!((IBlockBreakInterruptor)event.getState().getBlock()).CanBreakBlock(event.getState(), event.getPlayer(), ForgeHooks.canHarvestBlock(event.getState().getBlock(), event.getPlayer(), event.getWorld(), event.getPos())));
	}
}
