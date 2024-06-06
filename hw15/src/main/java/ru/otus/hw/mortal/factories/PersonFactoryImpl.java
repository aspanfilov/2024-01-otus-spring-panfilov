package ru.otus.hw.mortal.factories;

import org.springframework.stereotype.Component;
import ru.otus.hw.mortal.domain.Person;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PersonFactoryImpl implements PersonFactory {

    private static final List<String> NAMES = List.of("Вова", "Машенька", "Алёша", "Гульчатай",
            "Эдик", "Мишаня", "Настенька", "Никита", "Сара Коннор", "Ростислав", "Димон", "Анджела",
            "Артурчик", "Антонина", "Полиграф Иваныч", "Клавдия Ивановна", "Толик", "Вася",
            "Петросян", "Нео", "Валентина Петровна", "Мариванна", "Малыш Джонни", "Джумдуд", "Равшан");

    private final AtomicLong idGenerator = new AtomicLong();

    private final Random random = new Random();

    @Override
    public Person createPerson() {

        String name = NAMES.get(random.nextInt(NAMES.size()));

        return new Person(idGenerator.incrementAndGet(), name);
    }

}
