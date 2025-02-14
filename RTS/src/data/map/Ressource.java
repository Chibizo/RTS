package data.map;

public class Ressource {
	
	private Position position;
	private String type;
	private int health;
	
	public Ressource(Position positon,String type) {
		health=100;
		this.type=type;
		this.position=position;
	}

	public Position getPosition() {
		return position;
	}

	public String getType() {
		return type;
	}

	public int getHealth() {
		return health;
	}
	
	

}
