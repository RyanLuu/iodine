import java.awt.event.KeyEvent;

public enum KeyConfig {

	
	WASD(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_1, KeyEvent.VK_Q),
	IJKL(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U),
	ARROWS(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH);

	public int up, down, left, right, latt, hatt, shield;

	KeyConfig(int up, int down, int left, int right, int latt, int hatt, int shield) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.latt = latt;
		this.hatt = hatt;
		this.shield = shield;
	}
}
