package ch.epfl.alpano.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import static java.lang.Math.toDegrees;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public final class Alpano extends Application {

	private static final int PREF_COLUMN_ALT_ANGLE_VIS = 3;
	private static final int PREF_COLUMN_ALT_WIDTH_HEIGHT = 4;
	private static final int FIXED_POINT_SHIFT_DEFAULT = 0;
	private static final int FIXED_POINT_SHIFT_POS = 4;
	private static final int PREF_COLUMN_POS = 7;
	private final static Pos DEFAULT_ALIGNMENT = Pos.BASELINE_RIGHT;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, IOException {

		@SuppressWarnings("resource")
		DiscreteElevationModel h1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
		DiscreteElevationModel h2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
		DiscreteElevationModel h3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
		DiscreteElevationModel h4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
		@SuppressWarnings("resource")
		DiscreteElevationModel h5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
		DiscreteElevationModel h6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
		DiscreteElevationModel h7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
		DiscreteElevationModel h8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));

		DiscreteElevationModel dem = (h1.union(h2).union(h3).union(h4)).union(h5.union(h6).union(h7).union(h8));

		List<Summit> summits = GazetteerParser.readSummitsFrom(new File("alps.txt"));

		PanoramaParametersBean ppBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
		PanoramaComputerBean pcBean = new PanoramaComputerBean(new ContinuousElevationModel(dem), summits);

		// … création de l'interface graphique
		Pane panoPane = new StackPane();
		ScrollPane panoScrollPane = new ScrollPane();
		Pane panoGroup = new StackPane();

		// PanoView
		ImageView panoView = new ImageView();
		panoView.setPreserveRatio(true);
		panoView.fitWidthProperty().bind(ppBean.widthProperty());
		panoView.imageProperty().bind(pcBean.imageProperty());
		panoView.setSmooth(true);

		// LabelsPane
		Pane labelsPane = new Pane();
		labelsPane.prefWidthProperty().bind(ppBean.widthProperty());
		labelsPane.prefHeightProperty().bind(ppBean.heightProperty());
		labelsPane.setMouseTransparent(true);
		Bindings.bindContent(labelsPane.getChildren(), pcBean.getLabels());

		// UpdateNotice
		Pane updateNotice = new StackPane();
		updateNotice.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		updateNotice.setOpacity(0.9);
		updateNotice.visibleProperty().bind(ppBean.parametersProperty().isNotEqualTo(pcBean.parametersProperty()));
		updateNotice.setOnMouseClicked(e -> {
			pcBean.setParameters(ppBean.parametersProperty().get());
		});

		// UpdateText
		Text updateText = new Text("Les paramètres du panorama ont changé. Cliquez ici pour mettre le dessin à jour.");
		updateText.setFont(new Font(40));
		updateText.setTextAlignment(TextAlignment.CENTER);

		// Setup de PanoPane
		panoGroup.getChildren().addAll(panoView, labelsPane);
		panoScrollPane.setContent(panoGroup);
		updateNotice.getChildren().add(updateText);
		panoPane.getChildren().addAll(panoScrollPane, updateNotice);

		// ParamsGrid
		GridPane paramsGrid = new GridPane();

		TextField latitude = createTextField(PREF_COLUMN_POS, FIXED_POINT_SHIFT_POS, ppBean.observerLatitudeProperty());
		TextField longitude = createTextField(PREF_COLUMN_POS, FIXED_POINT_SHIFT_POS, ppBean.observerLongitudeProperty());
		TextField altitude = createTextField(PREF_COLUMN_ALT_WIDTH_HEIGHT, FIXED_POINT_SHIFT_DEFAULT, ppBean.observerElevationProperty());
		TextField azimuth = createTextField(PREF_COLUMN_ALT_ANGLE_VIS, FIXED_POINT_SHIFT_DEFAULT, ppBean.centerAzimuthProperty());
		TextField angle = createTextField(PREF_COLUMN_ALT_ANGLE_VIS, FIXED_POINT_SHIFT_DEFAULT, ppBean.horizontalFieldOfViewProperty());
		TextField visibility = createTextField(PREF_COLUMN_ALT_ANGLE_VIS, FIXED_POINT_SHIFT_DEFAULT, ppBean.maxDistanceProperty());
		TextField width = createTextField(PREF_COLUMN_ALT_WIDTH_HEIGHT, FIXED_POINT_SHIFT_DEFAULT, ppBean.widthProperty());
		TextField height = createTextField(PREF_COLUMN_ALT_WIDTH_HEIGHT, FIXED_POINT_SHIFT_DEFAULT, ppBean.heightProperty());

		ChoiceBox<Integer> box = new ChoiceBox<>();
		StringConverter<Integer> fChoice = new LabeledListStringConverter("non", "2x", "4x");
		box.setItems(FXCollections.observableList(Arrays.asList(0, 1, 2)));
		box.setConverter(fChoice);
		Bindings.bindBidirectional(box.valueProperty(), ppBean.superSamplingExponentProperty());

		TextArea textField = new TextArea();
		textField.editableProperty().setValue(false);
		textField.prefRowCountProperty().setValue(2);

		paramsGrid.addRow(0, new Label("Latitude(°)"), latitude, new Label("Longitude(°)"), longitude,
				new Label("Altitude (m)"), altitude);
		paramsGrid.addRow(1, new Label("Azimuth(°)"), azimuth, new Label("Angle de vue(°)"), angle,
				new Label("Visibilité(km)"), visibility);
		paramsGrid.addRow(2, new Label("Largeur(px)"), width, new Label("Hauteur(px)"), height,
				new Label("Suréchantillonage"), box);
		paramsGrid.add(textField, 7, 0, 20, 3);

		panoView.setOnMouseMoved(e -> { // sdfsdf
			int x = (int) Math.scalb(e.getX(), ppBean.superSamplingExponentProperty().get());
			int y = (int) Math.scalb(e.getY(), ppBean.superSamplingExponentProperty().get());
			PanoramaParameters pp = pcBean.getParameters().panoramaParameters();
			double d_longitude = Math.toDegrees(pcBean.getPanorama().longitudeAt(x, y));
			double d_latitude = Math.toDegrees(pcBean.getPanorama().latitudeAt(x, y));
			double d_distance = pcBean.getPanorama().distanceAt(x, y) / 1_000;
			double d_altitude = pcBean.getPanorama().elevationAt(x, y);
			double d_azimutRad = pp.azimuthForX(x);
			double d_azimut = Math.toDegrees(d_azimutRad);
			double d_elevation = Math.toDegrees(pp.altitudeForY(y));
			String octant = Azimuth.toOctantString(d_azimutRad, "N", "E", "S", "W");

			textField.textProperty()
					.set(String.format((Locale) null,
							"Position : %.4f°N %.4f°E%nDistance : %.1f km%nAltitude : %f m%nAzimut : %.1f° (%s)		Elévation : %.1f°",
							d_longitude, d_latitude, d_distance, d_altitude, d_azimut, octant, d_elevation));

		});
		panoView.setOnMouseClicked(e -> {
			int x = (int) Math.scalb(e.getX(), ppBean.superSamplingExponentProperty().get());
			int y = (int) Math.scalb(e.getY(), ppBean.superSamplingExponentProperty().get());
			double d_latitude = toDegrees(pcBean.getPanorama().latitudeAt(x, y));
			double d_longitude = toDegrees(pcBean.getPanorama().longitudeAt(x, y));

			String qy = String.format((Locale) null, "mlat=%.2f&mlon=%.2f", d_latitude, d_longitude);
			String fg = String.format((Locale) null, "map)15/%.2f/%.2f", d_latitude, d_longitude);
			;
			try {
				URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				java.awt.Desktop.getDesktop().browse(osmURI);
			} catch (IOException er) {
				er.printStackTrace();
			} catch (URISyntaxException er) {
				er.printStackTrace();
			}
		});

		BorderPane root = new BorderPane(panoPane, null, null, paramsGrid, null);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Alpano");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private <T> TextField createTextField(int prefColumnCount, int fixedShift, ObjectProperty<Integer> property) {
		TextField tf = new TextField();
		TextFormatter<Integer> f = new TextFormatter<>(new FixedPointStringConverter(fixedShift));
		tf.setTextFormatter(f);
		tf.setAlignment(DEFAULT_ALIGNMENT);
		tf.setPrefColumnCount(prefColumnCount);
		Bindings.bindBidirectional(f.valueProperty(), property);
		return tf;
	}
}