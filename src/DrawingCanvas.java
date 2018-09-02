import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawingCanvas extends JPanel {
    
    private List<Renderable> list;
    
    public DrawingCanvas() {
        list = new ArrayList<Renderable>();
    }
    
    public void setRenderables(List<Renderable> renderables) {
    	list = renderables;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Renderable d : list) {
            d.render(g);
        }
    }
    
}