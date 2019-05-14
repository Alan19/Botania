/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:25:22 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PlatformVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.TileCamo;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockPlatform extends BlockCamo implements ILexiconable, IWandable, IManaCollisionGhost {

	public enum Variant {
		ABSTRUSE,
		SPECTRAL,
		INFRANGIBLE
	}

	public final Variant variant;

	public BlockPlatform(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	/* todo 1.13
	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB par5AxisAlignedBB, @Nonnull List<AxisAlignedBB> stacks, Entity par7Entity, boolean isActualState) {
		if(variant == Variant.INFRANGIBLE || variant == Variant.ABSTRUSE && par7Entity != null && par7Entity.posY > pos.getY() + 0.9 && (!(par7Entity instanceof EntityPlayer) || !par7Entity.isSneaking()))
			super.addCollisionBoxToList(state, world, pos, par5AxisAlignedBB, stacks, par7Entity, isActualState);
	}
	*/

	@Override
	public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
		return true;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return variant != Variant.INFRANGIBLE;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TilePlatform();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return variant == Variant.ABSTRUSE ? LexiconData.platform : variant == Variant.INFRANGIBLE ? null : LexiconData.spectralPlatform;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		TilePlatform tile = (TilePlatform) world.getTileEntity(pos);
		return tile.onWanded(player);
	}

	@Override
	public boolean isGhost(IBlockState state, World world, BlockPos pos) {
		return true;
	}
}