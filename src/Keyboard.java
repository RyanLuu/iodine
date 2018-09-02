import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	public static final int nKeys = 256;
	
	private static boolean[] keys;
	private static boolean[] keysDown;
	private static boolean[] lastKeysDown;

	private static Keyboard instance;

	public static Keyboard getInstance() {
		if (instance == null) {
			instance = new Keyboard();
		}
		return instance;
	}

	private Keyboard() {
		keys = new boolean[nKeys];
		keysDown = new boolean[nKeys];
		lastKeysDown = new boolean[nKeys];
	}
	
	public static void update() {
		for (int i = 0; i < nKeys; i++) {
			lastKeysDown[i] = keysDown[i];
		}
		for (int i = 0; i < nKeys; i++) {
			keysDown[i] = keys[i];
		}
	}
	
	public static boolean isDown(int keyCode) {
		return keysDown[keyCode];
	}
	
	public static boolean isPressed(int keyCode) {
		return keysDown[keyCode] && !lastKeysDown[keyCode];
	}
	
	public static boolean isReleased(int keyCode) {
		return !keysDown[keyCode] && lastKeysDown[keyCode];
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < keysDown.length) {
			keys[e.getKeyCode()] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < keysDown.length) {
			keys[e.getKeyCode()] = false;
		}
	}
}