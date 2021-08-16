package com.kozyrev.Controller;

import com.kozyrev.Fields.Visitor;
import com.kozyrev.List.VisitorsList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static com.kozyrev.Controller.MainWindowJournalController.visitor;

/**
 * класс,отвечающий за вывод информации о посетителях в таблицу
 */
public class VisitorsJournalController {

    /**
     * объект контроллера основной панели
     * нужен для использования метода в контроллера основной панели
     */
    private MainWindowJournalController mwContr = new MainWindowJournalController();

    //элементы таблицы

    //нужен для того,чтобы потом непоказывать колонку с данными сотрудников
    //когда выводим черый список посетителей
    @FXML
    private TableColumn<Visitor, String> EmployeeColumn;

    //вся таблица
    @FXML
    private TableView<Visitor> journalTableView;

    //колонки с информацией о посетителях
    @FXML
    private TableColumn<Visitor, String> lastNameVisitorTableColumn;

    @FXML
    private TableColumn<Visitor, String> firstNameVisitorTableColumn;

    @FXML
    private TableColumn<Visitor, String> patronymicVisitorTableColumn;

    @FXML
    private TableColumn<Visitor, String> pasportDataTableColumn;

    //колнки с информацией о сотрудниках, к которым идут посетители
    @FXML
    private TableColumn<Visitor, String> lastNameEmployeeTableColumn;

    @FXML
    private TableColumn<Visitor, String> firstNameEmployeeTableColumn;

    @FXML
    private TableColumn<Visitor, String> patronymicEmployeeTableColumn;

    //колонки даты и времени регистарции посетителя
    @FXML
    private TableColumn<Visitor, String> dateTableColumn;

    @FXML
    private TableColumn<Visitor, String> timeTableColumn;

    /**
     * основной метод для вывода таблицы посетителей
     * @param visitors список всех посетителей
     * @param cheсkBlack флаг на черный список
     *                   true- если выводим черный список
     */
    public void showJournal(VisitorsList visitors, boolean cheсkBlack) {

        //создаем список для вывода на экран
        ObservableList<Visitor> observableVisitor = FXCollections.observableArrayList();
        //цикл для формирования списка в соответствии с флагом
        for (Visitor visitor : visitors) {
            //если флаг true
            //и человек находится в черном списке
            if (cheсkBlack && visitor.getBlack()) {
                observableVisitor.add(visitor);
                //скрываем колонку с данными сотрудников
                //так как оан в этой случае не нужна
                EmployeeColumn.setVisible(false);

                //флаг false
                //и у объекта посетителя есть фамилия сотрудника, к которому он идет
            } else if (!cheсkBlack && !visitor.getEmployeeLastName().equals("null")) {
                observableVisitor.add(visitor);
            }
        }
        //установление типов выводимый информации для каждой колонки
        //в соответствии с полями объекта класса Visitor
        lastNameVisitorTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameVisitorTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        patronymicVisitorTableColumn.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        pasportDataTableColumn.setCellValueFactory(new PropertyValueFactory<>("pasportData"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        firstNameEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("employeeFirstName"));
        lastNameEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("employeeLastName"));
        patronymicEmployeeTableColumn.setCellValueFactory(new PropertyValueFactory<>("employeePatronymic"));

        //устанавливаем все данные в основую таблицу
        journalTableView.setItems(observableVisitor);

    }

    /**
     * метод для отслеживания строчки в таблице
     * на которую нажали
     * чтобы определить какого посетителя пользователь хочет удалить из черного списка
     */
    public void removeFromBlack() {
        TableView.TableViewSelectionModel<Visitor> selectionModel = journalTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Visitor>() {
            @Override
            public void changed(ObservableValue<? extends Visitor> observable, Visitor oldValue, Visitor newValue) {
                //проверка на пустую строку
                if (newValue != null) {
                    //присваиваем статической переменной значение выбранного пользователя
                    //для того, чтобы потом поменять его состояние в черном спике
                    visitor=newValue;
                }
            }
        });
    }
}
