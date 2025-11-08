package fr.libnaus.noctisui.client.component.system;

import fr.libnaus.noctisui.client.common.QuickImports;
import fr.libnaus.noctisui.client.utils.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a notification with various properties such as type, title, message, color, duration, and animation state.
 *
 * <p>This class provides methods to update the notification's state, check if it should be removed, and manage stacking behavior.</p>
 *
 * <pre>
 *     {@code
 *     Notification notification = new Notification("notif-for-example", "Info", "This is an info message", NotificationType.INFO, 5000);
 *     }
 * </pre>
 *
 * @author axeno
 * @see NotificationType
 *
 */
@Getter
public class Notification implements QuickImports
{
    private final NotificationType type;
    private final String id;
    private final String title;
    private final String message;
    private final Color color;
    private final long duration;
    private float animationProgress;
    private final long creationTime;
    @Setter
    private float targetY;
    private float currentY;
    private float yVelocity;

    @Setter
    private int stackCount = 1;
    @Getter
    private long lastStackTime;

    /**
     * Constructs a new Notification with the specified parameters.
     *
     * @param id       A unique identifier for the notification.
     * @param title    The title of the notification.
     * @param message  The message content of the notification.
     * @param type     The type of the notification (e.g., INFO, WARNING, ERROR).
     * @param duration The duration (in milliseconds) for which the notification should be displayed.
     */
    public Notification(String id, String title, String message, NotificationType type, long duration)
    {
        this.id = id; this.title = title; this.message = message; this.type = type; this.color = type.getDefaultColor();
        this.duration = duration; this.creationTime = System.currentTimeMillis();
        this.lastStackTime = this.creationTime; this.animationProgress = 0f; this.targetY = 0f; this.currentY = 0f;
        this.yVelocity = 0f;
    }

    /**
     * Updates the notification's animation progress and position.
     * This method should be called periodically to animate the notification.
     */
    public void update()
    {
        long elapsedSinceCreation = System.currentTimeMillis() - creationTime;
        long elapsedSinceLastStack = System.currentTimeMillis() - lastStackTime;

        if (elapsedSinceCreation < 300) {
            float t = elapsedSinceCreation / 300f; animationProgress = 1f - (1f - t) * (1f - t);
        }
        else if (elapsedSinceLastStack > duration - 200) {
            float fadeProgress = (elapsedSinceLastStack - (duration - 200)) / 200f;
            animationProgress = 1f - fadeProgress * fadeProgress;
        }
        else {
            animationProgress = 1f;
        }

        updateYAnimation();
    }

    /**
     * Updates the Y position of the notification using a simple easing animation.
     */
    private void updateYAnimation()
    {
        float deltaY = targetY - currentY;

        if (Math.abs(deltaY) > 0.1f) {
            float speed = 0.12f; currentY = currentY + (deltaY * speed);

            if (Math.abs(deltaY) < 0.3f) {
                currentY = targetY; yVelocity = 0f;
            }
        }
        else {
            currentY = targetY; yVelocity = 0f;
        }
    }

    /**
     * Determines whether the notification should be removed based on its duration.
     *
     * @return true if the notification's display duration has elapsed; false otherwise.
     */
    public boolean shouldRemove()
    {
        long elapsed = System.currentTimeMillis() - lastStackTime; return elapsed > duration;
    }

    /**
     * Gets the slide offset for the notification animation.
     *
     * @return The slide offset value (currently always returns 0).
     */
    public float getSlideOffset()
    {
        return 0f;
    }

    /**
     * Gets the current alpha (opacity) value of the notification based on its animation progress.
     *
     * @return The alpha value between 0 (fully transparent) and 1 (fully opaque).
     */
    public float getAlpha()
    {
        return Math.max(0f, Math.min(1f, animationProgress));
    }

    /**
     * Checks if this notification is similar to another notification based on their IDs.
     *
     * @param other The other notification to compare with.
     *
     * @return true if both notifications have the same ID; false otherwise.
     */
    public boolean isSimilarTo(Notification other)
    {
        if (other == null) return false; return this.getId().equals(other.getId());
    }

    /**
     * Increments the stack count of the notification and updates the last stack time.
     * This method is used to handle stacking behavior for similar notifications.
     */
    public void incrementStack()
    {
        this.stackCount++; this.lastStackTime = System.currentTimeMillis();
    }

    /**
     * Checks if the notification has been stacked (i.e., if the stack count is greater than 1).
     *
     * @return true if the notification has been stacked; false otherwise.
     */
    public boolean hasStack()
    {
        return stackCount > 1;
    }
}