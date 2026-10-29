package com.reggarf.mods.reggadium.mixin.memory;

import com.mojang.authlib.GameProfile;
import com.reggarf.mods.reggadium.Config;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents FakePlayer advancement triggers from leaking memory (NeoForge Issue #1487).
 *
 * When FakePlayers (such as Create deployers, automated quarry blocks, or autocrafting users)
 * are initialized, their PlayerAdvancements listens to criterion triggers on the ServerLevel.
 * Since fake players never undergo normal player disconnection, these listeners permanently
 * anchor the ServerPlayer, ServerLevel, and IntegratedServer into RAM.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerAdvancementsMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void reggadium$detachFakePlayerAdvancements(
            MinecraftServer server,
            ServerLevel level,
            GameProfile gameProfile,
            ClientInformation clientInformation,
            CallbackInfo ci
    ) {
        if (Config.fixMemoryLeaksEnabled && Config.fixFakePlayerLeakEnabled) {
            if ((Object) this instanceof FakePlayer fakePlayer) {
                fakePlayer.getAdvancements().stopListening();
            }
        }
    }
}
