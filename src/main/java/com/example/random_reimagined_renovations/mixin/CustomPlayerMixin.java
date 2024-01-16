package com.example.random_reimagined_renovations.mixin;

import com.example.random_reimagined_renovations.RandomReimaginedRenovations;
import net.minecraft.entity.EquipmentSlot;
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
        ItemStack damagedArmor = player.getEquippedStack(EquipmentSlot.CHEST);
        boolean foundCompatibleArmor = false;

        for (ItemStack armorItemStack : player.getArmorItems()) {
            if (!((LivingEntity) (Object) this).getWorld().isClient()) {
                Item item = armorItemStack.getItem();
                if (item instanceof ArmorItem) {
                    ArmorItem armorItem = (ArmorItem) item;
                    int maxDurability = armorItem.getMaxDamage();
                    int currentDurability = maxDurability - armorItemStack.getDamage();
                    String armorSlot = armorItem.getSlotType().getName();
                    boolean didIRun = false;
                    player.sendMessage(Text.literal("Your " + armorSlot + " has " + currentDurability + " of durability."));

                    if (!foundCompatibleArmor && currentDurability < 20) {
                        for (ItemStack stack : player.getInventory().main) {
                            Item item1 = stack.getItem();
                            RandomReimaginedRenovations.LOGGER.info(String.valueOf(item1));
                            if (item1 instanceof ArmorItem) {
                                RandomReimaginedRenovations.LOGGER.info("Item1 is an armor item.");
                                ArmorItem armorItem1 = (ArmorItem) item1;
                                if (armorItem1.getSlotType() == EquipmentSlot.CHEST && !stack.equals(damagedArmor)) {
                                    int maxDurability1 = armorItem1.getMaxDamage();
                                    int currentDurability1 = maxDurability1 - stack.getDamage();
                                    if (currentDurability1 > 19) {
                                        RandomReimaginedRenovations.LOGGER.info(String.valueOf(stack.getItem()));
                                        player.getInventory().armor.set(2, ItemStack.EMPTY);
                                        RandomReimaginedRenovations.LOGGER.info("/");
                                        player.getInventory().armor.set(2, stack.copy()); // Equip the inventory chestplate
                                        RandomReimaginedRenovations.LOGGER.info("ok");
                                        player.getInventory().main.set(player.getInventory().main.indexOf(stack), damagedArmor.copy());
                                        RandomReimaginedRenovations.LOGGER.info("onh");
                                        //player.getInventory().main.set(player.getInventory().main.indexOf(stack), damagedArmor.copy());
                                        RandomReimaginedRenovations.LOGGER.info("Swapped armor due to low durability.");
                                        didIRun = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!foundCompatibleArmor && didIRun == false) {
                            RandomReimaginedRenovations.LOGGER.info("I ran");
                            boolean hasOpenSlot = false;
                            for (int i = 0; i < player.getInventory().size(); i++) {
                                if (player.getInventory().getStack(i).isEmpty()) {
                                    hasOpenSlot = true;
                                    break;
                                }
                            }
                            if (hasOpenSlot == false) {
                                player.dropItem(damagedArmor.copy(), true, false);
                                player.getInventory().armor.set(2, ItemStack.EMPTY);
                                break;
                            } else if (hasOpenSlot) {
                                for (int i = 0; i < player.getInventory().main.size(); i++) {
                                    ItemStack stack1 = player.getInventory().main.get(i);
                                    if (stack1.isEmpty()) {
                                        player.getInventory().main.set(i, damagedArmor.copy());
                                        player.getInventory().armor.set(2, ItemStack.EMPTY);
                                        break;
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
}
