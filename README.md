# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5T9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaKaIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6mwjUwFazsXJT14AAeLjoQHJYAFACUpjYnG4sHuQKedRg9lUBRcADNvDMAFIwtA-FwYgGYcEgu6VG6iepQTLZMCUH4AVQGsnkWIJKjxMm0ShU6nq9hQYCp5MmUGmcyxNMUyjUqkZxnqADEkJwYNyoEKYDpLDBefyxJgdMTgABrOUDdUwYAIbXfGAoV6kjSYIUs0V3e70lD1eVC67VUQOyo454wBQmlDAb7tXXoACirxU2AIBUBj1xpUd1V9gScwWG4zm6mAHL2Yag3khxtNKvkOvQYyumBulC98GQ5nqqfTozGWdUOfmi3zhfqxcDZrLFar6A4pltIvUdad9TQPgQCHdIgZ9wnrNUL21ZPlP15QsFzMnYvuEr9HA4+soburHpXlTXos3gbJCh8YHiP2Ab-i+-Hh-X4o6PUCjnn637Xk6dY+pCr7vte0HTsmkLjGqsxdvsKxjF+77tBA5ZoOclZLrciaVMUjYwOEThOBmEwGmhhGYdh8S4fhhEjpwpieN4fj+NA7AcjEkpwGG0hwAoMAADIQFkhSYORZT4kh9TNG0XS9AY6j5GgtGoXMjFjBsqjbFAuyLJcxG1mC8a+m8HxfL8WIIdZwK+tCsIIhAyKouimJxq5oJKcuzowAgMkypS1LaHSt6GKu-6PlCnI7npKAHvIdpTieQEwNKsqutoSoqqlphaoGeoAJJoFQxpIBeqU9EZJljjaCVZUmwUulF8jEZ6pEPAFLrTN+0BIAAXigHARlGMZyc5HU1E2TgAIy0e2nZ5gW0D1D4w3vqNE27FWNagvcClgEtq2tutubdltkK7cxB2TURZica1GVHohnWXgq2i9XeTKfeuT5yCgsEfsxv4fcKAHZcBoEQxBsVQTZMHgf92Jo99i0wCh9H6eZqxYd+rEVkTx1IXW531FRNGtqlBnMWTBEU6Yo5cZgXi+AEXgoOgMRxIkfMC9J9i+Fg8kNopVTUL6DTSGGklhu0YbdD0mmqNpwwQwdeSxjecuBQNELASNUDjfraCsAOtBYwFjIzqFMkS5+pN4egMXBYyD5sklYAQ27OEe2g6Ww-a8O5TKF5I4VypGu7+GldqlXVbVF7MTAkBJzDmXHgthIwHOC4A3FLmm3Ke3xM9U2Rig0baf5EI4ymK2WadZHSxdMCpst7Pvb7+dBVQhexz1hsj4Dg-1BwKDcC+35ByxIdh3ngH1BkMwQDQYFwZjkH9dB9RkD4R5NwmC2+nT7fU13tPUf3LXcTz-jEhe-jYDKeqSaSMAAOJoWtOdR2ylGh-xVurewaEdbm0trNG+5cQRm32hbMaVsbZBjts5YehdkA5AAVmJeLMvaTziveNqG5-aB2ZivP8wMI6VFPHlGOGN5BFQTsHHOZVdQwCqjVZAGdvxZxDnQ8O7UcEqFnPORcE8+qIN9BSKuNdpr13gfbZu-UTqXQQWdO+Pc25vRaoPHGo9WHAFLj7Ch9Q8FgAIWof4oi16RwyBYVAO87HXmMZo2K9Q4AQAXCgcA2kAA8HjtDlAsYfNGvj-HcCCQUUJaEhQRPURfYevpxhQKzAsBoGIXCdEpkbW+JRu501olktQOS8kFMfpzbmvEOAAHY3BOBQE4GIYZghwBEgANngFuQwdiuLAMvpCVSHRIHQLQLrVBVtyloQAHIMTZidRkR8lQORgAAal3tXWZajsEmyQVCVE8JEQogKL5FwTlsYF0kTAEAAy7FEJDmsCpSyBSRPIfQv2HIA6LxofhVeX1I7MN2YqeOgL0DJ3KrwtOAiOHxGETnLxdyQrFxkQfeRkJFFPVQZNFRDcDbzXSZCXuOjO4lO0YYxxIK0XIJ-PvFG8UfmUMec+FAzyKnQy8YwnKIELxhPHli700SoRoQqtIc+VlRn1EyRK6QCx-CFJIroqllFqLzLmJKpVHEn5cx4gESwc8wqbEFkgBIYBjULggGapEEAZT-zQjEZIoAdRFC7iAo2KkmgUnUj0CpMCUFwO1q2bACBgDGqgH4sKUADIVJ1bUZVCDRWDQ2WaHZMyQ3Etuamiu7k0BnK8hctEeSbkO3pTAAAVg6tAzyoVoDWOGyNlAY3QDeQqkhfVvliMoX86hidPa0rhnyqU0dwVxxVA2mFPC+Hp0Rci6Fuc6USPRdIyJ2Khp4vGgSuuRK5q3NlfovuE8ZWUoouSmly64aVrHuY2RU8rH+y5Z24dDCjA5TBUK4A7CE1Ss1CnOF-C6riu1f+1Fq6pElwfWXPNxyABCQYMgAEdUg5EJQcw9pKlpOApfWdVqZcNXog7LUh9Rv1fKBr2na2AtBkhfXMHlFD15ylo2DJ1jGmXeyiWmk+Z9UlntI1fB+p6O74YotfK9z9eJeEjeay1smVSIEDLABQ2Bw2ECth6kpXrcYKyVirNWvRjApqOb6KtwBgOFtnvPFA-wHkFDJK8SWhynY2c5HZrtj7WUvG4HgeUqgHHXvfaeaQc8PO-WRtxntedfMqYhoF4FI6P31DC7ZidwrmUxaPHFvAdjEtvqyqOmQ4WyQcZQFF0hqNeOn3XNKsTWiNVEdWf1GmTXH5AA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
