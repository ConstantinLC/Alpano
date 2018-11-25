package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 *         Représente un MNT discret.
 */
public interface DiscreteElevationModel extends AutoCloseable {

	/**
	 * Le nombre d'échantillons par degré d'un MNT discret, soit 3600.
	 */
	public final static int SAMPLES_PER_DEGREE = 3600;

	/**
	 * Le nombre d'échantillons par radian d'un MNT discret.
	 */
	public final static double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * (180 / Math.PI);

	/**
	 * Retourne l'index correspondant à l'angle donné, exprimé en radians ; cet
	 * angle peut représenter soit une longitude, soit une latitude.
	 * 
	 * @param angle
	 * @return l'index correspondant à l'angle donné
	 */
	public static double sampleIndex(double angle) {
		return angle * SAMPLES_PER_RADIAN;
	}

	/**
	 * Retourne l'étendue du MNT.
	 * 
	 * @return l'étendue du MNT
	 */
	public Interval2D extent();

	/**
	 * Retourne l'échantillon d'altitude à l'index donné, en mètres, ou lève
	 * l'exception IllegalArgumentException si l'index ne fait pas partie de
	 * l'étendue du MNT.
	 * 
	 * @param x
	 * @param y
	 * @return l'échantillon d'altitude à l'index donné
	 * @throws IllegalArgumentException
	 */
	public double elevationSample(int x, int y);

	/**
	 * Retourne un MNT discret représentant l'union du récepteur et de
	 * l'argument that, ou lève l'exception IllegalArgumentException si leurs
	 * étendues ne sont pas unionables.
	 * 
	 * @param that
	 * @return un MNT discret représentant l'union du récepteur et de l'argument
	 *         that
	 * @throws IllegalArgumentException
	 */
	public default DiscreteElevationModel union(DiscreteElevationModel that) {
		checkArgument(this.extent().isUnionableWith(that.extent()), "Étendues non unionables");
		return new CompositeDiscreteElevationModel(this, that);
	}
}
