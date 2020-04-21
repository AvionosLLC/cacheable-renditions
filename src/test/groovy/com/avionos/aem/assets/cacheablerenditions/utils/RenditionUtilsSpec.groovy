package com.avionos.aem.assets.cacheablerenditions.utils

import com.avionos.aem.assets.cacheablerenditions.api.impl.DefaultCacheableRendition
import com.icfolson.aem.prosper.specs.ProsperSpec
import spock.lang.Unroll

@Unroll
class RenditionUtilsSpec extends ProsperSpec {

    def setupSpec() {
        slingContext.addModelsForClasses(DefaultCacheableRendition)
    }

    def "get cacheable rendition path for resource"() {
        setup:
        def resource = getResource(path)

        expect:
        RenditionUtils.getCacheablePath(resource) == cacheablePath

        where:
        path                                                                                    | cacheablePath
        "/content/dam/avionos/images"                                                           | null
        "/content/dam/avionos/images/one.png"                                                   | "/content/dam/avionos/images/one.png"
        "/content/dam/avionos/images/one.png/jcr:content/renditions/original"                   | "/content/dam/avionos/images/one.png"
        "/content/dam/avionos/images/one.png/jcr:content/renditions/cq5dam.thumbnail.48.48.png" | "/content/dam/avionos/images/one.png.rendition/cq5dam.thumbnail.48.48.png"
    }
}
