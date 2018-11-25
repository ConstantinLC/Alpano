package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import static java.lang.Math.toRadians;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public final class PanoramaUserParameters {

	private final Map<UserParameter, Integer> map;
	
	public PanoramaUserParameters(Map<UserParameter, Integer> map){
		Map<UserParameter, Integer> temp = new EnumMap<>(UserParameter.class);
		
		for(UserParameter parameter : map.keySet()){
			temp.put(parameter, parameter.sanitize(map.get(parameter)));
		}

		int height = 170*((map.get(UserParameter.WIDTH) -1)/map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW))+1;
		temp.put(UserParameter.HEIGHT, UserParameter.HEIGHT.sanitize(Math.min(height, map.get(UserParameter.HEIGHT))));
		
		this.map = Collections.unmodifiableMap(new EnumMap<>(temp));
	}
	
	public PanoramaUserParameters(int OBSERVER_LONGITUDE, int OBSERVER_LATITUDE,
            int OBSERVER_ELEVATION, int CENTER_AZIMUTH, int HORIZONTAL_FIELD_OF_VIEW,
            int MAX_DISTANCE, int WIDTH, int HEIGHT, int SUPER_SAMPLING_EXPONENT){
        this(toMap(OBSERVER_LONGITUDE, OBSERVER_LATITUDE, OBSERVER_ELEVATION,
                CENTER_AZIMUTH, HORIZONTAL_FIELD_OF_VIEW, MAX_DISTANCE, WIDTH, HEIGHT, SUPER_SAMPLING_EXPONENT));
    }
    
    private static Map<UserParameter, Integer> toMap(int OBSERVER_LONGITUDE, int OBSERVER_LATITUDE,
            int OBSERVER_ELEVATION, int CENTER_AZIMUTH, int HORIZONTAL_FIELD_OF_VIEW,
            int MAX_DISTANCE, int WIDTH, int HEIGHT, int SUPER_SAMPLING_EXPONENT){
        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        map.put(UserParameter.OBSERVER_LONGITUDE, OBSERVER_LONGITUDE);
        map.put(UserParameter.OBSERVER_LATITUDE, OBSERVER_LATITUDE);
        map.put(UserParameter.OBSERVER_ELEVATION, OBSERVER_ELEVATION);
        map.put(UserParameter.CENTER_AZIMUTH, CENTER_AZIMUTH);
        map.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, HORIZONTAL_FIELD_OF_VIEW);
        map.put(UserParameter.MAX_DISTANCE, MAX_DISTANCE);
        map.put(UserParameter.WIDTH, WIDTH);
        map.put(UserParameter.HEIGHT, HEIGHT);
        map.put(UserParameter.SUPER_SAMPLING_EXPONENT, SUPER_SAMPLING_EXPONENT);
        return map;
    }
	
	public int get(UserParameter parameter){
		return map.get(parameter);
	}
	
	public int observerLongitude(){ return map.get(UserParameter.OBSERVER_LONGITUDE);}
	public int observerLatitude(){ return map.get(UserParameter.OBSERVER_LATITUDE);}
	public int observerElevation(){ return map.get(UserParameter.OBSERVER_ELEVATION);}
	public int centerAzimuth(){ return map.get(UserParameter.CENTER_AZIMUTH);}
	public int horizontalFieldOfView(){ return map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);}
	public int maxDistance(){ return map.get(UserParameter.MAX_DISTANCE);}
	public int width(){ return map.get(UserParameter.WIDTH);}
	public int height(){ return map.get(UserParameter.HEIGHT);}
	public int superSamplingExponent(){ return map.get(UserParameter.SUPER_SAMPLING_EXPONENT);}
	
	public PanoramaParameters panoramaParameters(){
		return new PanoramaParameters(new GeoPoint(toRadians(observerLongitude()/10_000d), toRadians(observerLatitude()/10_000d)), observerElevation(), toRadians(centerAzimuth()), toRadians(horizontalFieldOfView()), maxDistance()*1000, (int)Math.scalb(width(), superSamplingExponent()), (int)Math.scalb(height(), superSamplingExponent()));
	}
	
	public PanoramaParameters panoramaDisplayParameters(){
		return new PanoramaParameters(new GeoPoint(toRadians(observerLongitude()/10_000d), toRadians(observerLatitude()/10_000d)), observerElevation(), toRadians(centerAzimuth()), toRadians(horizontalFieldOfView()), maxDistance()*1000, width(), height());
	}
	
	@Override
	public boolean equals(Object thatO){
		if(!(thatO instanceof PanoramaUserParameters))
			return false;
		PanoramaUserParameters that = (PanoramaUserParameters)thatO;
		return this.map.equals(that.map);
	}
	
	@Override
	public int hashCode(){
		return map.hashCode();
	}
}
