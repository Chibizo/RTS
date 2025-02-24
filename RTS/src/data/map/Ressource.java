package data.map;

public class Ressource {
	
	private Zone zone;
	private String type;
	
	public Ressource(Zone zone,String type) {
		this.type=type;
		this.zone=zone;
	}

	public Zone getPosition() {
		return zone;
	}

	public String getType() {
		return type;
	}



	@Override
	public String toString() {
		return "Ressource [position=" + zone + ", type=" + type + "]";
	}
	
	

}
