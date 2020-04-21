package com.avionos.aem.assets.cacheablerenditions.api;

import com.day.cq.dam.api.Rendition;

/**
 * Extension of asset rendition that includes a Dispatcher-cacheable path.
 */
public interface CacheableRendition extends Rendition {

    /**
     * Get the Dispatcher-cacheable rendition path for the current rendition.  If this rendition is the original asset,
     * the asset path itself will be returned.
     *
     * @return cacheable rendition path
     */
    String getCacheablePath();
}
