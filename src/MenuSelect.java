
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.sound.sampled.AudioInputStream;

public class MenuSelect extends MenuObject {

	private AudioInputStream sound;
	private MenuSelectEntry[] entries;
	int selected;
	private Rectangle rect;
	private boolean horizontal;

	public MenuSelect(int x, int y, int width, int height, String[] entries, Runnable[] actions) {
		super(x, y, width, height);
		this.entries = new MenuSelectEntry[entries.length];
		for (int i = 0; i < this.entries.length; i++) {
			this.entries[i] = new MenuSelectEntry();
			if (entries[i] != null) {
				this.entries[i].label = entries[i];
			}
			if (actions != null && actions[i] != null) {
				this.entries[i].action = actions[i];
			}
		}
		this.sound = Resources.BUTTON_PRESS;
		this.rect = new Rectangle(x, y, width, height);
		selected = 0;
		horizontal = false;
	}

	public MenuSelect(int x, int y, int width, int height, String[] texts) {
		this(x, y, width, height, texts, null);
	}

	@Override
	public void update() {
		if (Mouse.isReleased(1) && contains(Mouse.getX(), Mouse.getY())) {
			int toBeSelected;
			if (horizontal) {
				toBeSelected = (int) (((float) (Mouse.getX() - x) / width) * entries.length);
			} else {
				toBeSelected = (int) (((float) (Mouse.getY() - y) / height) * entries.length);
			}
			if (entries[toBeSelected].enabled) {
				selected = toBeSelected;
				Sound.play(Resources.BUTTON_PRESS);
			}
			if (entries[selected].action != null) {
				entries[selected].action.run();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		boolean skipSelected = selected < entries.length - 1; // a flag used to
																// render
																// selected
																// entry last

		int xCurr = 0, xNext = 0;
		int yCurr = 0, yNext = 0;
		for (int i = 0; i < entries.length; i++) {
			if (i == selected && skipSelected) {
				skipSelected = false;
				continue;
			}
			Color cStroke = selected == i ? Color.MAGENTA : Color.MAGENTA.darker().darker();
			if (!entries[i].enabled) {
				cStroke = Util.transparentColor(Color.MAGENTA, 31);
			}
			Polygon p0, p1;
			if (horizontal) {
				xCurr = (int) (i * ((float) width / entries.length));
				xNext = (int) ((i + 1) * ((float) width / entries.length));
				p0 = new Polygon(new int[] { x + xCurr, x + xCurr + 20, x + xNext, x + xNext, x + xCurr },
						new int[] { y + 20, y, y, y + height, y + height }, 5);
				p1 = new Polygon(new int[] { x + xCurr, x + xCurr + 10, x + xCurr }, new int[] { y, y, y + 10 }, 3);
			} else {
				yCurr = (int) (i * ((float) height / entries.length));
				yNext = (int) ((i + 1) * ((float) height / entries.length));
				p0 = new Polygon(new int[] { x + 20, x, x, x + width, x + width },
						new int[] { y + yCurr, y + yCurr + 20, y + yNext, y + yNext, y + yCurr }, 5);
				p1 = new Polygon(new int[] { x, x, x + 10 }, new int[] { y + yCurr, y + yCurr + 10, y + yCurr }, 3);
			}
			Rectangle r = p0.getBounds();
			GradientPaint cFill = new GradientPaint(r.x, r.y, new Color(0, 0, 0, 0), r.x, (int) r.getMaxY(), cStroke);
			((Graphics2D) g).setPaint(cFill);
			g.fillPolygon(p0);
			g.setColor(cStroke);
			g.drawPolygon(p0);
			g.fillPolygon(p1);
			g.setFont(Resources.FONT.deriveFont(0.5625f * Main.SCALE));
			Util.drawCenteredString(g, entries[i].label, r);
			if (!skipSelected && i == selected) {
				i = entries.length;
			}
			if (i == entries.length - 1) {
				i = selected - 1;
			}
		}

	}

	public boolean contains(int x, int y) {
		return rect.contains(x, y) || rect.contains(x, y);
	}

	public AudioInputStream getSound() {
		return sound;
	}

	public void setSound(AudioInputStream stream) {
		this.sound = stream;
	}

	public int getSelected() {
		return selected;
	}

	public int getNumEntries() {
		return entries.length;
	}

	public void setHorizontal(boolean b) {
		horizontal = b;
	}

	public void enableEntry(int i) {
		entries[i].enabled = true;
	}

	public void disableEntry(int i) {
		entries[i].enabled = false;
	}
}

class MenuSelectEntry {
	String label;
	Runnable action;
	boolean enabled;

	public MenuSelectEntry() {
		label = "";
		enabled = true;
	}
}