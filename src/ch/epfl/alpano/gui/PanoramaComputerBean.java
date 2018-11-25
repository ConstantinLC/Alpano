package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

public final class PanoramaComputerBean {
	private final ObjectProperty<PanoramaUserParameters> parameters;
	private final ObjectProperty<Panorama> panorama;
	private final ObjectProperty<Image> image;
	private final ObservableList<Node> labels;
	private final ObservableList<Node> unmodifiableLabels;
	private final PanoramaComputer panoramaComputer;
	private final Labelizer labelizer;
	
	public PanoramaComputerBean(ContinuousElevationModel cdem, List<Summit> summits){

		this.panoramaComputer = new PanoramaComputer(cdem);
		this.labelizer = new Labelizer(cdem, summits);
		parameters = new SimpleObjectProperty<>();
		panorama = new SimpleObjectProperty<>();
		image = new SimpleObjectProperty<>();
		labels = FXCollections.observableArrayList();
		unmodifiableLabels = FXCollections.unmodifiableObservableList(labels);
				
		this.parameters.addListener((b, o, n)->{synchronise();});
	}
	
	public ObjectProperty<PanoramaUserParameters> parametersProperty(){
		return parameters;
	}
	public PanoramaUserParameters getParameters(){
		return parameters.get();
	}
	public void setParameters(PanoramaUserParameters newParameters){
		parameters.set(newParameters);
	}
	
	public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
		return panorama;
	}
	public Panorama getPanorama(){
		return panorama.get();
	}
	
	public ReadOnlyObjectProperty<Image> imageProperty(){
		return image;
	}
	public Image getImage(){
		return image.get();
	}
	
	public ObservableList<Node> getLabels(){
		return unmodifiableLabels;
	}
	
	private void synchronise(){
		PanoramaParameters pp = getParameters().panoramaParameters();
		PanoramaParameters pdp = getParameters().panoramaDisplayParameters();
		panorama.set(panoramaComputer.computePanorama(pp));
		image.set(PanoramaRenderer.renderPanorama(getPanorama(), ImagePainter.defaultPainter(getPanorama())));
		labels.setAll(labelizer.labels(pdp));
	}
}
