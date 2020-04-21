package com.avionos.aem.assets.cacheablerenditions.api.impl

import com.avionos.aem.assets.cacheablerenditions.api.CacheableRendition
import com.day.cq.dam.api.Asset
import com.icfolson.aem.prosper.specs.ProsperSpec
import spock.lang.Unroll

@Unroll
class DefaultCacheableRenditionSpec extends ProsperSpec {

    def setupSpec() {
        slingContext.addModelsForClasses(DefaultCacheableRendition)
    }

    def "cacheable rendition is adaptable from resource"() {
        expect:
        getResource("/content/dam/avionos/images/one.png/jcr:content/renditions/original").adaptTo(CacheableRendition)
    }

    def "cacheable rendition is adaptable from rendition"() {
        setup:
        def rendition = getResource("/content/dam/avionos/images/one.png").adaptTo(Asset).original

        expect:
        rendition.adaptTo(CacheableRendition)
    }

    def "get cacheable rendition path"() {
        setup:
        def asset = getResource("/content/dam/avionos/images/one.png").adaptTo(Asset)

        def rendition = asset.getRendition(renditionName).adaptTo(CacheableRendition)

        expect:
        rendition.cacheablePath == cacheablePath

        where:
        renditionName                | cacheablePath
        "original"                   | "/content/dam/avionos/images/one.png"
        "mobile"                     | "/content/dam/avionos/images/one.png.rendition/mobile.png"
        "cq5dam.thumbnail.48.48.png" | "/content/dam/avionos/images/one.png.rendition/cq5dam.thumbnail.48.48.png"
    }
}