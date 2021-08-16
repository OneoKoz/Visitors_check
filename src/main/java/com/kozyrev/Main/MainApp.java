package com.kozyrev.Main;

import com.kozyrev.Controller.*;

import com.kozyrev.Fields.Employee;
import com.kozyrev.Storage.EmployeesStorage;
import com.kozyrev.Storage.SubdivisionStorage;
import com.kozyrev.Storage.VisitorsStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.kozyrev.Controller.MainWindowJournalController.ShowVisitor;

import java.io.IOException;
import java.io.InputStream;

/**
 * Главный запускной класс JavaFx приложения
 */
public class MainApp extends Application {

    /**
     * Статическая переменная, которой хранится бъект главного класса приложения для облегчения доступа к классу
     */
    public static MainApp mainApp;
    /**
     * объект главного окна приложения
     */
    private Stage primaryStage;
    /**
     * корневая азметка приложения
     */
    private BorderPane rootWindow;
    /**
     * объект для хранения , записи и чтения из файла списка сотрудников
     */
    private EmployeesStorage employeesStorage = new EmployeesStorage();
    /**
     * объект для хранения , записи и чтения из файла списка посетителей
     */
    private VisitorsStorage visitorsStorage = new VisitorsStorage();
    /**
     * объект для хранения , записи и чтения из файла списка отделов кампании
     */
    private SubdivisionStorage subdivisionStorage=new SubdivisionStorage();

    /**
     * стартовый вывод главного окна приложения
     *
     * @param primaryStage объект главного окна приложения
     *
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainApp = this;
        this.primaryStage = primaryStage;
        //установления названия приложения
        this.primaryStage.setTitle("программное средство для учета посетителей организации");

        //добавление иконки проложения
        InputStream iconStream = getClass().getResourceAsStream("/images/icon.png");
        Image image = new Image(iconStream);
        this.primaryStage.getIcons().add(image);

        //инициализировать корневую разметку окна
        loadRootWindow();
    }

    /**
     * инициализировать корневую разметку окна
     */
    public void loadRootWindow() {
        try {
            //загрузить из fxml корневую разметку окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootWindow.fxml"));
            rootWindow = loader.load();

            //создаем сцену из корневой разметки
            Scene scene = new Scene(rootWindow);
            //передаем сцену главному окну приложения
            primaryStage.setScene(scene);
            //показываем журнал со всеми посетителями
            loadMainWindowJournal(ShowVisitor.ALL);

            //показываем главное окно приложения
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * главный метод для работы с данными всех посетителей и всех сотрудников
     * @param showVisitor нужен для определения типа той информации, которую необходимо вывести
     */
    public void loadMainWindowJournal(ShowVisitor showVisitor) {
        try {
            //загрузить из fxml главну панель
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/MainWindowJournal.fxml"));
            AnchorPane mainWindow = loader.load();

            //устанавливаем главную панель в центре корневой разметки
            rootWindow.setCenter(mainWindow);
            //создаем объект контроллера главной панели
            //и присваиваем значение полученное из загруженного fxml файла
            MainWindowJournalController mainWindowJournalController = loader.getController();
            //показ данный в соответсвии с вводимым типом
            mainWindowJournalController.show(showVisitor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * показываем диалоговое окно для редактирования данных сотрудника
     * @param employee данные редактируемого сотрудника
     *                 если ==null то мы создаем нового сотрудника
     */
    public void loadEditEmployeeWindow(Employee employee) {
        try {
            //загрузить из fxml панель для добавления или редактирования данных сотрудников
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/EditEmployeeWindow.fxml"));
            AnchorPane editLayout= loader.load();

            //создаем новое окно
            Stage editStage = new Stage();
            //присваиваем название этому окну
            editStage.setTitle("Редактировать данные работника");
            editStage.initModality(Modality.WINDOW_MODAL);
            //привязываем окно к галвному окну приложения
            editStage.initOwner(primaryStage);
            //создаем сцену из загруженой панели
            Scene scene= new Scene(editLayout);
            //загружаем сцену в новое окно
            editStage.setScene(scene);

            //создаем объект контроллера авнели для добавления или редактирования данный сотрудников
            //и присваиваем значение полученное из загруженного fxml файла
            EditEmployeeWindowController eewContr= loader.getController();
            //метод для формирования тех данных,
            //которые необходимо вывести при открытии окна
            //передаем значение объета Employee
            //значение нового окна
            //чтобы потом была возможность закрыть открывшееся окно
            eewContr.init(editStage,employee);

            //показываем новое окно
            editStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод для добавления нового посетителя
     * @param checkBlack флаг на черный список нужен для того, чтобы понять
     *                   добавляем мы посетителя в черный список
     *                   или в журнал посетителей
     */
    public void loadEditVisitorWindow (Boolean checkBlack) {
        try {
            //загрузить из fxml панель для добавления данных посетителя
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/EditVisitorWindow.fxml"));
            AnchorPane editLayout= loader.load();

            //аналогичный алгоритм , как и для диалогового окна для сотрудника
            //создаем окно
            Stage editStage = new Stage();
            // ставим ему определенное название
            editStage.setTitle("Добавить посетителя");
            editStage.initModality(Modality.WINDOW_MODAL);
            //привязываем к главному окну
            editStage.initOwner(primaryStage);
            //создаем сцену из загруженной панели
            Scene scene= new Scene(editLayout);
            //загружаем сцену в созданное окно
            editStage.setScene(scene);
            //создаем объект контроллера панели для добавления данный посетителя
            //и присваиваем значение полученное из загруженного fxml файла
            EditVisitorWindowController evwContr= loader.getController();
            //метод для отобразения определенного количество
            //полей для воода информации
            //в соответствии с флагом на черный список
            evwContr.init(editStage,checkBlack);
            //показываем новое окно
            editStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод для добавления нового отдела в фирме
     */
    public void laodEditSubdivisionWindow () {
        try {
            //загрузить из fxml панель для добавления данных отдела
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/EditSubdivisionWindow.fxml"));
            AnchorPane editLayout= loader.load();

            //аналогичный алгоритм , как и для диалогового окна для сотрудника
            //создаем окно
            Stage editStage = new Stage();
            // ставим ему определенное название
            editStage.setTitle("Добавить отдел");
            editStage.initModality(Modality.WINDOW_MODAL);
            //привязываем к главному окну
            editStage.initOwner(primaryStage);
            //создаем сцену из загруженой панели
            Scene scene= new Scene(editLayout);
            //загружаем сцену в новое окно
            editStage.setScene(scene);
            //создаем объект контроллера панели для добавления данный отдела
            //и присваиваем значение полученное из загруженного fxml файла
            EditSubdivisionWindowController evwContr= loader.getController();
            //метод для иницаилизации окна
            evwContr.init(editStage);
            //показываем окно
            editStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод для отображения графика
     */
    public void loadGraphWindow(){
        try {
            //загрузить из fxml панель для отображения графика посещаемости
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/GraphWindow.fxml"));
            AnchorPane graphLayout= loader.load();
            //загружаем панель в центр корневой разметки
            rootWindow.setCenter(graphLayout);
            //создаем объект контроллера панели для отображения графика посещаемости
            //и присваиваем значение полученное из загруженного fxml файла
            GraphWindowController gwContr= loader.getController();
            //инициализируем начальные данные для отображения графика
            gwContr.init();


        }catch (IOException e){e.printStackTrace();}

    }

    /**
     * получить объект для хранения , записи и чтения из файла списка посетителя
     * @return объект для хранения , записи и чтения из файла списка посетителя
     */
    public VisitorsStorage getVisitorsStorage() {
        return visitorsStorage;
    }

    /**
     * получить объект для хранения , записи и чтения из файла списка сотрудников
     * @return объект для хранения , записи и чтения из файла списка сотрудников
     */
    public EmployeesStorage getEmployeesStorage() {
        return employeesStorage;
    }

    /**
     * получить объект для хранения , записи и чтения из файла списка отеделов
     * @return объект для хранения , записи и чтения из файла списка отделов
     */
    public SubdivisionStorage getSundivisionStorage(){return subdivisionStorage;}


    public Stage getPrimaryStage() {return primaryStage;}

    /**
     *  запустить приложение
     * @param args запускные параметры
     */
    public static void main(String[] args) {
        launch(args);
    }
}
