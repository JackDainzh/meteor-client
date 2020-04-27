package minegame159.meteorclient.modules.combat;

//Created by squidoodly 26/04/2020

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import minegame159.meteorclient.events.TickEvent;
import minegame159.meteorclient.modules.Category;
import minegame159.meteorclient.modules.ModuleManager;
import minegame159.meteorclient.modules.ToggleModule;
import minegame159.meteorclient.modules.player.AutoArmor;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.utils.InvUtils;
import minegame159.meteorclient.utils.Utils;
import net.minecraft.container.SlotActionType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Iterator;

public class AutoExp extends ToggleModule {
    public AutoExp(){super(Category.Combat, "auto-exp", "Throws exp to mend your armour (only works with diamond)");}

    private final Setting<Boolean> replenish = addSetting(new BoolSetting.Builder()
            .name("replenish")
            .description("Replenishes your hotbar with Exp Bottles")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> replenishCount = addSetting(new IntSetting.Builder()
            .name("items-left")
            .description("The number of items before the stack gets replenished")
            .defaultValue(32)
            .min(1)
            .max(63)
            .build()
    );

    private boolean wasActive = false;

    @Override
    public void onActivate(){
        if(ModuleManager.INSTANCE.get(AutoArmor.class).isActive()) {
            wasActive = true;
            ModuleManager.INSTANCE.get(AutoArmor.class).toggle();
        }
    }

    @Override
    public void onDeactivate() {
        if(wasActive) {
            ModuleManager.INSTANCE.get(AutoArmor.class).toggle();
        }
        InvUtils.clickSlot(1, 0, SlotActionType.QUICK_MOVE);
        InvUtils.clickSlot(2, 0, SlotActionType.QUICK_MOVE);
        InvUtils.clickSlot(3, 0, SlotActionType.QUICK_MOVE);
        InvUtils.clickSlot(4, 0, SlotActionType.QUICK_MOVE);
    }

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        Iterator<ItemStack> armour = mc.player.getArmorItems().iterator();
        ItemStack boots = armour.next();
        ItemStack leggings = armour.next();
        ItemStack chestplate = armour.next();
        ItemStack helmet = armour.next();
        if(!boots.isDamaged() && !leggings.isDamaged() && !chestplate.isDamaged() && !helmet.isDamaged() &&
                (findBrokenArmour(Items.DIAMOND_BOOTS) == -1) && (findBrokenArmour(Items.DIAMOND_LEGGINGS) == -1)
                && (findBrokenArmour(Items.DIAMOND_CHESTPLATE) == -1) && (findBrokenArmour(Items.DIAMOND_HELMET) == -1)) {
            this.toggle();
            Utils.sendMessage("#redNo broken armour with mending. Disabling!");
            return;
        }
        int slot = findExpInHotbar();
        if(slot == -1){
            Utils.sendMessage("#redNo Exp in hotbar. Disabling!");
        }else if(mc.player.inventory.getInvStack(slot).getCount() < replenishCount.get()){
            for(int i = 9; i < 36; i++){
                if(mc.player.inventory.getInvStack(i).getItem() == Items.EXPERIENCE_BOTTLE){
                    InvUtils.clickSlot(i, 0, SlotActionType.PICKUP_ALL);
                    InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP_ALL);
                    InvUtils.clickSlot(i, 0, SlotActionType.PICKUP_ALL);
                }
            }
        }

        //for boots
        slot = findBrokenArmour(Items.DIAMOND_BOOTS);
        boolean empty = false;
        empty = boots.isEmpty();
        if(!boots.isDamaged() && (slot != -1)){
            InvUtils.clickSlot(8, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(8, 0, SlotActionType.PICKUP);
            }
        }else if(!boots.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() != -1)){
            InvUtils.clickSlot(8, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(mc.player.inventory.getEmptySlot(), 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(8, 0, SlotActionType.PICKUP);
            }
        }else if(!boots.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() == -1)){
            InvUtils.clickSlot(8, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(searchCraftingSlots(), 0, SlotActionType.PICKUP);
        }

        //for leggings
        slot = findBrokenArmour(Items.DIAMOND_LEGGINGS);
        empty = leggings.isEmpty();
        if(!leggings.isDamaged() && (slot != -1)){
            InvUtils.clickSlot(7, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(7, 0, SlotActionType.PICKUP);
            }
        }else if(!leggings.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() != -1)){
            InvUtils.clickSlot(7, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(mc.player.inventory.getEmptySlot(), 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(7, 0, SlotActionType.PICKUP);
            }
        }else if(!leggings.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() == -1)){
            InvUtils.clickSlot(7, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(searchCraftingSlots(), 0, SlotActionType.PICKUP);
        }

        //for chestplate
        slot = findBrokenArmour(Items.DIAMOND_CHESTPLATE);
        empty = chestplate.isEmpty();
        if(!chestplate.isDamaged() && (slot != -1)){
            InvUtils.clickSlot(6, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(6, 0, SlotActionType.PICKUP);
            }
        }else if(!chestplate.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() != -1)){
            InvUtils.clickSlot(6, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(mc.player.inventory.getEmptySlot(), 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(6, 0, SlotActionType.PICKUP);
            }
        }else if(!chestplate.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() == -1)){
            InvUtils.clickSlot(6, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(searchCraftingSlots(), 0, SlotActionType.PICKUP);
        }

        //for helmet
        slot = findBrokenArmour(Items.DIAMOND_HELMET);
        empty = helmet.isEmpty();
        if(!helmet.isDamaged() && (slot != -1)){
            InvUtils.clickSlot(5, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(slot, 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(5, 0, SlotActionType.PICKUP);
            }
        }else if(!helmet.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() != -1)){
            InvUtils.clickSlot(5, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(mc.player.inventory.getEmptySlot(), 0, SlotActionType.PICKUP);
            if(!empty) {
                InvUtils.clickSlot(5, 0, SlotActionType.PICKUP);
            }
        }else if(!helmet.isDamaged() && (slot == -1) && (mc.player.inventory.getEmptySlot() == -1)){
            InvUtils.clickSlot(5, 0, SlotActionType.PICKUP);
            InvUtils.clickSlot(searchCraftingSlots(), 0, SlotActionType.PICKUP);
        }
    });

    private int findBrokenArmour(Item item){
        for(int i = 0; i <mc.player.inventory.getInvSize(); i++){
            ItemStack itemStack = mc.player.inventory.getInvStack(i);
            if(itemStack.isDamaged() && itemStack.getItem() == item && (EnchantmentHelper.getLevel(Enchantments.MENDING, itemStack) >= 1)){
                return i;
            }
        }
        return -1;
    }

    private int findExpInHotbar(){
        int slot = -1;
        for(int i = 0; i < 9; i++){
            if (mc.player.inventory.getInvStack(i).getItem() == Items.EXPERIENCE_BOTTLE){
                slot = i;
            }
        }
        return slot;
    }

    private int searchCraftingSlots(){
        int slot = -1;
        for(int i = 0; i < 5; i++){
            InvUtils.clickSlot(i, 0, SlotActionType.PICKUP);
            if(mc.player.inventory.getCursorStack().isEmpty()){
                slot = i;
            }else{
                InvUtils.clickSlot(i, 0, SlotActionType.PICKUP);
            }
        }
        return slot;
    }
}
