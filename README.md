<h1 align="center">MAPGUESSЯ</h1>

<p align="center">
<img width=30%" src="https://github.com/sopra-fs21-group-24/client/blob/master/public/logo.png" />
</p>

## Introduction
We created a game similair to [GeoGuessr](https://www.geoguessr.com/). The players will be randomly placed somewhere on Google maps (for example: a country, a city or a monumental place like the eiffel tower). At the beginning of the game the map won't be visible and there are some clouds over the map. The goal of the player is to brush away as little as possible of the clouds to recognize the place he is located in. The player will give his answer by placing a pin on a map that is displayed on the lower left side of the screen. The scoring will be based on points. Depending on the game mode points are computed by time, accuracy of guess and amount of clouds brushed away.

Deployed instance: [sopra-fs21-group-24-client.herokuapp.com](https://sopra-fs21-group-24-client.herokuapp.com/)

Associated Front End repository can be found here: [Client Repository](https://github.com/sopra-fs21-group-24/client)

Data & Reports regarding the project: [Data Repository](https://github.com/sopra-fs21-group-24/data)

- [Technologies](#technologies)
- [High-level components](#high-level-components)
- [Launch & Deployment](#launch-&-deployment)
- [Roadmap](#roadmap)
    - [Question Type Selection Module](#question-type-selection-module)
    - [Tinting of Satellite Image](#tinting-of-satellite-image)
- [Authors and acknowledgment](#authors-and-acknowledgment)
- [License](#license)
## Technologies
Our geography-game is a Spring application running Java, Gradle and JPA Hibernate. JPA is used for all persistence related functionalities; declaring entities and handling the repositories. 
The Groovy based buld automation tool Gradle is used from compilation, packaging to testing and deployment.
<p float="left">
<img height="200px" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/java/java.png"></img>
</p>

## High-level components
The main components of ou project are also the main components of the whole Game. 
To be able to play the game a user has to register himself. The request is sent from the Front-End to the [UserController](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/UserController.java), which creates a new [**User**](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/User.java) entity.
Whenever a user clicks on a GameMode (Multiplayer, Singleplayer), a new [**GameEntity**](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/GameEntity.java) is created.
If a player chose the Multiplayer modes, additionally to the GameEntity a [**Lobby**](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/Lobby.java) is created. The lobby(service) handles all user organization functionalities, e.g. join/leave lobby join through RoomKey. 
Whenever a user finishes a game, a new [**Leaderboard**](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/Leaderboard.java) 
entity is created with the username, score and gameMode(Clouds, Pixelation, Time).
The new entity is stored in the LeaderboardRepository.


TODO: insert a graphic
## Launch & Deployment
One special thing in our project is the API key. We stored the key as a local variable on our server, so no one can use it without our permission.
To add a new local variable follow these steps:
1. Open the Start Search, type in “env”, and choose “Edit the system environment variables”:
2. Click the “Environment Variables…” button.
3. Under the “System Variables” section (the lower half) click on new.
4. Enter for the name of the variable: apikey nad for the value our provided api key.

### Build
To build the server use:
```bash
./gradlew build
```
### Run
To run the server use:
```bash
./gradlew bootRun
```
### Test
To run all tests use:
```bash
./gradlew test
```
### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

To deploy a new release, you have to merge the working (and tested) branch into the master branch, which then gets automatically pushed on to heroku.
## Roadmap
* Add a **new endpoint** in [gameConroller](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/controller/GameController.java) to choose from different **map modes**(e.g. Switzerland, Cities, Europe, Monuments). 
  Don't forget the corresponding functions in the Service! If the data is not present in the data folder, add it, or insert a new csv file. 
  Talk to the Front-End developers for deployment.
* Check **coordinates** in the [data](https://github.com/sopra-fs21-group-24/server/tree/master/data) folder, sometimes they are a bit of. Correct them and feel free to add new ones. E.g. for new map modes.
* Tweak the **scoring function** in the [GameMode](https://github.com/sopra-fs21-group-24/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs21/entity/gamemodes/GameMode.java) function for the new map modes created in the first point.

## Authors and acknowledgment
This project was started using the following front end [template](https://github.com/HASEL-UZH/sopra-fs21-template-client) provided by the University of Zurich.
The Satellite get fetched through the GoogleMaps API.
#### Team Members
* Claudio Gebbia - [@claudioge](https://github.com/claudioge)
* Jérôme Hadorn - [@jeromehadorn](https://github.com/jeromehadorn)
* Hoàng Ben Lê Giang - [@benlegiang](https://github.com/benlegiang)
* David Diener - [@Dave5252](https://github.com/Dave5252)
* Philip Giryes - [@Pieli](https://github.com/Pieli)
## License
This project is licensed under the MIT license. Check out the License text [here](https://github.com/sopra-fs21-group-24/server/blob/master/LICENSE).
