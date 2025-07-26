package fr.libnaus.noctisui.client.utils;

public class MathUtils {

    public static float lerp(float start, float end, float factor) {
        return start + (end - start) * Math.min(factor, 1f);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}
