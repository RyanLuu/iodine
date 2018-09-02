import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum GameState {

	MAIN_MENU, OPTIONS_MENU, CREDITS_MENU, MAP_MENU, PLAY;

	private static Player p1 = new Player(KeyConfig.WASD, Color.CYAN);
	private static Player p2 = new Player(KeyConfig.ARROWS, Color.ORANGE);
	private static Player p3 = new Player(KeyConfig.IJKL, Color.GREEN);

	private Runnable init;
	private HashMap<String, Object> variables;
	private List<Updatable> us;
	private List<Renderable> rs;
	private boolean loaded;

	public static void load(GameState state) {
		state.us.clear();
		state.rs.clear();
		if (state == MAIN_MENU) {
			Menu mm = new Menu();
			MenuTitle title = new MenuTitle(Main.WIDTH / 4, 0, Main.WIDTH / 2, Main.HEIGHT / 6, "iodine");
			MenuButton playButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT / 5, Main.WIDTH / 2, Main.HEIGHT / 6,
					"play", () -> {
						Main.setGameState(MAP_MENU);
					});
			MenuButton optionsButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 2 / 5, Main.WIDTH / 2,
					Main.HEIGHT / 6, "options", () -> Main.setGameState(OPTIONS_MENU));
			MenuButton creditsButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 3 / 5, Main.WIDTH / 2,
					Main.HEIGHT / 6, "credits", () -> Main.setGameState(CREDITS_MENU));
			MenuButton quitButton = new MenuButton(Main.WIDTH / 4, Main.HEIGHT * 4 / 5, Main.WIDTH / 2, Main.HEIGHT / 6,
					"quit", () -> Main.quit());
			mm.add(title, playButton, optionsButton, creditsButton, quitButton);
			MAIN_MENU.init = () -> BGM.setTrack(Resources.BGM_1);
			MAIN_MENU.add(mm);
		} else if (state == OPTIONS_MENU) {
			Menu om = new Menu();
			om.add(new MenuLabel(0, 0, Main.WIDTH, Main.HEIGHT / 9, "Options"));
			MenuButton back = new MenuButton(Main.WIDTH / 32, Main.HEIGHT - Main.HEIGHT / 6, Main.WIDTH / 8,
					Main.HEIGHT / 9, "Back", () -> Main.setGameState(MAIN_MENU));
			om.add(back);
			om.add(new MenuLabel(Main.WIDTH / 16, Main.HEIGHT * 4 / 18, Main.WIDTH * 3/8, Main.HEIGHT / 6, "Key Configuration"));
			MenuKeyConfig mkc = new MenuKeyConfig(Main.WIDTH / 16, Main.HEIGHT * 7 / 18, Main.WIDTH * 3 / 8,
					Main.HEIGHT * 7 / 18, p1);
			MenuSelect playerSelect = new MenuSelect(Main.WIDTH / 16, Main.HEIGHT / 3, Main.WIDTH * 3 / 8,
					Main.HEIGHT / 18, new String[] { "1", "2", "3" },
					new Runnable[] { () -> mkc.setPlayer(p1), () -> mkc.setPlayer(p2), () -> mkc.setPlayer(p3) });
			playerSelect.setHorizontal(true);
			playerSelect.disableEntry(2);
			om.add(playerSelect);
			om.add(mkc);
			om.add(new MenuSelect(Main.WIDTH / 8, Main.HEIGHT / 9, Main.WIDTH / 4, Main.HEIGHT * 5 / 36,
					new String[] { "2 Players", "3 Players" }, new Runnable[] { () -> {
						PLAY.variables.put("nPlayers", 2);
						playerSelect.disableEntry(2);
						if (playerSelect.selected == 2) {
							playerSelect.selected = 1;
						}
					}, () -> {
						PLAY.variables.put("nPlayers", 3);
						playerSelect.enableEntry(2);
					} }));
			om.add(new MenuVolumeSlider(Main.WIDTH / 2, Main.HEIGHT / 6, Main.WIDTH * 3 / 8, Main.HEIGHT / 9, "Volume"));
			int[] sizes = { 960, 1024, 1280, 1600 };
			MenuSelect resSelect = new MenuSelect(Main.WIDTH / 2, Main.HEIGHT * 7 / 18, Main.WIDTH * 3 / 8,
					Main.HEIGHT / 3, new String[] { "960x540", "1024x576", "1280x720", "1600x900" },
					new Runnable[] { () -> Main.setSize(sizes[0]), () -> Main.setSize(sizes[1]),
							() -> Main.setSize(sizes[2]), () -> Main.setSize(sizes[3]) });
			for (int i = 0; i < sizes.length; i++) {
				if (Main.WIDTH == sizes[i]) {
					resSelect.selected = i;
					break;
				}
			}
			om.add(resSelect);
			OPTIONS_MENU.add(om);
		} else if (state == CREDITS_MENU) {
			Menu cm = new Menu();
			cm.add(new MenuLabel(0, Main.HEIGHT / 9, Main.WIDTH, Main.HEIGHT / 9, "Credits"));
			cm.add(new MenuLabel(0, Main.HEIGHT * 2 / 9, Main.WIDTH, Main.HEIGHT / 9, "Programming: Ryan Luu"));
			cm.add(new MenuLabel(0, Main.HEIGHT * 3 / 9, Main.WIDTH, Main.HEIGHT / 9, "Music: Kevin MacLeod"));
			cm.add(new MenuLabel(0, Main.HEIGHT * 4 / 9, Main.WIDTH, Main.HEIGHT / 9,
					"UI Sound Effects: Michael Vogler"));
			cm.add(new MenuLabel(0, Main.HEIGHT * 5 / 9, Main.WIDTH, Main.HEIGHT / 9,
					"In-Game Sound Effects: Ryan Luu"));
			cm.add(new MenuLabel(0, Main.HEIGHT * 6 / 9, Main.WIDTH, Main.HEIGHT / 9,
					"Playtesting: Brandon Boje, John Dunaske, Ileana Diaz"));
			cm.add(new MenuButton(Main.WIDTH / 32, Main.HEIGHT - Main.HEIGHT / 6, Main.WIDTH / 8, Main.HEIGHT / 9,
					"Back", () -> Main.setGameState(MAIN_MENU)));
			CREDITS_MENU.add(cm);
		} else if (state == MAP_MENU) {
			Menu mm = new Menu();
			mm.add(new MenuLabel(0, 0, Main.WIDTH, Main.HEIGHT / 9, "Stage Select"));
			mm.add(new MenuButton(Main.WIDTH / 32, Main.HEIGHT - Main.HEIGHT / 6, Main.WIDTH / 8, Main.HEIGHT / 9,
					"Back", () -> Main.setGameState(MAIN_MENU)));
			Transition t = new Transition(50, () -> Main.setGameState(PLAY));
			List<String> files = new ArrayList<String>();
			List<String> names = new ArrayList<String>();
			try (BufferedReader br = new BufferedReader(new FileReader("assets/metadata/stages.txt"))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (line.trim().endsWith(".map")) {
						files.add(line);
						names.add(br.readLine().trim() + " (" + br.readLine().trim() + ")");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			names.add("??? Random ???");
			String[] namesArray = new String[names.size()];
			names.toArray(namesArray);
			MenuSelect mapSelect = new MenuSelect(Main.WIDTH / 6, Main.HEIGHT / 9, Main.WIDTH * 2 / 3,
					Main.HEIGHT * 2 / 3, namesArray);
			mapSelect.selected = namesArray.length - 1;
			MenuButton playButton = new MenuButton(Main.WIDTH * 3 / 16, Main.HEIGHT * 5 / 6, Main.WIDTH * 25 / 32,
					Main.HEIGHT / 9, "PLAY (Space)", () -> {
						t.play();
						PLAY.variables.put("map", mapSelect.selected < files.size() ? files.get(mapSelect.selected)
								: files.get((int) (Math.random() * files.size())));
						load(PLAY);
					});
			playButton.setSound(Resources.PLAY_BUTTON_PRESS);
			Updatable keyListener = () -> {
                if (Keyboard.isPressed(KeyEvent.VK_SPACE)) {
                    playButton.click();
                }
                if (Keyboard.isPressed(p1.getPrefs().keys.up)) {
                    mapSelect.selected--;
                    if (mapSelect.selected < 0) {
                        mapSelect.selected = mapSelect.getNumEntries() - 1;
                    }
                }
                if (Keyboard.isPressed(p1.getPrefs().keys.down)) {
                    mapSelect.selected++;
                    if (mapSelect.selected >= mapSelect.getNumEntries()) {
                        mapSelect.selected = 0;
                    }
                }
            };
			mm.add(playButton, mapSelect);
			MAP_MENU.add(mm, t, keyListener);
		} else if (state == PLAY) {
			if (PLAY.variables.get("nPlayers") == null) {
				PLAY.variables.put("nPlayers", 2);
			}
			Map map = null;
			GUI gui = null;
			int nPlayers = (int) PLAY.variables.get("nPlayers");
			if (nPlayers == 3) {
				map = new Map((String) PLAY.variables.get("map"), p1, p2, p3);
				gui = new GUI(p1, p2, p3);
			} else if (nPlayers == 2) {
				map = new Map((String) PLAY.variables.get("map"), p1, p2);
				gui = new GUI(p1, p2);
			}
			SplashScreen ss = new SplashScreen(map);
			PLAY.init = () -> {
				BGM.setTrack(Resources.BGM_2);
				ss.show();
			};
			PLAY.add(map, gui, ss);
		}
		state.loaded = true;
	}

	GameState() {
		us = new ArrayList<>();
		rs = new ArrayList<>();
		variables = new HashMap<>();
		loaded = false;
	}

	private void add(Object... os) {
		for (Object o : os) {
			add(o);
		}
	}

	private void add(Object o) {
		if (o instanceof Updatable) {
			us.add((Updatable) o);
		}
		if (o instanceof Renderable) {
			rs.add((Renderable) o);
		}
	}

	public void init() {
		if (init != null) {
			init.run();
		}
	}

	public List<Updatable> toBeUpdated() {
		return us;
	}

	public List<Renderable> toBeRendered() {
		return rs;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
