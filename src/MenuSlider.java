import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.function.Consumer;

public class MenuSlider extends MenuObject {

	private int sMin, sMax, sPos;
	private float slider;
	private boolean clickOn;
	private Rectangle sliderRect;
	private Consumer<Float> c;
	private String text;

	public MenuSlider(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		c = new Consumer<Float>() {

			@Override
			public void accept(Float t) {
				Sound.setVolume(t);
			}
		};
		sMax = x + width - height / 4;
		sMin = x + height / 4;
		sPos = (sMax + sMin) / 2;
		sliderRect = new Rectangle(x + height / 4, y + height / 4, width - height / 2, height / 2);
		this.text = text;
	}

	@Override
	public void render(Graphics g) {
		GradientPaint cFill = new GradientPaint(0, y, new Color(0, 0, 0, 0), 0, y + height, Color.MAGENTA);
		((Graphics2D) g).setPaint(cFill);
		g.fillRect(x, y, width, height);
		g.setColor(Color.MAGENTA);
		g.drawRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.fillRect(sliderRect.x, sliderRect.y, sliderRect.width, sliderRect.height);
		g.setColor(Color.MAGENTA);
		g.drawRect(sliderRect.x, sliderRect.y, sPos - sMin, sliderRect.height);
		
		g.setFont(Resources.FONT.deriveFont(0.375f * Main.SCALE));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int tx = sliderRect.x + (sliderRect.width - metrics.stringWidth(text)) / 2;
		int ty = sliderRect.y + ((sliderRect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.drawString(text, tx, ty);
	}

	@Override
	public void update() {
		if (Mouse.isPressed(1)) {
			clickOn = sliderRect.contains(Mouse.getX(), Mouse.getY());
		}
		if (clickOn && Mouse.isDown(1)) {
			sPos = Mouse.getX();
			sPos = Math.min(sMax, Math.max(sMin, sPos));
		}
		slider = ((float) sPos - sMin) / (sMax - sMin);
		c.accept(slider);
	}

}
