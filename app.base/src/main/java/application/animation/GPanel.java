package application.animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

public class GPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GApplication application;

	public GPanel(GApplication application) {
		this.application = application;

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				application.mouseDragged();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				application.mouseMoved();
			}

		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				application.mousePressed = true;
				if (e.getButton() == MouseEvent.BUTTON1) {
					application.mouseButton = GApplication.LEFT;
				} else if (e.getButton() == MouseEvent.BUTTON2) {
					application.mouseButton = GApplication.RIGHT;
				} else {
					application.mouseButton = GApplication.CENTER;
				}
				application.mousePressed();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				application.mousePressed = false;
				application.mouseButton = 0;
				application.mouseClicked();
				application.mouseReleased();
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				application.mouseWheel(e);
			}

		});
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyTyped(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyReleased(e);
			}

		});

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(application.renderingHints);
		g2d.drawImage(application.bufferedImage, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) application.width, (int) application.height);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension((int) application.width, (int) application.height);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension((int) application.width, (int) application.height);
	}

}
