import java.awt.Graphics;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel{

	static final int NUMBER_OF_CITIES = 100;
	static final double COOLING_COEFF = 0.97;
	static final int HEIGHT_CHART = 300;
	static final int WIDTH_CHART = 500;
	static final int MAX_CONST_TEMP_CHANGE = NUMBER_OF_CITIES*NUMBER_OF_CITIES/4;
	static final int MAX_NEW_STATE = 100;
	
	public double temperature = 200000; 
	public ArrayList<City> cities = new ArrayList<>();
	public ArrayList<City> optPath = new ArrayList<>();
	public ArrayList<Double> pathCostMemory = new ArrayList<Double>();
	
	public int graphXOrigins, graphYOrigins;
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		graphXOrigins = this.getWidth() - WIDTH_CHART - 50;
		graphYOrigins = this.getHeight() - 50;
		//draw cities and roads
		cities.forEach(new Consumer<City>() {
			@Override
			public void accept(City city) {
				// TODO Auto-generated method stub
				g.fillOval(city.getPosX() - 5, city.getPosY() - 5, 10, 10);
				g.drawLine(city.getPosX(), city.getPosY(), city.getNextCity().getPosX(), city.getNextCity().getPosY());
			}
		});
		
		//draw chart axes;
		g.drawLine(this.getWidth() - WIDTH_CHART - 50, this.getHeight() - 50, this.getWidth() - 50, this.getHeight() - 50);
		g.drawLine(this.getWidth() - WIDTH_CHART - 50, this.getHeight() - HEIGHT_CHART - 50, this.getWidth() - WIDTH_CHART - 50, this.getHeight() - 50);
		
		if (pathCostMemory.size() > 1) {
			double maxVal = 0;
			for (Double memCost : pathCostMemory) {
				maxVal = (maxVal < memCost) ? memCost : maxVal; 
			}
			//draw chart
			for (int i = 1; i < pathCostMemory.size(); i++) {
				g.drawLine(graphXOrigins + (i-1) * WIDTH_CHART/pathCostMemory.size(), (int)(graphYOrigins - (pathCostMemory.get(i-1))*HEIGHT_CHART/maxVal), graphXOrigins + (i) * WIDTH_CHART/pathCostMemory.size(), (int)(graphYOrigins - (pathCostMemory.get(i))*HEIGHT_CHART/maxVal));
			}
		}
	}
	
	public boolean simulatedAnnealing() {
		double cost = pathCost(cities);
		double newCost = 0;
		double deltaCost = 0;
		double proba = 1;
		boolean hasChanged = false;
		int steps = 0;
		int newConf = 0;
		City[] randomCities = new City[2];
		
		do {
			randomCities = getTwoNonConsecutiveRandomCities();
			change(randomCities);
			newCost = pathCost(cities);
			deltaCost = newCost - cost;
			if (deltaCost > 0) {
				proba = Math.exp(-deltaCost / temperature);
				if (proba > Math.random()) {
					hasChanged = true;
					newConf ++;
				}
				else {
					change(randomCities);
				}
			}
			else {
				hasChanged = true;
				newConf ++;
				if(newCost < pathCost(optPath)){
					optPath = clonePath(cities);
				}
			}
			
			steps ++;
		} while (steps < MAX_CONST_TEMP_CHANGE && newConf < MAX_NEW_STATE);
		
		temperature *= COOLING_COEFF;
		pathCostMemory.add(pathCost(cities));
		
		return hasChanged;
	}
	
	public Panel() {
		for (int i = 0; i < NUMBER_OF_CITIES; i++) {
			cities.add(new City((int)(Math.random()*500 + 50), (int)(Math.random()*500 + 50)));
			if(i != 0) {
				cities.get(i).linkPrevCity(cities.get(i-1));
				if (i == NUMBER_OF_CITIES - 1) {
					cities.get(i).linkNextCity(cities.get(0));
				}
			}
		}
	}
	
	public void randomConfig(){
		for (City city : cities) {
			if (city.getPrevCity() == null) {
				City prevCity;
				do {
					prevCity = randomCity();
				} while (prevCity == city || prevCity.getPrevCity() == city || prevCity.getNextCity() == city);
				city.linkPrevCity(prevCity);
			}
			if (city.getNextCity() == null) {
				City nextCity;
				do {
					nextCity = randomCity();
				} while (nextCity == city || nextCity.getNextCity() == city || nextCity.getPrevCity() == city);
				city.linkNextCity(nextCity);
			}
		}
	}
	
	public void cleanPath(){
		for (City city : cities) {
			city.setNextCity(null);
			city.setPrevCity(null);
		}
	}
	
	public double pathCost(ArrayList<City> path) {
		double cost = 0;
		
		for(City city : path) {
			cost += city.distanceToNextCity();
		}
		return cost;
	}
	
	public City[] getTwoNonConsecutiveRandomCities(){
		City[] cities = new City[2];
		do {
			cities[0] = randomCity();
			cities[1] = randomCity();
		} while (cities[0] == cities[1].getNextCity() || cities[0] == cities[1].getPrevCity() || cities[0] == cities[1]);
		return cities;
	}
	
	
	public void change(City[] cities){
		City precStartCity = cities[0].getPrevCity();
		City nextStartCity = cities[0].getNextCity();
		
		cities[0].linkNextCity(cities[1].getNextCity());
		cities[0].linkPrevCity(cities[1].getPrevCity());
		
		cities[1].linkNextCity(nextStartCity);
		cities[1].linkPrevCity(precStartCity);
	}
	
	public City randomCity() {
		return cities.get((int)(Math.random()*cities.size()));
	}
	
	public ArrayList<City> clonePath(ArrayList<City> pathSrc){
		ArrayList<City> pathDst = new ArrayList<>();
		
		for(City city : pathSrc){
			pathDst.add(new City(city));
		}
		
		return pathDst;
	}
}
