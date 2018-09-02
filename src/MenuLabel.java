
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class MenuLabel extends MenuObject {

	private String text;

	public MenuLabel(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		this.text = text;
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.MAGENTA);
		g.setFont(Resources.FONT.deriveFont(0.5625f * Main.SCALE));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int tx = x + (width - metrics.stringWidth(text)) / 2;
		int ty = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.drawString(text, tx, ty);
	}
}
