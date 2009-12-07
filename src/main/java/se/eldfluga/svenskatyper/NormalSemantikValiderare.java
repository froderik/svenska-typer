package se.eldfluga.svenskatyper;

/**
 * Denna klass anv�nds "default" av personnummerfabriken f�r att
 * g�ra de grundl�ggande valideringarna p� personnummer. Se metoden
 * validera f�r inneb�rden av detta.
 * 
 * @author Fredrik Rubensson
 *
 */
public class NormalSemantikValiderare implements PersonNummerValiderare 
{

	/**
	 * Validerar att ett personnummer �r semantiskt korrekt. F�ljande
	 * kontroller utf�rs:
	 * <ul>
	 * <li>Att numret f�r en person inte har m�nad och dag som �r orimliga.</li>
	 * <li>Att sekel f�r en person �r 18, 19 eller 20.</li>
	 * <li>Att sekel f�r en organisation �r 16.</li>
	 * <li>Att position 5-6 f�r en organisation �r h�gre �n 
	 * </ul>
	 */
	public void validera(String pnr) throws FelaktigtPersonnummerException 
	{
		int sekel = Integer.parseInt(pnr.substring(0, 1));
		if(sekel < 16 || sekel == 17 || sekel > 20) throw new FelaktigtPersonnummerException(pnr);
		
		int manad = Integer.parseInt(pnr.substring(4, 5));
		if(manad < 1 || manad > 12) throw new FelaktigtPersonnummerException(pnr);
	}

}
