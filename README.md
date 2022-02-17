# Aplikacja kokpitu sterowniczego

Projekt aplikacji kokpitu sterowniczego, realizowany w Zespole Platformy Mobilnej w Instytucie Automatyki i Informatyki Stosowanej Politechniki Warszawskiej.

## Licencja

Aplikacja podlega warunkom licencji MIT License (https://choosealicense.com/licenses/mit).  
Treść licencji i jej warunki zostały opisane w pliku LICENSE.txt (https://github.com/pkacperski/mobile-platform-repo/blob/develop/LICENSE.txt).

## Wymagania do uruchomienia aplikacji

Przed uruchomieniem aplikacji należy pobrać i zainstalować:  
- Java Development Kit (JDK) w wersji 13 lub wyższej - https://www.oracle.com/java/technologies/downloads
- Maven w wersji 3 lub wyższej - https://maven.apache.org/download.cgi  
- OpenCV w wersji 4.5.1 - https://opencv.org/opencv-4-5-1

a następnie dodać foldery zawierające pliki wykonywalne (dla systemu Windows np. `C:\Program Files\Java\jdk-13.0.2\bin` i `C:\Program Files\apache-maven-3.8.2\bin`) do zmiennej środowiskowej `PATH`.

Dla biblioteki OpenCV należy dodać do zmiennej `PATH` następujące cztery foldery z folderu instalacji: `...\build\java`, `...\build\java\x64`, `...\build\x64\vc14\bin`, `...\build\x64\vc15\bin` dla 64-bitowego operacyjnego - dla systemu 32-bitowego trzy ostatnie foldery powinny zostać wybrane ze ścieżki z przedrostkiem `x86` zamiast `x64`.  
Do klonowania repozytorium w wierszu poleceń konieczne jest zainstalowanie systemu kontroli wersji Git: https://git-scm.com/downloads

### Uruchomienie aplikacji w trybie deweloperskim (testowym)

1. Sklonowanie repozytorium do wybranego folderu (lub pobranie i rozpakowanie paczki .zip z kodem)   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`
2. Przejście do głównego folderu projektu  
`cd mobile-platform-repo`
3. Uruchomienie aplikacji backendowej  
`cd mobileplatform-backend`  
`mvn spring-boot:run`
4. Uruchomienie aplikacji frontendowej (w nowej karcie/nowym oknie terminala)  
`cd mobileplatform-frontend`  
`mvn clean install`  
`java -jar target\mobileplatform-frontend-1.0-SNAPSHOT.jar`  
Następnie jest możliwe dodawanie danych testowych w narzędziu OpenAPI Swagger Editor (https://swagger.io/tools/swagger-editor/download) lub z wykorzystaniem generatora danych testowych (https://github.com/pawelzakieta97/mgr_slam).

### Uruchomienie aplikacji w rzeczywistym środowisku w trybie 'produkcyjnym'

1. Pobranie i instalacja PostgreSQL: https://www.postgresql.org/download, a następnie dodanie folderu `bin` do zmiennej `PATH`  
2. Utworzenie pustej bazy danych PostgreSQL (np. w programie DBeaver https://dbeaver.io/download)  
Należy uruchomić program DBeaver i dodać nowe połączenie z bazą danych wybierając kolejno:  
`File -> New -> DBeaver -> Database Connection -> PostgreSQL -> Next`  
W polu `password` należy wpisać wybrane hasło do bazy danych i następnie to samo hasło ustawić w pliku `application.properties` w folderze `mobileplatform-backend\src\main\resources\`. Reszta ustawień pozostaje niezmieniona. Należy utworzyć połączenie klikając `Finish`.  
Następnie należy kliknąć prawym przyciskiem myszy na nowo dodanym połączeniu i wybrać `Create -> Database`, a w polu `Database name:` wpisać `mobileplatform`.
3. Sklonowanie repozytorium do wybranego folderu (lub pobranie i rozpakowanie paczki .zip z kodem spod adresu `https://github.com/pkacperski/mobile-platform-repo/tree/production`)  
`git clone https://github.com/pkacperski/mobile-platform-repo.git`  
4. Przejście do głównego folderu projektu  
`cd mobile-platform-repo`  
5. Przejście na branch 'production'  
`git checkout production`
6. Uruchomienie aplikacji backendowej i frontendowej - tak samo jak w krokach 3, 4 w trybie deweloperskim.

**Przed uruchomieniem aplikacji warto upewnić się, że żaden proces nie zajmuje portów o numerach od 8080 do 8087.**  
Porty o tych numerach muszą być wolne dla poprawnego działania aplikacji.
