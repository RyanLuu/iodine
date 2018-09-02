import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

	public static final int nButtons = 4;

	private static int x, y;
	private static boolean[] buttons;
	private static boolean[] buttonsDown;
	private static boolean[] lastButtonsDown;

	private static Mouse instance;

	public static Mouse getInstance() {
		if (instance == null) {
			instance = new Mouse();
		}
		return instance;
	}

	private Mouse() {
		buttons = new boolean[nButtons];
		buttonsDown = new boolean[nButtons];
		lastButtonsDown = new boolean[nButtons];
	}

	public static void update() {
		for (int i = 0; i < nButtons; i++) {
			lastButtonsDown[i] = buttonsDown[i];
		}
		for (int i = 0; i < nButtons; i++) {
			buttonsDown[i] = buttons[i];
		}
	}
	
	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static boolean isDown(int b) {
		return buttonsDown[b];
	}
	
	public static boolean isPressed(int b) {
		return buttonsDown[b] && !lastButtonsDown[b];
	}
	
	public static boolean isReleased(int b) {
		return !buttonsDown[b] && lastButtonsDown[b];
	}

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {

	}
}