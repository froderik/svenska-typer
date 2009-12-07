package se.eldfluga.svenskatyper;

/**
 * Klass som representerar ett personnummer. H�r �terfinns ocks� klassmetoder
 * f�r att validera str�ngar med personnummer om man inte �r s� intresserad
 * av objekt....
 * 
 * @author Fredrik Rubensson
 */
public class PersonNummer 
{
	private String pnr;
	
	/**
	 * Konstruktor som tar en str�ng som argument.
	 * 
	 * @param pnr Ska vara p� ett av f�ljande format:
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
	 * @return Detta personnummer som en str�ng.
	 */
	public String getNummer()
	{
		return this.pnr;
	}
	
	/**
	 * @return Detta personnummer som en int utifr�n en representation
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
