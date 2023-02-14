# Mark
Mark is a family-friendly Disord-Bot for LoL fans.

## Description

Mark is based on stelar7's [R4J](https://github.com/stelar7/R4J) library and the [Discord4J](https://github.com/Discord4J) library.
League enthusiasts can display their recent match history encouraging them to evaluate and improve their own gameplay.
Mark celebrates a hot streak with you and cheers you up after a couple of losses.

To lighten up things, Mark can provide you you with some trivia about a Champion together with a random skin you might not know yet (and might wanna buy).
There are some old ones that still look great and have some hilarious splash arts.

In upcoming versions Mark's goal is to further connect a small Discord community of LoL fans.

## Getting Started

### Dependencies

* [Discord4J](https://github.com/Discord4J)
* [R4J](https://github.com/stelar7/R4J)
* [Logback](https://github.com/qos-ch/logback)
* [Google Guava](https://github.com/google/guava)

### Installing

* Create a Discord Application following [this](https://docs.discord4j.com/discord-application-tutorial)
* Create an Riot Api Key following [this](https://developer.riotgames.com/docs/portal)
* Replace the Discord token and Riot api key in *SecretFile* with your personal token and key.

### Executing program

* Run the Gradle build task then run the *shadowJar* task to create a .jar file you can execute using

```
java -jar mark-all.jar
```

## Help

Instructions and help for the R4J and Discord4J library can be found in their respective repositories

## Authors

Linus

## Version History

    * Initial Release

## License

*Mark isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing Riot Games properties. Riot Games, and all associated properties are trademarks or registered trademarks of Riot Games, Inc.*

*Mark is inspired by one of my friends 'Mark' and has nothing to do with Co-Founder, Co-Chairman, and President of Games for Riot Games, Marc Merrill.*

## Acknowledgments

* [How to create a Discord Application by Discord4J](https://docs.discord4j.com/discord-application-tutorial/)
