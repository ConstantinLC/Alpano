package ch.epfl.alpano.gui;


import ch.epfl.alpano.Panorama;
import javafx.scene.paint.*;

@FunctionalInterface
public interface ImagePainter {

	public Color colorAt(int x, int y);

	public static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter luminosity, ChannelPainter opacity){
		return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), luminosity.valueAt(x, y), opacity.valueAt(x, y));
	}
	
	public static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity){
		return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
	}
	
	public static ImagePainter defaultPainter(Panorama panorama){
		ChannelPainter distance = panorama::distanceAt;
		ChannelPainter slope = panorama::slopeAt;
		ChannelPainter hue = distance.div(100_000).cycling().mul(360);
		ChannelPainter saturation = distance.div(200_000).clamped().inverted();
		ChannelPainter luminosity = slope.mul(2).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
		ChannelPainter opacity = (x,y)->distance.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;
		
		return ImagePainter.hsb(hue,saturation,luminosity,opacity);
	}
}
