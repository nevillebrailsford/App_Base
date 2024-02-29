package application.animation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import application.timer.TimerService;

public abstract class GApplication {
	public static final int TOP = 23;
	public static final int BOTTOM = 24;
	public static final int LEFT = 25;
	public static final int CENTER = 26;
	public static final int RIGHT = 27;
	public static final int RGB = 28;
	public static final int HSB = 29;
	public static final int CORNER = 30;

	public Graphics2D graphicContext = null;

	public float width = 480;
	public float height = 360;
	public float mouseX = 120;
	public float pmouseX = 0;
	public float mouseY = 0;
	public float pmouseY = 0;
	public boolean mousePressed = false;
	public int mouseButton = 0;
	public int[] pixels = null;
	public long frameCount;
	public int frameRate = 0;

	private int frameInterval = 16; // interval in milliseconds per refresh. Default is approx. 60 frames per second
	private JFrame frame;
	private GPanel canvas;
	private long startTime;
	private Stack<Matrix> matrixStack;
	private Matrix matrix;

	private Paint backgroundColor = Color.lightGray;
	private float strokeWeight = 1;
	private Paint strokeColor = Color.BLACK;
	private Paint fillColor = Color.WHITE;
	private int textSize = 20;
	private Font textFont = new Font(Font.SERIF, Font.PLAIN, textSize);
	private int colorMode = RGB;
	private int rectMode = CORNER;
	private int ellipseMode = CORNER;
	private int textAlignX = LEFT;
	private int textAlignY = BOTTOM;

	protected boolean noStroke = false;
	protected boolean noFill = false;

	private boolean inSetup = false;
	private boolean inDraw = false;
	private int myTimer = -1;

	protected BufferedImage bufferedImage = null;
	protected RenderingHints renderingHints = new RenderingHints(null);
	protected AffineTransform at = new AffineTransform();

	Timer timer = null;
	long timerTimer = 0;

	public static int toInt(float value) {
		return (int) value;
	}

	public static int toInt(long value) {
		return (int) value;
	}

	public void start() {
		frameRate = 60;
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		canvas = new GPanel(this);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frameCount = 0;
		matrixStack = new Stack<>();
		matrix = new Matrix();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				GApplication.this.stop();
				System.exit(0);
			}
		});
		bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
		graphicContext = bufferedImage.createGraphics();
		graphicContext.setRenderingHints(renderingHints);
		at.setToIdentity();

		background(backgroundColor);
		inSetup = true;
		setup();
		inSetup = false;
		frame.setVisible(true);
		frame.pack();

		startDrawing();
		startTime = System.currentTimeMillis();
		internalDraw();
	}

	public GApplication() {
		frame = new JFrame();
		frame.setLocationRelativeTo(null);
		new Thread(() -> {
			SwingUtilities.invokeLater(() -> {
				start();
			});

		}).start();
	}

	public void stop() {
		timer = null;
		if (myTimer > 0) {
			TimerService.instance().stop(myTimer);
			myTimer = -1;
		}
	}

	private void startDrawing() {
		if (myTimer > 0) {
			TimerService.instance().stop(myTimer);
			myTimer = -1;
		}

		if (frameInterval > 0) {
			myTimer = TimerService.instance().timer(new TimerTask() {
				@Override
				public void run() {
					SwingUtilities.invokeLater(() -> {
						internalDraw();
					});
				}
			}, frameInterval, frameInterval);

		}
	}

	private void internalDraw() {
		pmouseX = mouseX;
		pmouseY = mouseY;
		Point p = canvas.getMousePosition();
		if (p != null) {
			mouseX = (float) p.getX();
			mouseY = (float) p.getY();
		}
		inDraw = true;
		draw();
		inDraw = false;
		frameCount++;
		canvas.repaint();
		resetDefaults();
	}

	private void internalBackground() {
		graphicContext.setColor((Color) backgroundColor);
		graphicContext.fillRect(0, 0, (int) width, (int) height);
	}

	private void primeShape(GShape shape) {
		shape.strokeColor = strokeColor;
		shape.fillColor = fillColor;
		shape.noStroke = noStroke;
		shape.noFill = noFill;
		AffineTransform transform = new AffineTransform();
		transform.translate(matrix.translateY, matrix.translateY);
		shape.transform = transform;
		shape.stroke = new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	}

	private void showShape(Shape shape) {
		GShape gShape = new GShape(shape);
		primeShape(gShape);
		drawGShape(gShape);
	}

	private void drawGShape(GShape shape) {
		AffineTransform saveXform = graphicContext.getTransform();
		AffineTransform toNewLoc = new AffineTransform();
		toNewLoc.concatenate(shape.transform);
		graphicContext.transform(toNewLoc);
		graphicContext.setStroke(shape.stroke);
		if (!shape.noFill) {
			graphicContext.setColor((Color) shape.fillColor);
			graphicContext.fill(shape.shape);
		}
		if (!shape.noStroke) {
			graphicContext.setColor((Color) shape.strokeColor);
			graphicContext.draw(shape.shape);
		}
		graphicContext.setTransform(saveXform);
	}

	private void resetDefaults() {
		backgroundColor = Color.lightGray;
		strokeWeight = 1;
		strokeColor = Color.BLACK;
		fillColor = Color.WHITE;
		textSize = 20;
		textFont = new Font(Font.SERIF, Font.PLAIN, textSize);
		colorMode = RGB;
		rectMode = CORNER;
		ellipseMode = CORNER;
		textAlignX = LEFT;
		textAlignY = BOTTOM;
	}

	public abstract void setup();

	public abstract void draw();

	public void mousePressed() {
	}

	public void mouseDragged() {
	}

	public void mouseClicked() {
	}

	public void mouseWheel(MouseWheelEvent e) {
	}

	public void noLoop() {
		if (inDraw) {
			return;
		}
		frames(0);
	}

	public void frameRate(int rate) {
		frames(1000 / rate);
		frameRate = rate;
	}

	public void background(float mono) {
		background(new Color((int) mono, (int) mono, (int) mono));
	}

	public void background(float mono, float opacity) {
		background(new Color(mono, mono, mono, opacity));
	}

	public void background(float red, float green, float blue) {
		background(new Color((int) red, (int) green, (int) blue));
	}

	public void background(float red, float green, float blue, float opacity) {
		background(new Color(red, green, blue, opacity));
	}

	public void background(Paint color) {
		backgroundColor = color;
		internalBackground();
	}

	public void strokeWeight(float weight) {
		strokeWeight = weight;
	}

	public void title(String title) {
		frame.setTitle(title);
	}

	public void size(float width, float height) {
		if (!inSetup) {
			return;
		}
		this.width = width;
		this.height = height;
		canvas.setSize(new Dimension((int) width, (int) height));
		canvas.validate();
		frame.pack();
		frame.setLocationRelativeTo(null);
		bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
		graphicContext = bufferedImage.createGraphics();
		graphicContext.setRenderingHints(renderingHints);
	}

	public void frames(int frames) {
		this.frameInterval = frames;
		startDrawing();
	}

	public void pushMatrix() {
		matrixStack.push(matrix);
		matrix = new Matrix();
	}

	public void popMatrix() {
		if (matrixStack.empty()) {
			return;
		}
		matrix = matrixStack.pop();
	}

	public void stroke(Paint color) {
		strokeColor = color;
		noStroke = false;
	}

	public void fill(float mono) {
		fill(mono, mono, mono);
	}

	public void fill(float mono, float opacity) {
		fill(mono, mono, mono, opacity);
	}

	public void fill(float red, float green, float blue) {
		fill(red, green, blue, 1.0F);
	}

	public void fill(float v1, float v2, float v3, float opacity) {
		if (colorMode == RGB) {
			int red = toInt(Math.min(255, v1));
			int green = toInt(Math.min(255, v2));
			int blue = toInt(Math.min(255, v3));
			int alpha = (int) map(opacity, 0.0F, 1.0F, 0.0F, 255.0F);
			fill(new Color(red, green, blue, alpha));
		}
	}

	public void fill(Paint color) {
		fillColor = color;
		noFill = false;
	}

	public void noFill() {
		noFill = true;
	}

	public void noStroke() {
		noStroke = true;
	}

	public void colorMode(int mode) {
		colorMode = mode;
	}

	public void rectMode(int mode) {
		rectMode = mode;
	}

	// Date and Time

	public int year() {
		return LocalDateTime.now().getYear();
	}

	public int month() {
		return LocalDateTime.now().getMonthValue();
	}

	public int day() {
		return LocalDateTime.now().getDayOfMonth();
	}

	public int hour() {
		return LocalDateTime.now().getHour();
	}

	public int minute() {
		return LocalDateTime.now().getMinute();
	}

	public int second() {
		return LocalDateTime.now().getSecond();
	}

	public int millis() {
		return toInt((startTime - System.currentTimeMillis()));
	}

// Mathematical functions

	public float sq(float n) {
		return n * n;
	}

	public float sqrt(float n) {
		return (float) Math.sqrt(n);
	}

	public float abs(float value) {
		return Math.abs(value);
	}

	public float ceil(float value) {
		return (float) Math.ceil(value);
	}

	public float dist(float x1, float y1, float x2, float y2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1));
	}

	public float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
	}

	public float radian(float degrees) {
		return (float) Math.toRadians(degrees);
	}

	public float radiansToDegrees(float radians) {
		return (float) Math.toDegrees(radians);
	}

// String functions

	public String join(String[] list, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (!first) {
				builder.append(separator);
			}
			first = false;
			builder.append(s);
		}
		return builder.toString();
	}

	public String[] split(String value, char delim) {
		return split(value, String.valueOf(delim));
	}

	public String[] split(String value, String delim) {
		StringTokenizer st = new StringTokenizer(value, delim);
		int size = st.countTokens();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = st.nextToken();
		}
		return result;
	}

	public String[] splitTokens(String value) {
		return splitTokens(value, "\t\n\r\f ");
	}

	private String[] splitTokens(String value, char delim) {
		return split(value, delim);
	}

	public String[] splitTokens(String value, String delim) {
		String[] result = splitTokens(value, delim.charAt(0));
		for (int i = 1; i < delim.length(); i++) {
			result = splitTokens(result, delim.charAt(i));
		}
		return result;
	}

	private String[] splitTokens(String[] values, char delim) {
		int size = 0;
		for (int i = 0; i < values.length; i++) {
			String[] arr = split(values[i], delim);
			size += arr.length;
		}
		String[] result = new String[size];
		int index = 0;
		for (int i = 0; i < values.length; i++) {
			String[] arr = split(values[i], delim);
			for (int j = 0; j < arr.length; j++) {
				result[index++] = arr[j];
			}
		}
		return result;
	}

	public String trim(String str) {
		return str.trim();
	}

	public String[] trim(String[] array) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = trim(array[i]);
		}
		return result;
	}

	public String nf(int num) {
		return String.format("%0d", num);
	}

	public String nf(int num, int digits) {
		String formatter = null;
		if (digits == 0) {
			formatter = "%d";
		} else {
			formatter = "%0" + Integer.toString(digits) + "d";
		}
		return String.format(formatter, num);
	}

	public String[] nf(int[] nums) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i]);
		}
		return result;
	}

	public String[] nf(int[] nums, int digits) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], digits);
		}
		return result;
	}

	public String nf(float num) {
		return String.format("%0f", num);
	}

	public String nf(float num, int digits) {
		String formatter = "%0" + Integer.toString(digits) + "f";
		return String.format(formatter, num);
	}

	public String nf(float num, int left, int right) {
		String formatter = "%0" + Integer.toString(left + right + 1) + "." + Integer.toString(right) + "f";
		return String.format(formatter, num);
	}

	public String[] nf(float[] nums) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i]);
		}
		return result;
	}

	public String[] nf(float[] nums, int digits) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], digits);
		}
		return result;
	}

	public String[] nf(float[] nums, int left, int right) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], left, right);
		}
		return result;
	}

// Environmental functions 

	public float screenResolution() {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	public float screenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}

	public float screenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}

// Shapes
	public void point(float x, float y) {
		Rectangle2D.Float point = new Rectangle2D.Float(x, y, strokeWeight, strokeWeight);
		showShape(point);
	}

	public void line(float x1, float y1, float x2, float y2) {
		Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
		showShape(line);
	}

	public void arc(float x, float y, float w, float h, float start, float extent) {
		Arc2D.Float arc = new Arc2D.Float(x - (w / 2), y - (h / 2), w, h, start, extent, Arc2D.OPEN);
		showShape(arc);
	}

	public void ellipse(float x, float y, float w, float h) {
		if (ellipseMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		Ellipse2D.Float oval = new Ellipse2D.Float(x, y, w, h);
		showShape(oval);
	}

	public void circle(float x, float y, float w) {
		ellipse(x, y, w, w);
	}

	public void rect(float x, float y, float w, float h) {
		if (rectMode == CENTER) {
			x -= w / 2;
			y -= h / 2;
		}
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, w, h);
		showShape(rect);
	}

	public void square(float x, float y, float w) {
		rect(x, y, w, w);
	}

	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		GeneralPath quad = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		quad.moveTo(x1, y1);
		quad.lineTo(x2, y2);
		quad.lineTo(x3, y3);
		quad.lineTo(x4, y4);
		quad.closePath();
		showShape(quad);
	}

	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		GeneralPath quad = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		quad.moveTo(x1, y1);
		quad.lineTo(x2, y2);
		quad.lineTo(x3, y3);
		quad.closePath();
		showShape(quad);
	}

	// Text

	public void textAlign(int alignX, int alignY) {
		textAlignX = alignX;
		textAlignY = alignY;
	}

	public void textSize(float textSize) {
		this.textSize = (int) textSize;
		textFont = textFont.deriveFont(textSize);
	}

	public float textWidth(char c) {
		String word = String.valueOf(c);
		return textWidth(word);
	}

	public float textWidth(String word) {
		TextLayout textTl = new TextLayout(word, textFont, new FontRenderContext(null, false, false));
		float width = (float) textTl.getBounds().getWidth();
		return width;
	}

	public float textHeight() {
		TextLayout textTl = new TextLayout("X", textFont, new FontRenderContext(null, false, false));
		float height = (float) textTl.getBounds().getHeight();
		return height;
	}

	public void text(char c, double x, double y) {
		text(String.valueOf(c), x, y);
	}

	public void text(char[] c, double x, double y) {
		text(String.valueOf(c), x, y);
	}

	public void text(String word, double x, double y) {
		TextLayout textTl = new TextLayout(word, textFont, new FontRenderContext(null, false, false));
		AffineTransform textAt = new AffineTransform();
		float xTranslate = (float) x;
		float yTranslate = (float) y;
		if (textAlignX == CENTER) {
			xTranslate -= textTl.getBounds().getWidth() / 2;
		}
		if (textAlignX == RIGHT) {
			xTranslate -= textTl.getBounds().getWidth();
		}
		if (textAlignY == CENTER) {
			yTranslate += textTl.getBounds().getHeight() / 2;
		}
		if (textAlignY == BOTTOM) {
			yTranslate += textTl.getBounds().getHeight();
		}
		textAt.translate(xTranslate, yTranslate);
		Shape shape = textTl.getOutline(textAt);
		showShape(shape);
	}

	public void textFont(Font font) {
		textFont = font;
	}

	public void textFont(Font font, float size) {
		textSize = (int) size;
		textFont = font.deriveFont(size);
	}

	public static float alpha(int rgb) {
		return (rgb >> 24) & 0xff;
	}

	public static float red(int rgb) {
		return (rgb >> 16) & 0xff;
	}

	public static float green(int rgb) {
		return (rgb >> 8) & 0xff;
	}

	public static float blue(int rgb) {
		return (rgb) & 0xff;
	}

	public static float hue(int rgb) {
		float[] hsb = rgbTohsb(rgb);
		return map((float) hsb[0], 0, 360, 0, 255);
	}

	public static float saturation(int rgb) {
		float[] hsb = rgbTohsb(rgb);
		return map((float) hsb[1], 0, 1, 0, 255);
	}

	public static float brightness(int rgb) {
		float[] hsb = rgbTohsb(rgb);
		return map((float) hsb[2], 0, 1, 0, 255);
	}

	private static float[] rgbTohsb(int rgb) {
		return Color.RGBtoHSB((int) red(rgb), (int) green(rgb), (int) blue(rgb), null);
	}

	// Transformations

	public void translate(float x, float y) {
		matrix.translateX = x;
		matrix.translateY = y;
	}

	// Utilities

	public void println() {
		System.out.println();
	}

	public void println(String string) {
		System.out.println(string);
	}

	public void println(int value) {
		System.out.println(value);
	}

	public void println(double value) {
		System.out.println(value);
	}

	public void println(float value) {
		System.out.println(value);
	}

	public void println(boolean truth) {
		System.out.println(truth);
	}

	public void println(GVector v) {
		System.out.print("[ " + v.x + " " + v.y + " " + v.z + " ]");
	}

	public void println(Object thing) {
		System.out.println(thing);
	}

	public void printArray(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			println("[" + i + "] " + array[i]);
		}
	}

	public static float map(float value, float start1, float stop1, float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
	}

	public String hex(int value, int digits) {
		String stuff = Integer.toHexString(value).toUpperCase();
		if (digits > 8) {
			digits = 8;
		}

		int length = stuff.length();
		if (length > digits) {
			return stuff.substring(length - digits);

		} else if (length < digits) {
			return "00000000".substring(8 - (digits - length)) + stuff;
		}
		return stuff;
	}

	public int unhex(String value) {
		return toInt((Long.parseLong(value, 16)));
	}

	public float sin(float angle) {
		return (float) Math.sin(radian(angle));
	}

	public float cos(float angle) {
		return (float) Math.cos(radian(angle));
	}

// Color manipulation

	public static int color(int gray) {
		if (gray > 255)
			gray = 255;
		else if (gray < 0)
			gray = 0;
		return 0xff000000 | (gray << 16) | (gray << 8) | gray;
	}

	public static final int color(int gray, int alpha) {
		if (alpha > 255)
			alpha = 255;
		else if (alpha < 0)
			alpha = 0;
		if (gray > 255) {
			// then assume this is actually a #FF8800
			return (alpha << 24) | (gray & 0xFFFFFF);
		} else {
			// if (gray > 255) gray = 255; else if (gray < 0) gray = 0;
			return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
		}
	}

	public static final int color(float v1, float v2, float v3) {
		return color(toInt(v1), toInt(v2), toInt(v3));
	}

	public static final int color(int v1, int v2, int v3) {
		if (v1 > 255)
			v1 = 255;
		else if (v1 < 0)
			v1 = 0;
		if (v2 > 255)
			v2 = 255;
		else if (v2 < 0)
			v2 = 0;
		if (v3 > 255)
			v3 = 255;
		else if (v3 < 0)
			v3 = 0;

		return 0xff000000 | (v1 << 16) | (v2 << 8) | v3;
	}

	public static final int color(int v1, int v2, int v3, int alpha) {
		if (alpha > 255)
			alpha = 255;
		else if (alpha < 0)
			alpha = 0;
		if (v1 > 255)
			v1 = 255;
		else if (v1 < 0)
			v1 = 0;
		if (v2 > 255)
			v2 = 255;
		else if (v2 < 0)
			v2 = 0;
		if (v3 > 255)
			v3 = 255;
		else if (v3 < 0)
			v3 = 0;

		return (alpha << 24) | (v1 << 16) | (v2 << 8) | v3;
	}

	public static Color color(float red, float green, float blue, float alpha) {
		return new Color(toInt(red), toInt(green), toInt(blue), alpha);
	}

// Image

	public void loadPixels() {
		pixels = new int[toInt(width) * toInt(height)];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[x + y * toInt(width)] = bufferedImage.getRGB(x, y);
			}
		}
	}

	public void updatePixels() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bufferedImage.setRGB(toInt(x + matrix.translateX), toInt(y + matrix.translateY),
						pixels[x + y * toInt(width)]);
			}
		}
	}

	public GImage createImage(float width, float height) {
		GImage image = new GImage(width, height);
		return image;
	}

	public GImage loadImage(String name) {
		GImage image = new GImage(name);
		return image;
	}

	public GImage loadImage(String name, float width, float height, boolean preserveRatio) {
		GImage image = new GImage(name, width, height, preserveRatio);
		return image;
	}

	public void image(GImage image, float xPos, float yPos) {
		if (image.pixels == null) {
			image.loadPixels();
		}
		for (int x = 0; x < image.width - 1; x++) {
			for (int y = 0; y < image.height - 1; y++) {
				bufferedImage.setRGB((int) (x + xPos + matrix.translateX), (int) (y + yPos + matrix.translateY),
						image.pixels[x + y * toInt(image.width)]);
			}
		}
	}

	private class Matrix {
		private float translateX;
		private float translateY;
	}
}
