package com.teamacronymcoders.epos.pathfeature.feat;

import com.teamacronymcoders.epos.api.EposAPI;
import com.teamacronymcoders.epos.api.feat.IFeat;
import com.teamacronymcoders.epos.api.json.JsonUtils;
import com.teamacronymcoders.epos.api.pathfeature.IPathFeature;
import com.teamacronymcoders.epos.api.pathfeature.PathFeatureProvider;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FeatFeatureProvider extends PathFeatureProvider {
    @Override
    public IPathFeature provide(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
        String featName = JsonUtils.getString(jsonObject, "feat");
        IFeat feat = EposAPI.FEAT_REGISTRY.getEntry(featName);
        if (feat != null) {
            return new FeatFeature(feat);
        } else {
            throw new JsonParseException("No feat for registry name: " + featName);
        }

    }
}
