package com.luneruniverse.minecraft.mod.nbteditor.tagreferences.general;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.luneruniverse.minecraft.mod.nbteditor.localnbt.LocalNBT;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface TagReference<T, O> {
	public static <T> TagReference<T, ItemStack> forItems(Supplier<T> defaultValue, TagReference<T, NbtCompound> tagRef) {
		return new TagReference<>() {
			@Override
			public T get(ItemStack object) {
				if (!object.manager$hasNbt())
					return defaultValue.get();
				return tagRef.get(object.manager$getNbt());
			}
			@Override
			public void set(ItemStack object, T value) {
				object.manager$modifyNbt(nbt -> tagRef.set(nbt, value));
			}
		};
	}
	public static <T, O extends LocalNBT> TagReference<T, O> forLocalNBT(Supplier<T> defaultValue, TagReference<T, NbtCompound> tagRef) {
		return new TagReference<>() {
			@Override
			public T get(O object) {
				NbtCompound nbt = object.getNBT();
				if (nbt == null)
					return defaultValue.get();
				return tagRef.get(nbt);
			}
			@Override
			public void set(O object, T value) {
				object.modifyNBT(nbt -> { tagRef.set(nbt, value); });
			}
		};
	}
	public static <C, O> TagReference<List<C>, O> forLists(Class<C> clazz, TagReference<C[], O> tagRef) {
		return new TagReference<>() {
			@Override
			public List<C> get(O object) {
				return new ArrayList<>(Arrays.asList(tagRef.get(object)));
			}
			@SuppressWarnings("unchecked")
			@Override
			public void set(O object, List<C> value) {
				tagRef.set(object, value.toArray(len -> (C[]) Array.newInstance(clazz, len)));
			}
		};
	}
	public static <V, O> TagReference<Map<String, V>, O> forMaps(Function<NbtElement, V> getter, Function<V, NbtElement> setter, TagReference<NbtCompound, O> tagRef) {
		return new TagReference<>() {
			@Override
			public Map<String, V> get(O object) {
				NbtCompound nbt = tagRef.get(object);
				Map<String, V> output = new HashMap<>();
				for (String key : nbt.getKeys()) {
					V entryValue = getter.apply(nbt.get(key));
					if (entryValue != null)
						output.put(key, entryValue);
				}
				return output;
			}
			@Override
			public void set(O object, Map<String, V> value) {
				NbtCompound nbt = new NbtCompound();
				value.forEach((key, entryValue) -> nbt.put(key, setter.apply(entryValue)));
				tagRef.set(object, nbt);
			}
		};
	}
	
	/**
	 * If T is a collection, this should return a mutable copy
	 */
	public T get(O object);
	public void set(O object, T value);
	
	public default void modify(O object, UnaryOperator<T> modifier) {
		set(object, modifier.apply(get(object)));
	}
	public default void modify(O object, Consumer<T> modifier) {
		modify(object, value -> {
			modifier.accept(value);
			return value;
		});
	}
}