package ch.epfl.alpano.dem;

import static ch.epfl.alpano.dem.DiscreteElevationModel.*;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 *         Représente un MNT continu, obtenu par interpolation d'un MNT discret.
 */
public final class ContinuousElevationModel {

	/**
	 * la distance nord-sud, valant approximativement 30.89 m.
	 */
	private static final double distance = Distance.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
	private final DiscreteElevationModel dem;

	/**
	 * Construit un MNT continu basé sur le MNT discret passé en argument, ou
	 * lève l'exception NullPointerException si celui-ci est nul.
	 * 
	 * @param dem
	 * @throws NullPointerException
	 */
	public ContinuousElevationModel(DiscreteElevationModel dem){
		this.dem = requireNonNull(dem);
	}

	/**
	 * Retourne l'altitude au point donné, en mètres, obtenue par interpolation
	 * bilinéaire de l'extension du MNT discret passé au constructeur.
	 * 
	 * @param p
	 * @return l'altitude au point donné, en mètres
	 */
	public double elevationAt(GeoPoint p) {
		int x0 = (int) Math.floor(sampleIndex(p.longitude()));
		int y0 = (int) Math.floor(sampleIndex(p.latitude()));
		int x1 = (int) Math.ceil(sampleIndex(p.longitude()));
		int y1 = (int) Math.ceil(sampleIndex(p.latitude()));
		return Math2.bilerp(altitudeDEM(x0, y0), altitudeDEM(x1, y0), altitudeDEM(x0, y1), altitudeDEM(x1, y1),
				sampleIndex(p.longitude()) - x0, sampleIndex(p.latitude()) - y0);
	}

	/**
	 * Retourne la pente du terrain au point donné, en radians.
	 * 
	 * @param p
	 * @return la pente du terrain au point donné, en radians
	 */
	public double slopeAt(GeoPoint p) {
		int x0 = (int) Math.floor(sampleIndex(p.longitude()));
		int y0 = (int) Math.floor(sampleIndex(p.latitude()));
		int x1 = (int) Math.ceil(sampleIndex(p.longitude()));
		int y1 = (int) Math.ceil(sampleIndex(p.latitude()));
		return Math2.bilerp(slopeDEM(x0, y0), slopeDEM(x1, y0), slopeDEM(x0, y1), slopeDEM(x1, y1),
				sampleIndex(p.longitude()) - x0, sampleIndex(p.latitude()) - y0);
	}

	private double altitudeDEM(int x, int y) {
		if (dem.extent().contains(x, y))
			return dem.elevationSample(x, y);
		else
			return 0;
	}

	private double slopeDEM(int x, int y) {
		try{
		double deltaZA = altitudeDEM(x + 1, y) - altitudeDEM(x, y);
		double deltaZB = altitudeDEM(x, y + 1) - altitudeDEM(x, y);
		return Math.acos(distance / Math.sqrt(Math2.sq(deltaZA) + Math2.sq(deltaZB) + Math2.sq(distance)));
		} catch(Exception e){
			return 0;
		}
	}
}