import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Util {

	public static Color transparentColor(Color c, int alpha) {
		alpha = Math.max(Math.min(alpha, 255), 0);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}

	public static void drawCenteredString(Graphics g, String text, Rectangle rect) {
		drawCenteredString(g, text, rect, null);
	}

	public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		FontMetrics metrics = g.getFontMetrics(font != null ? font : g.getFont());
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		if (font != null) {
			g.setFont(font);
		}
		g.drawString(text, x, y);
	}

	public static int[] scaleToFit(int b, int m, float[] points) {
		int[] ret = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ret[i] = b + (int) (m * points[i]);
		}
		return ret;
	}
}
