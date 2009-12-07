package se.eldfluga.svenskatyper;

/**
 * Denna klass representerar en validerare av ett personnummer. Den
 * kan anv�ndas f�r att identifiera varianter av personnummer som inte 
 * ryms i Skatteverkets specifikation. Instanser av klassen kan anv�ndas
 * f�r att konfigurera en personnummerfabrik
 * 
 * @author Fredrik Rubensson
 */
public interface PersonNummerValiderare 
{
	/**
	 * Validera ett personnummer. 
	 * 
	 * @param pnr Ett tolvst�lligt personnummer p� formatet yyyyMMddxxxx.
	 * @throws FelaktigtPersonnummerException Om personnumret �r felaktigt.
	 */
	public void validera(String pnr) throws FelaktigtPersonnummerException;
}
