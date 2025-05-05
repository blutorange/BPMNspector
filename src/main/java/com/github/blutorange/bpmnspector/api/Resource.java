package com.github.blutorange.bpmnspector.api;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/** A resource that may come from different sources, such as a file path, a URL, or a stream. */
public final class Resource implements Comparable<Resource> {

    private final String resourceName;

    private final URL url;

    private final Path path;

    private final ResourceType type;

    /**
     * Creates a new resource for a file path.
     *
     * @param path the path to the resource
     */
    public Resource(Path path) {
        Objects.requireNonNull(path);

        this.path = path;
        resourceName = path.toAbsolutePath().toString();
        type = ResourceType.FILE;

        url = null;
    }

    /**
     * Creates a new resource for a URL.
     *
     * @param url the URL to the resource
     */
    public Resource(URL url) {
        Objects.requireNonNull(url);

        this.url = url;
        this.resourceName = url.toString();
        this.type = ResourceType.URL;

        path = null;
    }

    /**
     * Creates a new resource for a name.
     *
     * @param resourceName the name of the resource
     */
    public Resource(String resourceName) {
        this.resourceName = resourceName;
        this.type = ResourceType.STREAM;

        this.path = null;
        this.url = null;
    }

    /**
     * Gets the name of the resource.
     *
     * @return the name of the resource
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Gets the URL of the resource, if available.
     *
     * @return the URL of the resource
     */
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
    }

    /**
     * Gets the path of the resource, if available.
     *
     * @return the path of the resource
     */
    public Optional<Path> getPath() {
        return Optional.ofNullable(path);
    }

    /**
     * Gets the type of the resource.
     *
     * @return the type of the resource
     */
    public ResourceType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var resource = (Resource) o;

        if (type != resource.type) return false;

        return resourceName.equals(resource.resourceName);
    }

    @Override
    public int hashCode() {
        return resourceName.hashCode();
    }

    @Override
    public int compareTo(Resource resource) {
        Objects.requireNonNull(resource);

        return resourceName.compareTo(resource.getResourceName());
    }

    @Override
    public String toString() {
        return resourceName;
    }

    public enum ResourceType {
        URL,
        FILE,
        STREAM
    }
}
