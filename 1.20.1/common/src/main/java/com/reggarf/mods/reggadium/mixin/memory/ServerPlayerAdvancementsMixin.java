package com.reggarf.mods.reggadium.mixin.memory;

import com.mojang.authlib.GameProfile;
import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.memory.ReggadiumMemoryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents FakePlayer advancement triggers from leaking memory.
 *
 * When FakePlayers are initialized, their PlayerAdvancements listens to criterion triggers.
 * By detaching listeners on fake player initialization, servers avoid holding references to dead worlds.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerAdvancementsMixin {

    @Inject(method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    private void reggadium$detachFakePlayerAdvancements(
            MinecraftServer server,
            ServerLevel level,
            GameProfile gameProfile,
            CallbackInfo ci
    ) {
        if (Config.fixMemoryLeaksEnabled && Config.fixFakePlayerLeakEnabled) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            if (ReggadiumMemoryManager.isFakePlayer(player)) {
                player.getAdvancements().stopListening();
            }
        }
    }
}
