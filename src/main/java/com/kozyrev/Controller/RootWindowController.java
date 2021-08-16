package com.kozyrev.Controller;


import javafx.fxml.FXML;

import static com.kozyrev.Controller.MainWindowJournalController.ShowVisitor.*;
import static com.kozyrev.Main.MainApp.mainApp;

public class RootWindowController {

    /**
     * показать посетителей по сотруднику
     */
    @FXML
    public  void searchByEmployee() {
        mainApp.loadMainWindowJournal(BY_EMPLOYEE);
    }

    /**
     * показать всех сотрудников
     */
    @FXML
    public void showAllEmployee() {
        mainApp.loadMainWindowJournal(ALLEMPLOYEE);
    }

    /**
     * показать всех посетителей
     */
    @FXML
    public void showAllJournal() {
        mainApp.loadMainWindowJournal(ALL);
    }

    /**
     * показать черный список посетителей
     */
    @FXML
    public void showBlackList() { mainApp.loadMainWindowJournal(BLACK); }

    /**
     * показать посетителей в зависимости от выбранной даты
     */
    @FXML
    public void showByDate() {
        mainApp.loadMainWindowJournal(BY_DATE);
    }

    /**
     * показать сотрудников по отделам, к которым они относятся
     */
    @FXML
    public void showBySubdivision() {
        mainApp.loadMainWindowJournal(BY_SUBDIVISIONEMPLOYEE);
    }

    /**
     * показать посетителей в соответствии с введенным промежутком времени
     */
    @FXML
    public void showByTime() {
        mainApp.loadMainWindowJournal(BY_TIME);
    }

    /**
     * показать график посещаемости
     */
    @FXML
    public void showGraph() {mainApp.loadGraphWindow();}

}
