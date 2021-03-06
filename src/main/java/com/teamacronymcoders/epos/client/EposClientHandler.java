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

package com.teamacronymcoders.epos.client;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamacronymcoders.epos.Epos;
import com.teamacronymcoders.epos.client.renderer.model.DynamicRegistryBakedModel;
import com.teamacronymcoders.epos.client.renderer.model.EposResourceType;
import net.ashwork.dynamicregistries.DynamicRegistryManager;
import net.ashwork.dynamicregistries.registry.DynamicRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.resource.VanillaResourceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EposClientHandler {

    private static EposClientHandler instance;
    private final Minecraft mc;
    private final Map<ResourceLocation, EposResourceType> resourceLoaders;

    public EposClientHandler(IEventBus modBus, IEventBus forgeBus) {
        instance = this;
        this.mc = Minecraft.getInstance();
        this.resourceLoaders = new HashMap<>();
        modBus.addListener(this::registerModels);

        // FOR TESTING
        if (Epos.IS_TESTING) this.addTestCases(modBus, forgeBus);
    }

    public static final EposClientHandler clientInstance() {
        return instance;
    }

    private void registerModels(ModelRegistryEvent event) {
        this.addResourceType(new ResourceLocation(Epos.ID, "skill"), EposResourceType.SKILL);
    }

    @VisibleForTesting
    private void addTestCases(IEventBus modBus, IEventBus forgeBus) {
        forgeBus.addListener(this::testDraw);
        forgeBus.addListener(this::clientTick);
    }

    // Puts model at (1, 1, 1)
    @VisibleForTesting
    private void testDraw(RenderWorldLastEvent event) {
        PoseStack stack = event.getMatrixStack();
        Vec3 projection = this.mc.gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource buffer = this.mc.renderBuffers().bufferSource();
        stack.pushPose();
        stack.translate(1 - projection.x, 1 - projection.y, 1 - projection.z);
        VertexConsumer consumer = buffer.getBuffer(RenderType.translucentMovingBlock());
        List<BakedQuad> quads = EposResourceType.SKILL.getQuads(DynamicRegistryManager.STATIC
                .getRegistry(new ResourceLocation(Epos.ID, "skill")).getValue(new ResourceLocation(Epos.ID, "test")));
        for (BakedQuad quad : quads) {
            consumer.putBulkData(event.getMatrixStack().last(), quad, 1.0f, 1.0f, 1.0f, 1.0f,
                    LevelRenderer.getLightColor(this.mc.level, new BlockPos(1, 1, 1)), OverlayTexture.NO_OVERLAY, true);
        }
        stack.popPose();
    }

    private void addResourceType(ResourceLocation registryName, EposResourceType resourceType) {
        this.resourceLoaders.put(registryName, resourceType);
        ModelLoaderRegistry.registerLoader(registryName, new DynamicRegistryBakedModel.Loader(resourceType));
        ModelLoader.addSpecialModel(resourceType.getMainModel());
    }

    private void clientTick(final TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().options.keySprint.consumeClick()) {
            this.refreshResource(DynamicRegistryManager.DYNAMIC.getRegistry(new ResourceLocation(Epos.ID, "skill")));
        }
    }

    public void refreshResource(DynamicRegistry<?, ?> registry) {
        this.resourceLoaders.get(registry.getName()).refresh();
        ForgeHooksClient.refreshResources(this.mc, VanillaResourceType.MODELS);
    }
}
