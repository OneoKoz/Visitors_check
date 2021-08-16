package com.kozyrev.Fields;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * данные посетителя
 */
public class Visitor {
    /**
     * данные сотрудника
     */
    private String firstName;
    private String lastName;
    private String patronymic;
    //паспортные данные
    private String pasportData;

    /**
     * данные сотрудника, к которому идем посетитель
     */
    private String employeeFirstName;
    private String employeeLastName;
    private String employeePatronymic;
    private String employeeSubdivision;
    /**
     * дата и время посещения
     */
    private String date;
    private String time;
    /**
     * флаг
     * находится ли человек в черном списке
     */
    private Boolean black;


    /**
     * контроллер для загрузки данных из файла
     */
    public Visitor(String lastName, String firstName, String patronymic, String pasportData,
                   String employeeLastName, String employeeFirstName, String employeePatronymic, String emploeeSubdivision,
                   String date, String time, Boolean black) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.pasportData = pasportData;
        this.employeeLastName=employeeLastName;
        this.employeeFirstName=employeeFirstName;
        this.employeePatronymic=employeePatronymic;
        this.employeeSubdivision=emploeeSubdivision;

        this.date = date;
        this.time=time;
        this.black=black;

    }

    /**
     * контроллер используется в методе для добавления нового посетителя
     * дата и время заполняются автоматически
     */
    public Visitor(){
        date= new SimpleDateFormat("yyyy-MM-d").format(new GregorianCalendar().getTime());
        time= new SimpleDateFormat("HH:MM").format(new GregorianCalendar().getTime());
    }

    /**
     * получаем ФИО посетителя одной строкой
     * @return ФИО посетителя одной строкой
     */
    public String getFIO(){ return lastName.toLowerCase()+firstName.toLowerCase()+patronymic.toLowerCase(); }



    /**
     *  стандартные геттеры и сеттеры
     *
     */

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPatronymic() { return patronymic; }

    public Boolean getBlack() {
        return black;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeePatronymic() {
        return employeePatronymic;
    }

    public void setEmployeePatronymic(String employeePatronymic) {
        this.employeePatronymic = employeePatronymic;
    }

    public String getEmployeeSubdivision() {
        return employeeSubdivision;
    }

    public void setEmployeeSubdivision(String employeeSubdivision) {
        this.employeeSubdivision = employeeSubdivision;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPasportData() {
        return pasportData;
    }

    public void setPasportData(String pasportData) { this.pasportData = pasportData; }

    public void setBlack(boolean black) { this.black = black; }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    /**
     * получить строковое представление данных посетителя
     * @return строковое представление данных посетителя
     */
    @Override
    public String toString() {
        return (lastName+"|"+firstName+"|"+patronymic+"|"+pasportData+"|"+employeeLastName+"|"+employeeFirstName+"|"+employeePatronymic+"|"+employeeSubdivision+"|"+date+"|"+time+"|");
    }

}
