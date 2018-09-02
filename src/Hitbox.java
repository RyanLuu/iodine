import java.awt.Polygon;
import java.awt.Rectangle;

public class Hitbox implements Updatable {

	public static final int DURATION = 7;

	public int life;
	public int x, y, w, h;
	public float power;
	public Direction dir;
	private Player owner;
	private Rectangle aabb;
	private Polygon poly;
	public boolean render;

	public Hitbox(Player owner) {
		this.life = 0;
		this.owner = owner;
		this.render = false;
		aabb = new Rectangle();
		poly = new Polygon();
	}

	public void update() {
		if (isActive()) {
			updateLocation();
			life--;
			render = true;
		}
		if (life  <= 0) {
			render = false;
		}
	}

	public void updateLocation() {
		aabb.x = (int) (x + owner.x);
		aabb.y = (int) (y + owner.y);
		aabb.width = w;
		aabb.height = h;
		float[] xpoints, ypoints;
		switch (life) {
		case 7:
			xpoints = new float[] { 0, 0, 1 / 2f };
			ypoints = new float[] { 0, 1 / 2f, 0 };
			break;
		case 6:
			xpoints = new float[] { 0, 0, 1 };
			ypoints = new float[] { 0, 1 / 2f, 0 };
			break;
		case 5:
			xpoints = new float[] { 0, 1, 1 };
			ypoints = new float[] { 0, 0, 1 };
			break;
		case 4:
		case 3:
			xpoints = new float[] { 1 / 4f, 1, 1, 1 / 2f };
			ypoints = new float[] { 0, 0, 1, 1 };
			break;
		case 2:
			xpoints = new float[] { 0, 1, 1 };
			ypoints = new float[] { 1, 0, 1 };
			break;
		case 1:
		default:
			xpoints = new float[] { 0, 0, 1 / 2f };
			ypoints = new float[] { 1 / 2f, 1, 1 };
			break;
		}
		poly.npoints = xpoints.length;
		switch (dir) {
		case UP:
			poly.xpoints = Util.scaleToFit(aabb.x, aabb.width, ypoints);
			poly.ypoints = Util.scaleToFit(aabb.y + aabb.height, -aabb.height, xpoints);
			break;
		case DOWN:
			poly.xpoints = Util.scaleToFit(aabb.x + aabb.width, -aabb.width, ypoints);
			poly.ypoints = Util.scaleToFit(aabb.y, aabb.height, xpoints);
			break;
		case LEFT:
			poly.xpoints = Util.scaleToFit(aabb.x + aabb.width, -aabb.width, xpoints);
			poly.ypoints = Util.scaleToFit(aabb.y, aabb.height, ypoints);
			break;
		case RIGHT:
			poly.xpoints = Util.scaleToFit(aabb.x, aabb.width, xpoints);
			poly.ypoints = Util.scaleToFit(aabb.y, aabb.height, ypoints);
			break;
		}
	}

	public void activate(int life) {
		this.life = life;
	}

	public boolean intersects(Rectangle r) {
		if (life <= 0) {
			return false;
		}
		return aabb.intersects(r);
	}

	public void setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public boolean isActive() {
		return life > 0;
	}

	public Polygon getPoly() {
		return poly;
	}

	public Rectangle getAABB() {
		return aabb;
	}
}
