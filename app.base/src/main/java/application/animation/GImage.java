package application.animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GImage {
	public int[] pixels = null;
	public float width;
	public float height;
	protected BufferedImage image = null;

	private GImage() {
	}

	public GImage(String imageName) {
		try {
			image = ImageIO.read(getClass().getResource(imageName));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		width = (float) image.getWidth();
		height = (float) image.getHeight();
	}

	public GImage(String imageName, float width, float height, boolean preserveRatio) {
		try {
			BufferedImage im = ImageIO.read(getClass().getResource(imageName));
			image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(im, 0, 0, (int) width, (int) height, null);
			g.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		this.width = (float) image.getWidth();
		this.height = (float) image.getHeight();
	}

	public GImage(float width, float height) {
		this.width = width;
		this.height = height;
		pixels = new int[GApplication.toInt(width * height)];
	}

	public GImage copy() {
		GImage copy = new GImage();
		copy.image = this.image;
		copy.width = this.width;
		copy.height = this.height;
		copy.loadPixels();
		return copy;
	}

	public void loadPixels() {
		pixels = new int[GApplication.toInt(width * height)];
		if (image == null) {
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = 0;
			}
		} else {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					pixels[x + y * GApplication.toInt(width)] = image.getRGB(x, y);
				}
			}
		}
	}

	public void filterGray() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int w = GApplication.toInt(width);
				float r = GApplication.red(pixels[x + y * w]);
				float g = GApplication.green(pixels[x + y * w]);
				float b = GApplication.blue(pixels[x + y * w]);
				float gs = (r + g + b) / 3;
				pixels[x + y * w] = GApplication.color(GApplication.toInt(gs));
			}
		}
	}

}
