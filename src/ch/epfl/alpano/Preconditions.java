package ch.epfl.alpano;

/**
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Cette classe a pour but de faciliter l'écriture de préconditions dans
 * d'autres fonctions.
 */
public interface Preconditions {

    /**
     * Lève l'exception IllegalArgumentException, sans message attaché, si son
     * argument est faux, et ne fait rien sinon.
     * 
     * @param b
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean b) {
        if (!b)
            throw new IllegalArgumentException();
    }

    /**
     * Lève l'exception IllegalArgumentException avec le message donné attaché
     * si son argument est faux, et ne fait rien sinon.
     * 
     * @param b
     * @param message
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean b, String message) {
        if (!b)
            throw new IllegalArgumentException(message);
    }
}
