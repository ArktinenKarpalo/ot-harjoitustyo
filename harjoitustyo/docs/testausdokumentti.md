# Testausdokumentti

## Automaattiset testit

Pelissä on paljon asioita joita on vaikeaa, hyödytöntä, tai jopa lähes mahdotonta testata automatisoidusti. Suuri osa pelistä koostuu grafiikkaan, ääneen ja OpenGL liittyvästä koodista.

Yksikkötestejä on kuitenkin muutamalle luokalle, joiden toiminnallisuus on järkevästi testattavissa: tietokannalle, tasojen lataamiseen tiedostosta sekä yleiseen tiedostojen käsittelyyn liittyviä funktioita sisältävälle luokalle.

Testauskattavuus on alla olevan kuvan mukainen niiden luokkien osilta joille on automatisoidut testit.
 ![kaavio](https://github.com/ArktinenKarpalo/otm-harjoitustyo/blob/master/harjoitustyo/docs/testaus.png)
 
## Manuaalinen testaus

Pelin toiminnallisuuksia on pyritty testaamaan manuaalisesti mahdollisimman kattavasti.

Pelin asentamista ja suorittamista Readmen ohjeilla on testattu useammassa Linux-ympäristössä sekä Windows-ympäristössä. 
 
## Laatuongelmat
 Tietokanta ei toimi jostain syystä yliopiston tietokoneilla, jos projektin kansio sijaitsee työpöydällä Z-drivessä. Tietokanta kuitenkin toimii jos kansio sijaitsee työpöydän sijaan käyttäjän AD lxhome-kansiossa, millään muulla tietokoneella ei bugia ole saatu toistettua.
 
 Odottamattomia virheilmoituksia ei toistaiseksi erikseen keskitetysti käsitellä.