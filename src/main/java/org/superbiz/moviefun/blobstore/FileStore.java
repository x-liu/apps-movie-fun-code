package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.superbiz.moviefun.CsvUtils;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;


public class FileStore implements BlobStore {

    private final Tika tika = new Tika();


    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();


        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.inputStream, outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
//        File file = new File(name);
        InputStream is = CsvUtils.class.getClassLoader().getResourceAsStream(name);
        if (is == null) {
            return Optional.empty();
        }
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");
        return Optional.of(new Blob(
            name,
            is,
            tika.detect(is)
        ));
    }
}
