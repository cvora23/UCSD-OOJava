package module6;

import java.io.IOException;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import demos.Airport;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(11);
		pg.ellipse(x, y, 5, 5);


	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		
		String title = getName() + " " + getCity() + " " + getCountry() + " " + getCode();

		pg.pushStyle();
	
		/**
		 * 1: Show City Name
		 */
		pg.fill(255, 255, 255);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x+10, y, pg.textWidth(title) + 10, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(title, x+15, y+5);
		pg.popStyle();

	}
	
	private String getName()
	{
		return getStringProperty("name");
	}
	
	private String getCity()
	{
		return getStringProperty("city");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	public String getCode()
	{
		return getStringProperty("code");
	}
	
	public String isClicked(){
		return getStringProperty("Clicked");
	}

}
