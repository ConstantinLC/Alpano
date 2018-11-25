package ch.epfl.alpano.gui;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import javafx.scene.image.Image;

public class ExtraTests {

	final static File HGT_FILE = new File("N46E007.hgt");
	final static int IMAGE_WIDTH = 500;
	final static int IMAGE_HEIGHT = 200;

	final static double ORIGIN_LON = toRadians(7.65);
	final static double ORIGIN_LAT = toRadians(46.73);
	final static int ELEVATION = 600;
	final static double CENTER_AZIMUTH = toRadians(180);
	final static double HORIZONTAL_FOV = toRadians(60);
	final static int MAX_DISTANCE = 100_000;

	final static PanoramaParameters PARAMS = new PanoramaParameters(new GeoPoint(ORIGIN_LON, ORIGIN_LAT), ELEVATION,
			CENTER_AZIMUTH, HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);
	
	@Test
	public void Test() throws Exception {
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);		
			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);
			ChannelPainter distance = p::distanceAt;
			ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
			ChannelPainter slope = p::slopeAt;
			ChannelPainter h = distance.div(100_000).cycling().mul(360);
			ChannelPainter s = distance.div(200000).clamped().inverted();
			ChannelPainter b = slope.mul(2).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
			ChannelPainter o = (x,y)->distance.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;
			ImagePainter c = ImagePainter.hsb(h,s,b,o);
			Image i1 = PanoramaRenderer.renderPanorama(p, c);
			Image i2 = PanoramaRenderer.renderPanorama(p, (ImagePainter.defaultPainter(p)));
			
			assertEquals(i1.getHeight(), i2.getHeight(), 0);
			assertEquals(i1.getWidth(), i2.getWidth(), 0);
			
			for(int j = 0; j < i1.getHeight(); ++j){
				for(int i = 0; i < i1.getWidth(); ++i){
					assertTrue(i1.getPixelReader().getColor(i, j).equals(i2.getPixelReader().getColor(i, j)));
				}
			}
		}
	}

}
