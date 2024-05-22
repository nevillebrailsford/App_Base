package application.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class GImage {
	public int[] pixels = null;
	public float width;
	public float height;
	protected BufferedImage image = null;

	private GImage() {
	}

	public GImage(File fileName) {
		try {
			image = ImageIO.read(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		width = (float) image.getWidth();
		height = (float) image.getHeight();
	}

	public GImage(File fileName, float width, float height, boolean preserveRatio) {
		try {
			Metadata metadata = null;
			try {
				metadata = ImageMetadataReader.readMetadata(fileName);
			} catch (ImageProcessingException e) {
				e.printStackTrace();
			}
			String orientation = "";
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					if (tag.getTagName().equals("Orientation")) {
						orientation = tag.getDescription();
					}
				}
			}
			BufferedImage im = ImageIO.read(fileName);
			image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			if (orientation.toLowerCase().contains("rotate")) {
				g.setPaint(Color.black);
				g.fillRect(0, 0, (int) width, (int) height);
				float x = width / 2;
				float y = height / 2;
				float scale = height / width;
				float newWidth = width * scale;
				float newHeight = height * scale;
				float xTranslate = (width - (newWidth)) / 2;
				float yTranslate = (height - (newHeight)) / 2;
				g.translate(xTranslate, yTranslate);
				g.scale(scale, scale);
				g.rotate(Math.toRadians(90), x, y);
				g.drawImage(im, 0, 0, (int) width, (int) height, null);
			} else {
				g.drawImage(im, 0, 0, (int) width, (int) height, null);
			}
			g.dispose();
		} catch (

		IOException e) {
			e.printStackTrace();
			return;
		}
		this.width = (float) image.getWidth();
		this.height = (float) image.getHeight();
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
