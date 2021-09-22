package setadokalo.customfog.config.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class WarningWidget implements Drawable, Element {
	public enum Type {
		WARNING(0),
		ERROR(40);
		int texturePos;
		Type(int pos) {
			texturePos = pos;
		}
		int getTexturePos() {
			return texturePos;
		}
	}

	@NotNull
	protected final List<Text> warningText;
	protected int x, y;
	protected int width, height;
	@NotNull
	protected Type type;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}


	public WarningWidget(@NotNull Type type, int x, int y, int width, Text... lines) {
		this(type, width, lines);
		this.x = x;
		this.y = y;
	}
	public WarningWidget(int width, Text... lines) {
		this(Type.WARNING, width, lines);
	}

	public WarningWidget(@NotNull Type type, int width, Text... lines) {
		this.type = type;
		this.warningText = Arrays.asList(lines);
		this.width = width;
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		for (Text line : lines) {
			int sWidth = renderer.getWidth(line.asString()) + 28;
			if (sWidth > width) {
				width = sWidth;
			}
		}
		this.height = lines.length * (renderer.fontHeight + 1) + 16;
	}

	public static void drawNinepatchRect(
			MatrixStack matrices,
			int x, int y,
			int width, int height,
			int u, int v,
			int edgeSize, int centerSize) {
		// left to right, then top to bottom
		/* Top row */
		DrawableHelper.drawTexture(matrices,
				x, y,                            /* x, y */
				u, v,                            /* u, v */
				edgeSize, edgeSize,              /* width, height */
				256, 256); /* textureWidth, textureHeight */

		DrawableHelper.drawTexture(matrices,
				x + edgeSize, y,                   /* x, y */
				width - edgeSize * 2, edgeSize, /* width, height */
				u + edgeSize, v,                   /* u, v */
				centerSize, edgeSize,                 /* regionWidth, regionHeight */
				256, 256);      /* textureWidth, textureHeight */

		DrawableHelper.drawTexture(matrices, x + width - edgeSize, y,
				u + edgeSize + centerSize, v,
				edgeSize, edgeSize,
				256, 256);
		/* Middle row */
		DrawableHelper.drawTexture(matrices,
				x, y + edgeSize,                   /* x, y */
				edgeSize, height - edgeSize * 2, /* width, height */
				u, v + edgeSize,     /* u, v */
				edgeSize, centerSize,                /* regionWidth, regionHeight */
				256, 256);      /* textureWidth, textureHeight */
		DrawableHelper.drawTexture(matrices,
				x + edgeSize, y + edgeSize,                   /* x, y */
				width - edgeSize * 2, height - edgeSize * 2, /* width, height */
				u + edgeSize, v + edgeSize,     /* u, v */
				centerSize, centerSize,                /* regionWidth, regionHeight */
				256, 256);      /* textureWidth, textureHeight */

		DrawableHelper.drawTexture(matrices,
				x + width - edgeSize, y + edgeSize,                   /* x, y */
				edgeSize, height - edgeSize * 2, /* width, height */
				u + edgeSize + centerSize, v + edgeSize,     /* u, v */
				edgeSize, centerSize,                /* regionWidth, regionHeight */
				256, 256);      /* textureWidth, textureHeight */
		/* Bottom row */
		DrawableHelper.drawTexture(matrices,
				x, y + height - edgeSize, /* x, y */
				u, v + edgeSize + centerSize, /* u, v */
				edgeSize, edgeSize, /* width, height */
				256, 256); /* textureWidth, textureHeight */

		DrawableHelper.drawTexture(matrices,
				x + edgeSize, y + height - edgeSize,                   /* x, y */
				width - edgeSize * 2, edgeSize, /* width, height */
				u + edgeSize, v + edgeSize + centerSize,     /* u, v */
				centerSize, edgeSize,                /* regionWidth, regionHeight */
				256, 256);      /* textureWidth, textureHeight */

		DrawableHelper.drawTexture(matrices, x + width - edgeSize, y + height - edgeSize,
				u + edgeSize + centerSize, v + edgeSize + centerSize,
				edgeSize, edgeSize,
				256, 256);
	}


	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//		CustomFog.log(Level.INFO, "hhhh");'
		RenderSystem.setShaderTexture(0, new Identifier("custom-fog", "textures/gui/cfog-gui.png"));
		drawNinepatchRect(matrices, this.x, this.y,
				width, height,
				0, 0,
				5, 10);
		DrawableHelper.drawTexture(matrices, this.x + 5, this.y + (this.height - 20)/2,
				20, type.getTexturePos(),
				20, 20,
				256, 256);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int y = 7;
		for (Text line : this.warningText) {
			OrderedText orderedText = line.asOrderedText();
			int color = 0xFFFFFFFF;
			if (line.getStyle() != null && line.getStyle().getColor() != null)
				color = line.getStyle().getColor().getRgb();
//			textRenderer.drawWithShadow(matrices, orderedText, this.x + 20, this.y + y, color);
			DrawableHelper.drawCenteredTextWithShadow(matrices, textRenderer, orderedText, this.x + 10 + width / 2, this.y + y, color);
			y += textRenderer.fontHeight + 2;
		}
	}
}
