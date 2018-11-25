package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 *         Représente un MNT discret obtenu d'un fichier au format HGT.
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

	private static final int sideLength = 3601;
	private static final int totalLength = 25934402;
	private static final int fileNameLength = 11;
	private ShortBuffer b2;
	private final Interval2D interval;
	private final int longitude;
	private final int latitude;

	/**
	 * Construit un MNT discret dont les échantillons proviennent du fichier HGT
	 * passé en argument, ou lève l'exception IllegalArgumentException si le nom
	 * du fichier est invalide ou si sa longueur n'est pas celle attendue
	 * 
	 * @param file
	 * @throws IllegalArgumentException
	 */
	public HgtDiscreteElevationModel(File file){
		checkArgument(file.length() == totalLength);
		String s = file.getName();
		checkArgument(s.length() == fileNameLength);
		checkArgument(s.charAt(0) == 'N' || s.charAt(0) == 'S');
		int s13 = Integer.parseInt(s.substring(1, 3));
		checkArgument(s13 >= 0 && s13 <= 99);
		checkArgument(s.charAt(3) == 'E' || s.charAt(3) == 'W');
		int s47 = Integer.parseInt(s.substring(4, 7));
		checkArgument(s47 >= 0 && s47 <= 999);
		checkArgument(s.substring(7, 11).equals(".hgt"));

		try (FileInputStream stream = new FileInputStream(file)) {
			FileChannel channel = stream.getChannel();
			ByteBuffer b1 = channel.map(MapMode.READ_ONLY, 0, file.length());
			b2 = b1.asShortBuffer();
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		
		latitude = s.charAt(0) == 'N' ? s13 : -s13;
		longitude = s.charAt(3) == 'E' ? s47 : -s47;
		
		Interval1D i1 = new Interval1D(getLongitude() * SAMPLES_PER_DEGREE, (getLongitude() + 1) * SAMPLES_PER_DEGREE);
		Interval1D i2 = new Interval1D(getLatitude() * SAMPLES_PER_DEGREE, (getLatitude() + 1) * SAMPLES_PER_DEGREE);
		this.interval = new Interval2D(i1, i2);
		

	}

	/**
	 * Retourne l'échantillon d'altitude à l'index donné, en mètres, ou lève
	 * l'exception IllegalArgumentException si l'index ne fait pas partie de
	 * l'étendue du HGT discret.
	 * 
	 * @param x
	 * @param y
	 * @return l'échantillon d'altitude à l'index donné
	 * @throws IllegalArgumentException
	 */
	public double elevationSample(int x, int y){
		int x2 = x - (SAMPLES_PER_DEGREE * getLongitude());
		int y2 = y - (SAMPLES_PER_DEGREE * getLatitude());
		checkArgument((sideLength - y2 - 1) * sideLength + x2 <= totalLength);
		return b2.get((int) ((sideLength - y2 - 1) * sideLength + x2));
	}

	/**
	 * Libère les resources associées à un HGT discret.
	 * 
	 * @throws Exception
	 */
	@Override
	public void close() {
		b2 = null;
	}

	/**
	 * Retourne l'étendue du HGT discret
	 * 
	 * @return l'étendue du HGT discret
	 */
	public Interval2D extent() {
		return interval;
	}

	private int getLongitude() {
		return longitude;
	}

	private int getLatitude() {
		return latitude;
	}

}