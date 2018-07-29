import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class BubblePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Random rand = new Random();
	ArrayList<Bubble> bubbleList;
	int size = 25;
	Timer timer;
	int delay = 33;
	JSlider slider;
	
	public BubblePanel() {
		timer = new Timer(delay, new BubbleListener());
		bubbleList = new ArrayList<Bubble>();
		setBackground(Color.WHITE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		add(panel);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				if (btn.getText().equals("Pause")) {
					timer.stop();
					btn.setText("Start");
				}
				else {
					timer.start();
					btn.setText("Pause");
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Animation Speed:");
		panel.add(lblNewLabel);
		
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int speed = slider.getValue() + 1;
				int delay = 1000 / speed;
				timer.setDelay(delay);
			}
		});
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(30);
		slider.setMinorTickSpacing(5);
		slider.setMaximum(120);
		panel.add(slider);
		panel.add(btnPause);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bubbleList = new ArrayList<Bubble>();
				repaint();
			}
		});
		panel.add(btnClear);
		//testBubbles();
		addMouseListener(new BubbleListener());
		addMouseMotionListener(new BubbleListener());
		addMouseWheelListener(new BubbleListener());
		timer.start();
	}
	
	public void paintComponent(Graphics canvas) {
		super.paintComponent(canvas);
		for (Bubble b :bubbleList) {
			b.draw(canvas);
		}
	}
	
	public void testBubbles() {
		for (int n = 0; n < 100; n++) {
			int x = rand.nextInt(600);
			int y = rand.nextInt(400);
			int size = rand.nextInt(50);
			bubbleList.add(new Bubble(x, y, size));
		}
		repaint();
	}
	
	private class BubbleListener extends MouseAdapter implements ActionListener {
		public void mousePressed(MouseEvent e) {
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (System.getProperty("os.name").startsWith("Mac"))
				size += e.getUnitsToScroll();
			else
				size -= e.getUnitsToScroll();
		}
		
		public void actionPerformed(ActionEvent e) {
			for (Bubble b : bubbleList)
				b.update();
			repaint();
		}
	}
	
	private class Bubble {
		private int x;
		private int y;
		private int size;
		private Color color;
		private int xspeed, yspeed;
		private final int MAX_SPEED = 5;
		
		public Bubble(int newX, int newY, int newSize) {
			x = newX;
			y = newY;
			size = newSize;
			color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			xspeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
			yspeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
		}
		
		public void draw(Graphics canvas) {
			canvas.setColor(color);
			canvas.fillOval(x - size/2, y - size/2, size, size);
		}
		
		public void update() {
			if (xspeed == 0 && yspeed == 0) {
				xspeed += 10;
				yspeed += 10;
			}
			x += xspeed;
			y += yspeed;
			if (x <= 0 || x >= getWidth())
				xspeed = -xspeed;
			if (y <= 0 || y >= getHeight())
				yspeed = -yspeed;
		}
	}
}
