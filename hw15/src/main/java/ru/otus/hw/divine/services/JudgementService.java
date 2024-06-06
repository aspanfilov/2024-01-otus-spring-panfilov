package ru.otus.hw.divine.services;

import ru.otus.hw.divine.domain.KarmaPoint;
import ru.otus.hw.divine.domain.Soul;

import java.util.List;

public interface JudgementService {
    Soul judgePersonKarmaBaggage(List<KarmaPoint> karmaBaggage);
}
