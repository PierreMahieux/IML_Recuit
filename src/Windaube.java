import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Windaube extends JFrame {
	private Panel panel;
	private int width, height;
	
	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public Windaube(int w, int h) {
		width = w; height = h;
		
		setVisible(true);
		setTitle("Recuit");
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(width, height);
		
		panel = new Panel();
		
		setContentPane(panel);
		double t0 = setup();
		go();
		
		System.out.println("-----------------\nscore FINAL : " + panel.pathCost() + "\ntemp0: " + t0);
	}

	public void go() {
		int noChangeCount = 0;
		do {
			if (!panel.simulatedAnnealing()) {
				noChangeCount++;
			}
			else {
				noChangeCount = 0;
			}
			repaint();
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println("-----------------\ntempérature : " + panel.temperature + "\nscore : " + panel.pathCost());
		} while (panel.temperature > 1 && noChangeCount < 5);
	}
	
	public double setup() {
		double deltaE = 0;
		double temp0;
		double sigma0 = 0.5;
		
		for (int i = 0; i < 100; i++) {
			panel.change(panel.randomCity());
			deltaE += panel.pathCost();
		}
		
		deltaE /= 100;
		temp0 = -deltaE/Math.log(sigma0);
		
		panel.temperature = (int)temp0;
		return temp0;
	}
}
