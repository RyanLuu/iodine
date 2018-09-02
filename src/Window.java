import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private DrawingCanvas canvas;

	public Window(int width, int height, String title) {
		super(title);
		canvas = new DrawingCanvas();
		canvas.setPreferredSize(new Dimension(width, height));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void render() {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                canvas.repaint();
            }
        });
	}

	public DrawingCanvas getCanvas() {
		return canvas;
	}
}