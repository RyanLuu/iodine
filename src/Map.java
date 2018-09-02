import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class Map extends GameObject {

	public static final int WIDTH_IN_TILES = 32;
	public static final int HEIGHT_IN_TILES = 18;

	public static final int TILE_WIDTH = Main.SCALE;
	public static final int TILE_HEIGHT = TILE_WIDTH;

	private Tile[][] tiles;
	private Player[] players;
	private int sx, sy;
	private Rune rune;
	private String name, subname;
	private Color cf, cb;
	private Color cf0, cf2, cb0;

	public Map(String mapData, Player... players) {
		this.players = players;
		for (Player p : this.players) {
			p.setParent(this);
		}
		rune = new Rune(Main.WIDTH / 2, Main.HEIGHT / 8);
		tiles = new Tile[HEIGHT_IN_TILES][WIDTH_IN_TILES];
		try {
			InputStream in = new FileInputStream(mapData);
			byte[] data = new byte[HEIGHT_IN_TILES * WIDTH_IN_TILES / 8 * 2];
			in.read(data);
			int x = 0;
			int y = 0;
			for (int i = 0; i < data.length; i++) {
				byte b = data[i];
				for (int j = 3; j >= 0; j--) {
					int tile = b;
					while (tile < 0) {
						tile += 4; // i don't know why...
					}
					tile %= 4;
					if (tile == 0b00) {
						tiles[y][x + j] = Tile.AIR;
					} else if (tile == 0b11) {
						tiles[y][x + j] = Tile.SOLID;
					} else if (tile == 0b01) {
						tiles[y][x + j] = Tile.DECO;
					} else if (tile == 0b10) {
						tiles[y][x + j] = Tile.SPAWN;
						sx = (int) ((x + j + 0.5f) * TILE_WIDTH);
						sy = (int) ((y + 0.5f) * TILE_HEIGHT);
						rune.x = sx;
						rune.y = sy;
					} else {
						System.out.println(tile);
					}
					b >>= 2;
				}
				x += 4;
				if (x >= WIDTH_IN_TILES) {
					x = 0;
					y++;
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader("metadata.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals(mapData)) {
					name = br.readLine();
					subname = br.readLine();
					try {
						cf = Color.decode(br.readLine());
					} catch (NumberFormatException e) {
						cf = Color.GRAY;
					}
					try {
						cb = Color.decode(br.readLine());
					} catch (NumberFormatException e) {
						cb = Color.DARK_GRAY;
					}
					cf0 = new Color(cf.getRed(), cf.getGreen(), cf.getBlue(), 63);
					cf2 = cf;
					cb0 = new Color(cb.getRed(), cb.getGreen(), cb.getBlue(), 63);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Player p : players) {
			p.respawn();
		}
	}

	public void render(Graphics g) {
		g.drawImage(Resources.BACKGROUND_IMAGE, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		for (int y = 0; y < HEIGHT_IN_TILES; y++) {
			for (int x = 0; x < WIDTH_IN_TILES; x++) {
				if (tiles[y][x] == Tile.DECO) {
					g.setColor(cb0);
					g.fillRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
//					g.setColor(cb1);
//					g.fillRect(x * TILE_WIDTH, (int) ((y + 0.5f) * TILE_HEIGHT), TILE_WIDTH,
//							(int) (TILE_HEIGHT * 0.5f));
//					g.setColor(cb2);
//					g.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
				}
			}
		}
		for (int y = 0; y < HEIGHT_IN_TILES; y++) {
			for (int x = 0; x < WIDTH_IN_TILES; x++) {
				if (tiles[y][x] == Tile.SOLID) {
					g.setColor(cf0);
					g.fillRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
//					g.setColor(cf1);
//					g.fillRect(x * TILE_WIDTH, (int) ((y + 0.5f) * TILE_HEIGHT), TILE_WIDTH,
//							(int) (TILE_HEIGHT * 0.5f));
					g.setColor(cf2);
					g.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
				}
			}
		}
		rune.render(g);
		for (Player p : players) {
			p.render(g);
		}
	}

	public void update() {
		rune.update();
		for (Player p : players) {
			p.update();
		}
	}

	public Player[] getPlayers() {
		return players;
	}

	public boolean isSolid(int x, int y) {
		x /= TILE_WIDTH;
		y /= TILE_HEIGHT;
		if (x < 0 || x >= WIDTH_IN_TILES || y < 0 || y >= HEIGHT_IN_TILES) {
			return false;
		}
		return tiles[y][x] == Tile.SOLID;
	}

	public int getSpawnX() {
		return sx;
	}

	public int getSpawnY() {
		return sy;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSubname() {
		return subname;
	}
}