package ch.epfl.alpano;

import static java.lang.Math.toRadians;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class Etape7DrawProfile {
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

	public static void draw() throws Exception {
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

			ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p).sub(500).div(4500).clamped().inverted();

			ChannelPainter distance = p::distanceAt;
			ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

			ImagePainter l = ImagePainter.gray(gray, opacity);

			Image i = PanoramaRenderer.renderPanorama(p, l);
			ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("niesen-profile.png"));

			ChannelPainter slope = p::slopeAt;
			ChannelPainter h = distance.div(100_000).cycling().mul(360);
			ChannelPainter s = distance.div(200000).clamped().inverted();
			ChannelPainter b = slope.mul(2).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
			ChannelPainter o = (x,y)->distance.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;
			
			ImagePainter c = ImagePainter.hsb(h,s,b,o);

			Image i2 = PanoramaRenderer.renderPanorama(p, c);
			ImageIO.write(SwingFXUtils.fromFXImage(i2, null), "png", new File("niesen-shaded.png"));
			
//			System.out.println(p.distanceAt(277, 29));
//			Color c1 = i2.getPixelReader().getColor(277, 29);
//			System.out.println("actual: " + c1.getHue() +" "+ c1.getSaturation() +" "+ c1.getBrightness() +" "+ c1.getOpacity());
//			Color c2 = new Image(new FileInputStream("niesen-shadedEcho.png")).getPixelReader().getColor(277, 29);
//			System.out.println("expected: " + c2.getHue() +" "+ c2.getSaturation() +" "+ c2.getBrightness() +" "+ c2.getOpacity());

		}
	}
}