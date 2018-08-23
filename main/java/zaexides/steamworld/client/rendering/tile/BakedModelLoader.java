package zaexides.steamworld.client.rendering.tile;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import zaexides.steamworld.ModInfo;

public class BakedModelLoader implements ICustomModelLoader
{
	public static final PipeModel PIPE_MODEL = new PipeModel();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) 
	{
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) 
	{
		if(modelLocation.getResourceDomain().equals(ModInfo.MODID))
		{
			switch(modelLocation.getResourcePath())
			{
				case "block_pipe":
					return true;
			}
		}
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception 
	{
		if(modelLocation.getResourceDomain().equals(ModInfo.MODID))
		{
			switch(modelLocation.getResourcePath())
			{
				case "block_pipe":
					return PIPE_MODEL;
			}
		}
		return null;
	}

}
