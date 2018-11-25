package ch.epfl.alpano.gui;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;

import static javafx.application.Platform.*;

/**
 * Un bean JavaFX contenant les paramètres (utilisateur) du panorama.
 * @author Yralec
 *
 */
public class PanoramaParametersBean{
	private ObjectProperty<PanoramaUserParameters> parameters;
	private ObjectProperty<Integer> observerLongitude;
	private ObjectProperty<Integer> observerLatitude;
	private ObjectProperty<Integer> observerElevation;
	private ObjectProperty<Integer> centerAzimuth;
	private ObjectProperty<Integer> horizontalFieldOfView;
	private ObjectProperty<Integer> maxDistance;
	private ObjectProperty<Integer> width;
	private ObjectProperty<Integer> height;
	private ObjectProperty<Integer> superSamplingExponent;

	public PanoramaParametersBean(PanoramaUserParameters p){
		parameters = new SimpleObjectProperty<PanoramaUserParameters>(p);
		observerLongitude = new SimpleObjectProperty<Integer>(p.observerLongitude());
		observerLatitude = new SimpleObjectProperty<Integer>(p.observerLatitude());
		observerElevation = new SimpleObjectProperty<Integer>(p.observerElevation());
		centerAzimuth = new SimpleObjectProperty<Integer>(p.centerAzimuth());
		horizontalFieldOfView = new SimpleObjectProperty<Integer>(p.horizontalFieldOfView());
		maxDistance = new SimpleObjectProperty<Integer>(p.maxDistance());
		width = new SimpleObjectProperty<Integer>(p.width());
		height = new SimpleObjectProperty<Integer>(p.height());
		superSamplingExponent = new SimpleObjectProperty<Integer>(p.superSamplingExponent());
		
		ChangeListener<Integer> l = (b, o, n)->runLater(this::synchronizeParameters);
		
		observerLongitude.addListener(l);
		observerLatitude.addListener(l);
		observerElevation.addListener(l);
		centerAzimuth.addListener(l);
		horizontalFieldOfView.addListener(l);
		maxDistance.addListener(l);
		width.addListener(l);
		height.addListener(l);
		superSamplingExponent.addListener(l);
	}
	
	public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
		return parameters;
	}
	
	public ObjectProperty<Integer> observerLongitudeProperty(){return observerLongitude;}
	public ObjectProperty<Integer> observerLatitudeProperty(){return observerLatitude;}
	public ObjectProperty<Integer> observerElevationProperty(){return observerElevation;}
	public ObjectProperty<Integer> centerAzimuthProperty(){return centerAzimuth;}
	public ObjectProperty<Integer> horizontalFieldOfViewProperty(){return horizontalFieldOfView;}
	public ObjectProperty<Integer> maxDistanceProperty(){return maxDistance;}
	public ObjectProperty<Integer> widthProperty(){return width;}
	public ObjectProperty<Integer> heightProperty(){return height;}
	public ObjectProperty<Integer> superSamplingExponentProperty(){return superSamplingExponent;}

	private void synchronizeParameters(){
		PanoramaUserParameters p = new PanoramaUserParameters(observerLongitude.get(), observerLatitude.get(), observerElevation.get(), centerAzimuth.get(), horizontalFieldOfView.get(), maxDistance.get(), width.get(), height.get(), superSamplingExponent.get());
		parameters.set(p);
		observerLongitude.set(p.observerLongitude());
		observerLatitude.set(p.observerLatitude());
		observerElevation.set(p.observerElevation());
		centerAzimuth.set(p.centerAzimuth());
		horizontalFieldOfView.set(p.horizontalFieldOfView());
		maxDistance.set(p.maxDistance());
		width.set(p.width());
		height.set(p.height());
		superSamplingExponent.set(p.superSamplingExponent());
	}
}