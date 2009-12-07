package se.eldfluga.svenskatyper;

/**
 * Denna klass representerar en validerare av ett personnummer. Den
 * kan användas för att identifiera varianter av personnummer som inte 
 * ryms i Skatteverkets specifikation. Instanser av klassen kan användas
 * för att konfigurera en personnummerfabrik
 * 
 * @author Fredrik Rubensson
 */
public interface PersonNummerValiderare 
{
	/**
	 * Validera ett personnummer. 
	 * 
	 * @param pnr Ett tolvställigt personnummer på formatet yyyyMMddxxxx.
	 * @throws FelaktigtPersonnummerException Om personnumret är felaktigt.
	 */
	public void validera(String pnr) throws FelaktigtPersonnummerException;
}
