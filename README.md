# User Stories - Acceptatiecriteria

## Backend:als bezoeker van de website wil ik mij als klant kunnen registreren zodat ik een portfolio voor assets kan bijhouden

- [ ] Aangeven welke gegevens ingevoerd moeten worden
- [ ] Als onderdeel van de registratie moet ook een username en password worden gekozen en vastgelegd.
- [ ] naam: conform paspoort
- [ ] geboortedatum
- [ ] burgerservicenummer
- [ ] adres
- [ ] wachtwoord opslag voldoet aan OWASP best practices
- [ ] bij succes registereren wordt correcte http status code gecommuniceerd 
- [ ] bij falen registereren wordt correcte http status code gecommuniceerd met passende foutmelding
- [ ] response in json format

## Frontend: als bezoeker van de website wil ik mij als klant kunnen registreren zodat ik een portfolio voor assets kan bijhouden

- [ ] Gebruiker krijgt duidelijke feedback over eventuele fouten bij invoervelden
- [ ] Aangeven welke gegevens ingevoerd moeten worden
- [ ] Validatie / Check of juiste gegevens worden ingevoerd.
- [ ] bij registratie krijgt de gebruiker feedback of het aan te maken wachtwoord wel of niet aan de eisen voldoet.

## Als klant wil ik de totaalwaarde van mijn portfolio kunnen inzien

- [ ] waarde in euro
- [ ] delta met interval vorige waarde
- [ ] instelbaar interval delta: default is dag, mogelijk: maand, 3 maanden, jaar, start
- [ ] levert lijst met datapunten over interval

## Als klant wil een overzichtslijst van mijn assets kunnen inzien

- [ ] waarde in euro
- [ ] huidige koers
- [ ] aantal
- [ ] delta met interval vorige waarde: default is dag, mogelijk: maand, 3 maanden, jaar, start

## Als portfoliohouder wil ik assets bij het platform kunnen kopen

- [ ] keuze asset
- [ ] hoeveelheid

## Als portfoliohouder wil ik assets aan het platform kunnen verkopen

- [ ] keuze asset
- [ ] hoeveelheid

## Als portfoliohouder wil ik een order kunnen plaatsen waarbij ik kan aangeven dat er een asset moet worden aangekocht bij een bepaalde koerswaarde

- [ ] assettype
- [ ] koerswaarde aangeven
- [ ] aantal 

## Als portfoliohouder wil ik een order kunnen plaatsen waarbij ik kan aangeven dat er een asset moet worden verkocht bij een bepaalde koerswaarde

- [ ] assettype
- [ ] koerswaarde aangeven
- [ ] aantal 

## Als portfoliohouder wil ik bij naar andere klant assets kunnen overmaken

- [ ] klant kunnen selecteren
- [ ] asset  
- [ ] aantal

## Als portfoliohouder wil ik bij naar andere klant op een ander platform assets kunnen overmaken

## Als gebruiker wil ik mijn wachtwoord opnieuw kunnen instellen wanneer ik mijn wachtwoord vergeten ben, zodat ik weer toegang tot mijn gegevens heb

gebruikers vergeten nogal eens hun wachtwoord, en dan is het handig dat er een gebruikersvriendelijke- en veilige manier is om het wachtwoord weer opnieuw in te stellen. Een veelgebruikte, gebruiksvriendelijke manier is via email.

[voorbeeld](https://www.baeldung.com/spring-email)

- [ ] reset via email 
- [ ] de periode van aanvraag tot opnieuw instellen is beperkt om misbruik, fraude tegen te gaan

## Als systeembeheerder wil ik inzicht in het aantal queries per endpoint, zodat ik in de gaten kan houden wat de belasting is per endpoint

- [ ] alleen systeembeheerder kan erbij
- [ ] totaal aantal aantal queries afgelopen interval: default sinds middernacht
- [ ] overzichtlijst aantal queries per endpoint
- [ ] totaal interval selecteerbaar
- [ ] interval per endpoints in totaal selecteerbaar

## Als systeembeheerder wil ik van een gebruiker zijn huidige inlogmogelijkheid kunnen intrekken(blokkeren), zodat bij mogelijk frauduleuze activiteiten op betreffende accounts niet mogelijk zijn

In het geval van een gecompromitteerd account frauduleuze activiteiten  dient de organisatie verdere frauduleuze activiteiten tegen te kunnen gaan. permanent en tijdelijk. Permanent leidt tot het opnieuw moeten instellen van de inloggegevens. Na vrijgave door de systeembeheerder

- [ ] Systeembeheerder kan naar een passende beheerpagina toe navigeren waar deze een rekeninghouder kan opzoeken en zijn inlogmogelijkheid kan blokkeren/intrekken. Pagina toont huidige geblokkeerde, permanent en tijdelijke, accounts
- [ ] De functionaliteit is alleen toegankelijk voor een systeembeheerder
