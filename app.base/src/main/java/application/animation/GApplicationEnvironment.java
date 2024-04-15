package application.animation;

import java.awt.Toolkit;

public abstract class GApplicationEnvironment {

	public float screenResolution() {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	public float screenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}

	public float screenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}

}
