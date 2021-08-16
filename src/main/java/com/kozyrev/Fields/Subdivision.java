package com.kozyrev.Fields;

import javafx.scene.paint.Color;

/**
 * данные отедела
 */
public class Subdivision {
    //наименование отдела
    private String subdivision;
    //цвет отдела
    private String color;

    /**
     * котнроллер для загрузки данный из файла
     * @param subdivision нименование отдела
     * @param color цвет отдела
     */
    public Subdivision(String subdivision, String color) {
        this.subdivision = subdivision;
        this.color = color;
    }

    /**
     * пустой контроллер
     * используется в методе по дабавлению нового отдела
     */
    public Subdivision(){

    }

    /**
     * стандартные геттеры и сеттеры
     *
     */
    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getColor() {
        return color;
    }

    //сеттер
    //получаем объект  Сolor
    //и поочередно берем данные красного , зеленого , синего цветов
    //переводим числа в шестнадцатеричную систему
    //такой формат информации нужен для установления цвета панели сотрудника
    public void setColor(Color color) {
        this.color= String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
        /*COLOR= new java.awt.Color((float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity());*/
    }

    /**
     * получить строковое представление данных отдела
     * @return строковое представление данных отдела
     */
    @Override
    public String toString() {
        return subdivision+"|"+color;
    }

}
