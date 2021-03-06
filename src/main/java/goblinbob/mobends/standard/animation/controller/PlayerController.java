package goblinbob.mobends.standard.animation.controller;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.animation.bit.KeyframeAnimationBit;
import goblinbob.mobends.core.animation.controller.IAnimationController;
import goblinbob.mobends.core.animation.keyframe.ArmatureMask;
import goblinbob.mobends.core.animation.layer.HardAnimationLayer;
import goblinbob.mobends.core.animation.layer.KeyframeAnimationLayer;
import goblinbob.mobends.standard.animation.bit.biped.*;
import goblinbob.mobends.standard.animation.bit.player.*;
import goblinbob.mobends.standard.animation.bit.player.SprintAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.WalkAnimationBit;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.animation.bit.biped.BowAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.EatingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.FallingAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.FlyingAnimationBit;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is an animation controller for a player instance.
 * It's a part of the EntityData structure.
 */
public class PlayerController implements IAnimationController<PlayerData>
{
	
	protected HardAnimationLayer<BipedEntityData<?>> layerBase;
	protected HardAnimationLayer<BipedEntityData<?>> layerTorch;
	protected HardAnimationLayer<BipedEntityData<?>> layerSneak;
	protected HardAnimationLayer<BipedEntityData<?>> layerAction;
	protected KeyframeAnimationLayer<PlayerData> layerKeyframe;

	protected AnimationBit<BipedEntityData<?>> bitStand, bitJump, bitSneak, bitLadderClimb,
			bitSwimming, bitRiding, bitSitting, bitFalling;
	protected AnimationBit<PlayerData> bitWalk, bitSprint, bitSprintJump;
	protected AnimationBit<BipedEntityData<?>> bitTorchHolding;
	protected AnimationBit<PlayerData> bitAttack;
	protected FlyingAnimationBit bitFlying;
	protected BowAnimationBit bitBow;
	protected EatingAnimationBit bitEating;
	protected KeyframeAnimationBit<BipedEntityData<?>> bitBreaking;

	protected ArmatureMask upperBodyOnlyMask;

	public PlayerController()
	{
		this.layerBase = new HardAnimationLayer<>();
		this.layerTorch = new HardAnimationLayer<>();
		this.layerSneak = new HardAnimationLayer<>();
		this.layerAction = new HardAnimationLayer<>();
		this.layerKeyframe = new KeyframeAnimationLayer<>();

		this.bitStand = new StandAnimationBit<>();
		this.bitJump = new JumpAnimationBit<>();
		this.bitSneak = new SneakAnimationBit();
		this.bitLadderClimb = new LadderClimbAnimationBit();
		this.bitSwimming = new SwimmingAnimationBit();
		this.bitWalk = new WalkAnimationBit();
		this.bitSprint = new SprintAnimationBit();
		this.bitSprintJump = new SprintJumpAnimationBit();
		this.bitRiding = new RidingAnimationBit();
		this.bitSitting = new SittingAnimationBit();
		this.bitFalling = new FallingAnimationBit();
		this.bitFlying = new FlyingAnimationBit();
		this.bitTorchHolding = new TorchHoldingAnimationBit();
		this.bitAttack = new AttackAnimationBit();
		this.bitBow = new BowAnimationBit();
		this.bitEating = new EatingAnimationBit();
		this.bitBreaking = new BreakingAnimationBit(1.2F);

		this.upperBodyOnlyMask = new ArmatureMask(ArmatureMask.Mode.EXCLUDE_ONLY);
		this.upperBodyOnlyMask.exclude("root");
		this.upperBodyOnlyMask.exclude("head");
		this.upperBodyOnlyMask.exclude("leftLeg");
		this.upperBodyOnlyMask.exclude("leftForeLeg");
		this.upperBodyOnlyMask.exclude("rightLeg");
		this.upperBodyOnlyMask.exclude("rightForeLeg");
	}

	public static boolean shouldPerformBreaking(PlayerData data)
	{
		final Item item = data.getEntity().getHeldItemMainhand().getItem();

		return item instanceof ItemPickaxe || item instanceof ItemAxe;
	}

	@Override
	public Collection<String> perform(PlayerData data)
	{
		final AbstractClientPlayer player = data.getEntity();
		final EnumHandSide primaryHand = player.getPrimaryHand();
		final EnumHandSide offHand = primaryHand == EnumHandSide.RIGHT ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
		final ItemStack heldItemMainhand = player.getHeldItemMainhand();
		final ItemStack heldItemOffhand = player.getHeldItemOffhand();
		final ItemStack activeStack = player.getActiveItemStack();
		ModelBiped.ArmPose armPoseMain = getAction(player, heldItemMainhand);
		ModelBiped.ArmPose armPoseOff = getAction(player, heldItemOffhand);
		final EnumHand activeHand = player.getActiveHand();
		final EnumHandSide activeHandSide = activeHand == EnumHand.MAIN_HAND ? primaryHand : offHand;

		if (player.isRiding())
		{
			if (player.getRidingEntity() instanceof EntityLivingBase)
			{
				this.layerBase.playOrContinueBit(bitRiding, data);
			}
			else
			{
				this.layerBase.playOrContinueBit(bitSitting, data);
			}
			this.layerSneak.clearAnimation();
			this.bitBreaking.setMask(this.upperBodyOnlyMask);
		}
		else
		{
			if (data.isClimbing())
			{
				this.layerBase.playOrContinueBit(bitLadderClimb, data);
				this.layerSneak.clearAnimation();
				this.layerTorch.clearAnimation();
				this.bitBreaking.setMask(this.upperBodyOnlyMask);
			}
			else if (player.isInWater())
			{
				this.layerBase.playOrContinueBit(bitSwimming, data);
				this.layerSneak.clearAnimation();
				this.layerTorch.clearAnimation();
				this.bitBreaking.setMask(this.upperBodyOnlyMask);
			}
			else if (!data.isOnGround() || data.getTicksAfterTouchdown() < 1)
			{
				// Airborne
				if (data.isFlying())
				{
					// Flying
					this.layerBase.playOrContinueBit(bitFlying, data);
				}
				else
				{
					if (data.getTicksFalling() > FallingAnimationBit.TICKS_BEFORE_FALLING)
					{
						this.layerBase.playOrContinueBit(bitFalling, data);
					}
					else
					{
						if (player.isSprinting())
							this.layerBase.playOrContinueBit(bitSprintJump, data);
						else
							this.layerBase.playOrContinueBit(bitJump, data);
					}
				}
				this.layerSneak.clearAnimation();
				this.layerTorch.clearAnimation();
				this.bitBreaking.setMask(this.upperBodyOnlyMask);
			}
			else
			{
				if (data.isStillHorizontally())
				{
					this.layerBase.playOrContinueBit(bitStand, data);
					this.layerTorch.playOrContinueBit(bitTorchHolding, data);
					this.bitBreaking.setMask(null);
				}
				else
				{
					if (player.isSprinting())
					{
						this.layerBase.playOrContinueBit(bitSprint, data);
						this.layerTorch.clearAnimation();
					}
					else
					{
						this.layerBase.playOrContinueBit(bitWalk, data);
						this.layerTorch.playOrContinueBit(bitTorchHolding, data);
					}
					this.bitBreaking.setMask(this.upperBodyOnlyMask);
				}

				if (player.isSneaking())
					this.layerSneak.playOrContinueBit(bitSneak, data);
				else
					this.layerSneak.clearAnimation();
			}
		}

		/*
		 * ACTIONS
		 */
		if (activeStack.getItem() instanceof ItemFood)
		{
			this.bitEating.setActionHand(activeHandSide);
			this.layerAction.playOrContinueBit(bitEating, data);
		}
		else
		{
			if (armPoseMain == ArmPose.BOW_AND_ARROW || armPoseOff == ArmPose.BOW_AND_ARROW)
			{
				this.bitBow.setActionHand(armPoseMain == ArmPose.BOW_AND_ARROW ? primaryHand : offHand);
				this.layerAction.playOrContinueBit(this.bitBow, data);
			}
			else if (heldItemMainhand.getItem() instanceof ItemPickaxe || heldItemMainhand.getItem() instanceof ItemAxe)
			{
				if (player.isSwingInProgress)
					this.layerAction.playOrContinueBit(this.bitBreaking, data);
				else
					this.layerAction.clearAnimation();
			}
			else
			{
				this.layerAction.playOrContinueBit(this.bitAttack, data);
			}
		}

		final List<String> actions = new ArrayList<>();
		layerBase.perform(data, actions);
		layerSneak.perform(data, actions);
		layerTorch.perform(data, actions);
		layerAction.perform(data, actions);
		layerKeyframe.perform(data, actions);
		return actions;
	}

	private ArmPose getAction(AbstractClientPlayer player, ItemStack heldItem)
	{
		if (!heldItem.isEmpty())
		{
			if (player.getItemInUseCount() > 0)
			{
				EnumAction enumaction = heldItem.getItemUseAction();

				if (enumaction == EnumAction.BLOCK)
					return ArmPose.BLOCK;
				else if (enumaction == EnumAction.BOW)
					return ArmPose.BOW_AND_ARROW;
			}

			return ArmPose.ITEM;
		}

		return ArmPose.EMPTY;
	}
	
}
