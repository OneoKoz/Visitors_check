package com.kozyrev.Controller;


import com.kozyrev.Fields.Employee;
import com.kozyrev.Fields.Visitor;
import com.kozyrev.List.EmployeesList;
import com.kozyrev.List.VisitorsList;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import static com.kozyrev.Main.MainApp.mainApp;

/**
 * класс для работы с диалоговым окном
 */
public class EditVisitorWindowController {
    //флаг на черный список
    private boolean checkBlack;
    //диалоговое окно
    private Stage stage;
    //данные посетителя
    private Visitor visitor;
    //список всех посетителей
    private VisitorsList allvisitor = mainApp.getVisitorsStorage().getAllVisitors();
    //список всех сотрудников
    private EmployeesList allemployee = mainApp.getEmployeesStorage().getAllEmployees();


    //панель с информацией о сотруднике
    //ее не видно при добавлении посетителя в черный список
    @FXML
    private GridPane infoEmployeeGridPane;

    //поля с данными посетителя
    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField patronymicTextField;

    @FXML
    private TextField pasportDateTextField;

    //поля с данными сотрудника
    @FXML
    private TextField lastNameEmployeeTextField;

    @FXML
    private TextField firstNameEmployeeTextField;

    @FXML
    private TextField patronymicEmployeeTextField;

    @FXML
    private Label errorLabel;

    /**
     * если нажать кнопку Отмена
     * закроется окно
     */
    @FXML
    public void OnCancelVisitor() {
        stage.close();
    }

    /**
     * если нажали кнопку Ок
     * проверяем корректность ввода информации о посетителе
     * проверям флаг на черный список
     * true- устанавливаем флаг объекта visitor на true и обновляем данные
     * false- проверяем данные сотрудника
     * проверяем существует ли такой сотрудник
     * обновляем список
     * закрываем окно
     */
    @FXML
    public void onOKVisitor() {
        errorLabel.setVisible(false);

        //Проверка данных сотрудника
        if (checkInformation(lastNameTextField.getText())) return;
        visitor.setLastName(lastNameTextField.getText().trim());

        if (checkInformation(firstNameTextField.getText())) return;
        visitor.setFirstName(firstNameTextField.getText().trim());

        if (checkInformation(patronymicTextField.getText())) return;
        visitor.setPatronymic(patronymicTextField.getText().trim());

        if (checkInformation(pasportDateTextField.getText())) return;
        visitor.setPasportData(pasportDateTextField.getText().trim());

        //проверка флага
        if (checkBlack) {
            visitor.setBlack(true);
            visitor.setEmployeeLastName("null");
        } else {
            visitor.setBlack(false);
            //проверяем, находится ли вводимый посетитель в черном списке
            //проходимся по всем посетителям
            for (Visitor temVisitor : allvisitor) {
                //сравниваем ФИО
                //сравниваем паспортные данные
                //находится ли он в черном списке
                if (temVisitor.getFIO().toLowerCase().equals(visitor.getFIO().toLowerCase()) &&
                        temVisitor.getBlack() &&
                        temVisitor.getPasportData().toLowerCase().equals(visitor.getPasportData().toLowerCase())) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Данный посетитель находится в черном списке");
                    return;
                }
            }
            //создаем временного сотрудника
            //чтобы проверить , есть ли такой сотрудник
            // и если есть, то записать, к каому отдулу он принадлежит
            //чтобы потом использовать эти данные для постоения графика
            if (checkInformation(lastNameEmployeeTextField.getText())) return;
            if (checkInformation(firstNameEmployeeTextField.getText())) return;
            if (checkInformation(patronymicEmployeeTextField.getText())) return;
            Employee employee = new Employee(lastNameEmployeeTextField.getText().trim(),
                    //исключаем ошибки из-за пробелов по бокам
                    firstNameEmployeeTextField.getText().trim(),
                    patronymicEmployeeTextField.getText().trim());

            //переменная для того, чтобы понять
            //есть ли такой сотрудник
            boolean check = false;
            for (Employee tempEmployee : allemployee) {
                if (tempEmployee.getFIO().equals(employee.getFIO())) {
                    employee.setSubdivision(tempEmployee.getSubdivision());
                    //true- если есть
                    check = true;
                    break;
                }
            }
            if (check) {
                visitor.setEmployeeLastName(employee.getLastName());
                visitor.setEmployeeFirstName(employee.getFirstName());
                visitor.setEmployeeSubdivision(employee.getSubdivision());
                visitor.setEmployeePatronymic(employee.getPatronymic());
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Нет такого сотрудника");
                return;
            }
        }
        //добавляем посетителя в список
        allvisitor.addFirst(visitor);

        //записываем список в файл
        mainApp.getVisitorsStorage().writeElement();

        //закрываем диалогое окно
        stage.close();
        //флаг
        //true -показываем весь черный список
        //false- показываем список всех посетителей
        if (checkBlack) {
            mainApp.loadMainWindowJournal(MainWindowJournalController.ShowVisitor.BLACK);
        } else {
            mainApp.loadMainWindowJournal(MainWindowJournalController.ShowVisitor.ALL);
        }
    }

    /**
     * провека вводимой информации
     * @param string вводимая информация
     * @return true- если есть ошибка в информации
     */
    public boolean checkInformation(String string) {
        //проверка на пустоту строки
        if (string.equals("")) {
            errorLabel.setVisible(true);
            errorLabel.setText("вы не ввели всю информацию");
            return true;
        }
        //провека , чтобы не было данного элемента
        //так как он ифпользуется для разделения информации
        //при записи списка в файл
        if (string.contains("|")) {
            errorLabel.setVisible(true);
            errorLabel.setText("данные не должны включать \"|\" ");
            return true;
        }
        return false;
    }

    /**
     * передаем введенные данные в соответствующие поля
     * @param stage диалогое окно
     * @param checkBlack флаг на черный список
     */
    public void init(Stage stage, Boolean checkBlack) {
        this.stage = stage;
        this.visitor = new Visitor();
        this.checkBlack = checkBlack;
        if (checkBlack) {
            infoEmployeeGridPane.setVisible(false);
        }
    }

}