package roadsystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.awt.color.*;


public class Segment {
	int roadID;
	double length;
	Node toNode;
	Node fromNode;
	List<Location> locations = new ArrayList<>();
	boolean searched = false;
	
	public Segment(int roadID, double length, Node toNode, Node fromNode){
		this.roadID = roadID;
		this.length = length;
		this.toNode = toNode;
		this.fromNode = fromNode;
	}
	
	public void addLocation(Location loc){
		locations.add(loc);
	}
	
	public void draw(Graphics g, double scale, Location origin){
		Color c = Color.BLACK;
		if(searched){
			c = Color.GREEN;
			System.out.println("highlighting");
		}
		g.setColor(c);
		for(int i = locations.size()-1; i>0; i--){
			Point p1 = locations.get(i).asPoint(origin, scale);
			Point p2 = locations.get(i-1).asPoint(origin, scale);
			//g.setStroke(new BasicStroke((int) (1*scale/20)));
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	public int getRoadID(){
		return this.roadID;
	}
	
	public void highlight(){
		searched = true;
	}
	
	public void unhighlight(){
		searched = false;
	}
}
