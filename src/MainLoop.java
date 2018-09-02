import java.util.ArrayList;
import java.util.List;

public class MainLoop implements Runnable {

	private Window window;
	private List<Updatable> updatables;
	private Thread thread;

	public MainLoop(Window window) {
		this.window = window;
		this.updatables = new ArrayList<Updatable>();
		thread = new Thread(this);
	}

	public void start() {
		running = true;
		thread.start();
	}
	
	private int frameCount = 0;
    public static boolean running = false, paused = false;

	public void run() {
		// This value would probably be stored elsewhere.
		final double GAME_HERTZ = 30.0;
		// Calculate how many ns each frame should take for our target game
		// hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		// At the very most we will update the game this many times before a new
		// render.
		// If you're worried about visual hitches more than perfect timing, set
		// this to 1.
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		// We will need the last update time.
		double lastUpdateTime = System.nanoTime();
		// Store the last time we rendered.
		double lastRenderTime = System.nanoTime();

		// If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		// Simple way of finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;

			if (!paused) {
				// Do as many game updates as we need to, potentially playing
				// catchup.
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
					Keyboard.update();
					Mouse.update();
					update();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				// If for some reason an update takes forever, we don't want to
				// do an insane number of catchups.
				// If you were doing some sort of game that needed to keep EXACT
				// time, you would get rid of this.
				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				render();
				lastRenderTime = now;

				// Update the frames we got.
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					window.setTitle("iodine | " + frameCount);
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				// Yield until it has been at least the target time between
				// renders. This saves the CPU from hogging.
				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
						&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					// allow the threading system to play threads that are
					// waiting to run.
					Thread.yield();

					// This stops the app from consuming all your CPU. It makes
					// this slightly less accurate, but is worth it.
					// You can remove this line and it will still work (better),
					// your CPU just climbs on certain OSes.
					// FYI on some OS's this can cause pretty bad stuttering.
					// Scroll down and have a look at different peoples'
					// solutions to this.
					// On my OS it does not unpuase the game if i take this away
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}

					now = System.nanoTime();
				}
			}
		}
	}
/*
	public void run() {

		long prev = System.nanoTime();
		final int TARGET_FPS = 30;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long fpsUpdate = 0;
		int fps = 0;
		while (running) {
			long curr = System.nanoTime();
			long updateLength = curr - prev;
			prev = curr;
			float delta = updateLength / ((float) OPTIMAL_TIME);

			// fps counter
			fpsUpdate += updateLength;
			fps++;

			if (fpsUpdate >= 1000000000) {
				window.setTitle("iodine | " + fps);
				fpsUpdate = 0;
				fps = 0;
			}

			Keyboard.update();
			Mouse.update();

			update(delta);

			render();

			try {
				Thread.sleep((prev - System.nanoTime() + OPTIMAL_TIME) / 1000000);
			} catch (Exception e) {
			}
		}
	}
*/
	public void setUpdatables(List<Updatable> updatables) {
		this.updatables = updatables;
	}

	public void setRenderables(List<Renderable> renderables) {
		window.getCanvas().setRenderables(renderables);
	}

	public void update() {
		for (Updatable u : updatables) {
			u.update();
		}
	}

	public void render() {
		window.render();
		frameCount++;
	}
}