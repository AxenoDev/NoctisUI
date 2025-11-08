package fr.libnaus.noctisui.client.component.system;

import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;

/**
 * Enum representing different types of notifications, each with a default color and icon.
 *
 * <p>This enum provides a way to categorize notifications into types such as SUCCESS, ERROR, WARNING, and INFO,</p>
 * <p>each associated with a specific color and icon for visual representation.</p>
 *
 * @author axeno
 * @see Notification
 *
 */
@Getter
public enum NotificationType
{
    SUCCESS(new Color(52, 211, 153), "\uE951"),
    ERROR(new Color(248, 113, 113), "\uEB15"),
    WARNING(new Color(251, 146, 60), "\uE90A"),
    INFO(new Color(59, 130, 246), "\uEA0C");

    private final Color defaultColor;
    private final String icon;

    /**
     * Constructs a NotificationType with the specified default color and icon.
     *
     * @param defaultColor The default {@link Color} associated with the notification type.
     * @param icon         The icon associated with the notification type.
     */
    NotificationType(Color defaultColor, String icon)
    {
        this.defaultColor = defaultColor; this.icon = icon;
    }
}