import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GUI implements Renderable {

	private static final int METER_HEIGHT = Map.TILE_HEIGHT;

	private Player[] players;
	private Polygon[] meters;

	public GUI(Player... players) {
		this.players = players;
		meters = new Polygon[players.length];
		int n = players.length;
		for (int i = 0; i < n; i++) {
			int x0 = (int) (Main.WIDTH * ((double) i / n));
			int x1 = x0 + (int) ((Main.WIDTH / n - 1) * ((double) Player.ATTACK_THRESHOLD / Player.MAX_METER));
			int x2 = x0 + Main.WIDTH / n - 1;
			int y0 = Main.HEIGHT - METER_HEIGHT;
			int y1 = Main.HEIGHT - 1;
			meters[i] = new Polygon(new int[] { x0, x1 - 5, x1, x1 + 5, x2, x2, x1 + 5, x1, x1 - 5, x0 },
					new int[] { y0, y0, y0 + 5, y0, y0, y1, y1, y1 - 5, y1, y1 }, 10);
		}
	}

	public void render(Graphics g) {
		int n = players.length;
		for (int i = 0; i < n; i++) {
			int mx = (int) (Main.WIDTH * ((double) i / n));
			int my = Main.HEIGHT - METER_HEIGHT;
			int mw = Main.WIDTH / n - 1;
			int mh = METER_HEIGHT;
			Color c = players[i].getPrefs().color;
			g.setColor(c);
			g.setFont(Resources.FONT.deriveFont(36f));
			g.drawString("" + players[i].getScore(), mx, my + mh);

			g.drawPolygon(meters[i]);
			double m = players[i].getMeter();

			g.setClip(meters[i]);

			Color ct = new Color(c.getRed(), c.getGreen(), c.getBlue(), m >= 25 ? 127 : 63);
			GradientPaint gp = new GradientPaint(0, my, new Color(0, 0, 0, 0), 0, my + mh, ct);
			((Graphics2D)g).setPaint(gp);
			g.fillRect(mx, my, (int) (mw * m / 100), mh);
			g.setClip(null);
		}
	}

}