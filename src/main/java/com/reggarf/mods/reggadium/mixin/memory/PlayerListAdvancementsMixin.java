package com.reggarf.mods.reggadium.mixin.memory;

import com.reggarf.mods.reggadium.Config;
import com.reggarf.mods.reggadium.memory.ReggadiumMemoryManager;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sandboxes FakePlayer advancement tracking away from the main PlayerList advancement cache.
 *
 * In vanilla, PlayerList.advancements stores advancements keyed by UUID permanently.
 * By sandboxing fake players into Reggadium's unload-cleared map, fake players are
 * promptly garbage collected when levels unload.
 */
@Mixin(PlayerList.class)
public class PlayerListAdvancementsMixin {

    @Inject(method = "getPlayerAdvancements", at = @At("HEAD"), cancellable = true)
    private void reggadium$sandboxFakePlayerAdvancements(
            ServerPlayer player,
            CallbackInfoReturnable<PlayerAdvancements> cir
    ) {
        if (Config.fixMemoryLeaksEnabled && Config.fixFakePlayerLeakEnabled && player instanceof FakePlayer fakePlayer) {
            PlayerAdvancements adv = ReggadiumMemoryManager.getOrCreateFakePlayerAdvancements(
                    fakePlayer,
                    (PlayerList) (Object) this
            );
            if (adv != null) {
                cir.setReturnValue(adv);
            }
        }
    }
}
