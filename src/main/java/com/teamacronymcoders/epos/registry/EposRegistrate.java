/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Team Acronym Coders
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.teamacronymcoders.epos.registry;

import com.mojang.serialization.Codec;
import com.teamacronymcoders.epos.Epos;
import com.teamacronymcoders.epos.api.feat.FeatSerializer;
import com.teamacronymcoders.epos.api.feat.IFeat;
import com.teamacronymcoders.epos.api.feat.info.FeatInfo;
import com.teamacronymcoders.epos.api.path.IPath;
import com.teamacronymcoders.epos.api.path.PathSerializer;
import com.teamacronymcoders.epos.api.path.features.PathFeatureSerializer;
import com.teamacronymcoders.epos.api.skill.ISkill;
import com.teamacronymcoders.epos.api.skill.SkillSerializer;
import com.teamacronymcoders.epos.impl.feat.generic.spiritofbattle.dynamic.ISpiritualAid;
import com.teamacronymcoders.epos.impl.feat.generic.spiritofbattle.dynamic.SpiritualAidSerializer;
import com.teamacronymcoders.epos.path.feature.AbstractPathFeature;
import com.teamacronymcoders.epos.registry.builder.SerializerBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import net.ashwork.dynamicregistries.entry.ICodecEntry;
import net.ashwork.dynamicregistries.entry.IDynamicEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class EposRegistrate extends AbstractRegistrate<EposRegistrate> {

    public static EposRegistrate create(String modid) {
        return new EposRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private final Supplier<IForgeRegistry<PathSerializer>> pathSerializerRegistry;
    private final Supplier<IForgeRegistry<SkillSerializer>> skillSerializerRegistry;
    private final Supplier<IForgeRegistry<FeatSerializer>> featSerializerRegistry;
    private final Supplier<IForgeRegistry<PathFeatureSerializer>> pathFeatureSerializerRegistry;
    private final Supplier<IForgeRegistry<SpiritualAidSerializer>> spiritualAidSerializerRegistry;

    private final Supplier<IForgeRegistry<FeatInfo>> featInfoRegistry;

    protected EposRegistrate(String modid) {
        super(modid);
        // Init the Serializer Registries
        this.pathSerializerRegistry = this.makeRegistry("path_serializer", PathSerializer.class, () -> new RegistryBuilder<PathSerializer>().setDefaultKey(new ResourceLocation(Epos.ID, "path")));
        this.skillSerializerRegistry = this.makeRegistry("skill_serializer", SkillSerializer.class, () -> new RegistryBuilder<SkillSerializer>().setDefaultKey(new ResourceLocation(Epos.ID, "skill")));
        this.featSerializerRegistry = this.makeRegistry("feat_serializer", FeatSerializer.class, () -> new RegistryBuilder<FeatSerializer>().setDefaultKey(new ResourceLocation(Epos.ID, "feat")));
        this.pathFeatureSerializerRegistry = this.makeRegistry("path_feature_serializer", PathFeatureSerializer.class, () -> new RegistryBuilder<PathFeatureSerializer>().setDefaultKey(new ResourceLocation(Epos.ID, "path_feature")));
        this.spiritualAidSerializerRegistry = this.makeRegistry("spiritual_aid_serializer", SpiritualAidSerializer.class, () -> new RegistryBuilder<SpiritualAidSerializer>().setDefaultKey(new ResourceLocation(Epos.ID, "spiritual_aid")));
        this.featInfoRegistry = this.makeRegistry("feat_info", FeatInfo.class, () -> new RegistryBuilder<FeatInfo>().setDefaultKey(new ResourceLocation(Epos.ID, "feat_info")).missing((key, isNetwork) -> new FeatInfo()));
    }

    public IForgeRegistry<PathSerializer> getPathSerializerRegistry() {
        return pathSerializerRegistry.get();
    }

    public IForgeRegistry<SkillSerializer> getSkillSerializerRegistry() {
        return this.skillSerializerRegistry.get();
    }

    public IForgeRegistry<FeatSerializer> getFeatSerializerRegistry() {
        return featSerializerRegistry.get();
    }

    public IForgeRegistry<PathFeatureSerializer> getPathFeatureSerializerRegistry() {
        return pathFeatureSerializerRegistry.get();
    }

    public IForgeRegistry<SpiritualAidSerializer> getSpiritualAidSerializerRegistry() {
        return spiritualAidSerializerRegistry.get();
    }

    public Supplier<IForgeRegistry<FeatInfo>> getFeatInfoRegistry() {
        return featInfoRegistry;
    }

    // Path

    // Path Serializer
    public SerializerBuilder<PathSerializer, PathSerializer, EposRegistrate> pathSerializer(NonNullSupplier<Codec<? extends IPath>> codec) {
        return this.pathSerializer(this.self(), codec);
    }

    public <P> SerializerBuilder<PathSerializer, PathSerializer, P> pathSerializer(P parent, NonNullSupplier<Codec<? extends IPath>> codec) {
        return this.pathSerializer(parent, this.currentName(), codec);
    }

    public SerializerBuilder<PathSerializer, PathSerializer, EposRegistrate> pathSerializer(String name, NonNullSupplier<Codec<? extends IPath>> codec) {
        return this.pathSerializer(this.self(), name, codec);
    }

    public <P> SerializerBuilder<PathSerializer, PathSerializer, P> pathSerializer(P parent, String name, NonNullSupplier<Codec<? extends IPath>> codec) {
        return this.dynamicSerializer(parent, name, PathSerializer.class, () -> new PathSerializer(codec.get()));
    }

    // Skill
    public SerializerBuilder<SkillSerializer, SkillSerializer, EposRegistrate> skillSerializer(NonNullSupplier<Codec<? extends ISkill>> codec) {
        return this.skillSerializer(this.self(), codec);
    }

    public <P> SerializerBuilder<SkillSerializer, SkillSerializer, P> skillSerializer(P parent, NonNullSupplier<Codec<? extends ISkill>> codec) {
        return this.skillSerializer(parent, this.currentName(), codec);
    }

    public SerializerBuilder<SkillSerializer, SkillSerializer, EposRegistrate> skillSerializer(String name, NonNullSupplier<Codec<? extends ISkill>> codec) {
        return this.skillSerializer(this.self(), name, codec);
    }

    public <P> SerializerBuilder<SkillSerializer, SkillSerializer, P> skillSerializer(P parent, String name, NonNullSupplier<Codec<? extends ISkill>> codec) {
        return this.dynamicSerializer(parent, name, SkillSerializer.class, () -> new SkillSerializer(codec.get()));
    }

    // Feat
    public SerializerBuilder<FeatSerializer, FeatSerializer, EposRegistrate> featSerializer(NonNullSupplier<Codec<? extends IFeat>> codec) {
        return this.featSerializer(this.self(), codec);
    }

    public <P> SerializerBuilder<FeatSerializer, FeatSerializer, P> featSerializer(P parent, NonNullSupplier<Codec<? extends IFeat>> codec) {
        return this.featSerializer(parent, this.currentName(), codec);
    }

    public SerializerBuilder<FeatSerializer, FeatSerializer, EposRegistrate> featSerializer(String name, NonNullSupplier<Codec<? extends IFeat>> codec) {
        return this.featSerializer(this.self(), name, codec);
    }

    public <P> SerializerBuilder<FeatSerializer, FeatSerializer, P> featSerializer(P parent, String name, NonNullSupplier<Codec<? extends IFeat>> codec) {
        return this.dynamicSerializer(parent, name, FeatSerializer.class, () -> new FeatSerializer(codec.get()));
    }

    // Path Feature
    public SerializerBuilder<PathFeatureSerializer, PathFeatureSerializer, EposRegistrate> pathFeatureSerializer(NonNullSupplier<Codec<? extends AbstractPathFeature>> codec) {
        return this.pathFeatureSerializer(this.self(), codec);
    }

    public <P> SerializerBuilder<PathFeatureSerializer, PathFeatureSerializer, P> pathFeatureSerializer(P parent, NonNullSupplier<Codec<? extends AbstractPathFeature>> codec) {
        return this.pathFeatureSerializer(parent, this.currentName(), codec);
    }

    public SerializerBuilder<PathFeatureSerializer, PathFeatureSerializer, EposRegistrate> pathFeatureSerializer(String name, NonNullSupplier<Codec<? extends AbstractPathFeature>> codec) {
        return this.pathFeatureSerializer(this.self(), name, codec);
    }

    public <P> SerializerBuilder<PathFeatureSerializer, PathFeatureSerializer, P> pathFeatureSerializer(P parent, String name, NonNullSupplier<Codec<? extends AbstractPathFeature>> codec) {
        return this.dynamicSerializer(parent, name, PathFeatureSerializer.class, () -> new PathFeatureSerializer(codec.get()));
    }

    // Spiritual Aid
    public SerializerBuilder<SpiritualAidSerializer, SpiritualAidSerializer, EposRegistrate> spiritualAidSerializer(NonNullSupplier<Codec<? extends ISpiritualAid>> codec) {
        return this.spiritualAidSerializer(this.self(), codec);
    }

    public <P> SerializerBuilder<SpiritualAidSerializer, SpiritualAidSerializer, P> spiritualAidSerializer(P parent, NonNullSupplier<Codec<? extends ISpiritualAid>> codec) {
        return this.spiritualAidSerializer(parent, this.currentName(), codec);
    }

    public SerializerBuilder<SpiritualAidSerializer, SpiritualAidSerializer, EposRegistrate> spiritualAidSerializer(String name, NonNullSupplier<Codec<? extends ISpiritualAid>> codec) {
        return this.spiritualAidSerializer(this.self(), name, codec);
    }

    public <P> SerializerBuilder<SpiritualAidSerializer, SpiritualAidSerializer, P> spiritualAidSerializer(P parent, String name, NonNullSupplier<Codec<? extends ISpiritualAid>> codec) {
        return this.dynamicSerializer(parent, name, SpiritualAidSerializer.class, () -> new SpiritualAidSerializer(codec.get()));
    }

    // Dynamic
    public <D extends IDynamicEntry<D>, R extends ICodecEntry<?, R>, I extends R, P> SerializerBuilder<R, R, EposRegistrate> dynamicSerializer(Class<? super R> serializerClass, NonNullSupplier<? extends I> factory) {
        return this.dynamicSerializer(this.self(), serializerClass, factory);
    }

    public <D extends IDynamicEntry<D>, R extends ICodecEntry<?, R>, P> SerializerBuilder<R, R, EposRegistrate> dynamicSerializer(String name, Class<? super R> serializerClass, NonNullSupplier<? extends R> factory) {
        return this.dynamicSerializer(this.self(), name, serializerClass, factory);
    }

    public <D extends IDynamicEntry<D>, R extends ICodecEntry<?, R>, P> SerializerBuilder<R, R, P> dynamicSerializer(P parent, Class<? super R> serializerClass, NonNullSupplier<? extends R> factory) {
        return this.dynamicSerializer(parent, this.currentName(), serializerClass, factory);
    }

    public <D extends IDynamicEntry<D>, R extends ICodecEntry<?, R>, P> SerializerBuilder<R, R, P> dynamicSerializer(P parent, String name, Class<? super R> serializerClass, NonNullSupplier<? extends R> factory) {
        return this.entry(name, callback -> new SerializerBuilder<>(this, parent, name, callback, serializerClass, factory));
    }

//    public <R extends IDynamicEntry<R>, T extends ICodecEntry<R, T>> DynamicRegistryEntry<R, T> get(String name, Class<? super R> type) {
//        return this.<R, T>getRegistration(name, type).getDelegate();
//    }
}
