/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2014, 7:46:05 PM (GMT)]
 */
package vazkii.botania.client.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.AnyComponent;
import vazkii.botania.api.wiki.IWikiProvider;
import vazkii.botania.api.wiki.WikiHooks;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.handler.BoundTileRenderer;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.ColorHandler;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.CorporeaAutoCompleteHandler;
import vazkii.botania.client.core.handler.LayerTerraHelmet;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.fx.FXWisp;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.botania.client.render.entity.LayerGaiaHead;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.client.render.entity.RenderCorporeaSpark;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderManaStorm;
import vazkii.botania.client.render.entity.RenderPinkWither;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.entity.RenderPoolMinecart;
import vazkii.botania.client.render.entity.RenderSnowballStack;
import vazkii.botania.client.render.entity.RenderSpark;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.item.ItemSextant.MultiblockSextant;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;

public class ClientProxy implements IProxy {

	public static final VertexFormat POSITION_TEX_LMAP_NORMAL =
			new VertexFormat()
					.addElement(DefaultVertexFormats.POSITION_3F)
					.addElement(DefaultVertexFormats.TEX_2F)
					.addElement(DefaultVertexFormats.TEX_2S)
					.addElement(DefaultVertexFormats.NORMAL_3B);
	public static final VertexFormat POSITION_TEX_LMAP =
			new VertexFormat()
					.addElement(DefaultVertexFormats.POSITION_3F)
					.addElement(DefaultVertexFormats.TEX_2F)
					.addElement(DefaultVertexFormats.TEX_2S);
	public static boolean jingleTheBells = false;
	public static boolean dootDoot = false;

	private static final ModelBiped EMPTY_MODEL = new ModelBiped();
	static {
		EMPTY_MODEL.setVisible(false);
	}

	public static KeyBinding CORPOREA_REQUEST;

	@Override
	public void registerHandlers() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		PersistentVariableHelper.setCacheFile(new File(Minecraft.getInstance().gameDir, "BotaniaVars.dat"));
		try {
			PersistentVariableHelper.load();
			PersistentVariableHelper.save();
		} catch (IOException e) {
			Botania.LOGGER.fatal("Persistent Variables couldn't load!!");
		}

		MinecraftForge.EVENT_BUS.register(MiscellaneousIcons.INSTANCE);

		ModChallenges.init();

		if(ConfigHandler.CLIENT.boundBlockWireframe.get())
			MinecraftForge.EVENT_BUS.register(BoundTileRenderer.class);

		if(ConfigHandler.CLIENT.enableSeasonalFeatures.get()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2)
				jingleTheBells = true;
			if(now.getMonth() == Month.OCTOBER)
				dootDoot = true;
		}

		DeferredWorkQueue.runLater(() -> {
			CORPOREA_REQUEST = new KeyBinding("nei.options.keys.gui.botania_corporea_request", KeyConflictContext.GUI, InputMappings.getInputByCode(GLFW.GLFW_KEY_C, 0), LibMisc.MOD_NAME);
			ClientRegistry.registerKeyBinding(ClientProxy.CORPOREA_REQUEST);
		});
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
	    DeferredWorkQueue.runLater(() -> {
			ShaderHelper.initShaders();
			initAuxiliaryRender();
			ColorHandler.init();
		});
		CorporeaAutoCompleteHandler.updateItemList();
	}

	private void initAuxiliaryRender() {
		Map<String, RenderPlayer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new ContributorFancinessHandler());
		if(Botania.curiosLoaded)
			render.addLayer(new BaubleRenderHandler());
		render.addLayer(new LayerTerraHelmet());
		render.addLayer(new LayerGaiaHead(render.getMainModel().bipedHead));

		render = skinMap.get("slim");
		render.addLayer(new ContributorFancinessHandler());
		if(Botania.curiosLoaded)
			render.addLayer(new BaubleRenderHandler());
		render.addLayer(new LayerTerraHelmet());
		render.addLayer(new LayerGaiaHead(render.getMainModel().bipedHead));
	}

	@Override
	public void setEntryToOpen(LexiconEntry entry) {
		GuiLexicon.currentOpenLexicon = new GuiLexiconEntry(entry, new GuiLexiconIndex(entry.category));
	}

	@Override
	public void setToTutorialIfFirstLaunch() {
		if(PersistentVariableHelper.firstLoad)
			GuiLexicon.currentOpenLexicon = new GuiLexiconEntry(LexiconData.welcome, new GuiLexiconEntry(LexiconData.tutorial, new GuiLexicon())).setFirstEntry();
	}

	@Override
	public void setLexiconStack(ItemStack stack) {
		GuiLexicon.stackUsed = stack;
	}

	@Override
	public boolean isTheClientPlayer(EntityLivingBase entity) {
		return entity == Minecraft.getInstance().player;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return ItemMonocle.hasMonocle(Minecraft.getInstance().player);
	}

	@Override
	public boolean openWikiPage(World world, Block block, RayTraceResult pos) {
		if(ConfigHandler.CLIENT.lexicaOfflineMode.get())
			return false;
		IWikiProvider wiki = WikiHooks.getWikiFor(block);
		String url = wiki.getWikiURL(world, pos, Minecraft.getInstance().player);
		if(url != null && !url.isEmpty()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String getLastVersion() {
		String s = PersistentVariableHelper.lastBotaniaVersion;

		if(s == null)
			return "N/A";

		if(s.indexOf("-") > 0)
			return s.split("-")[1];

		return s;
	}

	@Override
	public long getWorldElapsedTicks() {
		return ClientTickHandler.ticksInGame;
	}

	@Override
	public void setMultiblock(World world, int x, int y, int z, double radius, Block block) {
		MultiblockSextant mb = new MultiblockSextant();

		int iradius = (int) radius + 1;
		for(int i = 0; i < iradius * 2 + 1; i++)
			for(int j = 0; j < iradius * 2 + 1; j++) {
				int xp = x + i - iradius;
				int zp = z + j - iradius;

				if((int) Math.floor(MathHelper.pointDistancePlane(xp, zp, x, z)) == iradius - 1)
					mb.addComponent(new AnyComponent(new BlockPos(xp - x, 1, zp - z), block.getDefaultState()));
			}

		MultiblockRenderHandler.setMultiblock(mb.makeSet());
		MultiblockRenderHandler.anchor = new BlockPos(x, y, z);
	}

	@Override
	public void removeSextantMultiblock() {
		MultiblockSet set = MultiblockRenderHandler.currentMultiblock;
		if(set != null) {
			Multiblock mb = set.getForFacing(EnumFacing.SOUTH);
			if(mb instanceof MultiblockSextant)
				MultiblockRenderHandler.setMultiblock(null);
		}
	}

	private static boolean noclipEnabled = false;
	private static boolean corruptSparkle = false;

	@Override
	public void setSparkleFXNoClip(boolean noclip) {
		noclipEnabled = noclip;
	}

	@Override
	public void setSparkleFXCorrupt(boolean corrupt) {
		corruptSparkle = corrupt;
	}

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		if(!doParticle() && !fake)
			return;

		FXSparkle sparkle = new FXSparkle(Minecraft.getInstance().world, x, y, z, size, r, g, b, m);
		sparkle.fake = fake;
		sparkle.setCanCollide(!fake);
		if(noclipEnabled)
			sparkle.setCanCollide(false);
		if(corruptSparkle)
			sparkle.corrupt = true;
		Minecraft.getInstance().particles.addEffect(sparkle);
	}

	private static boolean distanceLimit = true;
	private static boolean depthTest = true;

	@Override
	public void setWispFXDistanceLimit(boolean limit) {
		distanceLimit = limit;
	}

	@Override
	public void setWispFXDepthTest(boolean test) {
		depthTest = test;
	}

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		if(!doParticle())
			return;

		FXWisp wisp = new FXWisp(Minecraft.getInstance().world, x, y, z, size, r, g, b, distanceLimit, depthTest, maxAgeMul);
		wisp.setSpeed(motionx, motiony, motionz);
		Minecraft.getInstance().particles.addEffect(wisp);
	}

	private boolean doParticle() {
		if(Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			return false;

		if(!ConfigHandler.CLIENT.useVanillaParticleLimiter.get())
			return true;

		float chance = 1F;
		if(Minecraft.getInstance().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if(Minecraft.getInstance().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}

	@Override
	public void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		Minecraft.getInstance().particles.addEffect(new FXLightning(Minecraft.getInstance().world, vectorStart, vectorEnd, ticksPerMeter, seed, colorOuter, colorInner));
	}

	@Override
	public void addBoss(IBotaniaBoss boss) {
		BossBarHandler.bosses.add(boss);
	}

	@Override
	public void removeBoss(IBotaniaBoss boss) {
		BossBarHandler.bosses.remove(boss);
	}

	@Override
	public int getClientRenderDistance() {
		return Minecraft.getInstance().gameSettings.renderDistanceChunks;
	}

	@Override
	public ModelBiped getEmptyModelBiped() {
		return EMPTY_MODEL;
	}
}

