package com.kozyrev.List;

import java.util.Iterator;
import com.kozyrev.Fields.Employee;

public class EmployeesList implements Iterable<Employee> {
    // буферные объекты границ списка (не несут значения)
    //нужня для того , чтобы список был не замкнутый
    //а также для ссылок на первый и последний элементы списка
    private EMPElement firstEl;
    private EMPElement lastEl;
    //размер списка
    private int size = 0;

    /**
     * конструктор списка
     * передает начальные значения для буферныйх объектов
     * первоначально они ссылаются сами на себя
     */
    public EmployeesList() {
        lastEl = new EMPElement(null, firstEl, null);
        firstEl = new EMPElement(null, null, lastEl);
    }


    /**
     * добавление элемента в конец списка
     *
     * @param i передаваемое значение
     */
    public void addLast(Employee i) {
        //создаем копию левого крайнего буферного элемента
        EMPElement temp = lastEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый левый крайний буферный элемент с ссылкой на вводимый элемент
        lastEl = new EMPElement(null, temp, null);
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
    public void addFirst(Employee i) {
        //создаем копию правого крайнего буферного элемента
        EMPElement temp = firstEl;
        //меняем значение элемента
        temp.value = i;
        //создаем новый правый крайний буферный элемент с ссылкой на вводимый элемент
        firstEl = new EMPElement(null, null, temp);
        //указываем ссылку предыдущего элемента как левый буферный элемент
        temp.prevEl = firstEl;
        //инкрементируем размер списка
        size++;
    }

    public void remove (Employee element){
        Iterator<Employee> iterator=iterator();
        while(iterator.hasNext()){
            Employee next= iterator.next();
            if(element.equals(next)){
                iterator.remove();
            }
        }
    }

    /**
     * метод удаление элемента из списка по значению объекта
     * @param element удаляемый объект
     */
    public void removePolEl(EMPElement element){
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


    @Override
    public Iterator <Employee> iterator() {
        return new MyLinkIterator();
    }

    /**
     * реализация итератора для собстевнного списка
     */
    class MyLinkIterator implements Iterator<Employee> {
        //текущий элепмент итератора
        EMPElement nowEl;
        //следующий элемент итератора
        EMPElement nextELEMENT;

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
        public Employee next() {
            Employee result = null;
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
                EmployeesList.this.removePolEl(nowEl);
            }
            return;
        }
    }

    private class EMPElement {
        //значение объекта
        private Employee value;
        //сылка на слудующий объект
        private EMPElement nextEl;
        //ссылка на предыдущий объект
        private EMPElement prevEl;

        /**
         * конструктор класса Pol
         *
         * @param value  значение объекта
         * @param prevEl ссылка на предыдущий объект
         * @param nextEl сылка на слудующий объект
         */
        private EMPElement(Employee value, EMPElement prevEl, EMPElement nextEl) {
            this.value = value;
            this.prevEl = prevEl;
            this.nextEl = nextEl;
        }

        private EMPElement() {
        }
    }
}
