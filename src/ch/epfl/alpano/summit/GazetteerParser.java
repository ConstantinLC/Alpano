package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import ch.epfl.alpano.GeoPoint;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 * Représente un lecteur de fichier décrivant des sommets.
 */
public final class GazetteerParser {

	private GazetteerParser() {}

	/**
	 * Retourne un tableau dynamique non modifiable contenant les sommets lus
	 * depuis le fichier file, ou lève l'exception IOException en cas d'erreur
	 * d'entrée/sortie ou si une ligne du fichier n'obéit pas au format spécifié
	 * sur http://viewfinderpanoramas.org/ALPSDOC.txt.
	 * 
	 * @param file
	 * @return tableau dynamique non modifiable contenant les sommets lus depuis
	 *         le fichier
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<Summit> readSummitsFrom(File file) throws FileNotFoundException, IOException {
		List<Summit> summits = new ArrayList<Summit>();

		try (BufferedReader buffer = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
			String line;
			while ((line = buffer.readLine()) != null) {

				double longitude = toRadians(line.substring(0, 10).trim().split(":"));
				double latitude = toRadians(line.substring(10, 20).trim().split(":"));
				int elevation = Integer.parseInt(line.substring(20, 25).trim());
				String name = line.substring(36).trim();

				summits.add(
						new Summit(name, new GeoPoint(Math.toRadians(longitude), Math.toRadians(latitude)), elevation));

			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (NumberFormatException e) {
			throw new IOException();
		} catch (PatternSyntaxException e) {
			throw new IOException();
		} catch (IndexOutOfBoundsException e) {
			throw new IOException();
		}
		return Collections.unmodifiableList(summits);
	}

	private static double toRadians(String[] angle) {
		assert (angle.length == 3);

		return Double.parseDouble(angle[0]) + Double.parseDouble(angle[1]) / 60.0
				+ Double.parseDouble(angle[2]) / 3600.0;
	}
}
