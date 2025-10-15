package org.ppvon.ultimateCobblemonProgression.client.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.function.DoubleConsumer;

public final class DoubleField extends EditBox {
    private boolean valid = true;
    private Runnable onValidityChange = () -> {};
    private DoubleConsumer onValidChange = d -> {};

    public DoubleField(Font font, int x, int y, int w, int h, double initial) {
        super(font, x, y, w, h, Component.empty());
        setMaxLength(64);
        setValue(format(initial));
        setResponder(this::onChangeText);
    }

    public void setOnValidityChange(Runnable r) {
        this.onValidityChange = (r != null) ? r : () -> {};
    }

    public void setOnValidChange(DoubleConsumer c) {
        this.onValidChange = (c != null) ? c : d -> {};
    }

    private void onChangeText(String txt) {
        double[] out = new double[1];
        boolean newValid = tryParse(txt, out);

        if (newValid != valid) {
            valid = newValid;
            onValidityChange.run();
        }

        setTextColor(valid ? 0xE0E0E0 : 0xFF6868);

        if (newValid) {
            onValidChange.accept(out[0]);
        }
    }

    public boolean isValidNumber() {
        return tryParse(getValue(), null);
    }

    public double getOrElse(double fallback) {
        double[] out = new double[1];
        return tryParse(getValue(), out) ? out[0] : fallback;
    }

    private static boolean tryParse(String s, double[] out) {
        if (s == null || s.isBlank()) return false;
        try {
            double d = Double.parseDouble(s.trim());
            if (out != null) out[0] = d;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String format(double v) {
        String s = String.format(Locale.ROOT, "%.6f", v);
        while (s.contains(".") && (s.endsWith("0") || s.endsWith("."))) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
