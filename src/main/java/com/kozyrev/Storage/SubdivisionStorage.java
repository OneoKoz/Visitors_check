package com.kozyrev.Storage;

import com.kozyrev.Fields.Subdivision;
import com.kozyrev.List.SubdivisionList;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * класс для хранения, загруски в файл и чтения из него
 * информации о посетителях
 * более подробные комментарии есть в
 * VisitorsStorage
 * EmployeeStorage
 * так как все комментарииа налогичные
 */
public class SubdivisionStorage {
    private String pathPackage="storage/";
    private String pathFile="subdivision.txt";
    private final SubdivisionList allSubdivision = new SubdivisionList();

    public SubdivisionStorage() {readElement();}

    public void readElement(){
        try{
            Path filePath= Paths.get(pathPackage+pathFile);
            if(Files.exists(filePath)){
                List<String> lines = Files.readAllLines(filePath, UTF_8);
                allSubdivision.clear();
                for(String line:lines){
                    String[] str= line.split("\\|");
                    allSubdivision.addLast(new Subdivision(str[0],str[1]));
                }
            }
        } catch (IOException e ) {e.printStackTrace();}
    }
    public void writeElement(){
        try{
            if(!allSubdivision.isEmpty()){
                StringBuilder string= new StringBuilder();
                for(Subdivision subdivision:allSubdivision){
                    string.append(subdivision).append("\n");
                }

                Path dir= Paths.get(pathPackage);
                if(!Files.isDirectory(dir)){
                    Files.createDirectory(dir);
                }

                FileWriter fileWriter= new FileWriter(pathPackage+pathFile,UTF_8);
                fileWriter.write(string.toString());
                fileWriter.close();
            }

        } catch (IOException e ) {e.printStackTrace();}
    }

    public SubdivisionList getAllSubdivision() {return allSubdivision;}
}
