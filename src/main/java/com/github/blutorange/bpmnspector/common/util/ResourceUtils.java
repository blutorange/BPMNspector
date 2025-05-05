package com.github.blutorange.bpmnspector.common.util;

import com.github.blutorange.bpmnspector.api.Resource;
import com.github.blutorange.bpmnspector.api.ValidationException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class ResourceUtils {

    public static Resource determineAndCreateResourceFromString(String location, String baseLocation)
            throws ValidationException {
        try {
            Resource resource;
            var importUri = new URI(location);

            if (importUri.isAbsolute() && importUri.getScheme().toLowerCase().startsWith("http")) {
                // process as URL
                var asURL = importUri.toURL();
                resource = new Resource(asURL);
            } else {
                // process as file
                var importPath = Paths.get(importUri);
                if (!importPath.isAbsolute() && baseLocation != null) {
                    // resolve relative path based on the baseLocation
                    importPath = Paths.get(baseLocation)
                            .getParent()
                            .resolve(importPath)
                            .normalize()
                            .toAbsolutePath();
                }

                if (Files.notExists(importPath) || !Files.isRegularFile(importPath)) {
                    throw new ValidationException("File does not exist.");
                } else {
                    resource = new Resource(importPath);
                }
            }
            return resource;
        } catch (URISyntaxException | MalformedURLException | InvalidPathException e) {
            throw new ValidationException("Path " + location + " is invalid.", e);
        }
    }
}
