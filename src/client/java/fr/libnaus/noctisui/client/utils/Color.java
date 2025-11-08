package fr.libnaus.noctisui.client.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an RGBA color and provides utilities for color manipulation.
 *
 * <p>This class allows creating colors using red, green, blue, and alpha components,
 * as well as performing operations such as brightness adjustment, interpolation,
 * and conversion between RGB and HSB color models.</p>
 *
 * <p>Unlike {@link java.awt.Color}, this class stores the color as a single integer value.</p>
 *
 * <pre>
 * {@code
 * // Create a fully opaque red color
 * Color red = new Color(255, 0, 0);
 *
 * // Create a semi-transparent blue color
 * Color semiBlue = new Color(0, 0, 255, 128);
 *
 * // Adjust brightness
 * Color brighterRed = red.brighter();
 * Color darkerBlue = semiBlue.darker();
 *
 * // Interpolate between two colors (50% progress)
 * Color blended = red.interpolateColor(red, semiBlue, 0.5f);
 *
 * // Get RGB or individual components
 * int rgb = red.getRGB();
 * int alpha = semiBlue.getAlpha();
 * int green = semiBlue.getGreen();
 *
 * // HSB conversions
 * float[] hsb = Color.RGBtoHSB(255, 0, 0, null);
 * Color fromHSB = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
 * }
 * </pre>
 *
 * <p>This class also defines common colors as public fields for convenience, e.g., {@link #WHITE}, {@link #BLACK}, {@link #RED}.</p>
 *
 * @author axeno
 */
@Getter
@Setter
public class Color
{

    /** The packed RGBA color value as a single integer. */
    private int value;

    /** Factor used for brightening and darkening colors. */
    private static final double FACTOR = 0.7;

    /** Standard colors for convenience */
    public static Color WHITE = new Color(255, 255, 255);
    public static Color BLACK = new Color(0, 0, 0);
    public static Color RED = new Color(255, 0, 0);
    public static Color GREEN = new Color(0, 255, 0);
    public static Color BLUE = new Color(0, 0, 255);
    public static Color YELLOW = new Color(255, 255, 0);
    public static Color MAGENTA = new Color(255, 0, 255);
    public static Color CYAN = new Color(0, 255, 255);
    public static Color LIGHT_GRAY = new Color(192, 192, 192);
    public static Color GRAY = new Color(128, 128, 128);
    public static Color DARK_GRAY = new Color(64, 64, 64);

    /**
     * Constructs a color with the specified red, green, blue, and alpha components.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @param a Alpha component (0-255)
     */
    public Color(int r, int g, int b, int a) {
        this.value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                (b & 0xFF);
    }

    /**
     * Constructs an opaque color with the specified red, green, and blue components.
     *
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Constructs a color from a packed RGB integer value.
     * The alpha component is set to 255 (opaque).
     *
     * @param rgb Packed RGB value (0xRRGGBB)
     */
    public Color(int rgb) {
        this.value = 0xff000000 | rgb;
    }

    /**
     * Constructs a color from a packed RGBA integer value.
     *
     * @param rgba Packed RGBA value
     * @param hasAlpha True if the alpha component is included, false otherwise
     */
    public Color(int rgba, boolean hasAlpha) {
        if (hasAlpha) {
            this.value = rgba;
        } else {
            this.value = 0xff000000 | rgba;
        }
    }

    /** Returns the red component (0-255). */
    public int getRed() { return (value >> 16) & 0xFF; }

    /** Returns the green component (0-255). */
    public int getGreen() { return (value >> 8) & 0xFF; }

    /** Returns the blue component (0-255). */
    public int getBlue() { return value & 0xFF; }

    /** Returns the alpha component (0-255). */
    public int getAlpha() { return (value >> 24) & 0xFF; }

    /** Returns the packed RGBA integer value. */
    public int getRGB() { return value; }

    /**
     * Returns a brighter version of this color.
     *
     * @return New Color instance brighter than this one
     */
    public Color brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int alpha = getAlpha();

        int i = (int)(1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int)(r / FACTOR), 255),
                Math.min((int)(g / FACTOR), 255),
                Math.min((int)(b / FACTOR), 255),
                alpha);
    }

    /**
     * Returns a darker version of this color.
     *
     * @return New Color instance darker than this one
     */
    public Color darker() {
        return new Color(Math.max((int)(getRed() * FACTOR), 0),
                Math.max((int)(getGreen() * FACTOR), 0),
                Math.max((int)(getBlue() * FACTOR), 0),
                getAlpha());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color && ((Color) obj).getRGB() == this.getRGB();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    }

    /**
     * Decodes a color from a string representation of an integer.
     *
     * @param nm String representation of an integer, e.g., "0xFF0000"
     * @return Color instance
     */
    public static Color decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    /** Converts HSB to RGB packed integer. */
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0 -> { r = (int)(brightness*255+0.5f); g = (int)(t*255+0.5f); b = (int)(p*255+0.5f); }
                case 1 -> { r = (int)(q*255+0.5f); g = (int)(brightness*255+0.5f); b = (int)(p*255+0.5f); }
                case 2 -> { r = (int)(p*255+0.5f); g = (int)(brightness*255+0.5f); b = (int)(t*255+0.5f); }
                case 3 -> { r = (int)(p*255+0.5f); g = (int)(q*255+0.5f); b = (int)(brightness*255+0.5f); }
                case 4 -> { r = (int)(t*255+0.5f); g = (int)(p*255+0.5f); b = (int)(brightness*255+0.5f); }
                case 5 -> { r = (int)(brightness*255+0.5f); g = (int)(p*255+0.5f); b = (int)(q*255+0.5f); }
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * Converts RGB components to HSB.
     *
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @param hsbvals Optional array to store the result
     * @return Array containing hue, saturation, brightness
     */
    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        if (hsbvals == null) hsbvals = new float[3];
        int cmax = Math.max(r, Math.max(g, b));
        int cmin = Math.min(r, Math.min(g, b));

        float brightness = cmax / 255f;
        float saturation = (cmax != 0) ? (cmax - cmin) / (float)cmax : 0;
        float hue;
        if (saturation == 0) {
            hue = 0;
        } else {
            float rc = (cmax - r) / (float)(cmax - cmin);
            float gc = (cmax - g) / (float)(cmax - cmin);
            float bc = (cmax - b) / (float)(cmax - cmin);
            hue = (r == cmax) ? bc - gc : (g == cmax) ? 2f + rc - bc : 4f + gc - rc;
            hue /= 6f;
            if (hue < 0) hue += 1f;
        }

        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    /** Returns a Color instance from HSB values. */
    public static Color getHSBColor(float h, float s, float b) {
        return new Color(HSBtoRGB(h, s, b));
    }

    /**
     * Interpolates linearly between two colors.
     *
     * @param color1 Start color
     * @param color2 End color
     * @param progress Progress (0.0 to 1.0)
     * @return Interpolated color
     */
    public static Color interpolateColor(Color color1, Color color2, float progress) {
        float r = color1.getRed() + (color2.getRed() - color1.getRed()) * progress;
        float g = color1.getGreen() + (color2.getGreen() - color1.getGreen()) * progress;
        float b = color1.getBlue() + (color2.getBlue() - color1.getBlue()) * progress;
        float a = color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * progress;
        return new Color((int) r, (int) g, (int) b, (int) a);
    }
}
