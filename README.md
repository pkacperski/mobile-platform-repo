# Aplikacja kokpitu sterowniczego
Projekt aplikacji kokpitu sterowniczego, realizowany w Zespole Platformy Mobilnej w Instytucie Automatyki i Informatyki Stosowanej Politechniki Warszawskiej.

### Wymagania do uruchomienia aplikacji
- Java Development Kit (JDK) w wersji 13 lub wyższej - https://www.oracle.com/java/technologies/downloads/
- Maven w wersji 3 lub wyższej - https://maven.apache.org/download.cgi 

Do testowania działania aplikacji zalecana jest instalacja Swagger Editor - https://swagger.io/docs/open-source-tools/swagger-editor/

### Testowanie aplikacji (backendowej) w trybie deweloperskim
1. Sklonowanie repozytorium   
`git clone https://github.com/pkacperski/mobile-platform-repo.git`
2. Uruchomienie aplikacji (w głównym folderze projektu)   
`mvn spring-boot:run`
3. Testowanie usług aplikacji w przeglądarce pod adresem   
`http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/`  

### Instrukcja konfiguracji biblioteki OpenCV
Kroki konfiguracji (zgodnie z https://www.youtube.com/watch?v=vLf3ZcFotyA):
1. Download OpenCV 4.5.4 library from https://opencv.org/releases/ and extract the archive to a preferred location
2. In your IDE, add opencv-454.jar file as a dependency and attach to it a .dll file from <your_opencv_installation_folder>/build/java folder:
2.1. <your_opencv_installation_folder>/build/java/x64/opencv_454.dll if you are running on a 64-bit system
2.2. <your_opencv_installation_folder>/build/java/x86/opencv_454.dll if you are running on a 32-bit system
You should then be able to run the test with a video file on your PC.
This configuration was tested with IntelliJ IDEA 2021.2.3 under Windows 10 64-bit.

**Przed uruchomieniem aplikacji warto upewnić się, że żaden proces nie zajmuje portu 8080.**
