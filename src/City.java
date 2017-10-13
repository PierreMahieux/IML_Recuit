
public class City {

	private int posX, posY;
	private City precCity, nextCity;
	
	public City getPrecCity() {
		return precCity;
	}
	public void setPrecCity(City precCity) {
		this.precCity = precCity;
	}
	public City getNextCity() {
		return nextCity;
	}
	public void setNextCity(City nextCity) {
		this.nextCity = nextCity;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	
	public City(int x, int y) {
		posX = x; posY = y;
	}
	
	
	public void linkPrecCity(City city) {
		// TODO Auto-generated method stub
		this.setPrecCity(city);
		city.setNextCity(this);
	}
	
	public void linkNextCity(City city) {
		// TODO Auto-generated method stub
		this.setNextCity(city);
		city.setPrecCity(this);
	}
	
	public double distanceToNextCity() {
		double d = Math.sqrt(Math.pow((posX-nextCity.getPosX()),2) + Math.pow(posY-nextCity.getPosY(),2));
		return d;
	}
}
