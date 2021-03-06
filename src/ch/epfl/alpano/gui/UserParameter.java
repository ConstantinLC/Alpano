package ch.epfl.alpano.gui;

public enum UserParameter {
	OBSERVER_LONGITUDE (60000, 120000),
	OBSERVER_LATITUDE (450000, 480000),
	OBSERVER_ELEVATION (300, 10000),
	CENTER_AZIMUTH (0, 359),
	HORIZONTAL_FIELD_OF_VIEW (1, 360),
	MAX_DISTANCE (10, 600),
	WIDTH (30, 16000),
	HEIGHT (10, 4000),
	SUPER_SAMPLING_EXPONENT (0, 2);
	
	private final int min;
	private final int max;
	
	UserParameter(int min, int max){
		this.min = min;
		this.max = max;
	}
	
	public int sanitize(int value){
		if(value > max)
			return max;
		if(value < min)
			return min;
		return value;
	}
}
