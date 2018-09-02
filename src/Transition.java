import java.awt.Color;
import java.awt.Graphics;

public class Transition implements Renderable, Updatable {

	private Runnable action;
	private int age, length;

	public Transition(int length, Runnable action) {
		this.length = length;
		this.age = -1;
		this.action = action;
	}

	@Override
	public void update() {
		if (age >= 0) {
			age++;
		}
		if (age == length) {
			action.run();
		}
	}

	@Override
	public void render(Graphics g) {
		if (age >= 0) {
			int alpha = Math.min(255, (int) (((float) age / length) * 255));
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		}
	}

	public void play() {
		age = 0;
	}
}
