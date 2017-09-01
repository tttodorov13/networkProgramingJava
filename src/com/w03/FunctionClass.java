package com.w03;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardOpenOption.*;

public class FunctionClass {

    private static List<String> fileList = new ArrayList<>();
    private static final String SOURCE_FOLDER = "./";
    private static final String SOURCE_ZIP_FILE = "src.zip";
    private static final String OUTPUT_ZIP_FILE = "out.zip";

    public static void main(String[] args) {
        unzip();
        mergeAllTxt();
        archiveAllTxt();
    }

    private static void unzip() {
        File dir = new File(SOURCE_FOLDER);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(SOURCE_ZIP_FILE);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergeAllTxt() {
        Path outFile = Paths.get("out.txt");
        PathMatcher matcher1 = FileSystems.getDefault().getPathMatcher("glob:**.{txt}");
        PathMatcher matcher2 = FileSystems.getDefault().getPathMatcher("glob:./out.txt");
        try (FileChannel out = FileChannel.open(outFile, CREATE, WRITE)) {
            try (Stream<Path> paths = Files.walk(Paths.get(SOURCE_FOLDER))) {
                paths
                        .filter(path -> Files.isReadable(path) && matcher1.matches(path) && !matcher2.matches(path))
                        .forEach(path -> {
                            fileList.add(path.getFileName().toString());
                            try (FileChannel in = FileChannel.open(path, READ)) {
                                for (long p = 0, l = in.size(); p < l; )
                                    p += in.transferTo(p, l - p, out);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Merging: " + path.toAbsolutePath());
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Merged file: " + outFile.toAbsolutePath());
    }

    private static void archiveAllTxt() {
        byte[] buffer = new byte[1024];
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file : fileList) {
                System.out.println("Zipping: " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }

            zos.closeEntry();
            System.out.println("Zipped to: " + OUTPUT_ZIP_FILE);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}