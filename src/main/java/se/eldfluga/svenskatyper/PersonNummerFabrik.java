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
 * Fabriksklass f�r att skapa personnummerinstanser.
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
	 * @param pnr En str�ng som representerar det personnnummer som ska skapas.
	 * Ska vara p� n�got av formaten:
	 * <ul>
	 * <li>yyyymmdd-xxxx</li>
	 * <li>yyyymmddxxxx</li>		
	 * <li>yymmdd-xxxx (f�r personer �ldre �n hundra �r ska yymmdd+xxxx anv�ndas)</li>
	 * <li>yymmddxxxx</li>
	 * </ul>		
	 * @return
	 * @throws FelaktigtPersonnummerException om argumentet inneh�ller en str�ng som
	 * inte �r ett personnummer.
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
	 * Ber�kna korrekt kontrollsiffra f�r ett personnummer. Metoden
	 * f�ruts�tter att det finns n�got slags kontrollsiffra p� det inskickade 
	 * numret men returnerar den siffra som �r den r�tta.
	 * 
	 * @param pnr Ett nummer p� n�got av de formaten som �r beskrivna i 
	 * metoden skapas dokumentation.
	 * @return Den kontrollsiffra som �r korrekt f�r personnumret.
	 * @throws FelaktigtPersonnummerException om argumentet inneh�ler en str�ng
	 * som inte �r ett personnummer.
	 */
	public static int beraknaKontrollSiffra(String pnr) throws FelaktigtPersonnummerException
	{
		String normaliseratNummer = normaliseraNummer(pnr);
		return beraknaKontrollSiffraInternal(normaliseratNummer);
	}



	/**
	 * Hj�lpmetod f�r att normalisera ett nummer till det format som
	 * vi anv�nder internt - 12 tecken. Det inkluderar sekelsiffra men
	 * inte skiljetecknet.
	 * 
	 * @param pnr Det personnummer som ska normaliseras.
	 * @return Det normaliserade numret.
	 * @throws FelaktigtPersonnummerException om str�ngen inte matchar 
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
	 * Ta bort skiljetecken fr�n ett personnummer. Det 5:e sista tecknet tas
	 * bort och resultatet returneras. 
	 */
	private static String taBortSkiljetecken(String pnr) 
	{
		return pnr.substring(0, pnr.length() - 5) 
			+ pnr.substring(pnr.length() - 4);
	}

	/**
	 * L�gger till sekelsiffra till ett personnummer som saknar detta. Metoden 
	 * f�ruts�tter att numret �r yngre �n hundra �r.
	 */
	private static String laggTillSekelsiffra(String pnr) 
	{
		return laggTillSekelsiffra(pnr, false);
	}

	/**
	 * L�gger till sekelsiffra till ett personnummer som saknar detta. Om numret
	 * �r �ldre �n hundra �r kan detta anges med true i det andra argumentet.
	 */
	private static String laggTillSekelsiffra(String pnr, boolean hundraArEllerMer) 
	{
		try 
		{
			// �r pnr ett organisationsnummer
			char tredjeTecknet = pnr.charAt(2);
			boolean arOrganisationsNummer = Integer.parseInt(""+tredjeTecknet) >= 2;
			if(arOrganisationsNummer) return "16" + pnr;
			
			// annars ber�kna f�r personnummer
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
	 * Metod som kontrollerar att kontrollsiffran �r korrekt.
	 */
	private boolean kontrollSiffranKorrekt(String pnr) 
	{
		int kontrollSiffra = beraknaKontrollSiffraInternal(pnr);
		char sistaTecknet = pnr.charAt(pnr.length() -1);
		return kontrollSiffra == Integer.parseInt("" + sistaTecknet);
	}

	/**
	 * Hj�lpmetod som ber�knar kontrollsiffran f�r ett personnummer.
	 */
	private static int beraknaKontrollSiffraInternal(String pnr) 
	{
		int sum = 0;
		
		// loopa inte �ver sekelsiffra och kontrollsiffran
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
