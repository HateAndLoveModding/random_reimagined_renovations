package com.example.random_reimagined_renovations.CustomBlockClasses.entity;

import com.example.random_reimagined_renovations.recipe.FreezerRecipe;
import com.example.random_reimagined_renovations.screen.FreezerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static com.example.random_reimagined_renovations.RandomReimaginedRenovations.FREEZER_BLOCK_ENTITY;

public class FreezerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private static final int ICE_TRAY = 0;
    private static final int WATER = 1;
    private static final int BUCKET = 3;
    private static final int OUTPUT_SLOT = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;

    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(FREEZER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FreezerBlockEntity.this.progress;
                    case 1 -> FreezerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FreezerBlockEntity.this.progress = value;
                    case 1 -> FreezerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Freezer");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("freezer.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("freezer.progress");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FreezerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, FreezerBlockEntity entity) {
        if(world.isClient()) {
            return;
        }

        if(entity.isOutputSlotEmptyOrReceivable()) {
            if(hasRecipe(entity)) {
                entity.progress++;
                markDirty(world, pos, state);

                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                    entity.resetProgress();
                }
            } else {
                entity.resetProgress();
            }
        } else {
            entity.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private static void craftItem(FreezerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());

        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<FreezerRecipe> recipe = Objects.requireNonNull(entity.getWorld()).getRecipeManager().getFirstMatch(FreezerRecipe.Type.INSTANCE, inventory, entity.getWorld());;
        if(hasRecipe(entity)) {
            entity.removeStack(ICE_TRAY, 1);
            entity.removeStack(WATER, 1);

            entity.setStack(OUTPUT_SLOT, new ItemStack(recipe.get().getOutput1().getItem(),
                    entity.getStack(OUTPUT_SLOT).getCount() + recipe.get().getOutput1().getCount()));
            entity.setStack(BUCKET, new ItemStack(Items.BUCKET,
                    entity.getStack(BUCKET).getCount() + 1));
        }
    }

    private static boolean hasRecipe(FreezerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<FreezerRecipe> recipe = Objects.requireNonNull(entity.getWorld()).getRecipeManager().getFirstMatch(FreezerRecipe.Type.INSTANCE, inventory, entity.getWorld());;

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, recipe.get().getOutput1().getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item item) {
        return inventory.getStack(OUTPUT_SLOT).getItem() == item || inventory.getStack(OUTPUT_SLOT).isEmpty() && inventory.getStack(BUCKET).getItem() == item || inventory.getStack(BUCKET).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(OUTPUT_SLOT).getCount() <= inventory.getStack(OUTPUT_SLOT).getMaxCount() && inventory.getStack(BUCKET).getCount() + 1 <= inventory.getStack(BUCKET).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount() && this.getStack(BUCKET).isEmpty() || this.getStack(BUCKET).getCount() < this.getStack(BUCKET).getMaxCount();
    }
}

/*
    @Override
    public void markDirty() {
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    private Optional<RecipeEntry<FreezerRecipe>> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());

        for(int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        return Objects.requireNonNull(getWorld()).getRecipeManager().getFirstMatch(FreezerRecipe.Type.INSTANCE, inv, getWorld());
    }
 */
/*
    private static void craftItem(GemInfusingBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<GemInfusingRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(GemInfusingRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)) {
            entity.removeStack(1, 1);

            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(GemInfusingBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<GemInfusingRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(GemInfusingRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

 */
    /*

    public static void tick(World world, BlockPos blockPos, BlockState state, GemInfusingBlockEntity entity) {
        if(world.isClient()) {
            return;
        }

        if(hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

     */