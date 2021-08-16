package com.kozyrev.Storage;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.kozyrev.Fields.Visitor;
import com.kozyrev.List.VisitorsList;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * класс для хранения, загруски в файл и чтения из него
 * информации о посетителях
 */
public class VisitorsStorage {
    /**
     * название директории для хранения данных посетителей
     */
    private String pathPackage="storage/";
    /**
     * названия файла , в котором хранится инормация про всех посетителей
     */
    private String pathFile="visitors.txt";
    /**
     * список всех посетителей
     */
    private VisitorsList allVisitors= new VisitorsList();

    public VisitorsStorage() {readElement();}
    /**
     * считать данные посетителей из файла
     */
    public void readElement(){
        try{
            //путь к необхожимому файлу
            Path filePath= Paths.get(pathPackage+pathFile);
            //проверка на существование файла, находяегося по данному адресу
            if(Files.exists(filePath)){
                //считываем информацию про всех посетителей
                List<String> lines = Files.readAllLines(filePath, UTF_8);
                allVisitors.clear();//очищаем список всех посетителей
                //цикл для добавления новых посетителей в список всех посетителей
                for(String line:lines){
                    //разделяем строчку со всей информацией одного посетителя на отдельные компоненты
                    String[] str= line.split("\\|");
                    //добавление нового посетителя в список всех посетителей
                    allVisitors.addLast(new Visitor(str[0],str[1],str[2],str[3],str[4],str[5],str[6],str[7],str[8],str[9],Boolean.parseBoolean(str[10])));
                }
            }
        } catch (IOException e ) {e.printStackTrace();}
    }
    public void writeElement(){
        try{
            //проверка на пустоту списка
            if(!allVisitors.isEmpty()){
                //создание строки для данных всех посетителей
                StringBuilder string= new StringBuilder();
                //цикл в котором проходимся по всем объектам посетителей
                for(Visitor visitor:allVisitors){
                    //добавляем нового посетителя в строку
                    string.append(visitor).append(visitor.getBlack()).append("\n");
                }
                //проверка на существование и создание директории файла
                Path dir= Paths.get(pathPackage);
                if(!Files.isDirectory(dir)){
                    Files.createDirectory(dir);
                }
                //открываем поток для записи файла
                //указываем адрес , по которому будет происходить запись
                FileWriter fileWriter= new FileWriter(pathPackage+pathFile,UTF_8);
                //записываем строку
                fileWriter.write(string.toString());
                //закрываем поток
                fileWriter.close();
            }

        } catch (IOException e ) {e.printStackTrace();}
    }
    /**
     * получаем список всех посетителей
     * @return список всех посетителей
     */
    public VisitorsList getAllVisitors() {return allVisitors;}
}
