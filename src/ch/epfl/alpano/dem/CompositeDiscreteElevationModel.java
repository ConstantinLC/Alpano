package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 *         Représente l'union de deux modèles de terrain discrets.
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel, AutoCloseable {

	private final Interval2D interval;
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;

	/**
	 * Retourne un MNT discret représentant l'union des MNT donnés, ou lève
	 * l'exception NullPointerException si l'un de ces MNT est nul.
	 * 
	 * @param dem1
	 * @param dem2
	 * @throws NullPointerException
	 */
	public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
		this.dem1 = requireNonNull(dem1);
		this.dem2 = requireNonNull(dem2);
		interval = dem1.extent().union(dem2.extent());
	}

	/**
	 * Retourne l'étendue du MNT composé
	 * 
	 * @return l'étendue du MNT composé
	 */
	public Interval2D extent() {
		return interval;
	}

	/**
	 * Ferme le MNT composé, libère les resources associées à la composition de
	 * deux MNT discrets.
	 * 
	 * @throws Exception
	 */
	@Override
	public void close() throws Exception {
		dem1.close();
		dem2.close();
	}

	/**
	 * Retourne l'échantillon d'altitude à l'index donné, en mètres, ou lève
	 * l'exception IllegalArgumentException si l'index ne fait pas partie de
	 * l'étendue du MNT composé.
	 * 
	 * @param x
	 * @param y
	 * @return l'échantillon d'altitude à l'index donné
	 * @throws IllegalArgumentException
	 */
	@Override
	public double elevationSample(int x, int y){
		checkArgument(interval.contains(x, y));
		if (dem1.extent().contains(x, y)) {
			return dem1.elevationSample(x, y);
		} else {
			return dem2.elevationSample(x, y);
		}
	}

}