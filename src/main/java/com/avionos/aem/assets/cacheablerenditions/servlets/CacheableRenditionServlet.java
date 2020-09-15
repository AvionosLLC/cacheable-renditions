package com.avionos.aem.assets.cacheablerenditions.servlets;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import com.google.common.net.HttpHeaders;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * Servlet to render asset renditions using a path/extension/suffix combination that can be cached by Dispatcher.
 * <p>
 * URL composition: [asset path].rendition/[rendition name]
 * <p>
 * (ex: /content/dam/avionos/images/logo.png.rendition/cq5dam.thumbnail.319.319.png)
 * <p>
 * Original renditions should be referenced using the asset path directly instead of this servlet to prevent
 * extensionless URLs.
 */
@Component(service = Servlet.class)
@SlingServletResourceTypes(
    methods = "GET",
    resourceTypes = DamConstants.NT_DAM_ASSET,
    extensions = CacheableRenditionServlet.EXTENSION
)
public final class CacheableRenditionServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableRenditionServlet.class);

    public static final String EXTENSION = "rendition";

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws IOException {
        LOG.debug("asset rendition request resource : {}", request.getResource());

        final Asset asset = DamUtil.resolveToAsset(request.getResource());

        if (asset == null) {
            LOG.warn("asset is null for request, returning 404 response...");

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            final String suffix = request.getRequestPathInfo().getSuffix();

            if (suffix == null) {
                LOG.warn("suffix is null for request, returning 404 response...");

                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                final String renditionName = FilenameUtils.getName(suffix);
                final Rendition rendition = getRendition(asset, renditionName);

                LOG.debug("request suffix : {}, rendition name : {}, rendition path : {}", suffix, renditionName,
                    rendition.getPath());

                stream(response, rendition, rendition.getMimeType());
            }
        }
    }

    private Rendition getRendition(final Asset asset, final String renditionName) {
        return getRenditionForName(asset, renditionName).orElseGet(() -> getRenditionForBaseName(asset, renditionName));
    }

    private Rendition getRenditionForBaseName(final Asset asset, final String renditionName) {
        final String baseName = FilenameUtils.getBaseName(renditionName);

        // default to original if rendition does not exist for base name
        return getRenditionForName(asset, baseName).orElse(asset.getOriginal());
    }

    private Optional<Rendition> getRenditionForName(final Asset asset, final String name) {
        return asset.getRenditions()
            .stream()
            .filter(r -> r.getName().equals(name))
            .findFirst();
    }

    private void stream(final SlingHttpServletResponse response, final Rendition rendition, final String contentType)
        throws IOException {
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="
            + URLEncoder.encode(rendition.getName(), CharEncoding.UTF_8));

        try (final InputStream stream = rendition.getStream()) {
            IOUtils.copy(stream, response.getOutputStream());
        }
    }
}
