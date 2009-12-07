package se.eldfluga.svenskatyper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Fabriksklass för att skapa personnummerinstanser.
 * 
 * @author Fredrik Rubensson
 */
public class PersonNummerFabrik 
{
	private List<PersonNummerValiderare> validerare 
		= new ArrayList<PersonNummerValiderare>();
	
	public PersonNummerFabrik(PersonNummerValiderare ... validerare )
	{
		this.validerare.addAll( Arrays.asList(validerare) );
		this.validerare.add(0, new NormalSemantikValiderare());
	}

	/**
	 * Skapa ett personnummer.
	 * 
	 * @param pnr En sträng som representerar det personnnummer som ska skapas.
	 * Ska vara på något av formaten:
	 * <ul>
	 * <li>yyyymmdd-xxxx</li>
	 * <li>yyyymmddxxxx</li>		
	 * <li>yymmdd-xxxx (för personer äldre än hundra år ska yymmdd+xxxx användas)</li>
	 * <li>yymmddxxxx</li>
	 * </ul>		
	 * @return
	 * @throws FelaktigtPersonnummerException om argumentet innehåller en sträng som
	 * inte är ett personnummer.
	 */
	public PersonNummer skapa(String pnr) throws FelaktigtPersonnummerException
	{  
		String newPnr = normaliseraNummer(pnr);
		
		if(!kontrollSiffranKorrekt(newPnr))
		{
			throw new FelaktigtPersonnummerException(pnr);
		}

		return new PersonNummer(newPnr);
	}
	
	/**
	 * Beräkna korrekt kontrollsiffra för ett personnummer. Metoden
	 * förutsätter att det finns något slags kontrollsiffra på det inskickade 
	 * numret men returnerar den siffra som är den rätta.
	 * 
	 * @param pnr Ett nummer på något av de formaten som är beskrivna i 
	 * metoden skapas dokumentation.
	 * @return Den kontrollsiffra som är korrekt för personnumret.
	 * @throws FelaktigtPersonnummerException om argumentet innehåler en sträng
	 * som inte är ett personnummer.
	 */
	public static int beraknaKontrollSiffra(String pnr) throws FelaktigtPersonnummerException
	{
		String normaliseratNummer = normaliseraNummer(pnr);
		return beraknaKontrollSiffraInternal(normaliseratNummer);
	}



	/**
	 * Hjälpmetod för att normalisera ett nummer till det format som
	 * vi använder internt - 12 tecken. Det inkluderar sekelsiffra men
	 * inte skiljetecknet.
	 * 
	 * @param pnr Det personnummer som ska normaliseras.
	 * @return Det normaliserade numret.
	 * @throws FelaktigtPersonnummerException om strängen inte matchar 
	 * regexp - syntaktikt fel. 
	 */
	private static String normaliseraNummer(String pnr)
			throws FelaktigtPersonnummerException 
	{
		if(!pnr.matches("(\\d\\d)?\\d{6}(-?|\\+?)\\d{4}"))
		{
			throw new FelaktigtPersonnummerException(pnr);
		}
		
		String newPnr = pnr;
		
		switch(pnr.length())
		{
			case 10:
				newPnr = laggTillSekelsiffra(newPnr);
				break;
			case 11:
				char skiljeTecken = newPnr.charAt(6);
				boolean hundraArEllerMer = skiljeTecken == '+';
				newPnr = taBortSkiljetecken(newPnr);
				newPnr = laggTillSekelsiffra(newPnr, hundraArEllerMer);
				break;
			case 12:
				break;
			case 13:
				newPnr = taBortSkiljetecken(newPnr);
				break;
			default:
				throw new FelaktigtPersonnummerException(newPnr);
		}
		return newPnr;
	}

	/**
	 * Ta bort skiljetecken från ett personnummer. Det 5:e sista tecknet tas
	 * bort och resultatet returneras. 
	 */
	private static String taBortSkiljetecken(String pnr) 
	{
		return pnr.substring(0, pnr.length() - 5) 
			+ pnr.substring(pnr.length() - 4);
	}

	/**
	 * Lägger till sekelsiffra till ett personnummer som saknar detta. Metoden 
	 * förutsätter att numret är yngre än hundra år.
	 */
	private static String laggTillSekelsiffra(String pnr) 
	{
		return laggTillSekelsiffra(pnr, false);
	}

	/**
	 * Lägger till sekelsiffra till ett personnummer som saknar detta. Om numret
	 * är äldre än hundra år kan detta anges med true i det andra argumentet.
	 */
	private static String laggTillSekelsiffra(String pnr, boolean hundraArEllerMer) 
	{
		try 
		{
			// är pnr ett organisationsnummer
			char tredjeTecknet = pnr.charAt(2);
			boolean arOrganisationsNummer = Integer.parseInt(""+tredjeTecknet) >= 2;
			if(arOrganisationsNummer) return "16" + pnr;
			
			// annars beräkna för personnummer
			Date fodelseDag = new SimpleDateFormat("yyMMdd").parse(pnr.substring(0, 6));
			GregorianCalendar fodelseDagsCal = new GregorianCalendar();
			fodelseDagsCal.setTime(fodelseDag);
			int fodelseAr = fodelseDagsCal.get(Calendar.YEAR);
			int sekelsiffra = fodelseAr / 100;
			if(hundraArEllerMer) sekelsiffra -= 1;
			return sekelsiffra + pnr;
		} 
		catch (ParseException e) 
		{
			// won't happen so throw illegal state
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Metod som kontrollerar att kontrollsiffran är korrekt.
	 */
	private boolean kontrollSiffranKorrekt(String pnr) 
	{
		int kontrollSiffra = beraknaKontrollSiffraInternal(pnr);
		char sistaTecknet = pnr.charAt(pnr.length() -1);
		return kontrollSiffra == Integer.parseInt("" + sistaTecknet);
	}

	/**
	 * Hjälpmetod som beräknar kontrollsiffran för ett personnummer.
	 */
	private static int beraknaKontrollSiffraInternal(String pnr) 
	{
		int sum = 0;
		
		// loopa inte över sekelsiffra och kontrollsiffran
		for( int i = 2; i < pnr.length() - 1; i++)
		{
			char charAtPosition = pnr.charAt(i);
			int value = Integer.parseInt("" + charAtPosition);
			if(i % 2 == 0)
			{
				value = value * 2;
			}
			if(value >= 10 )
			{
				sum += (value % 10) + 1;
			}
			else 
			{
				sum += value;
			}
		}
		
		int rest = sum % 10;
		int kontrollSiffra = 10 - rest;
		
		if(kontrollSiffra == 10) kontrollSiffra = 0;
		return kontrollSiffra;
	}

}
