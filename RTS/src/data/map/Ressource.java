package data.map;

public class Ressource {
	
	private Position position;
	private String type;
	
	public Ressource(Position position,String type) {
		this.type=type;
		this.position=position;
	}

	public Position getPosition() {
		return position;
	}

	public String getType() {
		return type;
	}



	@Override
	public String toString() {
		return "Ressource [position=" + position + ", type=" + type + "]";
	}
	
	

}
