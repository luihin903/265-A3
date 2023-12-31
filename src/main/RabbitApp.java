/*
	I did the first bonus question:
		When the hunter kills a lion,
		the blood splashes out.
 */


package main;
import javax.swing.JFrame;

import others.Setting;

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
		System.out.println(app.getTitle());
	}
}
