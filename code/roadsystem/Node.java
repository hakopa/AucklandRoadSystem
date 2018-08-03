package roadsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node {
	
	private static final int SQUARE_SIZE = 5;
	
	Location location;
	int nodeID;
	//goingIn
	Map<Node, Double> goingInNodes = new HashMap<>();
	Set<Segment> goingInSegs = new HashSet<Segment>();
	//goingOut
	Map<Node, Double> goingOutNodes = new HashMap<>();
	Set<Segment> goingOutSegs = new HashSet<Segment>();
	
	//the point where the node is currently drawn on the panel
	Point p;
	boolean clicked = false;
	public Node(int id,Location loc){
		this.location = loc;
		this.nodeID = id;
	}
	
	public void addNodeInFrom(Node n, double dist){
		goingInNodes.put(n, dist);
	}
	public void addNodeOutTo(Node n, double dist){
		goingOutNodes.put(n, dist);
	}
	public void addSegInFrom(Segment s){
		this.goingInSegs.add(s);
	}
	
	public void addSegOutTo(Segment s){
		this.goingOutSegs.add(s);
	}
	
	public void draw(Graphics g, double scale, Location origin){
		
		this.p = location.asPoint(origin, scale);
		int size = (int) (scale/25) + 1;
		if(size>SQUARE_SIZE)
			size = 5;
		//dividing so the square will be in the middle of the intersection
		g.setColor(Color.BLUE);
		g.fillRect(p.x-size/2 , p.y-size/2, size, size);
		if(clicked){
			g.setColor(Color.red);
			g.drawRect(p.x - size/2 , p.y-size/2, size, size);
		}	
		
	}
	
	public int getID(){
		return this.nodeID;
	}
	
	/**
	 * This method increases the size of the square drawn on the map.
	 */
	public void highlight(){
		this.clicked = true;
	}
	
	public void unhighlight(){
		this.clicked = false;
	}
	
	public Set<Segment> getSegments(){
		Set<Segment> roads = new HashSet<>();
		for(Segment s: this.goingInSegs){
			roads.add(s);
		}
		for(Segment s: this.goingOutSegs){
			roads.add(s);
		}
		return roads;
	}
	
	@Override
	public String toString(){
		return (this.nodeID + ", " + this.location);
	}
}
