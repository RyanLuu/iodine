import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player extends GameObject {

	public static final int MAX_METER = 100;
	public static final int ATTACK_THRESHOLD = 25;
	public static final float DEFAULT_METER_RATE = 0.5f;

	// general
	private Map parent;
	private int width, height;

	// physics
	private float vx, vy;
	private float gravity;
	private float fastfall;
	private float vyMax;
	private float vjump, vmove;

	// movement
	private int nJumpsLeft;
	private boolean canJump;
	private boolean fastfalling;
	private boolean facingLeft;
	private boolean shielding;
	private Direction shieldDir;
	private boolean attacking;

	// mechanics
	private float invincibility;
	private float meterRate;
	private float meter;
	private float endingLag;
	private int score;
	private Hurtbox hurtbox;
	private boolean charging;
	private float attackCharge;

	private PlayerPrefs prefs;
	private GradientPaint fill;

	public Player(KeyConfig keys, Color color) {
		hurtbox = new Hurtbox(this);
		prefs = new PlayerPrefs();
		this.prefs.keys = keys;
		this.prefs.color = color;
		this.fill = new GradientPaint(x, y, Color.BLACK, x, y + height, color);
		this.width = (int) (Main.SCALE * 0.5);
		this.height = this.width;
	}

	@Override
	public void render(Graphics g) {
		Color drawColor = invincibility > 0 && (int) invincibility % 4 < 2 ? Color.WHITE : prefs.color;
		fill = new GradientPaint(x, y - height / 2, new Color(0, 0, 0, 0), x, y + height / 2, drawColor);
		int rx = (int) x;
		int ry = (int) y;
		if (hurtbox.isActive()) {
			hurtbox.updateLocation();
			g.setColor(Util.transparentColor(prefs.color, 191));
			g.fillPolygon(hurtbox.getPoly());
			g.setColor(Util.transparentColor(prefs.color, (int) (127 / Hurtbox.DURATION * hurtbox.life)));
			Rectangle r = hurtbox.getAABB();
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		if (shielding) {
			g.setColor(drawColor);
			if (shieldDir == Direction.LEFT) {
				g.drawLine(rx - width * 3 / 4, ry - height / 2, rx - width * 3 / 4, ry + height / 2);
			} else if (shieldDir == Direction.RIGHT) {
				g.drawLine(rx + width * 3 / 4, ry - height / 2, rx + width * 3 / 4, ry + height / 2);
			} else if (shieldDir == Direction.UP) {
				g.drawLine(rx - width / 2, ry - height * 3 / 4, rx + width / 2, ry - height * 3 / 4);
			}
		}

		((Graphics2D) g).setPaint(fill);
		// g.fillPolygon(p);
		g.fillRect(rx - width / 2, ry - height / 2, width, height);
		g.setColor(drawColor);
		// g.drawPolygon(p);
		g.drawRect(rx - width / 2, ry - height / 2, width, height);
		g.fillRect(facingLeft ? rx - width / 2 : rx + width / 4, ry - height / 4, width / 4, height / 4);
	}

	@Override
	public void update() {
		// out of bounds
		if (x >= Main.WIDTH || x < 0 || y >= Main.HEIGHT) {
			score--;
			kill();
		}
		// hit by opponent
		if (invincibility <= 0) {
			Rectangle aabb = new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
			for (Player p : parent.getPlayers()) {
				if (p == this) {
					continue;
				}
				if (p.hurtbox.power > 0 && p.hurtbox.intersects(aabb)) {
					if (shielding && shieldDir.opposes(p.hurtbox.dir)) {
						p.meter = 0;
						p.hurtbox.power = 0;
						Sound.play(Resources.SHIELD);
					} else {
						kill();
						p.incrementScore();
					}
				}
			}
		} else {
			invincibility--;
		}

		attacking = endingLag > 0;
		boolean grounded = collide(x, y + 1);
		int left = Keyboard.isDown(prefs.keys.left) ? -1 : 0;
		int right = Keyboard.isDown(prefs.keys.right) ? 1 : 0;
		int move = shielding ? 0 : left + right;

		if (move > 0) {
			facingLeft = false;
		} else if (move < 0) {
			facingLeft = true;
		}

		vx = move * vmove;
		vy += gravity;
		if (!grounded && !fastfalling && Keyboard.isPressed(prefs.keys.down) && vy > 0) {
			vy += fastfall;
			fastfalling = true;
		}
		if (vy <= 0 || grounded) {
			fastfalling = false;
		}

		if (vy > vyMax) {
			vy = vyMax;
		}
		if (grounded) {
			nJumpsLeft = 1;
		}

		if (!Keyboard.isDown(prefs.keys.up) && nJumpsLeft > 0) {
			canJump = true;
		}

		// jump
		if (!shielding && canJump && Keyboard.isDown(prefs.keys.up)) {
			vy = -vjump;
			if (!grounded) {
				nJumpsLeft--;
			}
			Sound.play(Resources.JUMP);
			canJump = false;
		}

		// variable jump height

		if (vy < 0 && !Keyboard.isDown(prefs.keys.up)) {
			vy = Math.max(vy, -vjump * .5f);
		}

		// shield
		if (grounded && !charging && !attacking && Keyboard.isDown(prefs.keys.shield) && meter > 0) {
			if (!shielding) {
				shieldDir = facingLeft ? Direction.LEFT : Direction.RIGHT;
			}
			shielding = true;
		} else {
			shielding = false;
		}
		if (shielding) {
			if (Keyboard.isPressed(prefs.keys.right)) {
				shieldDir = Direction.RIGHT;
			}
			if (Keyboard.isPressed(prefs.keys.left)) {
				shieldDir = Direction.LEFT;
			}
			if (Keyboard.isPressed(prefs.keys.up)) {
				shieldDir = Direction.UP;
			}
		}

		if (charging) {
			meter -= 5;
			attackCharge += .3;
			if (meter <= 0) {
				meter = 0;
				attack((int) attackCharge * width + width);
				attackCharge = 0;
				charging = false;
			}
		}

		if (!shielding && !attacking) {
			// heavy attack
			if (meter >= ATTACK_THRESHOLD && Keyboard.isPressed(prefs.keys.hatt)) {
				charging = true;
			}
			if (charging && Keyboard.isReleased(prefs.keys.hatt)) {
				attack((int) attackCharge * width + width);
				attackCharge = 0;
				charging = false;
			}
			// light attack
			if (meter >= 25 && !charging && Keyboard.isPressed(prefs.keys.latt)) {
				attack(width);
			}
		}

		hurtbox.update();

		if (endingLag > 0) {
			endingLag--;
		}

		if (meter < MAX_METER && !shielding) {
			meter += meterRate;
		}
		if (meter > MAX_METER) {
			meter = MAX_METER;
		}

		// horizontal collision

		if (collide(x + vx, y)) {
			while (!collide(x + Math.signum(vx), y)) {
				x += Math.signum(vx);
			}
			vx = 0;
		}

		x += vx;

		// vertical collision

		if (collide(x, y + vy)) {
			while (!collide(x, y + Math.signum(vy))) {
				y += Math.signum(vy);
			}
			vy = 0;
		}

		y += vy;
	}

	private void attack(int range) {
		int attackDir = (Keyboard.isDown(prefs.keys.up) ? -1 : 0) + (Keyboard.isDown(prefs.keys.down) ? 1 : 0);
		if (attackDir == -1) {
			hurtbox.setBounds(-width, -height - range, width * 2, height * 2 + range);
			hurtbox.dir = Direction.UP;
		} else if (attackDir == 1) {
			hurtbox.setBounds(-width, -height, width * 2, height * 2 + range);
			hurtbox.dir = Direction.DOWN;
		} else {
			if (facingLeft) {
				hurtbox.setBounds(-width - range, -height, width * 2 + range, height * 2);
				hurtbox.dir = Direction.LEFT;
			} else {
				hurtbox.setBounds(-width, -height, width * 2 + range, height * 2);
				hurtbox.dir = Direction.RIGHT;
			}
		}
		hurtbox.power = range;
		hurtbox.activate(Hurtbox.DURATION);
		endingLag = 20;
		Sound.play(Resources.ATTACK);
	}

	private boolean collide(float x, float y) {
		int ix = (int) x;
		int iy = (int) y;
		return parent.isSolid(ix - width / 2, iy - height / 2) || parent.isSolid(ix - width / 2, iy + height / 2)
				|| parent.isSolid(ix + width / 2, iy - height / 2) || parent.isSolid(ix + width / 2, iy + height / 2);
	}

	public void respawn() {
		x = parent.getSpawnX();
		y = parent.getSpawnY();
		vx = 0;
		vy = 0;
		gravity = 0.04f * Main.SCALE;
		fastfall = 0.5f * Main.SCALE;
		vyMax = 3.65f * Main.SCALE;
		vjump = 0.5f * Main.SCALE;
		vmove = 0.25f * Main.SCALE;
		fastfalling = false;
		nJumpsLeft = 1;
		meter = 0;
		invincibility = 40;
		meterRate = DEFAULT_METER_RATE;
		attackCharge = 0;
		hurtbox.life = 0;
		charging = false;
	}

	public void kill() {
		Sound.play(Resources.KILL);
		// increment scores and stuff
		respawn();
	}

	public void incrementScore() {
		score++;
	}

	public int getScore() {
		return score;
	}

	public float getMeter() {
		return meter;
	}

	public PlayerPrefs getPrefs() {
		return prefs;
	}

	public void setParent(Map parent) {
		this.parent = parent;
	}
}