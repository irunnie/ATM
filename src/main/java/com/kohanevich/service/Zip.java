package com.kohanevich.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    List<String> fileList;
    public static String outputZipFile;
    public static String sourceFolder;
    private static Properties paths;

    public Zip(){
        fileList = new ArrayList<>();
        outputZipFile = getOutputZipFile();
        sourceFolder = getSourceFolder();
    }

    private static String getSourceFolder(){
        paths = new Properties();
        try {
            paths.load(Zip.class.getResourceAsStream("/path.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths.getProperty("path-for-logs");
    }

    private static String getOutputZipFile(){
        paths = new Properties();
        try {
            paths.load(Zip.class.getResourceAsStream("/path.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths.getProperty("path-for-zip");
    }

    public void compress(String zipFile){
        byte[] buffer = new byte[1024];

        try(FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream))
        {
            for (String file : this.fileList) {
                ZipEntry zipEntry = new ZipEntry(file);
                zipOutputStream.putNextEntry(zipEntry);

                FileInputStream in = new FileInputStream(sourceFolder + File.separator + file);

                int length;
                while ((length = in.read(buffer)) > 0){
                    zipOutputStream.write(buffer, 0, length);
                }
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void generateFileList(File node){

        if(node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsolutePath()));
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file){
        return file.substring(sourceFolder.length()+1, file.length());
    }
}
