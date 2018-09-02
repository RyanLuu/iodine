
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu implements Renderable, Updatable {

	protected List<MenuObject> objects;

	public Menu(MenuObject... objects) {
		this.objects = new ArrayList<>(Arrays.asList(objects));
	}

	@Override
	public void update() {
		for (MenuObject o : objects) {
			if (o.active) {
				o.update();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Resources.BACKGROUND_IMAGE, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		for (MenuObject o : objects) {
			if (o.visible) {
				o.render(g);
			}
		}
	}

	public void add(MenuObject o) {
		objects.add(o);
	}
	
	public void add(MenuObject... os) {
		for (MenuObject o : os) {
			objects.add(o);
		}
	}
}
