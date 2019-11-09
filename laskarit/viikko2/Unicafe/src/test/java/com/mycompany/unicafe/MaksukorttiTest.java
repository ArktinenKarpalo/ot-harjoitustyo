package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void saldoOikeinAlussa() {
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void saldoKasvaa() {
        kortti.lataaRahaa(20);
        assertEquals(30, kortti.saldo());
    }

    @Test
    public void saldoVäheneeSaldo() {
        kortti.otaRahaa(10);
        assertEquals(0, kortti.saldo());
    }

    @Test
    public void otaLiikaarahaaSaldo() {
        kortti.otaRahaa(1000);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void saldoVäheneePalautus() {
        assertEquals(true, kortti.otaRahaa(10));
    }

    @Test
    public void otaLiikaarahaaPalautus() {
        assertEquals(false, kortti.otaRahaa(1000));
    }

    @Test
    public void toStringToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
