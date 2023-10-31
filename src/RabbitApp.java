import javax.swing.JFrame;

import processing.core.PVector;

public class RabbitApp extends JFrame {
	
	public static final int margin = 50;
	public static final int FPS = 30;
	public static final boolean drawBoundingBox = true;
	public static final PVector default_size = new PVector(1200, 800);

	public RabbitApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize((int) default_size.x, (int) default_size.y);

		RabbitPanel panel = new RabbitPanel(this.getSize());
		this.add(panel);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		RabbitApp app = new RabbitApp("My Rabbit App");
	}
}
