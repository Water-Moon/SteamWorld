package zaexides.steamworld.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.world.storage.loot.conditions.RandomChanceWithLootingAndLuck;
import zaexides.steamworld.world.storage.loot.conditions.RandomChanceWithLuck;

public class LootTableInitializer 
{
	public static final ResourceLocation ANCITE_CRYPT = register("chests/crypt/ancite_crypt");
	public static final ResourceLocation WITHER_LAB = register("chests/wither_lab/wither_lab");
	
	public static final ResourceLocation TOWER_FOOD = register("chests/tower/tower_food");
	public static final ResourceLocation TOWER_LIBRARY = register("chests/tower/tower_library");
	public static final ResourceLocation TOWER_RESIDENCE = register("chests/tower/tower_residence");
	
	public static final ResourceLocation TREASURE_BOX_FISHING = register("treasure_box/fishing");
	public static final ResourceLocation TREASURE_BOX_DUNGEON = register("treasure_box/dungeon");
	public static final ResourceLocation TREASURE_BOX_END = register("treasure_box/end");
	public static final ResourceLocation TREASURE_BOX_BIRTHDAY = register("treasure_box/bday");
	public static final ResourceLocation TREASURE_BOX_WITHER_LAB = register("treasure_box/wither_lab");
			
	public static final ResourceLocation DROPS_SKYFISH = register("entities/skyfish");
	public static final ResourceLocation DROPS_ANEMONE = register("entities/anemone");
	public static final ResourceLocation DROPS_GLOWDUSTY = register("entities/glowdusty");
	
	private static ResourceLocation register(String location)
	{
		return LootTableList.register(new ResourceLocation(ModInfo.MODID, location));
	}
	
	public static void registerConditions()
	{
		RandomChanceWithLootingAndLuck.Register();
		RandomChanceWithLuck.Register();
	}
}
