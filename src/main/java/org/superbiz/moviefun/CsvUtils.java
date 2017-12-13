package org.superbiz.moviefun;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.tika.Tika;
import org.superbiz.moviefun.blobstore.Blob;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CsvUtils {
    
    public static String readFile(String name) throws IOException {
//        File file = new File(name);
        InputStream is = CsvUtils.class.getClassLoader().getResourceAsStream(name);
        if (is == null) {
            throw new RuntimeException("missing resource " + name);
        }
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");
        if (scanner.hasNext()) {
            return scanner.next();
        } else {
            return "";
        }
    }

    public static <T> List<T> readFromCsv(ObjectReader objectReader, String path) throws IOException {
        List<T> results = new ArrayList<>();

        MappingIterator<T> iterator = objectReader.readValues(readFile(path));

        while (iterator.hasNext()) {
            results.add(iterator.nextValue());
        }

        return results;
    }
}
