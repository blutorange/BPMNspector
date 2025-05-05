package com.github.blutorange.bpmnspector.api;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public final class Location implements Comparable<Location> {

    private final LocationCoordinate location;
    private final String xpath;
    private final Resource resource;

    public Location(Path filePath, LocationCoordinate location) {
        this.location = location;
        this.xpath = null;
        this.resource = new Resource(filePath);
    }

    public Location(Path filePath, LocationCoordinate location, String xpath) {
        this.location = location;
        this.xpath = xpath;
        this.resource = new Resource(filePath);
    }

    public Location(URL url, LocationCoordinate location) {
        this.location = location;
        this.xpath = null;
        this.resource = new Resource(url);
    }

    public Location(URL url, LocationCoordinate location, String xpath) {
        this.location = location;
        this.xpath = xpath;
        this.resource = new Resource(url);
    }

    public Location(Resource resource, LocationCoordinate location) {
        this.location = location;
        this.xpath = null;
        this.resource = resource;
    }

    public Location(Resource resource, LocationCoordinate location, String xpath) {
        this.location = location;
        this.xpath = xpath;
        this.resource = resource;
    }

    public LocationCoordinate getLocation() {
        return location != null ? location : LocationCoordinate.EMPTY;
    }

    public String getXpath() {
        return xpath != null ? xpath : "";
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public int compareTo(Location o) {
        Objects.requireNonNull(o);

        if (resource.equals(o.getResource())) {
            return getLocation().getId().compareTo(o.getLocation().getId());
        } else {
            return resource.compareTo(o.getResource());
        }
    }

    @Override
    public String toString() {
        return "Location{" + "location=" + location + ", xpath=" + xpath + ", resource=" + resource + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var location1 = (Location) o;
        return Objects.equals(location, location1.location)
                && Objects.equals(xpath, location1.xpath)
                && Objects.equals(resource, location1.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, xpath, resource);
    }
}
