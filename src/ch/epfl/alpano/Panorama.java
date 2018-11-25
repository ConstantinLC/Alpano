package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Arrays;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Représente un panorama.
 */
public final class Panorama {

	private final PanoramaParameters parameters;
	private final float[] distance;
	private final float[] longitude;
	private final float[] latitude;
	private final float[] elevation;
	private final float[] slope;

	/**
	 * Construit un panorama, prend en arguments les paramètres du panorama
	 * ainsi que cinq tableaux contenant les valeurs des échantillons pour
	 * chaque point: distance, longitude, latitude, altitude et pente. Tous ces
	 * tableaux ont une taille égale au nombre de points du panorama et les
	 * échantillons y sont stockés selon leur index linéaire.
	 * 
	 * @param parameters
	 * @param distance
	 * @param longitude
	 * @param latitude
	 * @param elevation
	 * @param slope
	 * @throws IllegalArgumentException
	 * @throws NullPointerException
	 */
	private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude, float[] latitude,
			float[] elevation, float[] slope) {
		int size = parameters.width() * parameters.height();
		checkArgument(distance.length == size);
		checkArgument(longitude.length == size);
		checkArgument(latitude.length == size);
		checkArgument(elevation.length == size);
		checkArgument(slope.length == size);

		this.parameters = parameters;
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.slope = slope;
	}

	/**
	 * Retourne les paramètres du panorama
	 * 
	 * @return les paramètres du panorama
	 */
	public PanoramaParameters parameters() {
		return parameters;
	}

	/**
	 * Retourne la distance entre l'observateur et le point d'index (x,y). Lève
	 * une IndexOutOfBoundsException si l'index n'est pas valide
	 * 
	 * @param x
	 * @param y
	 * @return la distance entre l'observateur et le point d'index (x,y).
	 * @throws IndexOutOfBoundsException
	 */
	public float distanceAt(int x, int y) {
		return elementAt(x, y, distance);
	}

	/**
	 * Retourne la longitude au point d'index (x,y) Lève une
	 * IndexOutOfBoundsException si l'index n'est pas valide
	 * 
	 * @param x
	 * @param y
	 * @return la longitude au point d'index (x,y)
	 * @throws IndexOutOfBoundsException
	 */
	public float longitudeAt(int x, int y) {
		return elementAt(x, y, longitude);
	}

	/**
	 * Retourne la latitude au point d'index (x,y) Lève une
	 * IndexOutOfBoundsException si l'index n'est pas valide
	 * 
	 * @param x
	 * @param y
	 * @return la longitude au point d'index (x,y)
	 * @throws IndexOutOfBoundsException
	 */
	public float latitudeAt(int x, int y) {
		return elementAt(x, y, latitude);
	}

	/**
	 * Retourne l'élévation au point d'index (x,y) Lève une
	 * IndexOutOfBoundsException si l'index n'est pas valide
	 * 
	 * @param x
	 * @param y
	 * @return l'élévation au point d'index (x,y)
	 * @throws IndexOutOfBoundsException
	 */
	public float elevationAt(int x, int y) {
		return elementAt(x, y, elevation);
	}

	/**
	 * Retourne la pente au point d'index (x,y) Lève une
	 * IndexOutOfBoundsException si l'index n'est pas valide
	 * 
	 * @param x
	 * @param y
	 * @return la pente au point d'index (x,y)
	 * @throws IndexOutOfBoundsException
	 */
	public float slopeAt(int x, int y) {
		return elementAt(x, y, slope);
	}

	/**
	 * Retourne la distance entre l'observateur et le point d'index (x,y) si
	 * l'index est valide Sinon, retourne la valeur par défaut, c'est-à-dire la
	 * distance d passée en paramètre
	 * 
	 * @param x
	 * @param y
	 * @param d
	 * @return la distance au point d'index (x,y) ou la distance par défaut d
	 */
	public float distanceAt(int x, int y, float d) {
		if (parameters.isValidSampleIndex(x, y))
			return distance[parameters.linearSampleIndex(x, y)];
		else
			return d;
	}
	
	private float elementAt(int x, int y, float[] list){
		if (parameters.isValidSampleIndex(x, y))
			return list[parameters.linearSampleIndex(x, y)];
		else
			throw new IndexOutOfBoundsException();
	}
	
	/**
	 * 
	 * @author Eric Wilders (276016)
	 * @author Constantin Le Cleï (272849)
	 * 
	 * Représente un bâtisseur de panorama.
	 */
	public final static class Builder {

		private boolean built = false;
		private final PanoramaParameters parameters;
		private float[] distance;
		private float[] longitude;
		private float[] latitude;
		private float[] elevation;
		private float[] slope;

		/**
		 * Construit un bâtisseur de panorama dont les paramètres sont ceux
		 * donnés, ou lève l'exception NullPointerException s'ils sont nuls.
		 * 
		 * @param parameters
		 * @throws NullPointerException
		 */
		public Builder(PanoramaParameters parameters) {
			this.parameters = parameters;
			int size = parameters.width() * parameters.height();
			distance = new float[size];
			Arrays.fill(distance, Float.POSITIVE_INFINITY);
			longitude = new float[size];
			latitude = new float[size];
			elevation = new float[size];
			slope = new float[size];
		}

		/**
		 * Définit la distance de l'index(x, y), ou lève une
		 * IllegalStateException si le bâtisseur a déjà été construit, ou lève
		 * une IndexOutOfBoundsException si l'index n'est pas valide. Retourne
		 * le bâtisseur.
		 * 
		 * @param x
		 * @param y
		 * @param distance
		 * @return le bâtisseur
		 * @throws IllegalStateException
		 * @throws IndexOutOfBoundsException
		 */
		public Builder setDistanceAt(int x, int y, float distance) {
			return setAt(x, y, distance, this.distance);
		}

		/**
		 * Définit la longitude de l'index(x, y), ou lève une
		 * IllegalStateException si le bâtisseur a déjà été construit, ou lève
		 * une IndexOutOfBoundsException si l'index n'est pas valide. Retourne
		 * le bâtisseur.
		 * 
		 * @param x
		 * @param y
		 * @param longitude
		 * @return le bâtisseur
		 * @throws IllegalStateException
		 * @throws IndexOutOfBoundsException
		 */
		public Builder setLongitudeAt(int x, int y, float longitude) {
			return setAt(x, y, longitude, this.longitude);
		}

		/**
		 * Définit la latitude de l'index(x, y), ou lève une
		 * IllegalStateException si le bâtisseur a déjà été construit, ou lève
		 * une IndexOutOfBoundsException si l'index n'est pas valide. Retourne
		 * le bâtisseur.
		 * 
		 * @param x
		 * @param y
		 * @param latitude
		 * @return le bâtisseur
		 * @throws IllegalStateException
		 * @throws IndexOutOfBoundsException
		 */
		public Builder setLatitudeAt(int x, int y, float latitude) {
			return setAt(x, y, latitude, this.latitude);
		}

		/**
		 * Définit l'élévation de l'index(x, y), ou lève une
		 * IllegalStateException si le bâtisseur a déjà été construit, ou lève
		 * une IndexOutOfBoundsException si l'index n'est pas valide. Retourne
		 * le bâtisseur.
		 * 
		 * @param x
		 * @param y
		 * @param elevation
		 * @return le bâtisseur
		 * @throws IllegalStateException
		 * @throws IndexOutOfBoundsException
		 */
		public Builder setElevationAt(int x, int y, float elevation) {
			return setAt(x, y, elevation, this.elevation);
		}

		/**
		 * Définit la pente de l'index(x, y), ou lève une IllegalStateException
		 * si le bâtisseur a déjà été construit, ou lève une
		 * IndexOutOfBoundsException si l'index n'est pas valide. Retourne le
		 * bâtisseur.
		 * 
		 * @param x
		 * @param y
		 * @param slope
		 * @return le bâtisseur
		 * @throws IllegalStateException
		 * @throws IndexOutOfBoundsException
		 */
		public Builder setSlopeAt(int x, int y, float slope) {
			return setAt(x, y, slope, this.slope);
		}

		private Builder setAt(int x, int y, float value, float[] list){
			if (built)
				throw new IllegalStateException();
			else if (parameters.isValidSampleIndex(x, y))
				list[parameters.linearSampleIndex(x, y)] = value;
			else
				throw new IndexOutOfBoundsException();
			return this;
		}
		
		/**
		 * Retourne un panorama défini par les paramètres du bâtisseur, ou lève
		 * une IllegalStateException si le bâtisseur a déjà été construit
		 * 
		 * @return le panorama défini par les paramètres du bâtisseur
		 * @throws IllegalStateException
		 */
		public Panorama build() {
			if (built)
				throw new IllegalStateException();
			else
				built = true;
			Panorama panorama = new Panorama(parameters, distance, longitude, latitude, elevation, slope);
			distance = null;
			longitude = null;
			latitude = null;
			elevation = null;
			slope = null;
			return panorama;
		}
	}
}
