import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

public class Main {

	public static int WIDTH = 1024;
	public static int HEIGHT = (int) (WIDTH * 9.0 / 16.0);

	public static int SCALE = (int) ((double) WIDTH / Map.WIDTH_IN_TILES);

	private static Window window;
	private static MainLoop loop;

	public Main() {
		window = new Window(WIDTH, HEIGHT, "iodine");
		window.addKeyListener(Keyboard.getInstance());
		window.getCanvas().addMouseListener(Mouse.getInstance());
		window.getCanvas().addMouseMotionListener(Mouse.getInstance());
		loop = new MainLoop(window);
		Resources.load();
		GameState.load(GameState.MAIN_MENU);
		GameState.load(GameState.MAP_MENU);
		GameState.load(GameState.OPTIONS_MENU);
		GameState.load(GameState.CREDITS_MENU);
		
		setGameState(GameState.MAIN_MENU);
		loop.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main());
	}

	public static void setGameState(GameState gs) {
		if (gs.isLoaded()) {
			loop.setUpdatables(gs.toBeUpdated());
			loop.setRenderables(gs.toBeRendered());
			gs.init();
		} else {
			System.err.println("Tried to set unloaded game state! " + gs);
		}
	}

	public static void quit() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}

	public static void setSize(int width) {
		if (WIDTH != width) {
			WIDTH = width;
			HEIGHT = (int) (WIDTH * 9.0 / 16.0);
			window.setSize(WIDTH, HEIGHT);
			window.setLocationRelativeTo(null);
			GameState.load(GameState.MAIN_MENU);
			GameState.load(GameState.MAP_MENU);
			GameState.load(GameState.OPTIONS_MENU);
			GameState.load(GameState.CREDITS_MENU);
		}
	}
}