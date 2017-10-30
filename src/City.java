
public class City {

	private int posX, posY;
	private City precCity, nextCity;
	
	public City getPrevCity() {
		return precCity;
	}
	public void setPrevCity(City precCity) {
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
	
	public City(City src){
		this.posX = src.posX;
		this.posY = src.posY;
		this.precCity = src.precCity;
		this.nextCity = src.nextCity;
	}
	
	public void linkPrevCity(City city) {
		// TODO Auto-generated method stub
		this.setPrevCity(city);
		city.setNextCity(this);
	}
	
	public void linkNextCity(City city) {
		// TODO Auto-generated method stub
		this.setNextCity(city);
		city.setPrevCity(this);
	}
	
	public double distanceToNextCity() {
		double d = Math.sqrt(Math.pow((posX-nextCity.getPosX()),2) + Math.pow(posY-nextCity.getPosY(),2));
		return d;
	}
}
