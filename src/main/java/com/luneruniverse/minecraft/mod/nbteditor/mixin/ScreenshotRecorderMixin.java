package com.luneruniverse.minecraft.mod.nbteditor.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.luneruniverse.minecraft.mod.nbteditor.misc.MixinLink;
import com.luneruniverse.minecraft.mod.nbteditor.multiversion.MultiVersionMisc;
import com.luneruniverse.minecraft.mod.nbteditor.screens.ConfigScreen;
import com.luneruniverse.minecraft.mod.nbteditor.util.MainUtil;

import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
	@ModifyVariable(method = "saveScreenshotInner", at = @At("HEAD"), ordinal = 0)
	private static Consumer<Text> saveScreenshotInner(Consumer<Text> receiver) {
		if (!ConfigScreen.isScreenshotOptions())
			return receiver;
		return msg -> receiver.accept(MainUtil.attachFileTextOptions(MultiVersionMisc.copyText(msg), MixinLink.screenshotTarget));
	}
}
