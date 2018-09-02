import java.awt.Color;
import java.awt.Graphics;

public class Rune extends GameObject {
	
	private int WIDTH = 6;
	private int MARGIN = 1;
	private int HEIGHT = 12;
	private float SCALE = 3;
	private float a;

	Line[] lines;

	public Rune(float x, float y) {
		super(x, y);
		this.lines = Symbol.random().getLines();
	}

	@Override
	public void update() {
		a += 0.1;
		if (a >= Math.PI * 1.5) {
			a -= Math.PI * 2;
			this.lines = Symbol.random().getLines();
		}
	}

	@Override
	public void render(Graphics g) {
		if (Math.cos(a) >= 0) {
			g.setColor(Color.MAGENTA);
		} else {
			g.setColor(Color.MAGENTA.darker());
		}
		g.drawRect((int) (x - WIDTH * SCALE / 2 * Math.abs(Math.cos(a)) - MARGIN * SCALE),
				(int) (y - MARGIN * SCALE - HEIGHT * SCALE / 2),
				(int) (Math.abs(WIDTH * SCALE * Math.cos(a)) + MARGIN * SCALE * 2),
				(int) (HEIGHT * SCALE + MARGIN * SCALE * 2));
		for (Line l : lines) {
			int x1 = (int) ((l.x1 * SCALE - WIDTH * SCALE / 2) * Math.cos(a) + x);
			int y1 = (int) (l.y1 * SCALE - HEIGHT * SCALE / 2 + y);
			int x2 = (int) ((l.x2 * SCALE - WIDTH * SCALE / 2) * Math.cos(a) + x);
			int y2 = (int) (l.y2 * SCALE - HEIGHT * SCALE / 2 + y);
			g.drawLine(x1, y1, x2, y2);
		}
	}

}

class Line {
	public int x1, y1, x2, y2;

	public Line(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}
