# Cacheable Renditions

[Avionos](https://www.avionos.com)

## Overview

The Cacheable Renditions OSGi bundle for the Adobe Experience Manager (AEM) platform provides Dispatcher-cacheable paths for asset renditions.  By default, AEM rendition paths use a suffix to specify the rendition name, which can prevent these assets from being cached in Dispatcher when there is collision between the rendition path and the base asset path.  The `CacheableRendition` implementation and servlet provided in this bundle create an alternate rendition path that uses a new extension that prevents this collision and allows the asset to be cached in Dispatcher in the same manner as the original asset.  The default rendition path and rendering behavior is not altered.

## Cacheable Rendition Path Syntax

Asset Path | Default Rendition Path | Cacheable Rendition Path
------------ | ------------- | -------------
/content/dam/avionos/images/one.png | /content/dam/avionos/images/one.png/jcr:content/renditions/cq5dam.thumbnail.48.48.png | /content/dam/avionos/images/one.png.rendition/cq5dam.thumbnail.48.48.png

## Usage

```java
// get cacheable rendition from resource
final CacheableRendition cacheableRendition = resourceResolver.getResource("/content/dam/avionos/images/one.png/jcr:content/renditions/cq5dam.thumbnail.48.48.png")
    .adaptTo(CacheableRendition.class);

assertEquals("/content/dam/avionos/images/one.png.rendition/cq5dam.thumbnail.48.48.png", cacheableRendition.getPath());
```

```java
// get cacheable rendition from rendition
final CacheableRendition cacheableRendition = resourceResolver.getResource("/content/dam/avionos/images/one.png")
    .adaptTo(Asset.class)
    .getRendition("cq5dam.thumbnail.48.48.png")
    .adaptTo(CacheableRendition.class);

assertEquals("/content/dam/avionos/images/one.png.rendition/cq5dam.thumbnail.48.48.png", cacheableRendition.getPath());
```

## Compatibility

Bundle Version | AEM Version(s)
------------ | -------------
1.0.0 | 6.5

## Installation

Add the bundle as a Maven dependency to an existing project's OSGi bundle `pom.xml`:

```xml
<dependency>
    <groupId>com.avionos.aem.cacheablerenditions</groupId>
    <artifactId>cacheable-renditions</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

...and also add to the project's UI `pom.xml` as both a dependency and an embedded bundle in the `content-package-maven-plugin` configuration, if using: 

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.day.jcr.vault</groupId>
            <artifactId>content-package-maven-plugin</artifactId>
            <version>0.5.1</version>
            <extensions>true</extensions>
            <configuration>
                <embeddeds>
                    <embedded>
                        <groupId>com.avionos.aem.cacheablerenditions</groupId>
                        <artifactId>cacheable-renditions</artifactId>
                        <target>/apps/avionos/install</target>
                    </embedded>
                </embeddeds>
            </configuration>
        </plugin>
    </plugins>
</build>

<dependencies>
    <dependency>
        <groupId>com.avionos.aem.cacheablerenditions</groupId>
        <artifactId>cacheable-renditions</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Versioning

Follows [Semantic Versioning](http://semver.org/) guidelines.