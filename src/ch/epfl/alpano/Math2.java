package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 * Contient différentes méthodes mathématiques utiles au projet.
 */
public interface Math2 {

    /**
     * vaut 2*Math.PI, soit approximativement 2π.
     */
    public final static double PI2 = Math.PI * 2;

    /**
     * Retourne x élevé au carré
     * 
     * @param x
     * @return x élevé au carré
     */
    public static double sq(double x) {
        return x * x;
    }

    /**
     * Retourne le reste de la division entière par défaut de x par y.
     * Lève IllegalArgumentException si y vaut 0.
     * 
     * @param x
     * @param y
     * @return le reste de la division entière par défaut de x par y
     * @throws IllegalArgumentException
     */
    public static double floorMod(double x, double y) {
        checkArgument(y != 0);
        return x - y * Math.floor(x / y);
    }

    /**
     * Retourne le demi sinus verse de x.
     * 
     * @param x
     * @return le demi sinus verse de x
     */
    public static double haversin(double x) {
        return sq(Math.sin(x / 2));
    }

    /**
     * Retourne la différence angulaire de a1 et a2.
     * 
     * @param a1
     * @param a2
     * @return la différence angulaire de a1 et a2
     */
    public static double angularDistance(double a1, double a2) {
        return floorMod((a2 - a1 + Math.PI), PI2) - Math.PI;
    }

    /**
     * Retourne la valeur de f(x) obtenue par interpolation linéaire, sachant
     * que f(0) vaut y0 et f(1) vaut y1 ; notez que x peut être quelconque.
     * 
     * @param y0
     * @param y1
     * @param x
     * @return la valeur de f(x) obtenue par interpolation linéaire
     */
    public static double lerp(double y0, double y1, double x) {
        return (1 - x) * y0 + x * y1;
    }

    /**
     * Retourne la valeur de f(x,y) obtenue par interpolation bilinéaire,
     * sachant que f(0,0) vaut z00, f(1,0) vaut z10, f(0,1) vaut z01 et f(1,1)
     * vaut z11 ; notez que x et y peuvent être quelconques.
     * 
     * @param z00
     * @param z10
     * @param z01
     * @param z11
     * @param x
     * @param y
     * @return la valeur de f(x,y) obtenue par interpolation bilinéaire
     */
    public static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {
        return lerp(lerp(z00, z10, x), lerp(z01, z11, x), y);
    }

    /**
     * Cherche et retourne la borne inférieure du premier intervalle de taille
     * dX contenant un zéro de la fonction f et compris entre minX et maxX ; si
     * aucun zéro n'est trouvé, retourne Double.POSITIVE_INFINITY.
     * 
     * @param f
     * @param minX
     * @param maxX
     * @param dX
     * @return la borne inférieure du premier intervalle de taille dX contenant
     *         un zéro de la fonction f et compris entre minX et maxX
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {

        double m = minX;

        while (m+dX <= maxX) {

            double f1 = f.applyAsDouble(m);
            double f2 = f.applyAsDouble(m + dX);

            if (f1 * f2 <= 0) {
                return m;
            }
            m += dX;
        }

        return Double.POSITIVE_INFINITY;
    }

    /**
     * Utilise la méthode de la dichotomie pour déterminer un intervalle compris
     * entre x1 et x2, de taille inférieure ou égale à epsilon et contenant un
     * zéro de f, et retourne sa borne inférieure ; lève l'exception
     * IllegalArgumentException si f(x1) et f(x2) sont de même signe.
     * 
     * @param f
     * @param x1
     * @param x2
     * @param epsilon
     * @return sa borne inférieure
     * @throws IllegalArgumentException
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {

        double fx1 = f.applyAsDouble(x1);
        double fx2 = f.applyAsDouble(x2);

        if (fx1 * fx2 > 0) {
            throw new IllegalArgumentException();
        }

        while (Math.abs(x2 - x1) > epsilon) {
            double xm = (x1 + x2) / 2;
            double fxm = f.applyAsDouble(xm);
            if (fxm == 0) {
                return fxm;
            } else if (fx1 * fxm < 0) {
                x2 = xm;
            } else {
                x1 = xm;
            }
        }

        return x1;

    }
}
