package others;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import processing.core.PVector;

public abstract class Setting {
    
    public static final int margin = 50;
	public static final int FPS = 30;
	public static final boolean drawBoundingBox = false;
	public static final PVector default_size = new PVector(1200, 800);
	public static final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	public static Font font;
	public static final int rabbits = 5;
	public static final int lions = 2;
	

	static {

		if (Arrays.asList(fonts).contains("Comic Sans MS")) {
			font = new Font("Comic Sans MS", Font.PLAIN, 16);
		}
		else if (Arrays.asList(fonts).contains("Arial")) {
			font = new Font("Arial", Font.PLAIN, 16);
		}
		else {
			font = new Font(fonts[0], Font.PLAIN, 16);
		}
		
	}
}
