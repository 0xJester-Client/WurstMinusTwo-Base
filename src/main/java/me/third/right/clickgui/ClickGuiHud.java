package me.third.right.clickgui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.third.right.ThirdMod;
import me.third.right.clickgui.Settings.Component;
import me.third.right.hud.Hud;
import me.third.right.hud.HudList;
import me.third.right.modules.Client.ClickGuiHack;
import me.third.right.modules.HackList;
import me.third.right.utils.Client.Font.CFontRenderer;
import me.third.right.utils.Client.File.JsonUtils;
import me.third.right.utils.Client.Font.FontDrawing;
import me.third.right.utils.Client.Objects.Pair;
import me.third.right.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ClickGuiHud {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Pair<Window, Hud>> windows = new ArrayList<>();
    private final Path windowsFile;
    private float[] bgColor = new float[3];
    private float[] acColor = new float[3];
    private float[] opColour = new float[3];
    private float opacity;

    public ClickGuiHud(Path windowsFile)
    {
        this.windowsFile = windowsFile;
    }

    public void init(HudList hud)
    {
        final ArrayList<Pair<Window, Hud>> windowMap = new ArrayList<>();
        for(Hud hud1 : hud.getValues())
            windowMap.add(new Pair<>(new Window(hud1.getName()),hud1));

        windows.addAll(windowMap);

        for(Pair<Window, Hud> window : windows) {
            window.getFirst().setMinimizable(true);
            window.getFirst().setClosable(false);
            window.getFirst().setPinnable(true);
            window.getFirst().setMinimized(true);
        }

        int x = 5;
        int y = 5;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        for(Pair<Window, Hud> pair : windows) {
            final Window window = pair.getFirst();
            window.pack();
            if(x + window.getWidth() + 5 > sr.getScaledWidth()) {
                x = 5;
                y += 18;
            }
            window.setX(x);
            window.setY(y);
            x += window.getWidth() + 5;
        }

        final JsonObject json;
        try(BufferedReader reader = Files.newBufferedReader(windowsFile)) {
            json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();
        } catch(NoSuchFileException e) {
            saveWindows();
            return;
        } catch(Exception e) {
            System.out.println("Failed to load " + windowsFile.getFileName());
            e.printStackTrace();
            saveWindows();
            return;
        }

        for(Pair<Window, Hud> pair  : windows) {
            final Window window = pair.getFirst();
            JsonElement jsonWindow = json.get(window.getTitle());
            if(jsonWindow == null || !jsonWindow.isJsonObject()) continue;

            JsonElement jsonX = jsonWindow.getAsJsonObject().get("x");
            if(jsonX.isJsonPrimitive() && jsonX.getAsJsonPrimitive().isNumber())
                window.setX(jsonX.getAsInt());
            JsonElement jsonY = jsonWindow.getAsJsonObject().get("y");
            if(jsonY.isJsonPrimitive() && jsonY.getAsJsonPrimitive().isNumber())
                window.setY(jsonY.getAsInt());
            JsonElement jsonPinned = jsonWindow.getAsJsonObject().get("pinned");
            if(jsonPinned.isJsonPrimitive() && jsonPinned.getAsJsonPrimitive().isBoolean())
                window.setPinned(jsonPinned.getAsBoolean());
        }
        saveWindows();

        for(Pair<Window, Hud> pair : windows) {
            final Window window = pair.getFirst();
            final Hud hud1 = pair.getSecond();
            hud1.setX(window.getX());
            hud1.setY(window.getY());
            hud1.setPinned(window.isPinned());
            hud1.setWindowWidth(window.getWidth());
            hud1.setWindowHeight(window.getHeight());
        }
    }

    private void saveWindows() {
        final JsonObject json = new JsonObject();
        for(Pair<Window, Hud> pair : windows) {
            final Window window = pair.getFirst();
            if(window.isClosable()) continue;
            JsonObject jsonWindow = new JsonObject();
            jsonWindow.addProperty("x", window.getX());
            jsonWindow.addProperty("y", window.getY());
            jsonWindow.addProperty("pinned", window.isPinned());
            json.add(window.getTitle(), jsonWindow);
        }

        try(BufferedWriter writer = Files.newBufferedWriter(windowsFile)) {
            JsonUtils.prettyGson.toJson(json, writer);
        } catch(IOException e) {
            System.out.println("Failed to save " + windowsFile.getFileName());
            e.printStackTrace();
        }
    }

    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        handleWindowMouseClick(mouseX, mouseY, mouseButton);
        windows.removeIf(V -> V.getFirst().isClosing());
    }

    private void handleWindowMouseClick(int mouseX, int mouseY, int mouseButton) {
        for(int i = windows.size() - 1; i >= 0; i--) {
            final Pair<Window, Hud> windowHudPair = windows.get(i);
            final Window window = windowHudPair.getFirst();
            if(window.isInvisible()) continue;

            int x1 = window.getX();
            int y1 = window.getY();
            int x2 = x1 + window.getWidth();
            int y2 = y1 + window.getHeight();
            int y3 = y1 + 13;

            if(mouseX < x1 || mouseY < y1) continue;
            if(mouseX >= x2 || mouseY >= y2) continue;

            if(mouseY < y3) handleTitleBarMouseClick(windowHudPair, mouseX, mouseY, mouseButton);
            else if(!window.isMinimized()) window.validate();
            else continue;

            windows.remove(i);
            windows.add(windowHudPair);
            break;
        }
    }

    private void handleTitleBarMouseClick(Pair<Window, Hud> pair, int mouseX, int mouseY, int mouseButton) {
        if(mouseButton != 0) return;
        final Window window = pair.getFirst();
        if(mouseY < window.getY() + 2 || mouseY >= window.getY() + 11) {
            window.startDragging(mouseX, mouseY);
            return;
        }

        int x3 = window.getX() + window.getWidth();
        if(window.isPinnable()) {
            x3 -= 11;
            if(mouseX >= x3 && mouseX < x3 + 9) {
                window.setPinned(!window.isPinned());
                pair.getSecond().setPinned(window.isPinned());
                saveWindows();
                return;
            }
        }

        window.startDragging(mouseX, mouseY);
    }

    public void updateColors() {
        final ClickGuiHack hax = ThirdMod.getForgeWurst().getHax().clickGuiHack;
        opacity = hax.getOpacity();
        bgColor = hax.getBgColor();
        acColor = hax.getAcColor();
        opColour = hax.getOptionsColour();
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(mc);
        ThirdMod.hax.clickGuiHack.applyFontChanges();

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLineWidth(1);

        // scrolling
        int dWheel = Mouse.getDWheel();
        if(dWheel != 0)
            for(int i = windows.size() - 1; i >= 0; i--) {
                Window window = windows.get(i).getFirst();

                if(!window.isScrollingEnabled() || window.isMinimized() || window.isInvisible()) continue;
                if(mouseX < window.getX() || mouseY < window.getY() + 13) continue;
                if(mouseX >= window.getX() + window.getWidth() || mouseY >= window.getY() + window.getHeight()) continue;

                int scroll = window.getScrollOffset() + dWheel / 16;
                scroll = Math.min(scroll, 0);
                scroll = Math.max(scroll, -window.getInnerHeight() + window.getHeight() - 13);
                window.setScrollOffset(scroll);
                break;
            }

        for(Pair<Window, Hud> pair  : windows) {
            if(!pair.getSecond().requirementsMet()) continue;
            final Window window = pair.getFirst();
            if(window.isInvisible()) continue;

            if(window.getY() < 0)
                window.setY(0);
            else if(window.getY() + window.getHeight() > sr.getScaledHeight())
                window.setY(sr.getScaledHeight() - window.getHeight());

            if(window.getX() < 0)
                window.setX(0);
            else if(window.getX() + window.getWidth() > sr.getScaledWidth())
                window.setX(sr.getScaledWidth() - window.getWidth());

            // dragging
            if(window.isDragging()) {
                if (Mouse.isButtonDown(0))
                    window.dragTo(mouseX, mouseY);
                else {
                    window.stopDragging();
                    saveWindows();
                }
            }

            final Hud hud1 = pair.getSecond();
            hud1.setX(window.getX());
            hud1.setY(window.getY());
            //hud1.setPinned(window.isPinned());
            hud1.setWindowWidth(window.getWidth());
            hud1.setWindowHeight(window.getHeight());

            // scrollbar dragging
            if(window.isDraggingScrollbar())
                if(Mouse.isButtonDown(0)) window.dragScrollbarTo(mouseY);
                else window.stopDraggingScrollbar();

            renderWindow(window, mouseX, mouseY, partialTicks);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);//May delete

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderWindow(Window window, int mouseX, int mouseY, float partialTicks) {
        int x1 = window.getX();
        int y1 = window.getY();
        int x2 = x1 + window.getWidth();
        int y2 = y1 + window.getHeight();
        int y3 = y1 + 13;

        if(window.isMinimized())
            y2 = y3;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        if(!window.isMinimized()) {
            window.setMaxHeight(450);
            window.validate();

            int x3 = x1 + 2;
            int x4 = window.isScrollingEnabled() ? x2 - 3 : x2;
            int x5 = x4 - 2;
            int y4 = y3 + window.getScrollOffset();

            // window background
            // left & right
            GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(x1, y3);
            GL11.glVertex2i(x1, y2);
            GL11.glVertex2i(x3, y2);
            GL11.glVertex2i(x3, y3);
            GL11.glVertex2i(x5, y3);
            GL11.glVertex2i(x5, y2);
            GL11.glVertex2i(x4, y2);
            GL11.glVertex2i(x4, y3);
            GL11.glEnd();

            if(window.isScrollingEnabled()) {
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                int sf = sr.getScaleFactor();
                GL11.glScissor(x1 * sf, (int)((sr.getScaledHeight_double() - y2) * sf), window.getWidth() * sf, (y2 - y3) * sf);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
            }
            GL11.glPushMatrix();
            GL11.glTranslated(x1, y4, 0);

            GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
            GL11.glBegin(GL11.GL_QUADS);

            // window background
            // between children
            int xc1 = 2;
            int xc2 = x5 - x1;
            for(int i = 0; i < window.countChildren(); i++) {
                int yc1 = window.getChild(i).getY();
                int yc2 = yc1 - 2;
                GL11.glVertex2i(xc1, yc2);
                GL11.glVertex2i(xc1, yc1);
                GL11.glVertex2i(xc2, yc1);
                GL11.glVertex2i(xc2, yc2);
            }

            // window background
            // bottom
            int yc1;
            if(window.countChildren() == 0)
                yc1 = 0;
            else {
                Component lastChild = window.getChild(window.countChildren() - 1);
                yc1 = lastChild.getY() + lastChild.getHeight();
            }
            int yc2 = yc1 + 2;
            GL11.glVertex2i(xc1, yc2);
            GL11.glVertex2i(xc1, yc1);
            GL11.glVertex2i(xc2, yc1);
            GL11.glVertex2i(xc2, yc2);

            GL11.glEnd();

            GL11.glPopMatrix();
            if(window.isScrollingEnabled())
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        // window outline
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();

        if(!window.isMinimized()) {
            // title bar outline
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(x1, y3);
            GL11.glVertex2i(x2, y3);
            GL11.glEnd();
        }

        // title bar buttons
        int x3 = x2;
        int y4 = y1 + 2;
        int y5 = y3 - 2;
        boolean hoveringY = mouseY >= y4 && mouseY < y5;

        if(window.isPinnable()) {
            x3 -= 11;
            int x4 = x3 + 9;
            boolean hovering = hoveringY && mouseX >= x3 && mouseX < x4;
            renderPinButton(x3, y4, x4, y5, hovering, window.isPinned());
        }

        // title bar background
        // above & below buttons
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], opacity);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x3, y1);
        GL11.glVertex2i(x3, y4);
        GL11.glVertex2i(x2, y4);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x3, y5);
        GL11.glVertex2i(x3, y3);
        GL11.glVertex2i(x2, y3);
        GL11.glVertex2i(x2, y5);
        GL11.glEnd();

        // title bar background
        // behind title
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y3);
        GL11.glVertex2i(x3, y3);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();

        // window title
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 1, 1, 1);

        final boolean customFont = ThirdMod.hax.clickGuiHack.customFont.isChecked();
        final String title;
        if(customFont) title = window.getTitle();
        else title = Wrapper.getFontRenderer().trimStringToWidth(window.getTitle(), x3 - x1);
        FontDrawing.drawString(title, x1 + 2, y1 + 3, 0xf0f0f0);
    }

    private void renderPinButton(int x1, int y1, int x2, int y2, boolean hovering, boolean pinned) {
        renderTitleBarButton(x1, y1, x2, y2, hovering);
        float h = hovering ? 1 : 0.85F;

        if(pinned) {
            double xk1 = x1 + 2;
            double xk2 = x2 - 2;
            double xk3 = x1 + 1;
            double xk4 = x2 - 1;
            double yk1 = y1 + 2;
            double yk2 = y2 - 2;
            double yk3 = y2 - 0.5;

            // knob
            GL11.glColor4f(opColour[0], opColour[1], opColour[2], 0.5F);
            //h, 0, 0, 0.5F
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk1, yk2);
            GL11.glVertex2d(xk3, yk2);
            GL11.glVertex2d(xk4, yk2);
            GL11.glVertex2d(xk4, yk3);
            GL11.glVertex2d(xk3, yk3);
            GL11.glEnd();

            double xn1 = x1 + 3.5;
            double xn2 = x2 - 3.5;
            double yn1 = y2 - 0.5;
            double yn2 = y2;

            // needle
            GL11.glColor4f(h, h, h, 1);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn1, yn2);
            GL11.glEnd();

            // outlines
            GL11.glColor4f(0.0625F, 0.0625F, 0.0625F, 0.5F);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk1, yk2);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xk3, yk2);
            GL11.glVertex2d(xk4, yk2);
            GL11.glVertex2d(xk4, yk3);
            GL11.glVertex2d(xk3, yk3);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn1, yn2);
            GL11.glEnd();

        } else {
            double xk1 = x2 - 3.5;
            double xk2 = x2 - 0.5;
            double xk3 = x2 - 3;
            double xk4 = x1 + 3;
            double xk5 = x1 + 2;
            double xk6 = x2 - 2;
            double xk7 = x1 + 1;
            double yk1 = y1 + 0.5;
            double yk2 = y1 + 3.5;
            double yk3 = y2 - 3;
            double yk4 = y1 + 3;
            double yk5 = y1 + 2;
            double yk6 = y2 - 2;
            double yk7 = y2 - 1;

            // knob
            GL11.glColor4f(opColour[0], opColour[1], opColour[2], 1);
            //0, h, 0, 1
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk3, yk3);
            GL11.glVertex2d(xk4, yk4);
            GL11.glVertex2d(xk5, yk5);
            GL11.glVertex2d(xk6, yk6);
            GL11.glVertex2d(xk3, yk7);
            GL11.glVertex2d(xk7, yk4);
            GL11.glEnd();

            double xn1 = x1 + 3;
            double xn2 = x1 + 4;
            double xn3 = x1 + 1;
            double yn1 = y2 - 4;
            double yn2 = y2 - 3;
            double yn3 = y2 - 1;

            // needle
            GL11.glColor4f(h, h, h, 1);
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn3, yn3);
            GL11.glEnd();

            // outlines
            GL11.glColor4f(0.0625F, 0.0625F, 0.0625F, 0.5F);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xk1, yk1);
            GL11.glVertex2d(xk2, yk2);
            GL11.glVertex2d(xk3, yk3);
            GL11.glVertex2d(xk4, yk4);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xk5, yk5);
            GL11.glVertex2d(xk6, yk6);
            GL11.glVertex2d(xk3, yk7);
            GL11.glVertex2d(xk7, yk4);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(xn1, yn1);
            GL11.glVertex2d(xn2, yn2);
            GL11.glVertex2d(xn3, yn3);
            GL11.glEnd();
        }
    }

    private void renderTitleBarButton(int x1, int y1, int x2, int y2, boolean hovering)
    {
        int x3 = x2 + 2;

        // button background
        GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], hovering ? opacity * 1.5F : opacity);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();

        // background between buttons
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], opacity);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();

        // button outline
        GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x2, y1);
        GL11.glEnd();
    }
}
