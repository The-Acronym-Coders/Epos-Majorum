package com.teamacronymcoders.epos.impl.featinfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamacronymcoders.epos.api.feat.info.FeatInfo;

public class MBFFeatInfo extends FeatInfo {

    private static final Codec<MBFFeatInfo> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.BOOL.fieldOf("isUnlocked").forGetter(FeatInfo::isUnlocked),
                    Codec.INT.optionalFieldOf("remainingTime", 0).forGetter(MBFFeatInfo::getRemainingTime)
            ).apply(instance, MBFFeatInfo::new)
    );

    private int remainingTime;

    public MBFFeatInfo() {
        super(false);
        this.remainingTime = 0;
    }

    public MBFFeatInfo(boolean isUnlocked, int remainingTime) {
        super(isUnlocked);
        this.remainingTime = remainingTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void decrementRemainingTime() {
        this.remainingTime--;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public Codec<FeatInfo> getCodec() {
        return CODEC.xmap(info -> info, info -> (MBFFeatInfo) info);
    }

    @Override
    public FeatInfo create() {
        return new MBFFeatInfo();
    }

}
