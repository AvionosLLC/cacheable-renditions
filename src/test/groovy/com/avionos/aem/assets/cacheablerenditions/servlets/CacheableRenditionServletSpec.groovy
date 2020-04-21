package com.avionos.aem.assets.cacheablerenditions.servlets

import com.google.common.net.HttpHeaders
import com.icfolson.aem.prosper.specs.ProsperSpec
import spock.lang.Shared

import javax.servlet.http.HttpServletResponse

class CacheableRenditionServletSpec extends ProsperSpec {

    @Shared
    CacheableRenditionServlet servlet = new CacheableRenditionServlet()

    def "servlet returns 404 if asset is null"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/dam/avionos/images/none"
            extension = CacheableRenditionServlet.EXTENSION
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.status == HttpServletResponse.SC_NOT_FOUND
    }

    def "servlet returns 404 is suffix is null"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/dam/avionos/images/one.png"
            extension = CacheableRenditionServlet.EXTENSION
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.status == HttpServletResponse.SC_NOT_FOUND
    }

    def "servlet streams original rendition if rendition not found for suffix name"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/dam/avionos/images/one.png"
            extension = CacheableRenditionServlet.EXTENSION
            suffix = "/no-image.jpeg"
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.status == HttpServletResponse.SC_OK

        and:
        response.contentType == "image/png"

        and:
        response.getHeader(HttpHeaders.CONTENT_DISPOSITION) == "inline; filename=original"
    }

    def "servlet streams rendition for suffix name"() {
        setup:
        def request = requestBuilder.build {
            path = "/content/dam/avionos/images/one.png"
            extension = CacheableRenditionServlet.EXTENSION
            suffix = "/cq5dam.thumbnail.48.48.png"
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.status == HttpServletResponse.SC_OK

        and:
        response.contentType == "image/png"

        and:
        response.getHeader(HttpHeaders.CONTENT_DISPOSITION) == "inline; filename=cq5dam.thumbnail.48.48.png"
    }
}
