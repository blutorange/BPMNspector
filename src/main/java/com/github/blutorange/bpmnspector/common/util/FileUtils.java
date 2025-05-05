package com.github.blutorange.bpmnspector.common.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class.getSimpleName());

    public static List<Path> getAllBpmnFileFromDirectory(Path directory) {
        assertDirectory(directory);

        List<Path> bpmnFiles = new ArrayList<>();

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (FileSystems.getDefault()
                            .getPathMatcher("glob:**/*.{bpmn,bpmn2,bpmn20.xml}")
                            .matches(file)) {
                        bpmnFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("IOException while traversing folder.", e);
        }

        return bpmnFiles;
    }

    public static void assertDirectory(Path path) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path " + path.toAbsolutePath() + " is no directory.");
        }
    }
}
