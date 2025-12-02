Description
===========

Library with classes and functions for interacting with the Dataverse API.

Dataverse is an opensource web application to share, preserve, cite, explore, and analyze research data. See: <https://dataverse.org/about>{:target=_blank}. It
has several APIs that enable programmatic access. See: <https://guides.dataverse.org/en/latest/api/index.html>{:target=_blank}. This library facilitates
accessing this API from Java code.

Using the library
-----------------

To use this library in a Maven-based project, add the following to your `pom.xml`.

### 1. Declare the DANS maven repository

```xml

<repositories>
    <!-- possibly other repository declarations here ... -->
    <repository>
        <id>DANS</id>
        <releases>
            <enabled>true</enabled>
        </releases>
        <url>https://maven.dans.knaw.nl/releases/</url>
    </repository>
</repositories>
```

### 2. Include a dependency on this library

```xml

<dependency>
    <groupId>nl.knaw.dans.lib</groupId>
    <artifactId>dans-dataverse-client-lib</artifactId>
    <version>{version}</version> <!-- <=== FILL LIBRARY VERSION TO USE HERE -->
</dependency>
```