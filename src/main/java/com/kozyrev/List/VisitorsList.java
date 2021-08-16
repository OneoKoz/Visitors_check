package com.kozyrev.List;

import java.util.Iterator;
import com.kozyrev.Fields.Visitor;

public class VisitorsList implements Iterable<Visitor> {
    // буферные объекты границ списка (не несут значения)
    //нужня для того , чтобы список был не замкнутый
    //а также для ссылок на первый и последний элементы списка
    private VISElement firstEl;
    private VISElement lastEl;
    //размер списка
    private int size = 0;

    /**
     * конструктор списка
     * передает начальные значения для буферныйх объектов
     * первоначально они ссылаются сами на себя
     */
    public VisitorsList() {
        lastEl = new VISElement(null, firstEl, null);
        firstEl = new VISElement(null, null, lastEl);
    }


    /**
     * добавление элемента в конец списка
     *
     * @param i передаваемое значение
     */
    public void addLast(Visitor i) {
        //создаем копию левого крайнего буферного элемента
        VISElement temp = lastEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый левый крайний буферный элемент с ссылкой на вводимый элемент
        lastEl = new VISElement(null, temp, null);
        //указываем ссылку на левый буферный элемент
        temp.nextEl = lastEl;
        //инкрементируем размер списка
        size++;
    }
    //Переопределение метода

    /**
     * добавление элемента в начало списка
     *
     * @param i передаваемое значение объекта
     */
    public void addFirst(Visitor i) {
        //создаем копию правого крайнего буферного элемента
        VISElement temp = firstEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый правый крайний буферный элемент с ссылкой на вводимый элемент
        firstEl = new VISElement(null, null, temp);
        //указываем ссылку предыдущего элемента как левый буферный элемент
        temp.prevEl = firstEl;
        //инкрементируем размер списка
        size++;
    }

    /**
     * метод удаление элемента из списка по значению объекта
     * @param element удаляемый объект
     */
    public void removePolEl(VISElement element){
        //переопределяем указатели соседних элементиов таким образом,
        //чтобы не было объекта, имеющего ссылку на удаляемый объект
        if(element.prevEl!=null){
            element.prevEl.nextEl=element.nextEl;
        }else{
            firstEl.nextEl=element.nextEl;
        }
        element.nextEl.prevEl=element.prevEl;
        //декрементируем размер списка
        size--;
    }


    /**
     * @return возвращает размер нашего списка
     */
    public int size() {
        return size;
    }


    /**
     * метод для удаления всех элементов списка
     * установление значений всех базовых объектов
     * по умолчанию
     */
    public void clear() {
        size = 0;
        //правый и лувый буферные элементы
        //снова указывают друг на друга
        firstEl.nextEl = lastEl;
        lastEl.prevEl = firstEl;
    }


    /**
     * проверка на пустоту списка
     *
     * @return в соответствии с результатом возвращает true или false
     */
    public boolean isEmpty() {
        return (size == 0);
    }


    /**
     * полечение элемента списка по индексу
     *
     * @param index индекс необходимого элемента
     * @return значение самого элемента
     */
    public Visitor get(int index) {
        //создаем начальный элемент для нахождение необходимого объекта
        VISElement aim = firstEl.nextEl;
        //цикл для нахождения объекта
        //путем изменение значения самого обекта
        //на значение обекта ,на который указывает ссылка
        for (int i = 0; i < index; i++) {
            aim = aim.nextEl;
        }
        return aim.value;
    }

    @Override
    public Iterator<Visitor> iterator() {
        return new MyLinkIterator();
    }

    /**
     * реализация итератора для собстевнного списка
     */
    class MyLinkIterator implements Iterator<Visitor> {
        //текущий элепмент итератора
        VISElement nowEl;
        //следующий элемент итератора
        VISElement nextELEMENT;

        //конструктор
        public MyLinkIterator() {
            nextELEMENT = firstEl.nextEl;
        }

        /**
         * проверка есть ли элемент в итераторе
         * @return true- если есть следующий элемент
         */
        @Override
        public boolean hasNext() {
            return nextELEMENT != null && nextELEMENT.nextEl != null;
        }

        /**
         * возвращает слудующий элемент списка
         * @return элемент списка
         */
        @Override
        public Visitor next() {
            Visitor result = null;
            if (hasNext()) {
                nowEl = nextELEMENT;
                result = nowEl.value;
                nextELEMENT = nowEl.nextEl;
            }
            return result;
        }

        /**
         * удаляет элемент в списке
         */
        @Override
        public void remove() {
            if (nowEl != null) {
                VisitorsList.this.removePolEl(nowEl);
            }
            return;
        }
    }

    private class VISElement {
        //значение объекта
        private Visitor value;
        //сылка на слудующий объект
        private VISElement nextEl;
        //ссылка на предыдущий объект
        private VISElement prevEl;

        /**
         * конструктор класса Pol
         *
         * @param value  значение объекта
         * @param prevEl ссылка на предыдущий объект
         * @param nextEl сылка на слудующий объект
         */
        private VISElement(Visitor value, VISElement prevEl, VISElement nextEl) {
            this.value = value;
            this.prevEl = prevEl;
            this.nextEl = nextEl;
        }

        private VISElement() {
        }
    }
}
