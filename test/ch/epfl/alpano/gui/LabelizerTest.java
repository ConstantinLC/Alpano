package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;

public class LabelizerTest {

	/*@Test
	public void sortedSummitsTest() throws FileNotFoundException, IOException {
		
		PanoramaUserParameters x = PredefinedPanoramas.NIESEN;
		PanoramaParameters niesen = x.panoramaDisplayParameters();
		
		Labelizer labelizer = new Labelizer(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt"))), GazetteerParser.readSummitsFrom(new File("alps.txt")));
		labelizer.labels(niesen);
	}*/

}
