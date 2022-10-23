# PROX Backend

Backend service of the web-application [PROX](https://prox.aws.innovation-hub.de).

## Architectural Decisions

- References between Bounded Contexts do **always** use the ID
  - References within a BC **should** use direct references
- Communication between BCs is **always** done using Events
- We accept coupling declarative persistence logic using **JPA in our domain model**
- We make use of **use cases over god services**