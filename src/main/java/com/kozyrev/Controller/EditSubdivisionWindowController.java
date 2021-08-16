package com.kozyrev.Controller;

import com.kozyrev.Fields.Subdivision;
import com.kozyrev.List.SubdivisionList;
import com.kozyrev.Main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.kozyrev.Main.MainApp.mainApp;

/**
 * класс по вводу нового отдела
 */
public class EditSubdivisionWindowController {
    //диалогое окно
    private Stage stage;
    //данные отдела
    private Subdivision subdivision;
    //все отделы
    private SubdivisionList allSudivision= MainApp.mainApp.getSundivisionStorage().getAllSubdivision();

    //поля для воода названия отдела
    //и выбора цвета отдела
    @FXML
    private TextField nameSubdivisionTextField;

    @FXML
    private ColorPicker colorSubdivision;


    @FXML
    private Label errorLabel;

    /**
     * нажали Отмена
     * окно закрылось
     */
    @FXML
    void onCancel() {
        stage.close();
    }

    /**
     * назали кнопку Ок
     * отдел добавился
     */
    @FXML
    void onOk() {
        errorLabel.setVisible(false);
        //проверка на пустую строку
        if(nameSubdivisionTextField.getText().isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText("введите название отдела");
            return;
        }
        //провека , чтобы не было данного элемента
        //так как он ифпользуется для разделения информации
        //при записи списка в файл
        if(nameSubdivisionTextField.getText().contains("|")){
            errorLabel.setVisible(true);
            errorLabel.setText("нельзя использовать \"|\" в названии ");
            return;
        }
        subdivision.setSubdivision(nameSubdivisionTextField.getText().trim());
        //проверка на существования такого отдела в списке всех отделов
        for(Subdivision subdivision:allSudivision){
            if(subdivision.getSubdivision().toLowerCase().equals(this.subdivision.getSubdivision().toLowerCase())){
                errorLabel.setVisible(true);
                errorLabel.setText("Такой отдел уже существует");
                return;
            }

        }

        subdivision.setColor(colorSubdivision.getValue());

        //добавляем новый отдел
        allSudivision.addFirst(subdivision);

        //записываем список в файл
        MainApp.mainApp.getSundivisionStorage().writeElement();

        //закрываем диалогое окно
        stage.close();

        //показываем всех сотрудников
        mainApp.loadMainWindowJournal(MainWindowJournalController.ShowVisitor.ALLEMPLOYEE);
    }

    /**
     * передаем введенные данные в соответствующие поля
     * @param stage диалогое окно
     */
    public void init(Stage stage){
        this.stage=stage;
        this.subdivision=new Subdivision();
    }

}
