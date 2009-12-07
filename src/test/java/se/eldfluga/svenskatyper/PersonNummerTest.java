package se.eldfluga.svenskatyper;

import static org.junit.Assert.*;

import org.junit.Test;

import se.eldfluga.svenskatyper.FelaktigtPersonnummerException;
import se.eldfluga.svenskatyper.PersonNummer;
import se.eldfluga.svenskatyper.PersonNummerFabrik;

public class PersonNummerTest 
{
	@Test
	public void testKorrektaPersonNummer() throws FelaktigtPersonnummerException
	{
		assertValitPnr("19720202-0330", "197202020330");
		assertValitPnr("197202020330", "197202020330");
		assertValitPnr("720202-0330", "197202020330");
		assertValitPnr("720202+0330", "187202020330");
		assertValitPnr("7202020330", "197202020330");
	}

	@Test
	public void testKorrektaOrganisationsNummer() throws FelaktigtPersonnummerException
	{
		assertValitPnr("9696950253", "169696950253");
	}
	
	@Test
	public void testFelaktigaNummer()
	{
		assertFelaktigtPnr("720202020020202020");
		assertFelaktigtPnr("sjuttiotvŒ");
		assertFelaktigtPnr("197202020331");
		assertFelaktigtPnr("720202-+0330");
	}
	
	@Test
	public void testBeraknaKontrollSiffra() throws FelaktigtPersonnummerException 
	{
		assertKontrollSiffra("19720202-0330");
		assertKontrollSiffra("197202020330");
		assertKontrollSiffra("720202-0330");
		assertKontrollSiffra("7202020330");
	}

	private void assertKontrollSiffra(String pnr) throws FelaktigtPersonnummerException
	{
		int actualKontrollSiffra = PersonNummerFabrik.beraknaKontrollSiffra(pnr);
		String sistaTecknet = pnr.substring(pnr.length() - 1);
		int expectedKontrollSiffra = Integer.parseInt(sistaTecknet);
		assertEquals(expectedKontrollSiffra, actualKontrollSiffra);
	}

	private void assertValitPnr(String pnrStr, String expected) throws FelaktigtPersonnummerException 
	{
		PersonNummer pnr = new PersonNummerFabrik().skapa(pnrStr);
		assertEquals(expected, pnr.getNummer());
	}

	private void assertFelaktigtPnr(String pnrStr) 
	{
		try
		{
			PersonNummer pnr = new PersonNummerFabrik().skapa(pnrStr);
			fail("Personnumret " + pnr + " Šr fel och borde orsaka ett undantag.");
		}
		catch(FelaktigtPersonnummerException e)
		{
			assertNotNull(e.getMessage());
			assertTrue(e.getMessage().indexOf(pnrStr) >= 0);
		}
	}
}
