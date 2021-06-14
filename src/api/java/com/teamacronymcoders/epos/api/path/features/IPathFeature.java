package com.teamacronymcoders.epos.api.path.features;

import com.mojang.serialization.Codec;
import com.teamacronymcoders.epos.api.IDescribable;
import com.teamacronymcoders.epos.api.character.ICharacterStats;
import net.minecraft.entity.LivingEntity;

// TODO: Document Main Interface Object
public interface IPathFeature extends IDescribable {

    /**
     * Called to Apply the {@link IPathFeature} to the {@link LivingEntity} Character
     * @param character
     * @param stats
     */
    void applyTo(LivingEntity character, ICharacterStats stats);

    /**
     * Called to Revoke the {@link IPathFeature} to the {@link LivingEntity} Character
     * @param character
     * @param stats
     */
    void removeFrom(LivingEntity character, ICharacterStats stats);

    /**
     * @return Returns the {@link IPathFeature} Codec for the {@link IPathFeature}
     */
    Codec<? extends IPathFeature> getCodec();
}
