package ru.otus.hw.divine.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.divine.domain.Destination;
import ru.otus.hw.divine.domain.KarmaPoint;
import ru.otus.hw.mortal.domain.Person;
import ru.otus.hw.divine.domain.Soul;

import java.util.List;

@Service(value = "judgementService")
public class JudgementServiceImpl implements JudgementService {

    @Override
    public Soul judgePersonKarmaBaggage(List<KarmaPoint> karmaBaggage) {
        int totalKarma = karmaBaggage.stream()
                .mapToInt(KarmaPoint::getValue)
                .sum();

        Person usedPerson = karmaBaggage.get(0).getPerson();
        Destination destination = totalKarma >= 0 ? Destination.HEAVEN : Destination.HELL;

        return new Soul(usedPerson, totalKarma, destination);
    }
}
