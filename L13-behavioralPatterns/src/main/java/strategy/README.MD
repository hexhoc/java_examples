# Шаблон: Стратегия (Strategy)
==========================================================================================

### Цель:
Взаимозаменяемость семейств классов

==========================================================================================

### Для чего используется:
Определение семейства классов, инкапсулирование каждого из них и организация их взаимозаменяемости

==========================================================================================

### Пример использования:
- Есть несколько родственных классов, которые отличаются поведением;
- Необходимо иметь несколько вариантов поведения;
- В классе есть данные, о которых не должен знать клиент;
- С помощью условных операторов в классе определенно большое количество возможных поведений;

### Текущий пример:
Есть класс User у которого есть поле с типом Activity. Activity это интерфейс, поэтому мы можем передать в класс User
любой тип реализующий этот интерфейс.


