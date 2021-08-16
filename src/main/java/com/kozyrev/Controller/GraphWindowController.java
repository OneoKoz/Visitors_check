package com.kozyrev.Controller;

import com.kozyrev.Fields.Subdivision;
import com.kozyrev.Fields.Visitor;
import com.kozyrev.List.SubdivisionList;
import com.kozyrev.List.VisitorsList;
import com.kozyrev.Main.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * класс для посроения графика
 */
public class GraphWindowController {
    //объект класса для рисорания
    public Canvas canvas = new Canvas(900, 458);
    public GraphicsContext gc = canvas.getGraphicsContext2D();
    //список всех посетителей
    public VisitorsList visitors;
    //список всех отделов
    public SubdivisionList allsubdivision = MainApp.mainApp.getSundivisionStorage().getAllSubdivision();
    private ObservableList observableList = FXCollections.observableArrayList();
    public String[] month = {"Янв.", "Февр.", "Март", "Апр.", "Май", "Июнь", "Июль", "Авг.", "Сент.", "Окт.", "Ноябрь.", "Дек."};
    //массив для подсчета количества посетителей в соответствии с типом
    public int[] kol;
    //введенная дата
    public int[] date;
    //тип графика
    public int type;
    //самый популярный отдел
    public String mostPopular;
    //первый год посещения организации посетителем
    public int year;


    //выпадающий список с отделами
    //либо все отделы
    //лиюо какой-то один отдел
    @FXML
    private ChoiceBox<String> SecondChoiceBox;

    @FXML
    private TextField dateTextField;

    @FXML
    private Label popularLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Pane graphPane;

    /**
     * если нажать кнопку показать
     */
    @FXML
    void onShowGraph() {
        //очищается панель
        gc.clearRect(0, 0, 950, 458);
        graphPane.getChildren().clear();
        //оси координат
        gc.strokeLine(20, 0, 20, 430);
        gc.strokeLine(20, 430, 825, 430);
        errorLabel.setVisible(false);
        date = new int[2];
        //в зависимости от даты
        //формируется и тип
        //ничего- вывести график за все года
        //год- вывести график по месяцам за этот год
        //год и месяц - график по дням за этот месяц
        if (dateTextField.getText().equals("")) {
            type = 0;
            checkType();
            return;
        }
        //проверка на то, что год состоит только из цифр
        if (!dateTextField.getText().contains("-") && dateTextField.getText().matches("[0-9]*")) {
            date[0] = Integer.parseInt(dateTextField.getText());
            type = 1;
            checkType();
            return;
        }
        //проверка на то, что год и месяц это числа , месяц<12
        if (dateTextField.getText().contains("-")) {
            String str[] = dateTextField.getText().split("-");
            if (str.length == 2 && str[0].matches("[0-9]*") && str[1].matches("[0-9]*") &&
                    Integer.parseInt(str[1]) <= 12) {
                type = 2;
                date[0] = Integer.parseInt(str[0]);
                date[1] = Integer.parseInt(str[1]);
                checkType();
                return;
            }
        }
        errorLabel.setVisible(true);
        errorLabel.setText("вы неправильно ввели дату");
    }

    /**
     * инициализация выпадающего списка
     * и списка посетителей организации
     */
    public void init() {
        this.visitors = MainApp.mainApp.getVisitorsStorage().getAllVisitors();
        this.year = Integer.parseInt(visitors.get(visitors.size() - 1).getDate().split("-")[0]);
        //создаем ChoiceBox для выбора либо вывода графика для всех отделов
        //либо для того отдела, который мы выберем
        observableList.add("все отделы");
        for (Subdivision subdivision : allsubdivision) {
            observableList.add(subdivision.getSubdivision());
        }
        SecondChoiceBox.setItems(observableList);
        SecondChoiceBox.getSelectionModel().selectFirst();

    }

    /**
     * в зависимости от типа графика и выбранного в выпадающем списке пункта
     * формируется массив kol, в котором записывается количество посетителей в каждый элемент
     * в соответствии с типом графика
     */
    public void checkType() {
        //отслеживаем изменение состояния SecondChoiceBox
        if (date[0] == 0) {
            //если за все года то размер массива= год который сейчас - первый год +1 + количество отделов (для подсчета самого популярного)
            kol = new int[Integer.parseInt(visitors.get(0).getDate().split("-")[0]) - year + 1 + allsubdivision.size()];
        } else if (date[1] == 0) {
            //размер массива= 12 месяцев +количество отделов (для подсчета самого популярного)
            kol = new int[12 + allsubdivision.size()];
        } else {
            //размер массива=31 день(максимальное кодичество дней в месяце)+количество отделов (для подсчета самого популярного)
            kol = new int[31 + allsubdivision.size()];
        }
        //величина самого большое элемента массива
        int max = 0;
        for (Visitor visitor : visitors) {
            if (visitor.getBlack()) continue;
            //заносим данные в массив в соответствии с типом
            switch (type) {
                case 0:
                    //за все года
                    String[] date0 = visitor.getDate().split("-");
                    if (SecondChoiceBox.getValue().equals("все отделы") && !visitor.getEmployeeLastName().equals("null")) {
                        kol[Integer.parseInt(date0[0]) - year]++;
                        for (Subdivision subdivision : allsubdivision) {
                            //прибавляем в ячейки массива, предназначенный для выявления самого популярного отдела
                            if (visitor.getEmployeeSubdivision().equals(subdivision.getSubdivision())) {
                                kol[kol.length - 1 - allsubdivision.indexOf(subdivision)]++;
                                break;
                            }
                        }
                        //если график строим для конкретного отдела
                    } else if (visitor.getEmployeeSubdivision().equals(SecondChoiceBox.getValue())) {
                        kol[Integer.parseInt(date0[0]) - year]++;
                    }
                    //проверяем на максимальное число
                    if (max < kol[Integer.parseInt(date0[0]) - year]) max = kol[Integer.parseInt(date0[0]) - year];
                    break;
                case 1:
                    //за год
                    String[] date = visitor.getDate().split("-");
                    if (SecondChoiceBox.getValue().equals("все отделы") &&
                            Integer.parseInt(date[0]) == this.date[0] &&
                            !visitor.getEmployeeLastName().equals("null")) {
                        kol[Integer.parseInt(date[1]) - 1]++;
                        for (Subdivision subdivision : allsubdivision) {
                            //прибавляем в ячейки массива, предназначенный для выявления самого популярного отдела
                            if (visitor.getEmployeeSubdivision().equals(subdivision.getSubdivision())) {
                                kol[kol.length - 1 - allsubdivision.indexOf(subdivision)]++;
                                break;
                            }
                        }
                        //если график строим для конкретного отдела
                    } else if (visitor.getEmployeeSubdivision().equals(SecondChoiceBox.getValue()) &&
                            Integer.parseInt(date[0]) == this.date[0]) {
                        kol[Integer.parseInt(date[1]) - 1]++;
                    }
                    //проверяем на максимальное число
                    if (max < kol[Integer.parseInt(date[1]) - 1]) max = kol[Integer.parseInt(date[1]) - 1];
                    break;
                case 2:
                    //за месяц
                    String[] date1 = visitor.getDate().split("-");
                    //проверка введенного месяца и данного месяца посетителя
                    if (SecondChoiceBox.getValue().equals("все отделы") &&
                            Integer.parseInt(date1[0]) == this.date[0] &&
                            Integer.parseInt(date1[1]) == this.date[1] &&
                            !visitor.getEmployeeLastName().equals("null")) {
                        kol[Integer.parseInt(date1[2]) - 1]++;
                        for (Subdivision subdivision : allsubdivision) {
                            //прибавляем в ячейки массива, предназначенный для выявления самого популярного отдела
                            if (visitor.getEmployeeSubdivision().equals(subdivision.getSubdivision())) {
                                kol[kol.length - 1 - allsubdivision.indexOf(subdivision)]++;
                                break;
                            }
                        }
                        //если график строим для конкретного отдела
                    } else if (visitor.getEmployeeSubdivision().equals(SecondChoiceBox.getValue()) &&
                            Integer.parseInt(date1[0]) == this.date[0] &&
                            Integer.parseInt(date1[1]) == this.date[1]) {
                        kol[Integer.parseInt(date1[2]) - 1]++;
                    }
                    //проверяем на максимальное число
                    if (max < kol[Integer.parseInt(date1[2]) - 1]) max = kol[Integer.parseInt(date1[2]) - 1];
                    break;
            }
        }
        //данначя операция нужна для правильного подбора масштаба графика
        if (max != 0) {
            max = 410 / max;
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("посещаемость нулевая");
        }
        int pop = 0;
        //выводим ли мы график для всех отделов или для какого-то контретного
        //если для всех , то мы показываем самый популярный отдел
        if (SecondChoiceBox.getValue().equals("все отделы")) {
            for (int i = kol.length - 1; i >= kol.length - allsubdivision.size(); i--) {
                //нахождения отдела с максимальным количеством посетителей
                if (pop < kol[i]) {
                    pop = kol[i];
                    mostPopular = allsubdivision.get(kol.length - 1 - i).getSubdivision();
                }
            }
            popularLabel.setVisible(true);
            popularLabel.setText("самый популярный отдел " + mostPopular);
        } else {
            popularLabel.setVisible(false);
        }
        drawGraph(max);
    }

    /**
     * рисуем график в соответствии с типом
     *
     * @param max коэффициент для мастаба
     */
    public void drawGraph(int max) {
        int j = 0;
        switch (type) {
            case 0:
                //выводим график
                //определяем шаг в зависимости от количества лет
                for (int i = 47; i <= 825 - 778 / (kol.length - allsubdivision.size()); i += 778 / (kol.length - allsubdivision.size())) {
                    //изображаем год
                    gc.strokeText(String.valueOf(year + j), i - 10, 445, 778 / (kol.length - allsubdivision.size() + 2));
                    draw(i, j, max);
                    j++;
                }
                break;
            case 1:
                for (int i = 47; i <= 784; i += 67) {
                    //показываем месяцы
                    gc.strokeText(month[j], i - 7, 445);
                    draw(i, j, max);
                    j++;
                }
                break;
            case 2:
                for (int i = 47; i <= 797; i += 25) {
                    //показываем дни
                    gc.strokeText(String.valueOf(j + 1), i - 7, 445);
                    draw(i, j, max);
                    j++;
                }
                break;
        }
        graphPane.getChildren().add(canvas);
    }

    public void draw(int i, int j, int max) {
        //рисуем вертикальную примую
        //служит как метска
        gc.strokeLine(i, 425, i, 435);
        //рисуем горизонтальную прямую на высоте вершины столба
        gc.strokeLine(15, 430 - kol[j] * max, 25, 430 - kol[j] * max);
        //пишем количесвто посетителей
        gc.strokeText(String.valueOf(kol[j]), 0, 430 - kol[j] * max);
        gc.setFill(Color.GOLD);
        //рисуем столб
        //показываем количество посетителей
        gc.fillRect(i - 7, 430 - kol[j] * max, 14, kol[j] * max);
    }
}
