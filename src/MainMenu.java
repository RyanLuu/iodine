import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

public class MainMenu extends Menu {

	private MenuTitle title;
	private MenuButton playButton, optionsButton, creditsButton, quitButton;
	private float transition;

	public MainMenu() {
		title = new MenuTitle(Main.WIDTH / 4, 0, Main.WIDTH / 2, Main.HEIGHT / 6, "iodine");
		playButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT / 5, Main.WIDTH / 2, Main.HEIGHT / 6, "play", () -> {
			transition = 0;
			GameState.load(GameState.PLAY);
		});
		playButton.setSound(Resources.PLAY_BUTTON_PRESS);
		optionsButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 2 / 5, Main.WIDTH / 2, Main.HEIGHT / 6, "options",
				() -> Main.setGameState(GameState.OPTIONS_MENU));
		creditsButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 3 / 5, Main.WIDTH / 2, Main.HEIGHT / 6, "credits",
				() -> Main.setGameState(GameState.CREDITS_MENU));
		// Sounds by Michael Vogler
		// Music by Kevin MacLeod
		// Programming by Ryan Luu
		quitButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 4 / 5, Main.WIDTH / 2, Main.HEIGHT / 6, "quit",
				() -> Main.quit());
		objects = Arrays.asList(title, playButton, optionsButton, creditsButton, quitButton);

		transition = -1;
	}

	@Override
	public void update() {
		if (transition == -1) {
			super.update();
		} else if (transition >= 0 && transition < 2) {
			transition += 0.03;
		}
		if (transition >= 2) {
			Main.setGameState(GameState.PLAY);
		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		if (transition >= 0) {
			g.setColor(new Color(0, 0, 0, Math.min((int) (transition * 255), 255)));
			g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		}
	}
}
