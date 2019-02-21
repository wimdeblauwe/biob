package org.wimdeblauwe.biob.storage.localfilesystem;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

final class FileUtils {
    private FileUtils() {
    }

    /**
     * Deletes all files in the given directory (recursively).
     *
     * @param directory the directory to delete
     * @throws IOException when directory could not be deleted
     */
    static void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new DeletingFileVisitor());
    }

    /**
     * Returns if the given directory is empty or not.
     *
     * @param directory the directory to check
     * @return true if empty, false otherwise
     * @throws IOException when check could not be performed
     */
    static boolean isDirEmpty(Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

}
