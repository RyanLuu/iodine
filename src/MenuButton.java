
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.sound.sampled.AudioInputStream;

public class MenuButton extends MenuObject {

	private AudioInputStream sound;
	private boolean hover, click;
	private Runnable action;
	private String text;
	Polygon p0, p1;

	public MenuButton(int x, int y, int width, int height, String text, Runnable action) {
		super(x, y, width, height);
		p0 = new Polygon(
				new int[] { x + 40, x, x, x + width, x + width, x + width - 10, x + width - 10, x + width, x + width },
				new int[] { y, y + 40, y + height, y + height, y + height - 20, y + height - 30, y + 30, y + 20, y },
				9);
		p1 = new Polygon(new int[] { x + 10, x, x, x + 30 }, new int[] { y, y + 10, y + 30, y }, 4);
		this.action = action;
		this.text = text;
		this.sound = Resources.BUTTON_PRESS;
	}

	public MenuButton(int x, int y, int width, int height, String text) {
		this(x, y, width, height, text, null);
	}

	@Override
	public void update() {
		hover = contains(Mouse.getX(), Mouse.getY());
		click = hover && Mouse.isDown(1);
		if (action != null && hover && Mouse.isReleased(1)) {
			click();
		}
	}

	@Override
	public void render(Graphics g) {
		Color cStroke = Color.MAGENTA.darker();
		cStroke = hover ? click ? cStroke.darker() : cStroke.brighter() : cStroke;
		GradientPaint cFill = new GradientPaint(0, y, new Color(0, 0, 0, 0), 0, y + height, cStroke);
		((Graphics2D) g).setPaint(cFill);
		g.fillPolygon(p0);
		g.fillPolygon(p1);
		g.setColor(cStroke);
		g.drawPolygon(p0);
		g.drawPolygon(p1);
		g.setFont(Resources.FONT.deriveFont(0.5625f * Main.SCALE));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int tx = x + (width - metrics.stringWidth(text)) / 2;
		int ty = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.drawString(text, tx, ty);
	}

	public boolean contains(int x, int y) {
		return p0.contains(x, y) || p1.contains(x, y);
	}

	public AudioInputStream getSound() {
		return sound;
	}

	public void setSound(AudioInputStream stream) {
		this.sound = stream;
	}

	public void click() {
		action.run();
		Sound.play(sound);
	}
}
