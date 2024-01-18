package com.example.random_reimagined_renovations.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class CustomPlayerMixin {

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack damagedArmor;

        for (ItemStack armorItemStack : player.getArmorItems()) {
            if (!((LivingEntity) (Object) this).getWorld().isClient()) {
                Item item = armorItemStack.getItem();
                if (item instanceof ArmorItem armorItem) {
                    int maxDurability = armorItem.getMaxDamage();
                    int currentDurability = maxDurability - armorItemStack.getDamage();
                    boolean didIRun = false;

                    if (currentDurability < 20) {
                        for (ItemStack stack : player.getInventory().main) {
                            Item item1 = stack.getItem();
                            if (item1 instanceof ArmorItem armorItem1) {
                                int maxDurability1 = armorItem1.getMaxDamage();
                                int currentDurability1 = maxDurability1 - stack.getDamage();
                                if (currentDurability1 > 19) {
                                    damagedArmor = player.getEquippedStack(armorItem1.getSlotType());
                                    player.getInventory().armor.set(armorItem.getSlotType().getEntitySlotId(), stack.copy()); // Equip the inventory chestplate
                                    player.getInventory().main.set(player.getInventory().main.indexOf(stack), damagedArmor.copy());
                                    didIRun = true;
                                    player.sendMessage(Text.literal("Your " + armorItem1 + " was swapped with one with more durability."));
                                    break;
                                }
                            }
                        }
                        if (!didIRun) {
                            damagedArmor = player.getEquippedStack(armorItem.getSlotType());
                            boolean hasOpenSlot = false;
                            for (int i = 0; i < player.getInventory().main.size(); i++) {
                                if (player.getInventory().getStack(i).isEmpty()) {
                                    hasOpenSlot = true;
                                    break;
                                }
                            }
                            if (!hasOpenSlot) {
                                player.dropItem(damagedArmor.copy(), true, false);
                                player.getInventory().armor.set(armorItem.getSlotType().getEntitySlotId(), ItemStack.EMPTY);
                                player.sendMessage(Text.literal("Your " + armorItem + " was dropped because your inventory is full."));
                            } else {
                                for (int i = 0; i < player.getInventory().main.size(); i++) {
                                    ItemStack stack1 = player.getInventory().main.get(i);
                                    if (stack1.isEmpty()) {
                                        player.getInventory().main.set(i, damagedArmor.copy());
                                        player.getInventory().armor.set(armorItem.getSlotType().getEntitySlotId(), ItemStack.EMPTY);
                                        player.sendMessage(Text.literal("Your " + armorItem + " was moved to an open slot in you inventory."));
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
