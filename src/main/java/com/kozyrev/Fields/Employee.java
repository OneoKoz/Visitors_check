package com.kozyrev.Fields;

/**
 * данные сотрудника
 */
public class Employee {
    /**
     * имя
     * фамилия
     * отчество
     * отдел, в котором работает собрудник
     */
    private String firstName;
    private String lastName;
    private String patronymic;
    private String subdivision;

    /**
     *контроллеры для создания "пустого" сотрудника
     */
    public Employee(){

    }

    /**
     * контроллер для создания объекта толкьо с ФИО
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * используется в методе для добавления нового посетителя
     * при добавлении нового посетителя мы вводим только ФИО сотрудника
     *                   и потом ищем его в списке всех сотрудников
     *                   и потом присваем тот отдел в который пошел сотрудник
     */
    public Employee(String lastName, String firstName, String patronymic){
        this.firstName=firstName;
        this.lastName=lastName;
        this.patronymic=patronymic;
    }

    /**
     * контроллер используется при загрузки данный из файлы
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * @param subdivision Отдел
     */
    public Employee(String lastName, String firstName, String patronymic, String subdivision){
        this.firstName=firstName;
        this.lastName=lastName;
        this.patronymic=patronymic;
        this.subdivision=subdivision;
    }

    /**
     * получаем ФИО сотрудника одной строкой
     * @return ФИО сотрудника одной строкой
     */
    public String getFIO(){ return lastName.toLowerCase()+firstName.toLowerCase()+patronymic.toLowerCase();}

    /**
     * все стандартные геттеры и сеттеры для данного обекта
     *
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }


    /**
     * получить строковое представление данных сотрудника
     * @return строковое представление данных сотрудника
     */
    @Override
    public String toString() {
        return (lastName+"|"+firstName+"|"+patronymic+"|"+subdivision);
    }

}
