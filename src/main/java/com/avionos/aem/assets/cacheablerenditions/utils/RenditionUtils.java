package com.avionos.aem.assets.cacheablerenditions.utils;

import com.avionos.aem.assets.cacheablerenditions.api.CacheableRendition;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Utilities for managing cacheable renditions.
 */
public final class RenditionUtils {

    /**
     * Get the dispatcher-cacheable rendition path for the given resource.
     *
     * @param resource asset or rendition resource
     * @return cacheable path
     */
    public static String getCacheablePath(final Resource resource) {
        final String cacheablePath;

        final ResourceResolver resolver = resource.getResourceResolver();

        if (DamUtil.isRendition(resource)) {
            cacheablePath = resolver.map(resource.adaptTo(CacheableRendition.class).getCacheablePath());
        } else if (DamUtil.isAsset(resource)) {
            cacheablePath = resolver.map(resource.adaptTo(Asset.class).getPath());
        } else {
            cacheablePath = null;
        }

        return cacheablePath;
    }

    private RenditionUtils() {

    }
}
