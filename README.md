# Aplikacja kokpitu sterowniczego

Projekt aplikacji kokpitu sterowniczego, realizowany w Zespole Platformy Mobilnej w Instytucie Automatyki i Informatyki Stosowanej Politechniki Warszawskiej.

## Licencja

Aplikacja podlega warunkom licencji MIT License (https://choosealicense.com/licenses/mit/).  
Treść licencji i jej warunki zostały opisane w pliku LICENSE.txt (https://github.com/pkacperski/mobile-platform-repo/blob/develop/LICENSE.txt).

## Wymagania do uruchomienia aplikacji

Przed uruchomieniem aplikacji należy pobrać i zainstalować:  
- Java Development Kit (JDK) w wersji 13 lub wyższej - https://www.oracle.com/java/technologies/downloads/
- Maven w wersji 3 lub wyższej - https://maven.apache.org/download.cgi

a następnie dodać oba foldery zawierające pliki wykonywalne (dla systemu Windows np. `C:\Program Files\Java\jdk-13.0.2\bin` i `C:\Program Files\apache-maven-3.8.2\bin`) do zmiennej środowiskowej `PATH`.  
Do klonowania repozytorium w wierszu poleceń konieczne jest zainstalowanie systemu kontroli wersji Git: https://git-scm.com/downloads

### Uruchomienie aplikacji w trybie deweloperskim (testowym)

1. Sklonowanie repozytorium do wybranego folderu   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`
2. Przejście do głównego folderu projektu  
`cd mobile-platform-repo`
3. Uruchomienie aplikacji backendowej  
`cd mobileplatform-backend`  
`mvn spring-boot:run`
4. Uruchomienie aplikacji frontendowej (w nowej karcie/nowym oknie terminala)  
`cd mobileplatform-frontend`  
`mvn clean install`  
`java -jar .\target\mobileplatform-frontend-1.0-SNAPSHOT.jar`

### Uruchomienie aplikacji w rzeczywistym środowisku w trybie 'produkcyjnym'

1. Pobranie i instalacja PostgreSQL: https://www.postgresql.org/download/, a następnie dodanie folderu `bin` do zmiennej `PATH`  
2. Utworzenie pustej bazy danych PostgreSQL (np. w programie DBeaver https://dbeaver.io/download/)  
Należy uruchomić program DBeaver i dodać nowe połączenie z bazą danych wybierając kolejno:  
`File -> New -> DBeaver -> Database Connection -> Next -> PostgreSQL -> Next`  
W polu `database` należy podać nazwę `mobileplatform`.  
W polu `password` należy wpisać wybrane hasło do bazy danych i następnie to samo hasło ustawić w pliku `application.properties` w foilderze `mobileplatform-backend\src\main\resources\`. Reszta ustawień pozostaje niezmieniona.  
Należy utworzyć bazę klikając `Finish`.  
3. Sklonowanie repozytorium do wybranego folderu   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`  
4. Przejście do głównego folderu projektu  
`cd mobile-platform-repo`  
5. Przejście na branch 'production'  
`git checkout production`
6. Uruchomienie aplikacji backendowej i frontendowej - tak samo jak w krokach 3, 4 w trybie deweloperskim.

**Przed uruchomieniem aplikacji warto upewnić się, że żaden proces nie zajmuje portów o numerach od 8080 do 8087.**  
Porty o tych numerach muszą być wolne dla poprawnego działania aplikacji.
