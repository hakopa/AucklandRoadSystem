package roadsystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
 /**
  */
public class RoadSystem extends GUI {
	Map<Integer, Node> nodes = new HashMap<>();
	Map<Integer, Road> roads = new HashMap<>();
	Map<Integer, List<Segment>> segments = new HashMap<Integer, List<Segment>>();
	Set<Segment> highlightSegs = new HashSet<>();
	Location origin = Location.newFromLatLon(Location.CENTRE_LAT, Location.CENTRE_LON);
	//use to see if zoomed in or not
	//double scale = 5;
	double scale;
	double zoom_factor = 1.5;
	//use to see if pan in direction
	Node current;
	double min;
	double max;
	Graphics g;
	int counter = 0;
	
	Trie root;
	
	public RoadSystem() {
		super();
		root = new Trie();
	}
 	@Override
	protected void redraw(Graphics g) {
 		//draw all road segments
		for(List<Segment> segs : this.segments.values()){
			for(Segment s : segs)
				s.draw(g, scale, origin);
		}
		if(!highlightSegs.isEmpty())	unhighlight(highlightSegs);
		
		//draw all nodes
		if(current!=null) 	current.highlight();
		for(Node node : nodes.values()){
			node.draw(g, scale, origin);
		}
		if(current!=null)	current.unhighlight();
	}
	
	public void highlight(Set<Segment> high){
		int count = 0;
		for(Segment s : high){
			s.highlight();
		}
	}
	
	public void unhighlight(Set<Segment> unhigh){
		int count = 0;
		for(Segment s : unhigh){
			s.unhighlight();
		}
	}
 	@Override
	protected void onClick(MouseEvent e) {
		/*
		 * we search from the back to the front of the list (while drawing
		 * happens front-to-back) so that we always remove the top square if
		 * there are any overlapping. this is why we use a list and not a set to
		 * store the squares in the first place.
		 */
		
		//find if mouse click point is close enough to another location.
		//If it is, highlight this node
		Location loc = Location.newFromPoint(new Point(e.getX(), e.getY()), origin, scale);
		Location max = Location.newFromPoint(new Point(Integer.MAX_VALUE, Integer.MIN_VALUE), origin, scale);
		Node closest = new Node(0, null);
		for(Node n : nodes.values()){
			Location l = Location.newFromPoint(n.p, origin, scale);
			//found a closer node
			if(loc.isClosest(max, l).equals(l)){
				closest = n;
				max  = l;
			}
		}
		current = closest;
		
		//print id of intersection (node)
		getTextOutputArea().append("\nNode Id : " + closest.getID());
		//get names of roads at intersection
		Set<String> roadNames = new HashSet<>();
		for(Segment s : closest.getSegments()){
			roadNames.add(roads.get(s.getRoadID()).getRoadName());
		}
		for(String s : roadNames)
			getTextOutputArea().append("\n" + s);
	}
 	@Override
	protected void onSearch() {
		String search = getSearchBox().getText().toLowerCase();
		HashSet<Road> results = root.getAll(search);
		
		//road not found
		if(results.isEmpty()){	
			getTextOutputArea().setText("Street not found");
			return;
		}
		getTextOutputArea().setText("Searching for... " + search);
		highlightSegs.clear();
		for(Road r : results){
			getTextOutputArea().append("\n" + r.getRoadName() + ", " + r.label);
			highlightSegs.addAll(r.segments);
		}
		/*
		 * Core Search
		
		//getTextOutputArea().setText(getSearchBox().getText());
		//String search = getSearchBox().getText();
		Set<Integer> roadIDs = new HashSet<>();
		highlightSegs.clear();
		//check for the road names in all the roads
		for(Road r : roads.values()){
			if(search.equals(r.getRoadName())){
				roadIDs.add(r.getRoadID());
			}
		}
		/*
		 * Check for the road ids in all segments. If an id matches,
		 * add to the set to highlight on the map
		 */
		/*
		for(int r : roadIDs){
			List<Segment> segs= segments.get(r);
			for(Segment s : segs)
				highlightSegs.add(s);
		}
		*/
		
	}
	
	public void drawSegments(){
		if(highlightSegs.isEmpty())
			return;
		highlight(highlightSegs);
		for(Segment s : highlightSegs){
			s.draw(g, scale, origin);
		}
		unhighlight(highlightSegs);
	}
 	@Override
	protected void onMove(Move m) {
		double move = 1/this.scale*100;
		if(m==Move.EAST){
			this.origin = origin.moveBy(move, 0.0);
		}
		if(m==Move.NORTH){
			
			this.origin = origin.moveBy(0.0, move);
		}
		if(m==Move.SOUTH){
			this.origin = origin.moveBy(0.0, -(move));
		}
		if(m==Move.WEST){
			this.origin = origin.moveBy(-(move), 0.0);
		}
					
		double height = getDrawingAreaDimension().height;
		double width = getDrawingAreaDimension().width;
		
		double dx;
		double dy;
		if(m==Move.ZOOM_IN){
 			this.scale *= this.zoom_factor;
			
		}
		if(m==Move.ZOOM_OUT){
 			this.scale /= this.zoom_factor;
			
		}
	}
 	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		//getTextOutputArea().setText("example doesn't load any files.");
		loadNodes(nodes);
		loadRoads(roads);
		loadSegments(segments);
	}
	
	/**
	 * Reads the node file. Creates the nodes from the file and adds them to the arraylist holding the nodes.  
	 * @param nodes
	 */
	private void loadNodes(File nodes){
		try {
			BufferedReader in = new BufferedReader(new FileReader(nodes));
			String line = "";
			max = Double.MIN_VALUE;
			min = Double.MAX_VALUE;
			while((line = in.readLine()) != null){
				String[] values = line.split("\t");
				double lat = Double.parseDouble(values[1]);
				double lon = Double.parseDouble(values[2]);
				Location loc = Location.newFromLatLon(lat,lon);
				this.nodes.put(Integer.parseInt(values[0]), new Node(Integer.parseInt(values[0]),loc));
				
				if(lat>=max)	max = lat;
				if(lat<=min)	min = lat;
			}
			this.scale = getDrawingAreaDimension().getHeight()/(max-min);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the road file. Creates the roads from the file and adds them to the arraylist holding the roads.
	 * @param roads
	 */
	private void loadRoads(File roads){
		try {
			BufferedReader in = new BufferedReader(new FileReader(roads));
			String line = in.readLine();
			while((line = in.readLine()) != null){
				String[] values = line.split("\t");
				Road r =  new Road(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[2], values[3],
						Integer.parseInt(values[4]), Integer.parseInt(values[5]), Integer.parseInt(values[6]), Integer.parseInt(values[7]),
						Integer.parseInt(values[8]), Integer.parseInt(values[9]));
				this.roads.put(Integer.parseInt(values[0]), r);
				
				//set up tries for searching
				root.add(r);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 	/**
	 * Reads the segment file. Creates the segments from the file and adds them to the arraylist holding the segments.
	 * @param segments
	 */
	private void loadSegments(File segments){
		try {
			BufferedReader in = new BufferedReader(new FileReader(segments));
			//skips the first line
			String line = in.readLine();
			while((line = in.readLine()) != null){
				String[] values = line.split("\t");
				int roadID = Integer.parseInt(values[0]);
				//find the road
				Road r = this.roads.get(roadID);
				
				//find the two nodes
				Node n1 = this.nodes.get(Integer.parseInt(values[2]));
				Node n2 = this.nodes.get(Integer.parseInt(values[3]));
				double dist = Double.parseDouble(values[1]);
				Segment seg = new Segment(roadID, dist, n1, n2);
				
				//add the points for drawing the roads
				for(int i = 4; i<values.length; i++){
					seg.addLocation(Location.newFromLatLon(Double.parseDouble(values[i]), Double.parseDouble(values[++i])));
				}
				
				/*
				 * Load segments to nodes
				 */
				//add the node to node edges
				n2.addNodeInFrom(n1, dist);
				n1.addNodeOutTo(n2, dist);
				//segment to each node
				n1.addSegOutTo(seg);
				n2.addSegInFrom(seg);
				//add segment to segment list
				if(this.segments.containsKey(roadID)){
					this.segments.get(roadID).add(seg);
				}else{
					List<Segment> segs = new ArrayList<>();
					segs.add(seg);
					this.segments.put(roadID, segs);
				}
				/*
				 * Load segments to roads
				 */
				r.addSeg(seg);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 	public static void main(String[] args) {
		new RoadSystem();
	}
	@Override
	protected void updateSearch() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onScroll(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}
}