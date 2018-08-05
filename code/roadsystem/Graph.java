package roadsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	Map<Integer, Node> nodes = new HashMap<>();
	Map<Integer, Road> roads = new HashMap<>();
	Map<Integer, List<Segment>> segments = new HashMap<Integer, List<Segment>>();
	
	public void addNodes(Map<Integer, Node> nodes){
		this.nodes = nodes;
	}
	
	public void addRoads(Map<Integer, Road> roads){
		this.roads = roads;
	}
	public void addSegs(Map<Integer, List<Segment>> segs){
		this.segments = segs;
	}
	


}
