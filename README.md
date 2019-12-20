# Ohjelmistotekniikka syksy 2019

---

Sovellus on rytmipeli, jossa tarkoituksena on painella näppäimistön näppäimiä rytmikkäästi ennalta määritellysti musiikin tahdissa.

## Ohjeita

Projekti sijaitsee harjoitustyo-nimisessä kansiossa, jonka sisällä seuraavat ohjeet toimivat.

### Testaus & Testiraportti
Testit voidaan suorittaa seuraavalla komennolla
```mvn test```

Tulokset löytyvät tiedostosta `target/site/jacoco/index.html`

### Jar
Sovelluksen voi paketoida jar-tiedostoon seuraavalla komennolla
```mvn package -Djavacpp.platform.custom -Djavacpp.platform.host -Djavacpp.platform.linux-x86_64 -Djavacpp.platform.windows-x86_64 -Djavacpp.platform.macosx-x86_64```
Jar-tiedosto riippuvuuksineen sijaitsee `target/Harjoitustyo-1.0-SNAPSHOT-jar-with-dependencies.jar`

### Checkstyle
Checkstyle suoritetaan seuraavalla komennolla
```mvn checkstyle:checkstyle```
Tulokset löytyvät tiedostosta `target/site/checkstyle.html`

### Javadoc
Javadoc on generoitavissa seuraavalla komennolla
```mvn javadoc:javadoc```
Tulokset löytyvät tiedostosta `target/site/apidocs/index.html`


[Vaatimusmäärittely](https://github.com/ArktinenKarpalo/otm-harjoitustyo/blob/master/harjoitustyo/docs/vaatimusmarittely.md)

[Testausdokumentti](https://github.com/ArktinenKarpalo/otm-harjoitustyo/blob/master/harjoitustyo/docs/testausdokumentti.md)

[Tuntikirjanpito](https://github.com/ArktinenKarpalo/otm-harjoitustyo/blob/master/harjoitustyo/docs/tuntikirjanpito.MD)

[Arkkitehtuuri](https://github.com/ArktinenKarpalo/otm-harjoitustyo/blob/master/harjoitustyo/docs/arkkitehtuuri.md)

[Release 1](https://github.com/ArktinenKarpalo/otm-harjoitustyo/releases/tag/v0.1)
