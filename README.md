# The Boring Domain Service

## Getting Started

### Prerequisites
* JDK 17+

### Installation

1. Clone the application
``git clone``

2. Build the application
```./gradlew clean build```

3. Run the application
```./gradlew bootRun```

4. Give it a quick request like
````curl http://localhost:8080/v1/github/wrapper/name/athielen````

#### Other endpoints
* http://localhost:8080/actuator/info
* http://localhost:8080/actuator/health
* http://localhost:8080/actuator/metrics

## Decisions

#### General layout
Standard controller -> service -> domain access layer architecture. Pojos and domain objects are kept in packages nearest to their "entry" into the application. Meaning the Github Rest Client has a domain package to enforce that the domain structure is origianting from Github. Similar that the Rest controller package has a domain package to enforce that's where the top level domain objects are originating from  (i.e. the request and response classes).

Goal was to keep the controller, service and integration packages very focused. So configs are kept the config folder, anything that isn't explicitly a service or rest client is kept in utils. The only problem I really have is the exceptions package because it feels too vague.

One other things is Application is kept on the the root of the main package `boringdomainservice` to immediately inform developers this is a spring application.

#### Integration tests with Wiremock

#### Almost Production Ready
* Test coverage that provides confidence around features
* Metrics capturing infra metrics and application metrics to cover the 4 golden signals (latency, traffic, error, saturation, )
* Needs logging xml or grokked out format for improved triaging
* Needs API documentation (preferably through OpenApi)

## To-Do: Things to add as improvements and refinements
* More unit tests
* Even more unit tests
* Document some SLAs (alog with SLO's and SLI's)
* Refinements: Logging with unique identifers for better tracking and triaging (example context id's, user agent)
* Refinements: Actually add Github to the health checks
* Refinements: Customize the tags that are appended
* Refinements: Customize the actuator endpoint uri to remove the actuator so as not to reveal implementation details
* Refinements: Customize the contents of Info and Health endpoints
* Refinements: Improve the pojos with lombok or getter/setters to not have public variables
* Refinements: Improved error handling for the rest clients RestTempalte
* Refinements: Improved controller advice exception mapper with a fallback and IOException entry
* Refinements: Include Circuit breakers on github client methods
* Refinements: Separate out the Config objects into Root, Metrics, Logging, etc.