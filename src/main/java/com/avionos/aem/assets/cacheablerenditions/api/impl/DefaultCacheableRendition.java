package com.avionos.aem.assets.cacheablerenditions.api.impl;

import com.avionos.aem.assets.cacheablerenditions.api.CacheableRendition;
import com.avionos.aem.assets.cacheablerenditions.servlets.CacheableRenditionServlet;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.api.Rendition;
import org.apache.commons.io.FilenameUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.jcr.Binary;
import java.io.InputStream;
import java.util.Iterator;

@Model(adaptables = { Resource.class, Rendition.class }, adapters = CacheableRendition.class)
public final class DefaultCacheableRendition implements CacheableRendition {

    @Self
    private Rendition delegate;

    @Override
    public String getCacheablePath() {
        final String cacheablePath;

        if (getName().equals(DamConstants.ORIGINAL_FILE)) {
            cacheablePath = getAsset().getPath();
        } else {
            cacheablePath = new StringBuilder(getAsset().getPath())
                .append(".")
                .append(CacheableRenditionServlet.EXTENSION)
                .append("/")
                .append(getCacheableRenditionName())
                .toString();
        }

        return cacheablePath;
    }

    private String getCacheableRenditionName() {
        final String name = getName();
        final String cacheableName;

        if (name.contains(".")) {
            cacheableName = name;
        } else {
            // if rendition name is missing extension, borrow the extension from the original asset
            final String extension = FilenameUtils.getExtension(getAsset().getName());

            cacheableName = new StringBuilder(name)
                .append(FilenameUtils.EXTENSION_SEPARATOR)
                .append(extension)
                .toString();
        }

        return cacheableName;
    }

    // delegate methods

    @Override
    public String getMimeType() {
        return delegate.getMimeType();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public ValueMap getProperties() {
        return delegate.getProperties();
    }

    @Override
    public long getSize() {
        return delegate.getSize();
    }

    @Override
    public InputStream getStream() {
        return delegate.getStream();
    }

    @Override
    public Binary getBinary() {
        return delegate.getBinary();
    }

    @Override
    public Asset getAsset() {
        return delegate.getAsset();
    }

    @Override
    public Resource getParent() {
        return delegate.getParent();
    }

    @Override
    public Iterator<Resource> listChildren() {
        return delegate.listChildren();
    }

    @Override
    public Iterable<Resource> getChildren() {
        return delegate.getChildren();
    }

    @Override
    public Resource getChild(final String relPath) {
        return delegate.getChild(relPath);
    }

    @Override
    public String getResourceType() {
        return delegate.getResourceType();
    }

    @Override
    public String getResourceSuperType() {
        return delegate.getResourceSuperType();
    }

    @Override
    public boolean hasChildren() {
        return delegate.hasChildren();
    }

    @Override
    public boolean isResourceType(final String resourceType) {
        return delegate.isResourceType(resourceType);
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return delegate.getResourceMetadata();
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return delegate.getResourceResolver();
    }

    @Override
    public ValueMap getValueMap() {
        return delegate.getValueMap();
    }

    @Override
    public <AdapterType> AdapterType adaptTo(final Class<AdapterType> type) {
        return delegate.adaptTo(type);
    }
}
