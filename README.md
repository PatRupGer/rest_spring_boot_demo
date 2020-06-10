## Demo Projekt Rest, Java, Spring Boot, JPA, Hibernate 
Das Projekt stelt eine exemplarische Umsetzung eines RestService in Java da, welches Wechselkurse über eine Api 
abfragt. Das Projekt wurde in Java mit den Technologien Spring Boot, JPA und Hibernate umgesetze.
Zur Anwendung siehe Funktion.

Das DemoProjekt verwendet, eine MySQL-Datenbank.
Zur Vereinfachung des Setups habe ich ein Docker-Compose File beigefügt. 
Sollte das Setup jedoch manuel erfolgen, sind die folgenden Daten relevant für die Datenbank.

    Datenbank-Name: bk_db
    Datenbank-Benutzer: bkuser
    Datenbank-User-Passwort: bkpass
    
Verbindungsdetails zur Datenbank finden sich außerdem im Projekt-Verzeichnis unter `src/main/resources/application.properties` 
### Vorraussetzungen
    - docker-compose
    - mvn

### Setup
1. Start mysql Database durch Docker-Compose File im Hauptverzeichnis `docker-compose.yml`

    `docker-compose up`
    
2. Start Demo 

    `mvn spring-boot:run`

### Test
*Hinweis*:

    Die Tests der folgenden Klassen bestehen per Design nur mit externer mySQL Datenbank

    - BkSystemIntegrationTest
    - BkApplicationTest

    Für die anderen Testfälle wird eine interne Datenbank bzw. eine Mock-Datenbank verwendet

1. Start Tests

    `mvn clean test`

### Funktion

Nach dem Start der Anwendung, kann über `localhost:8080` auf die Funktionen des Service zugegriffen werden.
Zur Funktion stehen die Anfragen:
    
    `/api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}`

Diese erfragt den Wechselkurs zwischen zwei Währungen und erhält als Ergebniss
    - Wechsekurs
    - mittlerer Wchselkurs der letzten 5 Tage
    - gibt an ob der Kurs steigen, fallend, constant, oder undifiniert ist

Außerdem kann über die Anfragen 

    `/api/exchange-rate/history/daily/{yyyy}/{MM}/{dd}`
    `/api/exchange-rate/history/monthly/{yyyy}/{MM}`

Historische Ionformation erlangt werden.

Für die Umsetzung wird die API von `https://exchangeratesapi.io/` benutzt.
