package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.mortal.domain.Person;
import ru.otus.hw.divine.domain.Soul;

@MessagingGateway
public interface LifeGateway {

    @Gateway(requestChannel = "lifeChannel",
            replyChannel = "lifeReportChannel", replyTimeout = 120000)
    Soul process(Person person);

}
