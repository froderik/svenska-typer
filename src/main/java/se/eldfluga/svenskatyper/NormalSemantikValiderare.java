package se.eldfluga.svenskatyper;

/**
 * Denna klass används "default" av personnummerfabriken för att
 * göra de grundläggande valideringarna på personnummer. Se metoden
 * validera för innebörden av detta.
 * 
 * @author Fredrik Rubensson
 *
 */
public class NormalSemantikValiderare implements PersonNummerValiderare 
{

	/**
	 * Validerar att ett personnummer är semantiskt korrekt. Följande
	 * kontroller utförs:
	 * <ul>
	 * <li>Att numret för en person inte har månad och dag som är orimliga.</li>
	 * <li>Att sekel för en person är 18, 19 eller 20.</li>
	 * <li>Att sekel för en organisation är 16.</li>
	 * <li>Att position 5-6 för en organisation är högre än 
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
