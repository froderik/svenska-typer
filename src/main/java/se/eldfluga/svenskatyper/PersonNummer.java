package se.eldfluga.svenskatyper;

/**
 * Klass som representerar ett personnummer. Här återfinns också klassmetoder
 * för att validera strängar med personnummer om man inte är så intresserad
 * av objekt....
 * 
 * @author Fredrik Rubensson
 */
public class PersonNummer 
{
	private String pnr;
	
	/**
	 * Konstruktor som tar en sträng som argument.
	 * 
	 * @param pnr Ska vara på ett av följande format:
	 * <ul>
	 * <li>yyyymmdd-xxxx</li>
	 * <li>yyyymmddxxxx</li>		
	 * <li>yymmdd-xxxx</li>
	 * <li>yymmddxxxx</li>
	 * </ul>		
	 */
	protected PersonNummer(String pnr)
	{
		this.pnr = pnr;
	}
	
	/**
	 * @return Detta personnummer som en sträng.
	 */
	public String getNummer()
	{
		return this.pnr;
	}
	
	/**
	 * @return Detta personnummer som en int utifrån en representation
	 * med sekelsiffra.
	 */
	public int getNummerSomTal()
	{
		return Integer.parseInt(this.pnr);
	}
	
	public String toString()
	{
		return this.pnr;
	}
}
