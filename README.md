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
