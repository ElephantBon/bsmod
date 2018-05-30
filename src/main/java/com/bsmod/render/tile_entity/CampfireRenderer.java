package com.bsmod.render.tile_entity;

import org.lwjgl.opengl.GL11;

import com.bsmod.block.ModBlocks;
import com.bsmod.block.blockCampfire;
import com.bsmod.model.ModelCampfire;
import com.bsmod.render.RendererBlockInterface;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;

public class CampfireRenderer extends RendererBlockInterface {

   private final ModelCampfire model = new ModelCampfire();


   public CampfireRenderer() {	   
      ((blockCampfire)ModBlocks.campfire_lit).renderId = RenderingRegistry.getNextAvailableRenderId();
      ((blockCampfire)ModBlocks.campfire_unlit).renderId = ((blockCampfire)ModBlocks.campfire_lit).renderId;
      ((blockCampfire)ModBlocks.campfire_smoldering).renderId = ((blockCampfire)ModBlocks.campfire_lit).renderId;
      
      RenderingRegistry.registerBlockHandler(this);
   }

   public void renderTileEntityAt(TileEntity tile, double var2, double var4, double var6, float var8) {
      GL11.glDisable('\u803a');
      GL11.glPushMatrix();
      GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      //GL11.glRotatef((float)(45 * tile.rotation), 0.0F, 1.0F, 0.0F);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(RendererBlockInterface.PlanksOak);
      this.model.renderLog(0.0625F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(RendererBlockInterface.Stone);
      this.model.renderRock(0.0625F);
      GL11.glPopMatrix();
   }

   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 1.2F, 0.0F);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(RendererBlockInterface.PlanksOak);
      this.model.renderLog(0.0625F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(RendererBlockInterface.Stone);
      this.model.renderRock(0.0625F);
      GL11.glPopMatrix();
   }

   public int getRenderId() {
      return ModBlocks.campfire_lit.getRenderType();
   }
}
