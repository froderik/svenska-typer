package se.eldfluga.svenskatyper;

/**
 * Undatagsklass som representerar att ett peronnummer Šr felaktigt.
 * 
 * @author Fredrik Rubensson
 */
public class FelaktigtPersonnummerException extends Exception 
{

	public FelaktigtPersonnummerException(String pnr) 
	{
		super("Personnumret " + pnr + " Šr felaktigt");
	}

}
