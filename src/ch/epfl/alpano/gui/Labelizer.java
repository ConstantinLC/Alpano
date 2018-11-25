package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static java.lang.Math.abs;
import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class Labelizer {

    private final ContinuousElevationModel dem;
    private final List<Summit> summits;
    
    private static final int maxHeight = 170;

    public Labelizer(ContinuousElevationModel dem, List<Summit> summits){
        this.dem = dem;
        this.summits = Collections.unmodifiableList(new ArrayList<Summit>(summits));
    }

    public List<Node> labels(PanoramaParameters parameters){
        List<NewSummit> visibleSummits = visibleSummits(parameters);

        int highestPos = 0;
        boolean firstDone = false;
        
        BitSet available = new BitSet(parameters.width());
        available.set(0, 19, false);
        available.set(parameters.width() - 20, parameters.width() -1, false);
        available.set(20, parameters.width() - 21, true);
        
        List<Node> nodes = new ArrayList<>();
        
        for (NewSummit n : visibleSummits){
            if (n.height() > maxHeight){
                BitSet right = available.get(n.rWidth(), n.rWidth() + 19);
                right.flip(0, 19);
                if (right.equals(new BitSet(20))){
                    if (!firstDone){
                        highestPos = n.rHeight() - 22;
                        firstDone = true;
                    }
                    Text t = new Text(0, 0, n.toString());
                    t.getTransforms().add(new Translate(n.width(), highestPos));
                    t.getTransforms().addAll(new Rotate(300, 0, 0));
                    nodes.add(t);
                    Line l = new Line(n.width(), highestPos + 2, n.width(), n.height());
                    nodes.add(l);
                    available.set(n.rWidth(), n.rWidth() + 19, false);
                }
            }
        }
        
        return nodes;
        
    }

    private List<NewSummit> visibleSummits(PanoramaParameters parameters){
        List<Summit> visibleSummits = new LinkedList<>(summits);
        visibleSummits.removeIf(x->!isVisibleAndAttainsSummit(x, parameters));
        
        List<NewSummit> newVisibleSummits = NewSummit.toNewSummit(visibleSummits, parameters, dem);
        
        newVisibleSummits.sort((s1,s2)->{
            int compare = Integer.compare( (int)round(s1.height()), 
                    (int)round(s2.height()));
                if(compare == 0){
                    return Double.compare(s2.height(), s1.height());
                }
            return compare;
        });
        return newVisibleSummits;
    }

    private boolean isVisibleAndAttainsSummit(Summit summit, PanoramaParameters parameters){

        double distance = parameters.observerPosition().distanceTo(summit.position());
        boolean distanceContained = distance <= parameters.maxDistance();
        if(!distanceContained)
            return false;

        double azimuth = parameters.observerPosition().azimuthTo(summit.position());
        ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), azimuth, parameters.maxDistance());
        DoubleUnaryOperator ray0 = PanoramaComputer.rayToGroundDistance(profile, 0, 0);
        double raySlope = Math.atan2(-ray0.applyAsDouble(distance)-parameters.observerElevation(), distance);

        boolean horizontallyContained = abs(azimuth - parameters.centerAzimuth()) <= parameters.horizontalFieldOfView()/2d;
        boolean verticallyContained = (abs(raySlope) <= parameters.verticalFieldOfView()/2d);
        boolean isVisible = horizontallyContained && verticallyContained && distanceContained;
        if (!isVisible){
            return false;
        }

        DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), Math.atan2(summit.elevation() -parameters.observerElevation(), distance));
        double firstInterval = firstIntervalContainingRoot(f, 0, parameters.maxDistance(), PanoramaComputer.INTERVAL);
        boolean attainsSummit = distance - 200 <= firstInterval;
        if (!attainsSummit){
            return false;
        }
        
        return true;
    }
    
    public static class NewSummit{
        
        private double height;
        private double width;
        private Summit summit;
        
        public NewSummit(Summit s, PanoramaParameters parameters, ContinuousElevationModel dem){
            
            this.summit = s;
            
            double distance = parameters.observerPosition().distanceTo(s.position());
            double azimuth = parameters.observerPosition().azimuthTo(s.position());

            this.width = parameters.xForAzimuth(azimuth);
            
            ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), azimuth, parameters.maxDistance());
            DoubleUnaryOperator ray0 = PanoramaComputer.rayToGroundDistance(profile, 0, 0);
            double raySlope = Math.atan2(-ray0.applyAsDouble(distance)-parameters.observerElevation(), distance);
            
            this.height = parameters.yForAltitude(raySlope);
            
        }
        
        public String name(){
            return summit.name();
        }
        
        public double height(){
            return height;
        }
        
        public double width(){
            return width;
        }
        
        public int rHeight(){
            return (int) (Math.round(height()));
        }
        
        public int rWidth(){
            return (int) (Math.round(width()));
        }
        
        public Summit summit(){
            return summit;
        }
        
        public String toString(){
            return name() + " (" + summit.elevation() + " m)";
        }
        
        public static List<NewSummit> toNewSummit(List<Summit> list, PanoramaParameters parameters, ContinuousElevationModel dem){
            List<NewSummit> newList = new ArrayList<>();
            for (Summit s : list){
                newList.add(new NewSummit(s, parameters, dem));
            }
            return newList;
        }
    }
}