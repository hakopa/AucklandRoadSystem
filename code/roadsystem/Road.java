package roadsystem;

import java.util.HashSet;
import java.util.Set;

public class Road {
	int roadID;
	int type;
	String label;
	String city;
	//0 : both directions allowed
	//1 : one way road, direction from beginning to end
	int oneway;
	int speed;
	//0 : residential
	//1 : collector
	//2 : arterial
	//3 : principal HW
	//4 : major HW
	int roadclass;
	//0 : OK
	//1 : not OK
	int notforcar;
	int notforpede;
	int notforbic;
	
	Set<Segment> segments = new HashSet<>();
	
	public Road(int roadID, int type, String label, String city, int oneway, int speed, int rc, int nfc, int ntp, int ntb){
		this.roadID = roadID;
		this.type = type;
		this.label = label;
		this.city = city;
		this.oneway = oneway;
		this.speed = speed;
		roadclass = rc;
		notforcar = nfc;
		notforpede = ntp;
		notforbic = ntb;
	}
	
	public String getRoadName(){
		return this.label;// + ", " + this.city;
	}
	
	public int getRoadID(){
		return this.roadID;
	}
	public void addSeg(Segment s){
		segments.add(s);
	}
	
	public String toString(){
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + notforbic;
		result = prime * result + notforcar;
		result = prime * result + notforpede;
		result = prime * result + oneway;
		result = prime * result + roadID;
		result = prime * result + roadclass;
		result = prime * result + ((segments == null) ? 0 : segments.hashCode());
		result = prime * result + speed;
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Road))
			return false;
		Road other = (Road) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (notforbic != other.notforbic)
			return false;
		if (notforcar != other.notforcar)
			return false;
		if (notforpede != other.notforpede)
			return false;
		if (oneway != other.oneway)
			return false;
		if (roadID != other.roadID)
			return false;
		if (roadclass != other.roadclass)
			return false;
		if (segments == null) {
			if (other.segments != null)
				return false;
		} else if (!segments.equals(other.segments))
			return false;
		if (speed != other.speed)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	
}
