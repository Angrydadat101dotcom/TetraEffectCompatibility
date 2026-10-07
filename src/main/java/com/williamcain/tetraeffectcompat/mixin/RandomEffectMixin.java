package com.williamcain.tetraeffectcompat.mixin;


import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.IForgeRegistry;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;



@Mixin(targets = "fr.bck.tetralibs.RandomEffect", remap = false)
public abstract class RandomEffectMixin {

    /*
    * Tetra Libs selects random MobEffects from Forge's full effect registry.
    *
    * Craft-Vania effects are designed to run through Craft-Vania's own
    * procedures and may expect context that is not available when Tetra Libs
    * applies them directly to Tetra Nova Mythic Mobs.
    *
    * Filter Craft-Vania's namespace from the collection seen by RandomEffect
    * without removing or disabling those effects elsewhere in the game.
    */

    @Redirect(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/registries/IForgeRegistry;getValues()Ljava/util/Collection;",
            remap = false

        )

    )
    private static Collection<MobEffect> tetraEffectCompat$filterEffects(
            IForgeRegistry<MobEffect> registry) {

        Collection<MobEffect> effects = new ArrayList<>(registry.getValues());

        effects.removeIf(effect -> {
            ResourceLocation key = registry.getKey(effect);

            return key != null && "craftvania2".equals(key.getNamespace());
        });

        return effects;

    }
    
}
