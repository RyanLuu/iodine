import java.awt.Graphics;

public abstract class GameObject implements Renderable, Updatable {

	protected float x, y;
	protected boolean paused;
	protected boolean hidden;

	public GameObject() {
	}

	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public abstract void update();

	@Override
	public abstract void render(Graphics g);

	public void pause() {
		paused = true;
	}

	public void unpause() {
		paused = false;
	}

	public void hide() {
		hidden = true;
	}

	public void unhide() {
		hidden = false;
	}
}