# Käyttöohje

Lataa [uusimman releasen jar-tiedosto](https://github.com/ArktinenKarpalo/otm-harjoitustyo/releases)

## Käynnistäminen
Suorita komento 
```java -jar <uusimman releasen .jar tiedoston nimi>``` ilman <>

## Käyttäminen
Peli avautuu valikkoon josta voi valita pelattavan tason.
Valittua tasoa voi vaihtaa nuolinäppäimillä, kun valinta on tehty, käynnistyy taso painamalla Enter-näppäintä.

Painele D, F, J, K näppäimiä oikeisiin aikoihin saadaksesi pisteitä. Oikea aika selviää alhaalta ylös liikkuvista laatikoista, pienten laatikoiden kohdalla riittää painaa näppäin alas, mutta pidempien laatikoiden tapauksessa täytyy näppäintä pitää pohjassa koko laatikon ajan ja nostaa laatikon ollessa ohi. Oikea aika on, kun laatikko on neliön kohdalla.



Vasemmalla ylhäällä näkyy tämänhetkiset pisteet.
Oikealla ylhäällä näkyy combo-kerroin, combo-kerroin kasvaa, jos saa painettua peräkkäin riittävän tarkasti oikeita näppäimiä.

Pisteitä saa seuraavasti:

|Tarkkuus|Pisteet|
|---|---|
|50ms|300|
|100ms|250|
|150ms|150|
|200ms|100|
|350ms|50|
|\>350ms|0|

Kaikki pisteet kerrotaan senhetkisellä combo-kertoimella.

Kun pelatava taso on ohi, pääsee kirjoittamaan oman nimimerkin ja tämän jälkeen näkemään pistetaulukon kaikista pelikerroista. Painamalla Enter-näppäintä pistetaulukossa pääsee takaisin tasonvalintavalikkoon.

Esc-näppäintä painamalla peli sulkeutuu.