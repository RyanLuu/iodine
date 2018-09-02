
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class MenuKeyConfig extends MenuObject {

	private KeyConfig kc;
	private Color color;
	private MenuKeyConfigButton[] buttons;
	private String[] keyLabels = new String[] { "UP", "DOWN", "LEFT", "RIGHT", "LIGHT ATTACK", "HEAVY ATTACK",
			"SHIELD" };
	private int[] bindings;

	public MenuKeyConfig(int x, int y, int width, int height, Player p) {
		super(x, y, width, height);
		this.kc = p.getPrefs().keys;
		bindings = new int[] { kc.up, kc.down, kc.left, kc.right, kc.latt, kc.hatt, kc.shield };
		this.color = p.getPrefs().color;
		int w = width;
		int h = height / 7;
		buttons = new MenuKeyConfigButton[keyLabels.length];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new MenuKeyConfigButton(x, y + h * i, w, h);
		}
	}

	@Override
	public void update() {
		if (Mouse.isPressed(1)) {
			for (MenuKeyConfigButton b : buttons) {
				b.selected = b.contains(Mouse.getX(), Mouse.getY());
			}
		}
		for (int i = 0; i < buttons.length; i++) {
			MenuKeyConfigButton b = buttons[i];
			if (b.selected) {
				for (int j = 0; j < Keyboard.nKeys; j++) {
					if (Keyboard.isPressed(j)) {
						bindings[i] = j;
						switch (i) {
						case 0:
							kc.up = j;
							break;
						case 1:
							kc.down = j;
							break;
						case 2:
							kc.left = j;
							break;
						case 3:
							kc.right = j;
							break;
						case 4:
							kc.latt = j;
							break;
						case 5:
							kc.hatt = j;
							break;
						case 6:
							kc.shield = j;
							break;
						}
						b.selected = false;
					}

				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		for (int i = 0; i < keyLabels.length; i++) {
			MenuKeyConfigButton b = buttons[i];
			Rectangle br = b.buttonRect, lr = b.labelRect;
			Color cStroke = Util.transparentColor(color, 127);
			GradientPaint cFill = new GradientPaint(0, br.y, new Color(0, 0, 0, 0), 0, br.y + br.height, cStroke);
			((Graphics2D) g).setPaint(cFill);
			g.setColor(cStroke);
			g.drawRect(lr.x, lr.y, lr.width, lr.height);
			g.setFont(Resources.FONT.deriveFont(0.375f * Main.SCALE));
			Util.drawCenteredString(g, keyLabels[i], lr);
			cStroke = b.selected ? color : Util.transparentColor(color, 159);
			cFill = new GradientPaint(0, lr.y, new Color(0, 0, 0, 0), 0, lr.y + lr.height, cStroke);
			((Graphics2D) g).setPaint(cFill);
			g.fillRect(br.x, br.y, br.width, br.height);
			g.setColor(cStroke);
			g.drawRect(br.x, br.y, br.width, br.height);
			Util.drawCenteredString(g, KeyEvent.getKeyText(bindings[i]), br);
		}
	}

	public void setPlayer(Player p) {
		this.kc = p.getPrefs().keys;
		this.color = p.getPrefs().color;
		bindings[0] = kc.up;
		bindings[1] = kc.down;
		bindings[2] = kc.left;
		bindings[3] = kc.right;
		bindings[4] = kc.latt;
		bindings[5] = kc.hatt;
		bindings[6] = kc.shield;
	}

	class MenuKeyConfigButton {
		private Rectangle buttonRect, labelRect;
		private boolean selected;

		public MenuKeyConfigButton(int x, int y, int width, int height) {
			buttonRect = new Rectangle(x + width / 2, y, width / 2, height);
			labelRect = new Rectangle(x, y, width / 2, height);
		}

		public boolean contains(int x, int y) {
			return buttonRect.contains(x, y);
		}
	}

}
