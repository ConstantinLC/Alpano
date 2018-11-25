package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.*;

public interface PanoramaRenderer {

	public static WritableImage renderPanorama(Panorama panorama, ImagePainter painter){
		
		WritableImage image = new WritableImage(panorama.parameters().width(), panorama.parameters().height());
		PixelWriter pixelWriter = image.getPixelWriter();
		
		for(int y = 0; y < image.getHeight(); ++y){
			for(int x = 0; x < image.getWidth(); ++x){
				pixelWriter.setColor(x, y, painter.colorAt(x, y));
			}
		}
		return image;
	}
}
