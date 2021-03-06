package goblinbob.mobends.standard;

import goblinbob.mobends.standard.client.renderer.entity.mutated.SpiderRenderer;
import goblinbob.mobends.standard.client.renderer.entity.mutated.SquidRenderer;
import goblinbob.mobends.standard.client.renderer.entity.mutated.ZombieRenderer;
import goblinbob.mobends.standard.data.PigZombieData;
import goblinbob.mobends.standard.data.SpiderData;
import goblinbob.mobends.standard.data.SquidData;
import goblinbob.mobends.standard.data.ZombieData;
import goblinbob.mobends.standard.main.ModConfig;
import goblinbob.mobends.standard.mutators.PigZombieMutator;
import goblinbob.mobends.standard.mutators.SpiderMutator;
import goblinbob.mobends.standard.mutators.SquidMutator;
import goblinbob.mobends.core.addon.IAddon;
import goblinbob.mobends.core.bender.AddonAnimationRegistry;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.client.renderer.entity.ArrowTrailManager;
import goblinbob.mobends.standard.mutators.ZombieMutator;
import goblinbob.mobends.standard.previewer.BipedPreviewer;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import goblinbob.mobends.standard.previewer.SpiderPreviewer;
import goblinbob.mobends.standard.previewer.ZombiePreviewer;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;

public class DefaultAddon implements IAddon
{

	@Override
	public void registerAnimatedEntities(AddonAnimationRegistry registry)
	{
		registry.registerEntity(new PlayerBender());
		
		registry.registerNewEntity(EntityZombie.class, ZombieData::new, ZombieMutator::new, new ZombieRenderer<>(),
				new ZombiePreviewer(),
				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
		
//		registry.registerNewEntity(EntityZombieVillager.class, ZombieVillagerData::new, ZombieVillagerMutator::new, new ZombieRenderer<>(),
//				new BipedPreviewer<>(),
//				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
//				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
//
		registry.registerNewEntity(EntityPigZombie.class, PigZombieData::new, PigZombieMutator::new, new ZombieRenderer<>(),
				new BipedPreviewer<>(),
				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");

		registry.registerNewEntity(EntitySpider.class, SpiderData::new, SpiderMutator::new, new SpiderRenderer<>(),
				new SpiderPreviewer(),
				"head", "body", "neck", "leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7", "leg8",
				"foreLeg1", "foreLeg2", "foreLeg3", "foreLeg4", "foreLeg5", "foreLeg6", "foreLeg7", "foreLeg8");

		registry.registerNewEntity(EntitySquid.class, SquidData::new, SquidMutator::new, new SquidRenderer<>(),
				"body", "tentacle1", "tentacle2", "tentacle3", "tentacle4", "tentacle5", "tentacle6", "tentacle7", "tentacle8");
		
//		registry.registerEntity(new AnimatedEntity(EntityHusk.class,
//						new RenderBendsHusk(Minecraft.getMinecraft().getRenderManager()),
//						new String[] { "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm", "leftLeg",
//								"rightLeg", "leftForeLeg", "rightForeLeg" }));
//		registry.registerEntity(new AnimatedEntity(EntityCaveSpider.class,
//						new RenderBendsCaveSpider(Minecraft.getMinecraft().getRenderManager()),
//						new String[] { "head", "body", "neck", "leg1", "leg2", "leg3", "leg4", "leg5", "leg6", "leg7",
//								"leg8", "foreLeg1", "foreLeg2", "foreLeg3", "foreLeg4", "foreLeg5", "foreLeg6",
//								"foreLeg7", "foreLeg8" }));
//		registry.registerEntity(new AnimatedEntity(EntitySkeleton.class,
//						new RenderBendsSkeleton(Minecraft.getMinecraft().getRenderManager()),
//						new String[] { "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm", "leftLeg",
//								"rightLeg", "leftForeLeg", "rightForeLeg" }));
//		registry.registerEntity(new AnimatedEntity(EntityWitherSkeleton.class,
//						new RenderBendsWitherSkeleton(Minecraft.getMinecraft().getRenderManager()),
//						new String[] { "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm", "leftLeg",
//								"rightLeg", "leftForeLeg", "rightForeLeg" }));
//		registry.registerEntity(new AnimatedEntity(EntityStray.class,
//						new RenderBendsStray(Minecraft.getMinecraft().getRenderManager()),
//						new String[] { "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm", "leftLeg",
//								"rightLeg", "leftForeLeg", "rightForeLeg" }));
	}

	@Override
	public void onRenderTick(float partialTicks)
	{
		if (ModConfig.showArrowTrails)
			ArrowTrailManager.onRenderTick();
		PlayerPreviewer.updatePreviewData(partialTicks);
	}
	
	@Override
	public void onClientTick()
	{
		PlayerPreviewer.updatePreviewDataClient();
	}
	
	@Override
	public void onRefresh()
	{
		ArmorModelFactory.refresh();
	}
	
	@Override
	public String getDisplayName()
	{
		return "Default";
	}
	
}
