package dev.afkassistant;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AfkScreen extends Screen {
    private TextFieldWidget blocks, widthField, lengthField, heightField, commandField, intervalField;
    protected AfkScreen() { super(Text.literal("AFK Assistant")); }

    @Override protected void init() {
        int cx = width / 2;
        int y = 38;
        blocks = new TextFieldWidget(textRenderer, cx-150, y, 300, 20, Text.literal("Block IDs"));
        blocks.setMaxLength(500);
        blocks.setText(String.join(", ", AfkConfig.INSTANCE.blockIds));
        addDrawableChild(blocks);
        y += 27;

        widthField = field(cx-150, y, 94, "Width", AfkConfig.INSTANCE.width);
        lengthField = field(cx-48, y, 94, "Length", AfkConfig.INSTANCE.length);
        heightField = field(cx+54, y, 96, "Height", AfkConfig.INSTANCE.height);
        addDrawableChild(widthField); addDrawableChild(lengthField); addDrawableChild(heightField);
        y += 28;

        addDrawableChild(ButtonWidget.builder(Text.literal("Tryb obszaru: " +
                (AfkConfig.INSTANCE.useDimensionArea ? "wymiary" : "dwa rogi")),
                b -> {
                    AfkConfig.INSTANCE.useDimensionArea = !AfkConfig.INSTANCE.useDimensionArea;
                    b.setMessage(Text.literal("Tryb obszaru: " +
                            (AfkConfig.INSTANCE.useDimensionArea ? "wymiary" : "dwa rogi")));
                }).dimensions(cx-150, y, 300, 20).build());
        y += 25;

        addDrawableChild(ButtonWidget.builder(Text.literal("Zapisz róg 1 z pozycji gracza"),
                b -> setCorner(true)).dimensions(cx-150, y, 300, 20).build());
        y += 23;
        addDrawableChild(ButtonWidget.builder(Text.literal("Zapisz róg 2 z pozycji gracza"),
                b -> setCorner(false)).dimensions(cx-150, y, 300, 20).build());
        y += 28;

        commandField = new TextFieldWidget(textRenderer, cx-150, y, 200, 20, Text.literal("Command"));
        commandField.setMaxLength(100);
        commandField.setText("/repair");
        intervalField = new TextFieldWidget(textRenderer, cx+56, y, 94, 20, Text.literal("Seconds"));
        intervalField.setMaxLength(6);
        intervalField.setText("300");
        addDrawableChild(commandField); addDrawableChild(intervalField);
        y += 25;
        addDrawableChild(ButtonWidget.builder(Text.literal("Dodaj komendę"),
                b -> addCommand()).dimensions(cx-150, y, 145, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Włącz/wyłącz kopanie"),
                b -> {
                    AfkConfig.INSTANCE.minerEnabled = !AfkConfig.INSTANCE.minerEnabled;
                    if (!AfkConfig.INSTANCE.minerEnabled) Miner.stop();
                    AfkConfig.save();
                }).dimensions(cx+5, y, 145, 20).build());
        y += 24;
        addDrawableChild(ButtonWidget.builder(Text.literal("Włącz/wyłącz harmonogram komend"),
                b -> {
                    AfkConfig.INSTANCE.schedulerEnabled = !AfkConfig.INSTANCE.schedulerEnabled;
                    AfkConfig.save();
                }).dimensions(cx-150, y, 300, 20).build());
        y += 24;
        addDrawableChild(ButtonWidget.builder(Text.literal("Zapisz i zamknij"),
                b -> { saveFields(); close(); }).dimensions(cx-150, y, 145, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"),
                b -> close()).dimensions(cx+5, y, 145, 20).build());
    }

    private TextFieldWidget field(int x, int y, int w, String name, int value) {
        TextFieldWidget f = new TextFieldWidget(textRenderer, x, y, w, 20, Text.literal(name));
        f.setMaxLength(5); f.setText(Integer.toString(value)); return f;
    }

    private void setCorner(boolean first) {
        if (client == null || client.player == null) return;
        BlockPos p = client.player.getBlockPos();
        if (first) {
            AfkConfig.INSTANCE.corner1X=p.getX(); AfkConfig.INSTANCE.corner1Y=p.getY(); AfkConfig.INSTANCE.corner1Z=p.getZ();
        } else {
            AfkConfig.INSTANCE.corner2X=p.getX(); AfkConfig.INSTANCE.corner2Y=p.getY(); AfkConfig.INSTANCE.corner2Z=p.getZ();
        }
        AfkConfig.save();
    }

    private void addCommand() {
        try {
            String cmd = commandField.getText().trim();
            int seconds = Integer.parseInt(intervalField.getText().trim());
            if (!cmd.isBlank() && seconds >= 10) {
                AfkConfig.INSTANCE.commands.add(new AfkConfig.CommandEntry(cmd, seconds));
                AfkConfig.save();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void saveFields() {
        try {
            AfkConfig.INSTANCE.width = Math.max(1, Math.min(16, Integer.parseInt(widthField.getText())));
            AfkConfig.INSTANCE.length = Math.max(1, Math.min(16, Integer.parseInt(lengthField.getText())));
            AfkConfig.INSTANCE.height = Math.max(1, Math.min(8, Integer.parseInt(heightField.getText())));
        } catch (NumberFormatException ignored) {}
        AfkConfig.INSTANCE.blockIds.clear();
        for (String id : blocks.getText().split(",")) {
            String clean = id.trim();
            if (!clean.isEmpty()) AfkConfig.INSTANCE.blockIds.add(clean);
        }
        AfkConfig.save();
    }

    @Override public void close() { saveFields(); super.close(); }
    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width/2, 15, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, "Bloki (ID rozdziel przecinkami)", width/2-150, 27, 0xAAAAAA);
        context.drawTextWithShadow(textRenderer, "Wymiary obszaru", width/2-150, 66, 0xAAAAAA);
        context.drawTextWithShadow(textRenderer, "Komenda i interwał w sekundach (min. 10)", width/2-150, 149, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
    @Override public boolean shouldPause() { return false; }
}
