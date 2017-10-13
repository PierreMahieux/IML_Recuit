import java.awt.Graphics;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel{

	static final int NUMBER_OF_CITIES = 20;
	static final double COOLING_COEFF = 0.93;
	public int temperature = 200000; 
	public ArrayList<City> cities = new ArrayList<>();
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		cities.forEach(new Consumer<City>() {

			@Override
			public void accept(City city) {
				// TODO Auto-generated method stub
				g.fillOval(city.getPosX() - 5, city.getPosY() - 5, 10, 10);
				g.drawLine(city.getPosX(), city.getPosY(), city.getNextCity().getPosX(), city.getNextCity().getPosY());
			}
		});
	}
	
	public boolean simulatedAnnealing() {
		double cost = pathCost();
		double newCost = 0;
		double deltaCost = 0;
		double proba = 1;
		boolean hasChanged = false;
		int steps = 0;
		
		do {
			City startCity = randomCity();
			change(startCity);
			newCost = pathCost();
			deltaCost = newCost - cost;
			if (deltaCost > 0) {
				proba = Math.exp(-deltaCost / temperature);
				if (proba > Math.random()) {
//					System.out.println("----------\nACCPETED PROBA\ntemperature : " + temperature + "\ndeltaCost : " + deltaCost + "\nproba : " + proba);
					hasChanged = true;
				}
				else {
//					System.out.println("REVERSE");
					change(startCity);
				}
			}
			else {
//				System.out.println("----------\nACCPETED DIRECT\ntemperature : " + temperature);
				hasChanged = true;
			}
			
			steps ++;
		} while (!hasChanged && steps < NUMBER_OF_CITIES*NUMBER_OF_CITIES/4);
		
		temperature *= COOLING_COEFF;
		
		return hasChanged;
	}
	
	public Panel() {
		for (int i = 0; i < NUMBER_OF_CITIES; i++) {
			cities.add(new City((int)(Math.random()*500 + 50), (int)(Math.random()*500 + 50)));
			if(i != 0) {
				cities.get(i).linkPrecCity(cities.get(i-1));
				if (i == NUMBER_OF_CITIES - 1) {
					cities.get(i).linkNextCity(cities.get(0));
				}
			}
		}
	}
	
	public double pathCost() {
		double cost = 0;
		
		for(City city : cities) {
			cost += city.distanceToNextCity();
		}
		return cost;
	}
	
	public void change(City startCity) {
		City[] randomTities = new City[4];
		
		randomTities[0] = startCity;
		
		for (int i = 1; i < randomTities.length; i++) {
			randomTities[i] = randomTities[i-1].getNextCity();
		}
		
		randomTities[0].linkNextCity(randomTities[2]);
		randomTities[1].linkNextCity(randomTities[3]);
		randomTities[2].linkNextCity(randomTities[1]);
	}
	
	public City randomCity() {
		return cities.get((int)(Math.random()*cities.size()));
	}
}
