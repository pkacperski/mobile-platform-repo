# Steering cockpit for an autonomous vehicle

[PL] Wersja polska instrukcji poniżej

Project of the steering cockpit application, made in the Mobile Platform Team at the Institute of Control and Computation Engineering (IAiIS) of the Warsaw University of Technology.

## License

The application is subject to the terms of the [MIT License](https://choosealicense.com/licenses/mit).
The content of the license and its terms are described in the [LICENSE.txt](https://github.com/pkacperski/mobile-platform-repo/blob/develop/LICENSE.txt) file.

## Requirements to run the application

Before starting the application, install the following:
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads) version 13 or higher
- [Maven](https://maven.apache.org/download.cgi) version 3 or higher
- [OpenCV](https://opencv.org/opencv-4-5-1) version 4.5.1

and then add folders containing executable files (for Windows, e.g. `C:\Program Files\Java\jdk-13.0.2\bin` and `C:\Program Files\apache-maven-3.8.2\bin`) to the `PATH` environment variable.

For the OpenCV library, add the following four folders from the installation folder to the `PATH` variable: `...\build\java`, `...\build\java\x64`, `...\build\x64\vc14\bin`, `...\build\x64\vc15\bin` for 64-bit operating system - for 32-bit operating system the last three folders should be selected from the path with the `x86` prefix instead of `x64`.
To clone the repository in the command line, you must install the version control system [Git](https://git-scm.com/downloads).

### Running the application in development (test) mode

1. Clone the repository to the selected folder (or download and unpack the .zip package with the code)   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`
2. Go to the main project folder   
`cd mobile-platform-repo`
3. Launch the backend application   
`cd mobileplatform-backend`   
`mvn spring-boot: run`
4. Launch the frontend application (in a new tab / new terminal window)   
`cd mobileplatform-frontend`   
`mvn clean install`   
`java -jar target\mobileplatform-frontend-1.0-SNAPSHOT.jar`

Then it is possible to add test data in [OpenAPI Swagger Editor](https://swagger.io/tools/swagger-editor/download) or using [test data generator] (https://github.com/pawelzakieta97/platforma-mobile-control).

### Running the application in a real environment in 'production' mode

1. Download and install [PostgreSQL](https://www.postgresql.org/download), then add the `bin` folder to the `PATH` variable   
2. Create an empty PostgreSQL database (e.g. in [DBeaver](https://dbeaver.io/download))   
Run DBeaver and add a new connection to the database by selecting:   
`File -> New -> DBeaver -> Database Connection -> PostgreSQL -> Next`   
In the `password` field, enter the selected database password and then set the same password in the `application.properties` file in the `mobileplatform-backend\src\main\resources\` folder. The rest of the settings remain unchanged. Create a connection by clicking on 'Finish'.   
Then right-click on the newly added connection and select `Create -> Database`, and in the `Database name:` field enter `mobileplatform`.
3. Clone the repository to the selected folder (or download and unpack the .zip package with the code from the address `https://github.com/pkacperski/mobile-platform-repo/tree/production`)   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`
4. Go to the main project folder   
`cd mobile-platform-repo`
5. Move to branch 'production'   
`git checkout production`
6. Launch the backend and frontend applications - the steps are the same as steps 3, 4 in the developer mode.

**Before starting the application, please make sure that no process is occupying ports with numbers from 8080 to 8087.**
Ports with these numbers must be free for the application to function properly

# [PL] Aplikacja kokpitu sterowniczego dla pojazdu autonomicznego

Projekt aplikacji kokpitu sterowniczego, realizowany w Zespole Platformy Mobilnej w Instytucie Automatyki i Informatyki Stosowanej Politechniki Warszawskiej.

## Licencja

Aplikacja podlega warunkom licencji [MIT License](https://choosealicense.com/licenses/mit).  
Treść licencji i jej warunki zostały opisane w pliku [LICENSE.txt](https://github.com/pkacperski/mobile-platform-repo/blob/develop/LICENSE.txt).

## Wymagania do uruchomienia aplikacji

Przed uruchomieniem aplikacji należy pobrać i zainstalować:  
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads) w wersji 13 lub wyższej
- [Maven](https://maven.apache.org/download.cgi) w wersji 3 lub wyższej  
- [OpenCV](https://opencv.org/opencv-4-5-1) w wersji 4.5.1

a następnie dodać foldery zawierające pliki wykonywalne (dla systemu Windows np. `C:\Program Files\Java\jdk-13.0.2\bin` i `C:\Program Files\apache-maven-3.8.2\bin`) do zmiennej środowiskowej `PATH`.

Dla biblioteki OpenCV należy dodać do zmiennej `PATH` następujące cztery foldery z folderu instalacji: `...\build\java`, `...\build\java\x64`, `...\build\x64\vc14\bin`, `...\build\x64\vc15\bin` dla 64-bitowego operacyjnego - dla systemu 32-bitowego trzy ostatnie foldery powinny zostać wybrane ze ścieżki z przedrostkiem `x86` zamiast `x64`.  
Do klonowania repozytorium w wierszu poleceń konieczne jest zainstalowanie systemu kontroli wersji [Git](https://git-scm.com/downloads).

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
Następnie jest możliwe dodawanie danych testowych w narzędziu [OpenAPI Swagger Editor](https://swagger.io/tools/swagger-editor/download) lub z wykorzystaniem [generatora danych testowych](https://github.com/pawelzakieta97/platforma-mobilna-sterowanie).

### Uruchomienie aplikacji w rzeczywistym środowisku w trybie 'produkcyjnym'

1. Pobranie i instalacja [PostgreSQL](https://www.postgresql.org/download), a następnie dodanie folderu `bin` do zmiennej `PATH`  
2. Utworzenie pustej bazy danych PostgreSQL (np. w programie [DBeaver](https://dbeaver.io/download))  
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
