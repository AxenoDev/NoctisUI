package fr.libnaus.noctisui.client.component.system;

import fr.libnaus.noctisui.client.NoctisUIClient;
import fr.libnaus.noctisui.client.api.system.Render2DEngine;
import fr.libnaus.noctisui.client.api.system.render.font.FontAtlas;
import fr.libnaus.noctisui.client.api.system.render.font.Fonts;
import fr.libnaus.noctisui.client.common.QuickImports;
import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A manager for displaying and handling notifications in the UI.
 *
 * <p>This class allows you to create, display, and manage notifications with different types, durations, and styles.</p>
 *
 * <pre>
 *     {@code
 *     NotificationManager notificationManager = new NotificationManager();
 *     notificationManager.success("save_success", "Success", "Your changes have been saved.");
 *     notificationManager.error("save_error", "Error", "Failed to save changes.");
 *     }
 * </pre>
 *
 * @author axeno
 *
 */
public class NotificationManager
{

    private static final int NOTIFICATION_WIDTH = 220;
    private static final int NOTIFICATION_MIN_HEIGHT = 40;
    private static final int NOTIFICATION_MAX_HEIGHT = 80;
    private static final int LINE_HEIGHT = 12;
    private static final int NOTIFICATION_SPACING = 6;
    private static final int MARGIN_X = 12;
    private static final int MARGIN_Y = 12;
    @Getter
    private static NotificationManager instance;
    @Setter
    @Getter
    private static FontAtlas font;
    @Setter
    @Getter
    private static FontAtlas fontBold;
    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    /**
     * Creates a new NotificationManager instance and sets it as the singleton instance.
     */
    public NotificationManager()
    {
        instance = this;
    }

    /**
     * Initializes the fonts used for rendering notifications.
     *
     * @param fonts The Fonts instance containing the desired fonts.
     */
    public static void initFont(Fonts fonts)
    {
        font = fonts.getInterMedium();
        fontBold = fonts.getInterBold();
    }

    /**
     * Registers the notification rendering callback to the HUD render event.
     * This method should be called once during the client initialization.
     */
    public static void init()
    {
        HudRenderCallback.EVENT.register(NotificationManager::renderNotifications);
    }

    /**
     * Renders the notifications on the HUD.
     *
     * @param ctx       The DrawContext for rendering.
     * @param tickDelta The tick delta for smooth animations.
     */
    private static void renderNotifications(DrawContext ctx, float tickDelta)
    {
        NotificationManager manager = getInstance();
        manager.update();
        manager.render(ctx.getMatrices());
    }

    /**
     * Adds a new notification to be displayed.
     *
     * @param id      A unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     * @param type    The type of the notification (e.g., INFO, WARNING, ERROR).
     */
    public void addNotification(String id, String title, String message, NotificationType type)
    {
        addNotification(id, title, message, type, 3000);
    }

    /**
     * Adds a new notification to be displayed with a custom duration.
     *
     * @param id       A unique identifier for the notification.
     * @param title    The title of the notification.
     * @param message  The message content of the notification.
     * @param type     The type of the notification (e.g., INFO, WARNING, ERROR).
     * @param duration The duration (in milliseconds) for which the notification should be displayed.
     */
    public void addNotification(String id, String title, String message, NotificationType type, long duration)
    {
        Notification newNotification = new Notification(id, title, message, type, duration);

        for (Notification existing : notifications) {
            if (existing.isSimilarTo(newNotification)) {
                existing.incrementStack();
                return;
            }
        }

        notifications.add(newNotification);
    }

    /**
     * Adds a success notification.
     *
     * @param id      A unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    public void success(String id, String title, String message)
    {
        addNotification(id, title, message, NotificationType.SUCCESS);
    }

    /**
     * Adds an error notification.
     *
     * @param id      A unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    public void error(String id, String title, String message)
    {
        addNotification(id, title, message, NotificationType.ERROR);
    }

    /**
     * Adds a warning notification.
     *
     * @param id      A unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    public void warning(String id, String title, String message)
    {
        addNotification(id, title, message, NotificationType.WARNING);
    }

    /**
     * Adds an info notification.
     *
     * @param id      A unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    public void info(String id, String title, String message)
    {
        addNotification(id, title, message, NotificationType.INFO);
    }

    /**
     * Updates the state of all notifications, including their positions and removal status.
     */
    private void update()
    {
        List<Notification> toRemove = new ArrayList<>();
        List<Notification> visibleNotif = new ArrayList<>();

        for (Notification notification : notifications) {
            notification.update();
            if (notification.shouldRemove()) toRemove.add(notification);
            else visibleNotif.add(notification);
        }

        notifications.removeAll(toRemove);

        int currentY = 0;
        for (Notification notification : visibleNotif) {
            notification.setTargetY(currentY);
            currentY += calculateNotificationHeight(notification) + NOTIFICATION_SPACING;
        }
    }

    /**
     * Renders all active notifications on the screen.
     *
     * @param matrices The MatrixStack used for rendering transformations.
     */
    private void render(MatrixStack matrices)
    {
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

    /**
     * Renders a single notification at the specified position with the given alpha transparency.
     *
     * @param matrices     The MatrixStack used for rendering transformations.
     * @param notification The Notification to be rendered.
     * @param x            The X-coordinate for rendering the notification.
     * @param y            The Y-coordinate for rendering the notification.
     * @param alpha        The alpha transparency value (0.0 to 1.0) for the notification.
     */
    private void renderNotification(MatrixStack matrices, Notification notification, int x, int y, float alpha)
    {
        alpha = Math.max(0f, Math.min(1f, alpha));

        int notificationHeight = calculateNotificationHeight(notification);

        Color bgColor = new Color(24, 26, 29, (int) (250 * alpha));
        Render2DEngine.drawRoundedRect(matrices, x, y, NOTIFICATION_WIDTH, notificationHeight, 8, bgColor);

        Color borderColor = new Color(52, 58, 64, (int) (180 * alpha));
        Render2DEngine.drawRoundedOutline(matrices, x, y, NOTIFICATION_WIDTH, notificationHeight, 8, 1.2f, borderColor);

        Color accentColor = new Color(notification.getColor().getRed(), notification.getColor().getGreen(), notification.getColor().getBlue(), (int) (255 * alpha));

        Render2DEngine.drawRoundedRect(matrices, x + 4, y + 6, 3, notificationHeight - 12, 1, accentColor);

        Color iconBgColor = new Color(notification.getColor().getRed(), notification.getColor().getGreen(), notification.getColor().getBlue(), (int) (25 * alpha));
        Render2DEngine.drawRoundedRect(matrices, x + 13, y + 10 + (float) (notificationHeight - 20) / 2 - 10, 20, 20, 6, iconBgColor);

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

        if (notification.hasStack()) renderStackCounter(matrices, notification, x, y, alpha);

        renderProgressBar(matrices, notification, x, y + notificationHeight - 4, alpha);
    }

    /**
     * Calculates the height of a notification based on its content.
     *
     * @param notification The Notification for which to calculate the height.
     *
     * @return The calculated height of the notification.
     */
    private int calculateNotificationHeight(Notification notification)
    {
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

    /**
     * Wraps the given text into multiple lines based on the specified maximum width.
     *
     * @param text     The text to be wrapped.
     * @param maxWidth The maximum width (in pixels) for each line.
     * @param bold     Whether to use the bold font for width calculations.
     *
     * @return A list of strings, each representing a line of wrapped text.
     */
    private List<String> wrapText(String text, int maxWidth, boolean bold)
    {
        FontAtlas police = bold ? fontBold : font;
        List<String> lines = new ArrayList<>();

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;

            if (police.getWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            }
            else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
                else {
                    // Si même un mot seul dépasse, on le tronque
                    lines.add(truncateText(word, maxWidth, bold));
                }
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    /**
     * Renders wrapped text at the specified position, handling line breaks as needed.
     *
     * @param matrices The MatrixStack used for rendering transformations.
     * @param text     The text to be rendered.
     * @param x        The X-coordinate for rendering the text.
     * @param y        The starting Y-coordinate for rendering the text.
     * @param maxWidth The maximum width (in pixels) for each line of text.
     * @param color    The color of the text.
     * @param bold     Whether to use the bold font for rendering.
     *
     * @return The Y-coordinate after rendering the text (useful for further rendering).
     */
    private int renderWrappedText(MatrixStack matrices, String text, int x, int y, int maxWidth, Color color,
                                  boolean bold)
    {
        List<String> lines = wrapText(text, maxWidth, bold);
        int currentY = y;

        for (String line : lines) {
            drawText(matrices, line, x, currentY, color, bold);
            currentY += LINE_HEIGHT;
        }

        return currentY;
    }

    /**
     * Renders the stack counter for a notification if it has multiple stacked instances.
     *
     * @param matrices     The MatrixStack used for rendering transformations.
     * @param notification The Notification for which to render the stack counter.
     * @param x            The X-coordinate for rendering the stack counter.
     * @param y            The Y-coordinate for rendering the stack counter.
     * @param alpha        The alpha transparency value (0.0 to 1.0) for the stack counter.
     */
    private void renderStackCounter(MatrixStack matrices, Notification notification, int x, int y, float alpha)
    {
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

    /**
     * Renders the icon for a notification type at the specified position.
     *
     * @param matrices The MatrixStack used for rendering transformations.
     * @param type     The NotificationType whose icon is to be rendered.
     * @param x        The X-coordinate for rendering the icon.
     * @param y        The Y-coordinate for rendering the icon.
     * @param color    The color to apply to the icon.
     */
    private void renderIcon(MatrixStack matrices, NotificationType type, int x, int y, Color color)
    {
        FontAtlas lucide = NoctisUIClient.getInstance().getFonts().getLucide();
        lucide.render(matrices, type.getIcon(), x - 5, y - 5, 10, color.getRGB());
    }

    /**
     * Renders the progress bar for a notification at the specified position.
     *
     * @param matrices     The MatrixStack used for rendering transformations.
     * @param notification The Notification for which to render the progress bar.
     * @param x            The X-coordinate for rendering the progress bar.
     * @param y            The Y-coordinate for rendering the progress bar.
     * @param alpha        The alpha transparency value (0.0 to 1.0) for the progress bar.
     */
    private void renderProgressBar(MatrixStack matrices, Notification notification, int x, int y, float alpha)
    {
        long elapsed = System.currentTimeMillis() - notification.getLastStackTime();
        float progress = Math.min(elapsed / (float) notification.getDuration(), 1f);

        Color trackColor = new Color(40, 44, 48, (int) (120 * alpha));
        Render2DEngine.drawRoundedRect(matrices, x + 4, y, NOTIFICATION_WIDTH - 8, 3, 1, trackColor);

        if (progress < 1f) {
            int barWidth = (int) ((NOTIFICATION_WIDTH - 8) * (1f - progress));
            Color progressColor = new Color(notification.getColor().getRed(), notification.getColor().getGreen(), notification.getColor().getBlue(), (int) (200 * alpha));
            Render2DEngine.drawRoundedRect(matrices, x + 4, y, barWidth, 3, 1, progressColor);
        }
    }

    /**
     * Draws text at the specified position with the given color and font style.
     *
     * @param matrices The MatrixStack used for rendering transformations.
     * @param text     The text to be drawn.
     * @param x        The X-coordinate for rendering the text.
     * @param y        The Y-coordinate for rendering the text.
     * @param color    The color of the text.
     * @param bold     Whether to use the bold font for rendering.
     */
    private void drawText(MatrixStack matrices, String text, int x, int y, Color color, boolean bold)
    {
        FontAtlas police = bold ? fontBold : font;

        police.render(matrices, text, x, y, color.getRGB());
    }

    /**
     * Draws text at the specified position with the given size, color, and font style.
     *
     * @param matrices The MatrixStack used for rendering transformations.
     * @param text     The text to be drawn.
     * @param x        The X-coordinate for rendering the text.
     * @param y        The Y-coordinate for rendering the text.
     * @param size     The size of the font.
     * @param color    The color of the text.
     * @param bold     Whether to use the bold font for rendering.
     */
    private void drawText(MatrixStack matrices, String text, int x, int y, float size, Color color, boolean bold)
    {
        FontAtlas police = bold ? fontBold : font;

        police.render(matrices, text, x, y, size, color.getRGB());
    }

    /**
     * Truncates the given text to fit within the specified maximum width, adding an ellipsis if necessary.
     *
     * @param text     The text to be truncated.
     * @param maxWidth The maximum width (in pixels) for the text.
     * @param bold     Whether to use the bold font for width calculations.
     *
     * @return The truncated text with an ellipsis if it exceeds the maximum width.
     */
    private String truncateText(String text, int maxWidth, boolean bold)
    {
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

        return truncated + ellipsis;
    }
}