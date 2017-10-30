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
		for (int i = 0; i < 1; i++) {
			double costInit = panel.pathCost(panel.cities);
			double t0 = setup();
			int rounds = go();
			System.out.println("----------\nscore Initial : " + costInit + "\nscore FINAL : " + panel.pathCost(panel.cities) + "\nt0: " + t0
					+ "\ntFinal : " + panel.temperature + "\nrounds count : " + rounds);
			panel.randomConfig();
		}
	}

	public int go() {
		int noChangeCount = 0;
		int numRounds = 0;
		
		panel.optPath = panel.cities;
		
		do {
			if (!panel.simulatedAnnealing()) {
				noChangeCount++;
			}
			else {
				noChangeCount = 0;
			}
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			numRounds ++;
		} while (panel.temperature > 1 && noChangeCount < 5);
		
		return numRounds;
	}
	
	public double setup() {
		double deltaE = 0;
		double temp0;
		double sigma0 = 0.5;
		double costInit = panel.pathCost(panel.cities);
		
		for (int i = 0; i < 100; i++) {
			City[] randomCities = panel.getTwoNonConsecutiveRandomCities();
			panel.change(randomCities);
			deltaE += Math.abs(panel.pathCost(panel.cities) - costInit);
			panel.change(randomCities);
		}
		
		deltaE /= 100;
		temp0 = -deltaE/Math.log(sigma0);
		panel.temperature = temp0;
		return temp0;
	}
}
