package com.kozyrev.Controller;

import com.kozyrev.Fields.Employee;
import com.kozyrev.Fields.Subdivision;
import com.kozyrev.Fields.Visitor;
import com.kozyrev.List.EmployeesList;
import com.kozyrev.List.SubdivisionList;
import com.kozyrev.List.VisitorsList;
import com.kozyrev.Main.MainApp;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import static java.nio.charset.StandardCharsets.UTF_8;


public class MainWindowJournalController {
    //объект контроллера для отображения всех таблицы
    //нужен для испольхования методов данного контроллера
    private VisitorsJournalController vjContr;
    //список всех посетителей
    public VisitorsList allvisitors;
    //список всех сотрудников
    public EmployeesList allemployee;
    //список всех отделов
    public SubdivisionList allsubdivision;
    //объект класса Visitor
    public static Visitor visitor;
    //наименование удаляемого отдела
    public String removeSub;

    public VisitorsList tempVis= new VisitorsList();

    /**
     * типы отображаемой информации
     */
    public enum ShowVisitor {
        /**
         * показать всех посетителей
         */
        ALL,
        /**
         * показать посетителей в зависимости от даты
         */
        BY_DATE,
        /**
         * показать посетителей в зависимости от времени
         */
        BY_TIME,
        /**
         * показать посетителей в зависимости от сотрудника
         */
        BY_EMPLOYEE,
        /**
         * показать всех сотрудников
         */
        ALLEMPLOYEE,
        /**
         * показать сотрудников по отделам
         */
        BY_SUBDIVISIONEMPLOYEE,
        /**
         * показать черный список
         */
        BLACK
    }

    private String[] byDateType = {"за весь день", "после", "перед"};
    private String[] byTypeFIO = {"фамилия", "имя", "отчество"};
    private int number = 0;

    /**
     * кнопка для добавления нового посетителя
     */
    @FXML
    private Button buttonNEWVisitor;

    /**
     * кнопка для показа посетителей в зависимости от времени
     */
    @FXML
    private Button buttonShowByTime;

    /**
     * кнопа для добавления в черный список посетителя
     */
    @FXML
    private Button buttonAddBlack;

    /**
     * кнопка для добавления нового посетителя
     */
    @FXML
    private Button buttonNEWEmplyoeee;

    /**
     * панель в которой будут отобразаться списки
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * виджет, в который добавляются панели с информацией об отдельный сотрудниках
     */
    @FXML
    private VBox employeeVBox;

    /**
     * виджет для выбора даты
     */
    @FXML
    private DatePicker datePicker;

    /**
     * виджет, в котором находятся текстовые поля для ввода диапазона врмени
     */
    @FXML
    private HBox timeHBox;

    //текстовые поля для ввода диапазона врмени
    @FXML
    private TextField hourStartTextField;

    @FXML
    private TextField minuteStartTextField;

    @FXML
    private TextField hourEndTextField;

    @FXML
    private TextField minuteEndTextField;

    @FXML
    private Button saveButton;

    /**
     * виджет выпадающего списка со всеми отделами организации
     */
    @FXML
    private ChoiceBox<String> subdivisionChoiceBox;

    /**
     * виджет в котором находится поле да информации для поиска
     * и кнопка для поиска
     */
    @FXML
    private HBox searchHBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label errorLabel;

    /**
     * выпадающее окно с выбором типа показа всех посетителей относительно даты
     * за данный день, после этой даты, перед этой датой
     */
    @FXML
    private ChoiceBox<String> morelessChoiceBox;

    /**
     * кнопка удаления посетителя из черного спика
     */
    @FXML
    public Button buttonRemoveBlack;

    /**
     * кнопка добавления нового отдела
     */
    @FXML
    private Button buttonAddNewSubdivision;

    /**
     * кнопка удаления отдела
     */
    @FXML
    private Button buttonRemoveSubdivision;

    /**
     * если нажали кнопку удаления отдела из списка
     */
    @FXML
    public void removeSubdivision() {
        for (Subdivision subdivision : allsubdivision) {
            //сравниваем данный отдел с отделом, который выбрал пользователь
            if (subdivision.getSubdivision().equals(removeSub)) {
                allsubdivision.remove(subdivision);
                //загрузка списка отделов в файл
                MainApp.mainApp.getSundivisionStorage().writeElement();
                //показываем всех посетителей
                show(ShowVisitor.ALLEMPLOYEE);
                return;
            }
        }
    }

    /**
     * если нажали кнопку добавления нового отдела
     */
    @FXML
    public void addNewSubdivision() {
        //загрузка диалогового окна для добавления новго отдела
        MainApp.mainApp.laodEditSubdivisionWindow();
    }

    /**
     * если нажали кнопку добавления нового посетителя
     */
    @FXML
    void addNewVisitor() {
        //загрузка диалогового окна для добавления нового посетителя
        MainApp.mainApp.loadEditVisitorWindow(false);
    }

    /**
     * если нажали кнопку добавления посетителя в черный список
     */
    @FXML
    void onAddInBlackList() {
        //загрузка диалогового окна для добавления посетителя в черный список
        MainApp.mainApp.loadEditVisitorWindow(true);
    }

    /**
     * метод для отображения посетителей относительно даты
     */
    @FXML
    void onDatePicker() {
        //берем введенную дату
        //и переводим данные в тип данных Date
        String date = datePicker.getValue().toString();
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date temp = Date.from(instant);

        //метод для отлеживания состояния выпадающего списка
        // со списком типов вывода посетителей относительно даты
        //за данный день, после этой даты, перед этой датой
        morelessChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                   if(!tempVis.isEmpty())tempVis.clear();
                    errorLabel.setVisible(false);
                    //формируем список относительно выбранного типа
                    switch (newValue) {
                        //добавляем в список если даты совпадают
                        case "за весь день":
                            for (Visitor visitor : allvisitors) {
                                if (visitor.getDate().equals(date)) tempVis.addLast(visitor);
                            }
                            break;
                        case "после":
                            //сравниваем даты
                            //и добавляем , если введенная дата раньше данной даты
                            for (Visitor visitor : allvisitors) {
                                try {
                                    Date dateCompare = new SimpleDateFormat("yyyy-MM-d").parse(visitor.getDate());
                                    if (temp.before(dateCompare)) tempVis.addLast(visitor);
                                } catch (ParseException e) {
                                    e.getErrorOffset();
                                }
                            }
                            break;
                        case "перед":
                            //сравниваем даты
                            //и добавляем , если введенная дата позже данной даты
                            for (Visitor visitor : allvisitors) {
                                try {
                                    Date dateCompare = new SimpleDateFormat("yyyy-MM-d").parse(visitor.getDate());
                                    if (temp.after(dateCompare)) tempVis.addLast(visitor);
                                } catch (ParseException e) {
                                    e.getErrorOffset();
                                }

                            }
                            break;
                    }
                    showVisitors(tempVis,false);
                }
        );
        morelessChoiceBox.getSelectionModel().clearSelection();
        morelessChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * метод для отображения пользователей относительно данных сотрудника
     */
    @FXML
    public void onSearchByEmployee() {
        //в выпадающий списко записываем
        // "фамилия","имя","отчество"
        //в зависимости от выбранного пункта
        //будет происходить поиск совпадений данных сотрудников
        morelessChoiceBox.setVisible(true);
        morelessChoiceBox.setItems(FXCollections.observableArrayList(byTypeFIO));
        String dataEmployee = searchTextField.getText().toLowerCase().trim();
        VisitorsList employeeVisitor = new VisitorsList();
        //метод отслеживания выбранного пункта
        // и в соответсвии формируется список посетителей
        morelessChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    errorLabel.setVisible(false);
                    if (dataEmployee.equals("")){
                        errorLabel.setVisible(true);
                        errorLabel.setText("введите данные сотрудника");
                        return;
                    }
                    //очищаем список
                    employeeVisitor.clear();
                    if(newValue==null)return;
                    switch (newValue) {
                        case "фамилия":
                            //добавляем в список посетителя
                            //если введенная фамилия совпадает с фамилией сотруднка, к которому пошел посетитель
                            for (Visitor visitor : allvisitors) {
                                if (visitor.getEmployeeLastName().toLowerCase().equals(dataEmployee))
                                    employeeVisitor.addLast(visitor);
                            }
                            number = 0;
                            break;
                        case "имя":
                            //добавляем в список посетителя
                            //если введенное имя совпадает с именем сотруднка, к которому пошел посетитель
                            for (Visitor visitor : allvisitors) {
                                if (visitor.getEmployeeFirstName().toLowerCase().equals(dataEmployee))
                                    employeeVisitor.addLast(visitor);
                            }
                            number = 1;
                            break;
                        case "отчество":
                            //добавляем в список посетителя
                            //если введенное отчество совпадает с отчеством сотруднка, к которому пошел посетитель
                            for (Visitor visitor : allvisitors) {
                                if (visitor.getEmployeePatronymic().toLowerCase().equals(dataEmployee))
                                    employeeVisitor.addLast(visitor);

                            }
                            number = 2;
                            break;
                    }
                    showVisitors(employeeVisitor, false);
                }
        );
        morelessChoiceBox.getSelectionModel().clearSelection();
        //нужен для того, чтобы , когда мы нажали кнопку показа посетителей
        //предыдущий выбранный пункт сохранился
        morelessChoiceBox.getSelectionModel().select(byTypeFIO[number]);
    }

    /**
     * метод для отображения списка посетителей относительно врменного диапазона
     */
    @FXML
    public void onTime() {
        errorLabel.setVisible(false);
        //введенная дата
        String date = datePicker.getValue().toString();
        //проверка кооректности введенной даты
        if (checkTime()) return;
        if(!tempVis.isEmpty())tempVis.clear();
        for (Visitor visitor : allvisitors) {
            //если введенная дата соправдает с данной
            if (visitor.getDate().equals(date)) {
                String[] time = visitor.getTime().split(":");
                //если час регистарции посетителя находится в данном промежутке
                if (Integer.parseInt(time[0]) > Integer.parseInt(hourStartTextField.getText()) &&
                        Integer.parseInt(time[0]) < Integer.parseInt(hourEndTextField.getText())) {
                    tempVis.addLast(visitor);
                    //если час регистрации совпадает с гарничными данными
                    //сравниваем минуты диапазона и данного времени
                } else if (Integer.parseInt(time[0]) == Integer.parseInt(hourStartTextField.getText()) &&
                        Integer.parseInt(time[1]) >= Integer.parseInt(minuteStartTextField.getText()) ||
                        Integer.parseInt(time[0]) == Integer.parseInt(hourEndTextField.getText()) &&
                                Integer.parseInt(time[1]) <= Integer.parseInt(minuteEndTextField.getText())) {
                    tempVis.addLast(visitor);
                }
            }
        }
        showVisitors(tempVis,false);
    }

    /**
     * проверка на вводимое время
     *
     * @return true- если есть ошибка
     */
    public boolean checkTime() {
        //проверка на пустую строку
        if (hourEndTextField.getText().equals("") || hourStartTextField.getText().equals("") ||
                minuteEndTextField.getText().equals("") || minuteStartTextField.getText().equals("")) {
            errorLabel.setVisible(true);
            errorLabel.setText("вы не ввели диапазон полностью");
            return true;
        }
        //проверка на то, чтобы были введены числа
        if (!hourEndTextField.getText().matches("[0-9]*") || !hourStartTextField.getText().matches("[0-9]*")) {
            errorLabel.setVisible(true);
            errorLabel.setText("время должно состоять только из цифр");
            return true;
        }
        //часы не превышают 23
        if (Integer.parseInt(hourEndTextField.getText()) > 23 || Integer.parseInt(hourStartTextField.getText()) > 23) {
            errorLabel.setVisible(true);
            errorLabel.setText("часы не должены превышать 12");
            return true;
        }
        //час начала диапазона должен быть меньше часа окончания диапазона
        if (Integer.parseInt(minuteStartTextField.getText()) > Integer.parseInt(minuteEndTextField.getText())) {
            errorLabel.setVisible(true);
            errorLabel.setText("время начала диапазона должно быть раньше , чем время окончания диапазона");
            return true;
        }
        //проверка на то, чтобы были введены числа
        if (!minuteStartTextField.getText().matches("[0-9]*") || !minuteEndTextField.getText().matches("[0-9]*")) {
            errorLabel.setVisible(true);
            errorLabel.setText("время должно состоять только из цифр");
            return true;
        }
        //минуты не превышают 59
        if (Integer.parseInt(minuteEndTextField.getText()) > 59 || Integer.parseInt(minuteStartTextField.getText()) > 59) {
            errorLabel.setVisible(true);
            errorLabel.setText("минуты не должны превышать 59");
            return true;
        }
        return false;
    }

    @FXML
    private void onSaveButton(){
        //создать объект файлового диалога для указания пути и имени файла
        FileChooser fileChooser = new FileChooser();
        //создать допустимые расширения для сохраняемого файла "*.txt"
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "INFO(*.txt)", "*.txt");
        //добавить допустимые расширения в файловый диалог
        fileChooser.getExtensionFilters().add(extFilter);

        //показать диалоговое окно для выбора и сохрания файла и привязать его к главному окну приложения
        File file = fileChooser.showSaveDialog(MainApp.mainApp.getPrimaryStage());

        //если файл создан
        if (file != null) {
            //если файл не имеет допустимое расширение ".txt"
            if (!file.getPath().endsWith(".txt")) {
                //создать файл с допустимым расширением ".txt" и указанным в файловом диалоге именем
                file = new File(file.getPath() + ".txt");
            }
            //сохраниить недельное расписание в созданном файле
            writeFile(file);
        }
    }

    /**
     * запись списка в созданный файл
     * @param file-созданный файл
     */
    public void writeFile( File file) {
        try {
            //создаем строку для списка посетителей
            StringBuilder string = new StringBuilder("");
            //проверка на пустоту списка
            if (!tempVis.isEmpty()) {
                for (Visitor visitor : tempVis) {
                    //в строку добавляем только тех посеттителей, который не находятся в черном списке
                    if (!visitor.getEmployeeLastName().equals("null")) string.append(visitor).append("\n\n");
                }
                byte[]infoToBytes=string.toString().getBytes(UTF_8);
                Files.write(file.toPath(),infoToBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBlack() {
        showVisitors(allvisitors, true);
        vjContr.removeFromBlack();
    }

    /**
     * метод для удаления посетителя из черного списка
     */
    @FXML
    public void onRemoveFromBlack() {
        //проверка на то, что пользователь выбрал посетителя,которого необходимо удалить из списка
        if (this.visitor != null) {
            errorLabel.setVisible(false);
            this.visitor.setBlack(false);
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("выберете посетителя, которого надо удалить из списка");
        }
        //записываем список в файл
        MainApp.mainApp.getVisitorsStorage().writeElement();
        //показываем весь черный список
        show(ShowVisitor.BLACK);

    }

    /**
     * показываем весь список сотрудников
     */
    public void showAllEmployee() {
        errorLabel.setVisible(allemployee.isEmpty());
        errorLabel.setText("список пуст");

        showEmployee(allemployee);
    }

    /**
     * формируем список сотрудников относительно выбранного отдела
     */
    public void showEmployeeBySubdivision() {
        //создаем список всех отделов
        ObservableList<String> allsubdivision = FXCollections.observableArrayList();
        for (Subdivision subdivision : this.allsubdivision) {
            allsubdivision.add(subdivision.getSubdivision());
        }
        //отслеживаем изменение выбранного пункта выплывающем списке
        subdivisionChoiceBox.setItems(allsubdivision);
        subdivisionChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    EmployeesList subEmployee = new EmployeesList();
                    //формируем список сотрудника относительно выбранного отдела
                    for (Employee employee : allemployee) {
                        if (employee.getSubdivision().equals(newValue)) subEmployee.addFirst(employee);
                    }
                    //данное поле нужно для того ,чтобы знать , как отдел пользователь хочет удалить
                    removeSub = subdivisionChoiceBox.getValue();
                    showEmployee(subEmployee);
                }
        );
        subdivisionChoiceBox.getSelectionModel().clearSelection();
        subdivisionChoiceBox.getSelectionModel().selectFirst();
        removeSub = subdivisionChoiceBox.getValue();
    }

    /**
     * показываем панели с информацией о сотрудниках
     *
     * @param employees список сотрудников
     */
    public void showEmployee(EmployeesList employees) {
        //очищаем виджет , в которм будем размещать панели с информацией о стодрудниках
        employeeVBox.getChildren().clear();
        for (Employee employee : employees) {
            try {
                //загрузить из fxml панель для информации о сотруднике
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("/fxml/EmployeeInformation.fxml"));
                AnchorPane employeeLayOut = loader.load();

                //добавляем панель с виджет
                employeeVBox.getChildren().add(employeeLayOut);
                ////создаем объект контроллера для показа данных сотрудника
                // и присваиваем значение полученное из загруженного fxml файла
                EmployeeInformationController employeeInformationController = loader.getController();
                employeeInformationController.showEmployeeInformation(employee);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * если нажали кнопку добавления нового сотрудника
     */
    @FXML
    public void onAddNewEmployee() {
        //загружаем диалоговое окно для добавления сотрудника
        MainApp.mainApp.loadEditEmployeeWindow(null);
    }

    /**
     * основной метод для показа данных в соответсвии с вводимым типом
     *
     * @param showVisitor вводимый тип
     */
    public void show(ShowVisitor showVisitor) {
        //присваиваем соответсвующим полям данный соответсвтующих списков
        allvisitors = MainApp.mainApp.getVisitorsStorage().getAllVisitors();
        allemployee = MainApp.mainApp.getEmployeesStorage().getAllEmployees();
        allsubdivision = MainApp.mainApp.getSundivisionStorage().getAllSubdivision();

        buttonNEWVisitor.setVisible(false);

        //выбор показа списка в зависимости от типа
        switch (showVisitor) {
            case BY_DATE:
                //показ виджетов ввода даты
                //устанавливае текущую дату
                //показываем выпадающий список с типом отображения посетителей относительно даты
                datePicker.setVisible(true);
                datePicker.setValue(LocalDate.now());
                morelessChoiceBox.setVisible(true);
                morelessChoiceBox.setItems(FXCollections.observableArrayList(byDateType));
                saveButton.setVisible(true);
                //метод для формирования списка посетителей относительно даты
                onDatePicker();
                break;

            case BY_EMPLOYEE:
                //показываем виджет для ввода данных сотрудника
                searchHBox.setVisible(true);
                break;

            case BY_TIME:
                //показываем виджет для ввода даты и времени
                datePicker.setVisible(true);
                datePicker.setValue(LocalDate.now());
                timeHBox.setVisible(true);
                buttonShowByTime.setVisible(true);
                saveButton.setVisible(true);
                break;

            case BLACK:
                //показываем кнопку для добавления в черный список и удаления из черного списка
                buttonAddBlack.setVisible(true);
                buttonRemoveBlack.setVisible(true);
                showBlack();
                break;

            case ALLEMPLOYEE:
                //показываем кнопку добавления новго сотрудника и нового отдела
                buttonNEWEmplyoeee.setVisible(true);
                buttonAddNewSubdivision.setVisible(true);
                //показываем всех сотрудников
                showAllEmployee();
                break;

            case BY_SUBDIVISIONEMPLOYEE:
                //показываем выпадающий список со всеми отделами
                //показываем кнопку добавления и удаления отдела
                subdivisionChoiceBox.setVisible(true);
                buttonAddNewSubdivision.setVisible(true);
                buttonRemoveSubdivision.setVisible(true);
                showEmployeeBySubdivision();
                break;

            default:
                //показываем кнопку для добавления новго посетителя
                buttonNEWVisitor.setVisible(true);
                //формируем таблицу всех посетителей
                showVisitors(allvisitors, false);
        }
    }

    /**
     * метод для отображения таблицы в центральной части корневой разметки
     *
     * @param visitors   список посетителей
     * @param checkBLACK флаг на черный список
     */
    public void showVisitors(VisitorsList visitors, boolean checkBLACK) {
        //удаляем предыдующую таблицу из панели
        scrollPane.setContent(null);
        //проверка на пустой список
        if (visitors.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("список пустой");
            return;
        }
        try {
            //загрузить из fxml панель для отображения таблица посетителей
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/VisitorsJournal.fxml"));
            AnchorPane visLayout = loader.load();
            //присваиваем котнроллеру значение загруженное из fxml файла
            vjContr = loader.getController();
            //показываем загруженную панель в центре корневой разметки
            scrollPane.setContent(visLayout);
            //метод для создания таблицы и передечи ей данных
            vjContr.showJournal(visitors, checkBLACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
