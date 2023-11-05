import javax.swing.JFrame;

public class RabbitApp extends JFrame {
	
	public RabbitApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize((int) Setting.default_size.x, (int) Setting.default_size.y);

		RabbitPanel panel = new RabbitPanel(this.getSize());
		this.add(panel);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		RabbitApp app = new RabbitApp("My Rabbit App");
	}
}
