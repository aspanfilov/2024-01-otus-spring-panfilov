package ru.otus.hw.mortal.factories;

import org.springframework.stereotype.Component;
import ru.otus.hw.mortal.domain.Deed;
import ru.otus.hw.mortal.domain.DeedType;
import ru.otus.hw.mortal.domain.Person;

import java.util.List;
import java.util.Random;

@Component
public class DeedFactoryImpl implements DeedFactory {

    private static final List<String> GOOD_DEEDS = List.of("Провел бабушку через дорогу",
            "Вернул кошелек тёте", "Усыновил бездомного человека", "Защитил девочку от злых людей",
            "Накормил бомжа", "Помог соседу починить крышу", "Сходил в православную церковь", "Подал милостыню",
            "Совершил подвиг", "Приютил уличного котенка", "Занялся благотворительством",
            "Бросил курить", "Возлюбил ближнего", "Просто хорошо себя повел", "Уступил место в очереди",
            "Пожелал здоровья бабушке", "Воздержался", "Пожертвовал почку бездомному", "Помог припарковать машину",
            "Вкрутил лампочку в подъезде", "Купил одежду голым людям", "Принес продукты инвалиду", "Дал прикурить деду",
            "Вывел из леса блудного", "Научил программированию человека", "Догнал вора в переулке");

    private static final List<String> BAD_DEEDS = List.of("Украл деньги", "Прелюбодействовал с женой соседа",
            "Довел родственника до ручки", "Забыл пару долгов", "Давал деньги под большие проценты", "Очень разгневался",
            "Изнасиловал другого человека", "Занимался обжорством", "Не помог тестю починить крышу",
            "Не уступил место беременной", "Ограбил ларек у дома", "Отнял конфету у ребенка", "Проспал свидание",
            "Напился до беспамятства", "Потерял стыд", "Душнил на работе", "Давал ложные показания в суде",
            "Оклеветал хорошего человека", "Дал взятку гаишнику", "Громко бранился в общественном месте",
            "Потратил лучшие годы не на то", "Совершил членовредительство");

    private static final List<String> NEUTRAL_DEEDS = List.of("Смотрел новости по телевизору",
            "Погулял в парке", "Спал на диване", "Поковырял в носу", "Поймал рукой муху",
            "Ел бутерброд с колбасой", "Покормил кота", "Подумал о вечном", "Сходил в магазин за майонезом",
            "Созерцал на птичек", "Искал под столом колпачок", "Бегал вокруг дома", "Смотрел на новые обои",
            "Принял контрастный душ", "Застрял в лифте", "Включил свет в комнате", "Рассказал несмешной анекдот",
            "Плавал в речке", "Поискал второй носок", "Пытался вспомнить", "Починил замок входной двери",
            "Оставил на работе ключи", "Поговорил с прохожим", "собирал в лесу грибы");

    private final Random random = new Random();

    @Override
    public Deed doDeed(Person person) {
        DeedType type = getRandomDeedType();
        String description = getRandomDeedDescription(type);
        return new Deed(person, type, description);
    }

    @Override
    public Deed doOriginalSin(Person person) {
        return new Deed(person, DeedType.BAD, "Родился (первородный грех)");
    }

    private DeedType getRandomDeedType() {
        int pick = random.nextInt(DeedType.values().length);
        return DeedType.values()[pick];
    }

    private String getRandomDeedDescription(DeedType type) {
        return switch (type) {
            case GOOD -> GOOD_DEEDS.get(getRandomInt(GOOD_DEEDS.size()));
            case BAD -> BAD_DEEDS.get(getRandomInt(BAD_DEEDS.size()));
            case NEUTRAL -> NEUTRAL_DEEDS.get(getRandomInt(NEUTRAL_DEEDS.size()));
        };
    }

    private int getRandomInt(int max) {
        return random.nextInt(max);
    }
}
