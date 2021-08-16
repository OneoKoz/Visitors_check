package com.kozyrev.Storage;

import com.kozyrev.Fields.Employee;
import com.kozyrev.List.EmployeesList;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * класс для хранения, загруски в файл и чтения из него
 * информации о сотрудниках
 */
public class EmployeesStorage {
    /**
     * название директории для хранения данных сотрудников
     */
    private String pathPackage="storage/";
    /**
     * названия файла , в котором хранится инормация про всех сотрудников
     */
    private String pathFile="employees.txt";
    /**
     * список всех сотрудников
     */
    private final EmployeesList allEmployees= new EmployeesList();


    public EmployeesStorage() {readElement();}

    /**
     * считать данные сотрудника из файла
     */
    public void readElement(){
        try{
            //путь к необхожимому файлу
            Path filePath= Paths.get(pathPackage+pathFile);
            //проверка на существование файла, находяегося по данному адресу
            if(Files.exists(filePath)){
                //считываем информацию про всех сотрудников
                List<String> lines = Files.readAllLines(filePath, UTF_8);
                //очищаем список всех струдников
                allEmployees.clear();
                //цикл для добавления новых сотрудников в список всех сотрудников
                for(String line:lines){
                    //разделяем строчку со всей информацией одного сотрудника на отдельные компоненты
                    String[] str= line.split("\\|");
                    //добавление нового сотрудника в список всех сотрудников
                    allEmployees.addLast(new Employee(str[0],str[1],str[2],str[3]));
                }
            }
        } catch (IOException e ) {e.printStackTrace();}
    }
    public void writeElement(){
        try{
            //создание строки для данных всех сотрудников
            StringBuilder string= new StringBuilder();
            //проверка на пустоту списка
            if(!allEmployees.isEmpty()){
                //цикл в котором проходимся по всем объектам сотрудников
                for(Employee employee:allEmployees){
                    //добавляем нового сотрудника в строку
                    string.append(employee).append("\n");
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
     * получаем список всех сотрудников
     * @return список всех сотрудников
     */
    public EmployeesList getAllEmployees() {return allEmployees;}
}
