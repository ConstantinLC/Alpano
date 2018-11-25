package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 *         Représente un sommet.
 */
public final class Summit {

	private final String name;
	private final GeoPoint position;
	private final int elevation;

	/**
	 * Construit un sommet dont le nom, la position et l'altitude sont ceux
	 * donnés, ou lève l'exception NullPointerException si le nom ou la position
	 * sont nuls.
	 * 
	 * @param name
	 * @param position
	 * @param elevation
	 * @throws NullPointerException
	 */
	public Summit(String name, GeoPoint position, int elevation) {
		this.name = requireNonNull(name);
		this.position = requireNonNull(position);
		this.elevation = elevation;
	}

	/**
	 * Retourne le nom du sommet.
	 * 
	 * @return nom du sommet
	 */
	public String name() {
		return name;
	}

	/**
	 * Retourne la position du sommet.
	 * 
	 * @return position du sommet
	 */
	public GeoPoint position() {
		return position;
	}

	/**
	 * Retourne l'élévation du sommet.
	 * 
	 * @return élévation du sommet
	 */
	public int elevation() {
		return elevation;
	}

	/**
	 * Retourne une chaîne composée, dans l'ordre, du nom du sommet, de sa
	 * position et de son altitude.
	 * 
	 * @return chaîne composée, dans l'ordre, du nom du sommet, de sa position
	 *         et de son altitude.
	 */
	@Override
	public String toString() {
		return name + " " + position + " " + elevation;
	}
}
