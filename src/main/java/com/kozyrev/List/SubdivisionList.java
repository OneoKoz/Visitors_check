package com.kozyrev.List;

import java.util.Iterator;
import com.kozyrev.Fields.Subdivision;

public class SubdivisionList implements Iterable<Subdivision> {
    // буферные объекты границ списка (не несут значения)
    //нужня для того , чтобы список был не замкнутый
    //а также для ссылок на первый и последний элементы списка
    private SUBElement firstEl;
    private SUBElement lastEl;
    //размер списка
    private int size = 0;

    /**
     * конструктор списка
     * передает начальные значения для буферныйх объектов
     * первоначально они ссылаются сами на себя
     */
    public SubdivisionList() {
        lastEl = new SUBElement(null, firstEl, null);
        firstEl = new SUBElement(null, null, lastEl);
    }


    /**
     * добавление элемента в конец списка
     *
     * @param i передаваемое значение
     */
    public void addLast(Subdivision i) {
        //создаем копию левого крайнего буферного элемента
        SUBElement temp = lastEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый левый крайний буферный элемент с ссылкой на вводимый элемент
        lastEl = new SUBElement(null, temp, null);
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
    public void addFirst(Subdivision i) {
        //создаем копию правого крайнего буферного элемента
        SUBElement temp = firstEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый правый крайний буферный элемент с ссылкой на вводимый элемент
        firstEl = new SUBElement(null, null, temp);
        //указываем ссылку предыдущего элемента как левый буферный элемент
        temp.prevEl = firstEl;
        //инкрементируем размер списка
        size++;
    }

    public void remove (Subdivision element){
        Iterator<Subdivision> iterator=iterator();
        while(iterator.hasNext()){
            Subdivision next= iterator.next();
            if(element.equals(next)){
                iterator.remove();
            }
        }
    }

    /**
     * метод удаление элемента из списка по значению объекта
     * @param element удаляемый объект
     */
    public void removePolEl(SUBElement element){
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
    public Subdivision get(int index) {
        //создаем начальный элемент для нахождение необходимого объекта
        SUBElement aim = firstEl.nextEl;
        //цикл для нахождения объекта
        //путем изменение значения самого обекта
        //на значение обекта ,на который указывает ссылка
        for (int i = 0; i < index; i++) {
            aim = aim.nextEl;
        }
        return aim.value;
    }


    /**
     * возвращает индекс индекс первого вхождения элемента в список
     *
     * @param object значение искомого элемента
     * @return индекс первого вхождения в список
     * -1 если такого элемента нет в списке
     */
    public int indexOf(Subdivision object) {
        //создаем начальный элемент для нахождение индекса искомого объекта
        SUBElement aim = firstEl.nextEl;
        //цикл для сравнения значения данного обекта
        // с существующими в списке
        for (int j = 0; j < size; j++) {
            if (aim.value.equals(object)) {
                return j;
            }
            //изменение значения самого обекта
            //на значение обекта ,на который указывает ссылка
            aim = aim.nextEl;
        }
        // в случае не нахождения соответствующего объекта
        return -1;
    }

    @Override
    public Iterator <Subdivision> iterator() {
        return new MyLinkIterator();
    }

    /**
     * реализация итератора для собстевнного списка
     */
    class MyLinkIterator implements Iterator<Subdivision> {
        //текущий элепмент итератора
        SUBElement nowEl;
        //следующий элемент итератора
        SUBElement nextELEMENT;

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
        public Subdivision next() {
            Subdivision result = null;
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
                SubdivisionList.this.removePolEl(nowEl);
            }
            return;
        }
    }

    private class SUBElement {
        //значение объекта
        private Subdivision value;
        //сылка на слудующий объект
        private SUBElement nextEl;
        //ссылка на предыдущий объект
        private SUBElement prevEl;

        /**
         * конструктор класса Pol
         *
         * @param value  значение объекта
         * @param prevEl ссылка на предыдущий объект
         * @param nextEl сылка на слудующий объект
         */
        private SUBElement(Subdivision value, SUBElement prevEl, SUBElement nextEl) {
            this.value = value;
            this.prevEl = prevEl;
            this.nextEl = nextEl;
        }

        private SUBElement() {
        }
    }
}
