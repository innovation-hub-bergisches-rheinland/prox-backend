# PROX Backend

[![Docker Repository on Quay](https://quay.io/repository/innovation-hub-bergisches-rheinland/prox-backend/status "Docker Repository on Quay")](https://quay.io/repository/innovation-hub-bergisches-rheinland/prox-backend)

Backend service of the web-application [PROX](https://prox.aws.innovation-hub.de).

## Architectural Decisions

- References between modules do **always** use the ID
  - References within a module **should** use direct references
- Communication between modules **should** be done using Events. This is especially true for modifying actions.
  - It is okay to rely on a synchronous message call for read-only actions. Use a Facade inside a contract module for that.
- We accept coupling declarative persistence logic using **JPA in our domain model**
- We make use of **use cases** to **prevent god services**

## Release

Realeasing a new version is done with the [gradle-release plugin](https://github.com/researchgate/gradle-release). 
```sh
./gradlew release
```
Note that the plugin will automatically create release commits and pushes your changes.