package com.mycompany.unicafe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {

	Kassapaate kassa;
	Maksukortti kortti;

	@Before
	public void setUp() {
		kortti = new Maksukortti(100000);

		kassa = new Kassapaate();
	}

	@Test
	public void alkutilaOikein() {
		Assert.assertEquals(100000, kassa.kassassaRahaa());
		Assert.assertEquals(0, kassa.edullisiaLounaitaMyyty());
		Assert.assertEquals(0, kassa.maukkaitaLounaitaMyyty());
	}

	@Test
	public void syoMaukkaastiKateinenVaihtorahalla() {
		Assert.assertEquals(99600, kassa.syoMaukkaasti(100000));
		Assert.assertEquals(1, kassa.maukkaitaLounaitaMyyty());
	}

	@Test
	public void syoEdullisestiKateinenVaihtorahalla() {
		Assert.assertEquals(99760, kassa.syoEdullisesti(100000));
		Assert.assertEquals(1, kassa.edullisiaLounaitaMyyty());
	}

	@Test
	public void syoMaukkaastiKateinenEiRiita() {
		Assert.assertEquals(5, kassa.syoMaukkaasti(5));
		Assert.assertEquals(0, kassa.maukkaitaLounaitaMyyty());
	}

	@Test
	public void syoEdullisestiKateinenEiRiita() {
		Assert.assertEquals(5, kassa.syoEdullisesti(5));
		Assert.assertEquals(0, kassa.edullisiaLounaitaMyyty());
	}

	@Test
	public void syoMaukkaastiKortti() {
		Assert.assertEquals(true, kassa.syoMaukkaasti(kortti));
		Assert.assertEquals(99600, kortti.saldo());
		Assert.assertEquals(1, kassa.maukkaitaLounaitaMyyty());
		Assert.assertEquals(100000, kassa.kassassaRahaa());
	}

	@Test
	public void syoEdullisestiKortti() {
		Assert.assertEquals(true, kassa.syoEdullisesti(kortti));
		Assert.assertEquals(99760, kortti.saldo());
		Assert.assertEquals(1, kassa.edullisiaLounaitaMyyty());
		Assert.assertEquals(100000, kassa.kassassaRahaa());
	}

	@Test
	public void syoMaukkaastiKorttiEiRiita() {
		kortti.otaRahaa(99999); // Jäljellä 1
		Assert.assertEquals(false, kassa.syoMaukkaasti(kortti));
		Assert.assertEquals(1, kortti.saldo());
		Assert.assertEquals(0, kassa.maukkaitaLounaitaMyyty());
	}

	@Test
	public void syoEdullisestiKorttiEiRiita() {
		kortti.otaRahaa(99999); // Jäljellä 1
		Assert.assertEquals(false, kassa.syoEdullisesti(kortti));
		Assert.assertEquals(1, kortti.saldo());
		Assert.assertEquals(0, kassa.edullisiaLounaitaMyyty());
	}

	@Test
	public void lataaRahaa() {
		kassa.lataaRahaaKortille(kortti, 500);
		Assert.assertEquals(100500, kortti.saldo());
		Assert.assertEquals(100500, kassa.kassassaRahaa());
	}

	@Test
	public void lataaRahaaNegatiivinen() {
		kassa.lataaRahaaKortille(kortti, -500);
		Assert.assertEquals(100000, kortti.saldo());
		Assert.assertEquals(100000, kassa.kassassaRahaa());

	}
}
