package net.gobbob.mobends.core.client.gui.packswindow;

import net.gobbob.mobends.core.client.gui.elements.IGuiListElement;
import net.gobbob.mobends.core.pack.IBendsPack;
import net.gobbob.mobends.core.util.Draw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class GuiPackEntry implements IGuiListElement
{

    private static final int HEIGHT = 31;

    protected FontRenderer fontRenderer;
    protected String name;
    private String displayName;
    protected String author;
    protected String description;
    private ResourceLocation thumbnailLocation;

    private int x, y;
    private boolean hover, selected;

    public GuiPackEntry(IBendsPack pack)
    {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.name = pack.getKey();
        this.displayName = pack.getDisplayName();
        this.author = pack.getAuthor();
        this.description = pack.getDescription();
        this.thumbnailLocation = pack.getThumbnail();

        this.hover = false;
        this.selected = false;
    }

    @Override
    public void initGui(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        this.hover = mouseX >= x && mouseX <= x + 102 &&
                     mouseY >= y && mouseY <= y + HEIGHT;
    }

    @Override
    public boolean handleMouseClicked(int mouseX, int mouseY, int state)
    {
        this.update(mouseX, mouseY);
        return this.hover;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public int getHeight()
    {
        return HEIGHT;
    }

    @Override
    public void draw()
    {
        Minecraft mc = Minecraft.getMinecraft();
        TextureManager textureManager = mc.getTextureManager();

        textureManager.bindTexture(GuiPacksWindow.BACKGROUND_TEXTURE);
        GlStateManager.color(1, 1, 1, 1);
        final int SELECTED_TEXTURE_Y = 62;
        final int HOVER_TEXTURE_Y = 31;
        final int NEUTRAL_TEXTURE_Y = 0;
        int textureY = this.selected ? SELECTED_TEXTURE_Y : this.hover ? HOVER_TEXTURE_Y : NEUTRAL_TEXTURE_Y;
        Draw.texturedModalRect(x - 1, y - (this.selected ? 1 : 0), 0, textureY, 102, HEIGHT);

        textureManager.bindTexture(thumbnailLocation);
        GlStateManager.color(1, 1, 1, 1);
        Draw.texturedRectangle(x + 2, y + 2, 25, 25, 0, 0, 25.0f / 32, 25.0f / 32);

        fontRenderer.drawStringWithShadow(fontRenderer.trimStringToWidth(this.displayName, 70), x + 32, y + 1, 0xffffff);
        Draw.rectangle_xgradient(x + 101 - 40, y + 1, 39, 9, 0x004e4e4e, 0xff4e4e4e);
    }

}