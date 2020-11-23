package com.github.ybroeker.pmdidea;

import java.io.*;
import java.util.stream.Collectors;


public class ResourceUtil {

    public static String getInputStreamAsString(final InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }

    }

}
