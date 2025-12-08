# Orne Java log management

Provides utilities for runtime management of logging configuration.

## Status

[![License][status.license.badge]][status.license]
[![Latest version][status.maven.badge]][status.maven]
[![Javadoc][status.javadoc.badge]][status.javadoc]
[![Maven site][status.site.badge]][status.site.site]

| Branch | CI Status | Quality | Coverage |
| :------------: | :-------------: | :-------------: | :-------------: |
| Main | [![Build Status][status.latest.ci.badge]][status.latest.ci] | [![Quality][status.sonar.quality.badge]][status.sonar] | [![Coverage][status.sonar.cov.badge]][status.sonar] |
| Develop | [![Build Status][status.dev.ci.badge]][status.dev.ci] | | |

## Features provided (or planned)

The library provides the following features:

*Unchecked featured are planned for 1.0.0 release*

- [ ] Logging system status API
    - [ ] Available log levels
    - [ ] Active loggers
        - [ ] Log level change
    - [ ] Active appenders
        - [ ] Dynamic appenders creation/deletion
- [ ] REST API
    - [ ] JAX-RS support
- [ ] Enterprise integration support
    - [ ] Link dynamic appenders lifecycle to HTTP Session
    - [ ] JMS based clustered instances coordination system
- [ ] Spring integration support
    - [ ] Spring web support for REST API

## Usage and further information

The binaries can be obtained from [Maven Central][status.maven] with the
`dev.orne:orne-log-management` coordinates:

```xml
<dependency>
  <groupId>dev.orne</groupId>
  <artifactId>orne-log-management</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Further information

For further information refer to the [Maven Site][site] and [Javadoc][javadoc].

[site]: https://orne-dev.github.io/java-log-management/
[javadoc]: https://javadoc.io/doc/dev.orne/orne-log-management
[status.license]: https://www.gnu.org/licenses/gpl-3.0.txt
[status.license.badge]: https://img.shields.io/github/license/orne-dev/java-log-management
[status.maven]: https://central.sonatype.com/artifact/dev.orne/orne-log-management
[status.maven.badge]: https://img.shields.io/maven-central/v/dev.orne/orne-log-management.svg?label=Maven%20Central
[status.javadoc]: https://javadoc.io/doc/dev.orne/orne-log-management
[status.javadoc.badge]: https://javadoc.io/badge2/dev.orne/orne-log-management/javadoc.svg
[status.site.badge]: https://img.shields.io/website?url=https%3A%2F%2Forne-dev.github.io%2Fjava-log-management%2F
[status.latest.ci]: https://github.com/orne-dev/java-log-management/actions/workflows/release.yml
[status.latest.ci.badge]: https://github.com/orne-dev/java-log-management/actions/workflows/release.yml/badge.svg?branch=master
[status.dev.ci]: https://github.com/orne-dev/java-log-management/actions/workflows/build.yml
[status.dev.ci.badge]: https://github.com/orne-dev/java-log-management/actions/workflows/build.yml/badge.svg?branch=develop
[status.sonar.quality.badge]: https://sonarcloud.io/api/project_badges/quality_gate?project=orne-dev_java-log-management
[status.sonar.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-log-management&metric=coverage
