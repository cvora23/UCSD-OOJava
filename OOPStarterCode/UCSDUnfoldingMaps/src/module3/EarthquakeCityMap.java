package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;


//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
	    //TODO: Add code here as appropriate
	    // DONE:
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    for (PointFeature earthquake: earthquakes) {
	    	System.out.println(earthquake.getProperties());
	    	Object magObj = earthquake.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    	SimplePointMarker marker = createMarker(earthquake); 
		    enhanceMarker(marker,mag);
		    map.addMarker(marker);
	    }
	}
	
	//Helper method to color each country based on life expectancy
	//Red-orange indicates low (near 40)
	//Blue indicates high (near 100)
	private void enhanceMarker(SimplePointMarker marker,float mag) {
	    int red = color(255, 0, 0);
	    int redRadius = 20;
	    int yellow = color(255, 255, 0);
	    int yellowRadius = 10;
	    int blue = color(0,0,255);
	    int blueRadius = 5;
	    
		if(mag >5.0){
			marker.setColor(red);
			marker.setRadius(redRadius);
		}else if(mag>4.0){
			marker.setColor(yellow);
			marker.setRadius(yellowRadius);
		}else{
			marker.setColor(blue);
			marker.setRadius(blueRadius);
		}
	}

		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	// DONE:
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	// DONE:
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255,253,208);
		rect(20, 50, 150, 500);
		
		textSize(12);
		String s = "Earthquake Key";
		fill(50);
		text(s,45,100);

		fill(255, 0, 0);
		ellipse(45,150,20,20);
		
		textSize(12);
		s = "5.0+ Magnitude";
		fill(50);
		text(s,65,150);
		
		fill(255, 255, 0);
		ellipse(45,200,10,10);
		
		textSize(12);
		s = "4.0+ Magnitude";
		fill(50);
		text(s,65,200);
		
		fill(0, 0, 255);
		ellipse(45,250,5,5);
		
		textSize(12);
		s = "Below 4.0 ";
		fill(50);
		text(s,65,250);
	}
}
