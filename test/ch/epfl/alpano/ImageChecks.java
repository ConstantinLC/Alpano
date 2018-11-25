package ch.epfl.alpano;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import static ch.epfl.alpano.CompareImages.*;

public class ImageChecks {

	@Test
	public void ElevationProfileImage() throws Exception {
		
		DrawElevationProfile.draw();
		
		File fileA = new File("profile.png");
	    File fileB = new File("profileEcho.png");
		assertEquals(100, compareImageWithDifference(fileA, fileB, "profile"), 0);
	}
	
	@Test
	public void ElevationAndSlopeImage() throws Exception {
		
		DrawDEM.draw();
		
		File fileA = new File("elevation.png");
	    File fileB = new File("elevationEcho.png");
	    assertEquals(100, compareImageWithDifference(fileA, fileB, "elevation"), 0);
	    
	    fileA = new File("slope.png");
	    fileB = new File("slopeEcho.png");
	    assertEquals(100, compareImageWithDifference(fileA, fileB, "slope"), 0);
	}
	
	@Test
	public void DEMImage() throws Exception {
		
		DrawHgtDEM.draw();
		
		File fileA = new File("dem.png");
	    File fileB = new File("demEcho.png");
		assertEquals(100, compareImageWithDifference(fileA, fileB, "dem"), 0);
	}
	
	@Test
	public void NiesenImage() throws Exception {
		
		DrawPanorama.draw();
		
		File fileA = new File("niesen.png");
	    File fileB = new File("niesenEcho.png");
		assertEquals(100, compareImageWithDifference(fileA, fileB, "niesen"), 0);
	}
	
	@Test
	public void NiesenProfileAndShadedImage() throws Exception {
		
		Etape7DrawProfile.draw();
		
		File fileA = new File("niesen-profile.png");
	    File fileB = new File("niesen-profileEcho.png");
		assertEquals(100, compareImageWithDifference(fileA, fileB, "niesen-profile"), 0);
		
		fileA = new File("niesen-shaded.png");
	    fileB = new File("niesen-shadedEcho.png");
	    assertEquals(100, compareImageWithDifference(fileA, fileB, "niesen-shaded"), 1);
	}
}
