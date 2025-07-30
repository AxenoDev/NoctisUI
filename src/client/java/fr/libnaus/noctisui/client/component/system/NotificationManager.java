package fr.libnaus.noctisui.client.component.system;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.Render2DEngine;
import fr.libnaus.noctisui.client.api.system.render.font.FontAtlas;
import fr.libnaus.noctisui.client.api.system.render.font.Fonts;
import fr.libnaus.noctisui.client.common.QuickImports;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager implements QuickImports {

    @Getter
    private static NotificationManager instance;
    private final List<Notification> notifications = new CopyOnWriteArrayList<>();
    private static final int NOTIFICATION_WIDTH = 220;
    private static final int NOTIFICATION_MIN_HEIGHT = 40;
    private static final int NOTIFICATION_MAX_HEIGHT = 80;
    private static final int LINE_HEIGHT = 12;
    private static final int NOTIFICATION_SPACING = 6;
    private static final int MARGIN_X = 12;
    private static final int MARGIN_Y = 12;
    @Setter @Getter private static FontAtlas font;
    @Setter @Getter private static FontAtlas fontBold;

    public NotificationManager() {
        instance = this;
    }

    public static void initFont(Fonts fonts) {
        font = fonts.getInterMedium();
        fontBold = fonts.getInterBold();
    }

    public static void init() {
        HudRenderCallback.EVENT.register(NotificationManager::renderNotifications);
    }

    public void addNotification(String id, String title, String message, NotificationType type) {
        addNotification(id, title, message, type, 3000);
    }

    public void addNotification(String id, String title, String message, NotificationType type, long duration) {
        Notification newNotification = new Notification(id, title, message, type, duration);

        for (Notification existing : notifications) {
            if (existing.isSimilarTo(newNotification)) {
                existing.incrementStack();
                return;
            }
        }

        notifications.add(newNotification);
    }

    public void success(String id, String title, String message) {
        addNotification(id, title, message, NotificationType.SUCCESS);
    }

    public void error(String id, String title, String message) {
        addNotification(id, title, message, NotificationType.ERROR);
    }

    public void warning(String id, String title, String message) {
        addNotification(id, title, message, NotificationType.WARNING);
    }

    public void info(String id, String title, String message) {
        addNotification(id, title, message, NotificationType.INFO);
    }

    private static void renderNotifications(DrawContext ctx, float tickDelta) {
        NotificationManager manager = getInstance();
        manager.update();
        manager.render(ctx.getMatrices());
    }

    private void update() {
        List<Notification> toRemove = new ArrayList<>();
        List<Notification> visibleNotif = new ArrayList<>();

        for (Notification notification : notifications) {
            notification.update();
            if (notification.shouldRemove())
                toRemove.add(notification);
            else
                visibleNotif.add(notification);
        }

        notifications.removeAll(toRemove);

        int currentY = 0;
        for (Notification notification : visibleNotif) {
            notification.setTargetY(currentY);
            currentY += calculateNotificationHeight(notification) + NOTIFICATION_SPACING;
        }
    }

    private void render(MatrixStack matrices) {
        if (notifications.isEmpty()) return;
        int screenWidth = mc.getWindow().getScaledWidth();

        List<Notification> notificationsCopy = new ArrayList<>(notifications);

        for (Notification notification : notificationsCopy) {
            float offsetX = notification.getSlideOffset();
            float alpha = notification.getAlpha();
            float animatedY = notification.getCurrentY();
            int x = (int) (screenWidth - NOTIFICATION_WIDTH - MARGIN_X + offsetX);
            int y = (int) (MARGIN_Y + animatedY);
            renderNotification(matrices, notification, x, y, alpha);
        }
    }

    private void renderNotification(MatrixStack matrices, Notification notification, int x, int y, float alpha) {
        alpha = Math.max(0f, Math.min(1f, alpha));

        int notificationHeight = calculateNotificationHeight(notification);

        Color bgColor = new Color(24, 26, 29, (int) (250 * alpha));
        Render2DEngine.drawRoundedRect(matrices, x, y, NOTIFICATION_WIDTH, notificationHeight, 8, bgColor);

        Color borderColor = new Color(52, 58, 64, (int) (180 * alpha));
        Render2DEngine.drawRoundedOutline(matrices, x, y, NOTIFICATION_WIDTH, notificationHeight, 8, 1.2f, borderColor);

        Color accentColor = new Color(
                notification.getColor().getRed(),
                notification.getColor().getGreen(),
                notification.getColor().getBlue(),
                (int) (255 * alpha)
        );

        Render2DEngine.drawRoundedRect(matrices, x + 4, y + 6, 3, notificationHeight - 12, 1, accentColor);

        Color iconBgColor = new Color(
                notification.getColor().getRed(),
                notification.getColor().getGreen(),
                notification.getColor().getBlue(),
                (int) (25 * alpha)
        );
        Render2DEngine.drawRoundedRect(matrices, x + 13, y + 10 + (notificationHeight - 20) / 2 - 10, 20, 20, 6, iconBgColor);

        renderIcon(matrices, notification.getType(), x + 23, y + 10 + (notificationHeight - 20) / 2, accentColor);

        int textStartX = x + 38;
        int maxTextWidth = NOTIFICATION_WIDTH - (textStartX - x) - 8;
        int currentTextY = y + 10;

        if (notification.getTitle() != null && !notification.getTitle().isEmpty()) {
            Color titleColor = new Color(255, 255, 255, (int) (255 * alpha));
            currentTextY = renderWrappedText(matrices, notification.getTitle(), textStartX, currentTextY, maxTextWidth, titleColor, true);
            currentTextY += 2; // Espacement entre titre et message
        }

        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            Color messageColor = new Color(170, 178, 190, (int) (240 * alpha));
            renderWrappedText(matrices, notification.getMessage(), textStartX, currentTextY, maxTextWidth, messageColor, false);
        }

        if (notification.hasStack())
            renderStackCounter(matrices, notification, x, y, alpha);

        renderProgressBar(matrices, notification, x, y + notificationHeight - 4, alpha);
    }

    private int calculateNotificationHeight(Notification notification) {
        int height = NOTIFICATION_MIN_HEIGHT;
        int maxTextWidth = NOTIFICATION_WIDTH - 38 - 8; // 38 = position du texte, 8 = marge droite

        if (notification.getTitle() != null && !notification.getTitle().isEmpty()) {
            List<String> titleLines = wrapText(notification.getTitle(), maxTextWidth, true);
            height += (titleLines.size() - 1) * LINE_HEIGHT;
        }

        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            List<String> messageLines = wrapText(notification.getMessage(), maxTextWidth, false);
            height += (messageLines.size() - 1) * LINE_HEIGHT;
        }

        return Math.min(height, NOTIFICATION_MAX_HEIGHT);
    }

    private List<String> wrapText(String text, int maxWidth, boolean bold) {
        FontAtlas police = bold ? fontBold : font;
        List<String> lines = new ArrayList<>();

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

            if (police.getWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Si même un mot seul dépasse, on le tronque
                    lines.add(truncateText(word, maxWidth, bold));
                }
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private int renderWrappedText(MatrixStack matrices, String text, int x, int y, int maxWidth, Color color, boolean bold) {
        List<String> lines = wrapText(text, maxWidth, bold);
        int currentY = y;

        for (String line : lines) {
            drawText(matrices, line, x, currentY, color, bold);
            currentY += LINE_HEIGHT;
        }

        return currentY;
    }

    private void renderStackCounter(MatrixStack matrices, Notification notification, int x, int y, float alpha) {
        String stackText = "x" + notification.getStackCount();

        int textWidth = (int) font.getWidth(stackText);
        int stackWidth = Math.max(textWidth + 8, 20);

        int stackX = x + NOTIFICATION_WIDTH - stackWidth - 6;
        int stackY = y + 6;

        Color stackTextColor = new Color(255, 255, 255, (int) (120 * alpha));
        int textX = stackX + (stackWidth - textWidth) / 2;
        int textY = stackY + 2;

        drawText(matrices, stackText, textX, textY, 8, stackTextColor, true);
    }

    private void renderIcon(MatrixStack matrices, NotificationType type, int x, int y, Color color) {
        FontAtlas lucide = NoctisUIClient.getInstance().getFonts().getLucide();
        lucide.render(matrices, type.getIcon(), x - 5, y - 5, 10, color.getRGB());
    }

    private void renderProgressBar(MatrixStack matrices, Notification notification, int x, int y, float alpha) {
        long elapsed = System.currentTimeMillis() - notification.getLastStackTime();
        float progress = Math.min(elapsed / (float) notification.getDuration(), 1f);

        Color trackColor = new Color(40, 44, 48, (int) (120 * alpha));
        Render2DEngine.drawRoundedRect(matrices, x + 4, y, NOTIFICATION_WIDTH - 8, 3, 1, trackColor);

        if (progress < 1f) {
            int barWidth = (int) ((NOTIFICATION_WIDTH - 8) * (1f - progress));
            Color progressColor = new Color(
                    notification.getColor().getRed(),
                    notification.getColor().getGreen(),
                    notification.getColor().getBlue(),
                    (int) (200 * alpha)
            );
            Render2DEngine.drawRoundedRect(matrices, x + 4, y, barWidth, 3, 1, progressColor);
        }
    }

    private void drawText(MatrixStack matrices, String text, int x, int y, Color color, boolean bold) {
        FontAtlas police = bold
                ? fontBold
                : font;

        police.render(matrices, text, x, y, color.getRGB());
    }

    private void drawText(MatrixStack matrices, String text, int x, int y, float size, Color color, boolean bold) {
        FontAtlas police = bold
                ? fontBold
                : font;

        police.render(matrices, text, x, y, size, color.getRGB());
    }

    private String truncateText(String text, int maxWidth, boolean bold) {
        FontAtlas police = bold ? fontBold : font;

        if (police.getWidth(text) <= maxWidth) {
            return text;
        }

        String ellipsis = "...";
        float ellipsisWidth = police.getWidth(ellipsis);

        StringBuilder truncated = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String candidate = truncated.toString() + text.charAt(i);
            if (police.getWidth(candidate) + ellipsisWidth > maxWidth) {
                break;
            }
            truncated.append(text.charAt(i));
        }

        return truncated.toString() + ellipsis;
    }
}