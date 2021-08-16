package com.kozyrev.Controller;

import com.kozyrev.Fields.Employee;
import com.kozyrev.Fields.Subdivision;
import com.kozyrev.List.SubdivisionList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


import static com.kozyrev.Main.MainApp.mainApp;

/**
 * класс для отображения панели с информацией одного сотрудника
 */
public class EmployeeInformationController {

    //список всех сотрудников
    private SubdivisionList subdivisions= mainApp.getSundivisionStorage().getAllSubdivision();
    //поле отдельного сотрудника
    //нужен для облегчения передачи информации между методами
    private Employee employee;

    @FXML
    private GridPane employeeGridPane;

    @FXML
    private Label fioLabel;

    @FXML
    private Label subdivisionLabel;

    /**
     * вывод диалогового окна
     * для редактирования информации о сотруднике
     */
    @FXML
    public void onEditEmployeeInformation() {
        mainApp.loadEditEmployeeWindow(employee);
    }

    /**
     * инициализируем панель в соответствии с веденной информацией
     * @param employee данные пользователя, которые необходимо вывести
     */
    public void showEmployeeInformation(Employee employee){
        //устанавливаем значени полю
        this.employee=employee;
        String color = null;
        //цикл для определения цвета фона панели
        //в соответствии с отделом , в котором сотрудник работает
        for(Subdivision subdivision: subdivisions){
            if(employee.getSubdivision().equals(subdivision.getSubdivision())){
                color=subdivision.getColor();
                break;
            }
        }
        //проверка чтобы цвет был не пустым
        //установление фона
        if(color!=null) employeeGridPane.setStyle("-fx-background-color: "+color+" ;");
        //инфиализация поля с ФИО сотруднка в соответствии с веденными данными
        fioLabel.setText(employee.getLastName()+" "
                        +employee.getFirstName()+" "
                        +employee.getPatronymic());
        //инфиализация поля с отделом сотрудника в соответствии с веденными данными
        subdivisionLabel.setText(employee.getSubdivision());
    }

}
