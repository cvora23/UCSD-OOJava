package module6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	static List<ShapeFeature> routes;
	
	public void setup() {
		// setting up PAppler
		size(1400,700, OPENGL);
		
		horLoc = 350;
		f = createFont("Jokerman",72,true);
		// setting up map and default events
		map = new UnfoldingMap(this, 800, 50, 650, 600);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		
		// parse route data
		routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int sourceId = Integer.parseInt((String)route.getProperty("sourceId"));
			int destId = Integer.parseInt((String)route.getProperty("destinationId"));

			// get locations for airports on route
			if(airports.containsKey(sourceId) && airports.containsKey(destId)) {
				route.addLocation(airports.get(sourceId));
				route.addLocation(airports.get(destId));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			//routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}else{
			selectMarkerIfHover(airportList);
		}
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			lastClicked.setProperty("Clicked","no");
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}
	
	// Helper method that will check if a city marker was clicked on
	// and respond appropriately
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the city markers to see if one of them is selected
		for (Marker marker : airportList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other cities and hide
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
						marker.setProperty("Clicked","yes");
					}
				}
				return;
			}
		}		
	}
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}
	
	public void draw() {
		background(0);
		map.draw();
		try {
			addKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addKey() throws IOException{
		
		if( (lastClicked != null) && (((AirportMarker)lastClicked).isClicked() != null) && 
				(((AirportMarker) lastClicked).isClicked().equals("yes"))){

			
			String code = ((AirportMarker)lastClicked).getCode().replaceAll("\"", "");
			String sourceAirportCityStr = "Source Airport City: "+ IataCodeReader.getCityNameGivenIataCode(code);
			String destAirportCityHdr = "Destination Airport Cities listed below: ";
			
			List<String>destList = null;
			destList = AirportMap.getListOfDest(code);
		
			// Remember you can use Processing's graphics methods here
			fill(255, 255, 255);
			rect(xbase, ybase, 350, 350);
			
			fill(color(0, 0, 0));
			textFont(f,15);
			text(sourceAirportCityStr,100,75);
			text(destAirportCityHdr,60,150);

			if(destList.size() > 0){
				textAlign(LEFT);
				fill(color(0, 0, 0));
				text(IataCodeReader.getCityNameGivenIataCode(destList.get(index)),horLoc,225);
				horLoc = horLoc-30;
				float w = textWidth(IataCodeReader.getCityNameGivenIataCode(destList.get(index)));
				if(horLoc < -w){
					horLoc = 350;
					index = (index+1) % (destList.size());
				}
			}
		}
		else if(lastClicked == null){
			index = 0;
			horLoc = 350;
		}
	}
	
	public static List<String> getListOfDest(String source){
		List<String> dest = new ArrayList<String>();
		
		for(ShapeFeature route : routes){
			
			if(((String)route.getProperty("source")).equals(source)){
				if(!dest.contains((String)route.getProperty("destination")))
					dest.add((String)route.getProperty("destination"));
			}
		}
		
		return dest;
	}

	private float horLoc; // horizontal locations of headlines
	private int index = 0;
	private int xbase = 25;
	private int ybase = 50;
	PFont f;
}
