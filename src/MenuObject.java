import java.awt.Graphics;

public abstract class MenuObject implements Renderable, Updatable {

	protected int x, y, width, height;
	protected boolean visible, active;

	public MenuObject() {
		visible = true;
		active = true;
	}

	public MenuObject(int x, int y, int width, int height) {
		this();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void render(Graphics g) {

	}
}
