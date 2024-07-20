package com.luneruniverse.minecraft.mod.nbteditor.tagreferences.specific.data;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.luneruniverse.minecraft.mod.nbteditor.multiversion.TextInst;
import com.luneruniverse.minecraft.mod.nbteditor.multiversion.nbt.NBTManagers;
import com.luneruniverse.minecraft.mod.nbteditor.tagreferences.general.ComponentTagReference;
import com.luneruniverse.minecraft.mod.nbteditor.tagreferences.general.TagReference;
import com.luneruniverse.minecraft.mod.nbteditor.tagreferences.specific.HideFlagsNBTTagReference;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.BlockPredicatesChecker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.text.Text;

public enum HideFlag implements TagReference<Boolean, ItemStack> {
	ENCHANTMENTS(TextInst.translatable("nbteditor.hide_flags.enchantments"), 1,
			getComponent(DataComponentTypes.ENCHANTMENTS, component -> component.showInTooltip, ItemEnchantmentsComponent::withShowInTooltip)),
	ATTRIBUTE_MODIFIERS(TextInst.translatable("nbteditor.hide_flags.attribute_modifiers"), 2,
			getComponent(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent::showInTooltip, AttributeModifiersComponent::withShowInTooltip)),
	UNBREAKABLE(TextInst.translatable("nbteditor.hide_flags.unbreakable"), 4,
			getComponent(DataComponentTypes.UNBREAKABLE, UnbreakableComponent::showInTooltip, UnbreakableComponent::withShowInTooltip)),
	CAN_BREAK(TextInst.translatable("nbteditor.hide_flags.can_" + (NBTManagers.COMPONENTS_EXIST ? "break" : "destroy")), 8,
			getComponent(DataComponentTypes.CAN_BREAK, BlockPredicatesChecker::showInTooltip, BlockPredicatesChecker::withShowInTooltip)),
	CAN_PLACE_ON(TextInst.translatable("nbteditor.hide_flags.can_place_on"), 16,
			getComponent(DataComponentTypes.CAN_PLACE_ON, BlockPredicatesChecker::showInTooltip, BlockPredicatesChecker::withShowInTooltip)),
	MISC(TextInst.translatable("nbteditor.hide_flags.misc"), 32,
			ComponentTagReference.forExistance(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)),
	DYED_COLOR(TextInst.translatable("nbteditor.hide_flags.dyed_color"), 64,
			getComponent(DataComponentTypes.DYED_COLOR, DyedColorComponent::showInTooltip, DyedColorComponent::withShowInTooltip)),
	
	TOOLTIP(TextInst.translatable("nbteditor.hide_flags.tooltip"), -1,
			ComponentTagReference.forExistance(DataComponentTypes.HIDE_TOOLTIP)),
	/**
	 * Was previously covered by MISC
	 */
	STORED_ENCHANTMENTS(TextInst.translatable("nbteditor.hide_flags.stored_enchantments"), -1,
			getComponent(DataComponentTypes.STORED_ENCHANTMENTS, component -> component.showInTooltip, ItemEnchantmentsComponent::withShowInTooltip)),
	TRIM(TextInst.translatable("nbteditor.hide_flags.trim"), -1,
			getComponent(DataComponentTypes.TRIM, component -> component.showInTooltip, ArmorTrim::withShowInTooltip));
	
	private static <C> ComponentTagReference<Boolean, C> getComponent(DataComponentType<C> component, Predicate<C> getter, BiFunction<C, Boolean, C> setter) {
		return new ComponentTagReference<>(component,
				null,
				componentValue -> componentValue == null ? false : !getter.test(componentValue),
				(componentValue, value) -> componentValue == null ? null : setter.apply(componentValue, value == null ? true : !value))
				.passNullValue();
	}
	
	private final Text text;
	private final int code;
	private final TagReference<Boolean, ItemStack> tagRef;
	
	private HideFlag(Text text, int code, ComponentTagReference<Boolean, ?> compTagRef) {
		this.text = text;
		this.code = code;
		this.tagRef = NBTManagers.COMPONENTS_EXIST ? compTagRef : new HideFlagsNBTTagReference(this);
	}
	
	public Text getText() {
		return text;
	}
	public boolean isOnlyForComponents() {
		return code <= 0;
	}
	public boolean isInThisVersion() {
		return code > 0 || NBTManagers.COMPONENTS_EXIST;
	}
	
	public boolean isEnabled(int code) {
		return (code & this.code) != 0;
	}
	public int set(int code, boolean enabled) {
		return enabled ? (code | this.code) : (code & ~this.code);
	}
	public int toggle(int code) {
		return (code & ~this.code) | (~code & this.code);
	}
	
	@Override
	public Boolean get(ItemStack object) {
		return tagRef.get(object);
	}
	@Override
	public void set(ItemStack object, Boolean value) {
		tagRef.set(object, value);
	}
}