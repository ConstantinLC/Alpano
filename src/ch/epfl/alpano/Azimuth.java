package ch.epfl.alpano;

import static java.lang.Math.PI;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static ch.epfl.alpano.Math2.*;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Regroupe les méthodes permettant de manipuler des nombres
 * représentant des azimuts exprimés en radians.
 */
public interface Azimuth {

    /**
     * Retourne vrai ssi son argument est un azimut « canonique », c-à-d compris
     * dans l'intervalle [0;2π[.
     * 
     * @param azimuth
     * @return vrai si son argument est un azimut « canonique »
     */
    public static boolean isCanonical(double azimuth) {
        return (azimuth >= 0 && azimuth < PI2);
    }

    /**
     * Retourne l'azimut canonique équivalent à celui passé en argument, c-à-d
     * compris dans l'intervalle [0;2π[.
     * 
     * @param azimuth
     * @return azimut canonique
     */
    public static double canonicalize(double azimuth) {
        return (floorMod(azimuth, PI2));
    }

    /**
     * Transforme un azimut en angle mathématique, c-à-d exprimé dans le sens
     * antihoraire, ou lève l'exception IllegalArgumentException si son argument
     * n'est pas un azimut canonique.
     * 
     * @param azimuth
     * @return angle mathématique
     * @throws IllegalArgumentException
     */
    public static double toMath(double azimuth){
        checkArgument(isCanonical(azimuth));
        return (floorMod(PI2 - azimuth, PI2));
    }

    /**
     * Transforme un angle mathématique en azimut, c-à-d exprimé dans le sens
     * horaire, ou lève l'exception IllegalArgumentException si l'argument n'est
     * pas compris dans l'intervalle [0;2π[.
     * 
     * @param angle
     * @return azimut
     * @throws IllegalArgumentException
     */
    public static double fromMath(double angle){
        checkArgument(isCanonical(angle));
        return (floorMod(PI2 - angle, PI2));
    }

    /**
     * Retourne une chaîne correspondant à l'octant dans lequel se trouve
     * l'azimut donné, formée en combinant les chaînes n, e, s et w
     * correspondant aux quatre points cardinaux (resp. nord, est, sud et
     * ouest), comme décrit ci-dessous ; lève l'exception
     * IllegalArgumentException si l'azimut donnée n'est pas canonique.
     * 
     * @param azimuth
     * @param n
     * @param e
     * @param s
     * @param w
     * @return string octant
     * @throws IllegalArgumentException
     */
    public static String toOctantString(double azimuth, String n, String e,
            String s, String w){
        checkArgument(isCanonical(azimuth));
        String result = "";
        if (azimuth <= 3 * PI / 8 || azimuth >= 13 * PI / 8) {
            result += n;
        } else if (azimuth >= 5 * PI / 8 && azimuth <= 11 * PI / 8) {
            result += s;
        }
        if (azimuth >= PI / 8 && azimuth <= 7 * PI / 8) {
            result += e;
        } else if (azimuth >= 9 * PI / 8 && azimuth <= 15 * PI / 8) {
            result += w;
        }
        return result;
    }
}