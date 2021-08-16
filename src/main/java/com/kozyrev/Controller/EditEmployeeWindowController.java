package com.kozyrev.Controller;


import com.kozyrev.Fields.Employee;
import com.kozyrev.Fields.Subdivision;
import com.kozyrev.List.EmployeesList;
import com.kozyrev.List.SubdivisionList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.kozyrev.Main.MainApp.mainApp;

/**
 * класс отвечающий за диалоговое окно добавлени и редактирования данных сотрудников
 */
public class EditEmployeeWindowController {
    //диалогое окно
    private Stage stage;
    //данные о сотруднике
    private Employee employee;
    //список всех сотрудников
    private EmployeesList allEmployee= mainApp.getEmployeesStorage().getAllEmployees();
    //список всех отделов
    private SubdivisionList allSubdivision= mainApp.getSundivisionStorage().getAllSubdivision();
    //список для вывода информации в ChoiceBox со всеми отделами
    private ObservableList observableList= FXCollections.observableArrayList();

    /**
     * заполнение списка названиями всех отделов
     */
    public void INItsubdivision(){
        for(Subdivision subdivision: allSubdivision){
            observableList.add(subdivision.getSubdivision());
        }
    }

    /**
     * виджет выпадающего списка всех отделов
     */
    @FXML
    private ChoiceBox<String> subdivisionChoiceBox;

    //текстовыек поля с ФИО сотрудника
    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField patronymicTextField;

    //кнопка удаления сотрудника и списка
    //всех сотрудников
    @FXML
    private Button removeButton;

    //виджет показа ошибки ввода информации
    @FXML
    private Label errorLabel;


    /**
     * если нажали на кнопку Ок
     * идет процесс обновления информации о сотруднике , если такой сотрудник уже есть
     * или процесс добавления нового сотрудника
     */
    @FXML
    public void onOKEmployee() {
        errorLabel.setVisible(false);
        //удаляем старого сотрудника из списка
        //чтобы обновить о нем информацию
        allEmployee.remove(employee);
        //проверка вводимой в текстовые поля информации о сотруднике
        if(checkInformation(lastNameTextField.getText()))return;
        employee.setLastName(lastNameTextField.getText().trim());

        if(checkInformation(firstNameTextField.getText()))return;
        employee.setFirstName(firstNameTextField.getText().trim());

        if(checkInformation(patronymicTextField.getText()))return;
        employee.setPatronymic(patronymicTextField.getText().trim());

        employee.setSubdivision(subdivisionChoiceBox.getValue());

        //добавляем сотрудника в список
        allEmployee.addFirst(employee);

        //записываем список в файл
        mainApp.getEmployeesStorage().writeElement();

        //закрываемдиалогое окно
        stage.close();

        //загружаем список всех сотрудников
        mainApp.loadMainWindowJournal(MainWindowJournalController.ShowVisitor.ALLEMPLOYEE);
    }

    /**
     * проверка на введенную информацию
     * @param string введенная информация
     * @return true - если информация введена некорректно
     */
    public boolean checkInformation(String string){
        //проверка на пустую строку
        if(string.equals("")){
            errorLabel.setVisible(true);
            errorLabel.setText("вы не ввели всю информацию");
            return true;
        }
        //провека , чтобы не было данного элемента
        //так как он ифпользуется для разделения информации
        //при записи списка в файл
        if(string.contains("|")){
            errorLabel.setVisible(true);
            errorLabel.setText("данные не должны включать \"|\" ");
            return true;
        }
        return false;
    }

    /**
     * если нажали на кнопку Отмена
     */
    @FXML
    public void OnCancelEmployee() {
        //закрывается диалоговое окно
        stage.close();
    }


    /**
     * если нажали кнопку Удалить
     * такая кнопка появляется толькое ,если мы редактируем данные существующего сотрудника
     */
    @FXML
    public void onRemoveEmployee() {
        //удаляем сотрудника из спика
        allEmployee.remove(employee);
        //закрываем диалоговое окно
        stage.close();
        //записываем получившийся список в файл
        mainApp.getEmployeesStorage().writeElement();
        //показываем список всех сотрудников
        mainApp.loadMainWindowJournal(MainWindowJournalController.ShowVisitor.ALLEMPLOYEE);
    }

    /**
     * инициализируем диалоговое окно в соответсвии с введенными данными
     * @param stage диалоговое окно
     * @param employee данные сотрудника
     *                 если == null то просто ставить данные по умолчанию
     */
    public void init(Stage stage, Employee employee){
        this.stage=stage;
        this.employee=employee;
        //инициализируем список всех отделов
        INItsubdivision();
        subdivisionChoiceBox.setItems(observableList);
        //показываем информацию о сотруднике если она есть
        showInformation();
    }

    /**
     * показываем информацию о сотруднике если она есть
     */
    public void showInformation(){
        if(employee!=null){
            lastNameTextField.setText(employee.getLastName());
            firstNameTextField.setText(employee.getFirstName());
            patronymicTextField.setText(employee.getPatronymic());
            subdivisionChoiceBox.getSelectionModel().select(employee.getSubdivision());
        }else{
            employee= new Employee();
            removeButton.setVisible(false);
            subdivisionChoiceBox.getSelectionModel().selectFirst();
        }
    }
}
