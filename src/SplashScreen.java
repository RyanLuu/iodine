import java.awt.Color;
import java.awt.Graphics;

public class SplashScreen implements Updatable, Renderable {
	// not really a splash screen, but close enough

	private Map map;
	private long birthday;
	private int age;

	public SplashScreen(Map map) {
		this.map = map;
		this.birthday = 0;
	}

	@Override
	public void render(Graphics g) {
		int decayTime = 55;
		int alpha = (int) ((float) (255 / decayTime) * Math.sqrt(-age + decayTime*decayTime));
		if (alpha >= 0) {
			Color c = new Color(255, 0, 255, alpha);
			g.setColor(c);
			g.setFont(Resources.FONT.deriveFont(1.6875f * Main.SCALE));
			g.drawString(map.getName(), Main.WIDTH / 32, Main.HEIGHT / 9);
			g.setFont(Resources.FONT.deriveFont(0.84375f * Main.SCALE));
			g.drawString(map.getSubname(), Main.WIDTH / 32, Main.HEIGHT / 6);
		}
	}

	@Override
	public void update() {
		this.age = (int) (System.currentTimeMillis() - birthday);
	}

	public void show() {
		this.birthday = System.currentTimeMillis();
	}

}
