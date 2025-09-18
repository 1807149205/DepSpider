package org.wzl.depspider.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void resolvePathFindsNestedDirectory() throws IOException {
        File root = Files.createTempDirectory("root").toFile();
        File child = new File(root, "a");
        File nested = new File(child, "b");
        assertTrue(nested.mkdirs());
        File resolved = FileUtil.resolvePath(root, Arrays.asList("a", "b"));
        assertTrue(resolved.isDirectory());
        assertEquals(nested.getAbsolutePath(), resolved.getAbsolutePath());
    }

    @Test
    public void validateFileThrowsOnWrongSuffix() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();
        try {
            FileUtil.validateFile(tempFile.getAbsolutePath(), ".jsx");
            fail("Expected RuntimeException");
        } catch (RuntimeException expected) {
            // expected exception
        }
    }
}
