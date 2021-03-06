package com.teamacronymcoders.epos.api.capability;

import com.teamacronymcoders.epos.api.character.ICharacterSheet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class EposCapabilities {

    public static final ResourceLocation SHEET_ID = new ResourceLocation("epos", "character_sheet");

    @CapabilityInject(ICharacterSheet.class)
    public static Capability<ICharacterSheet> CHARACTER_SHEET;

}
