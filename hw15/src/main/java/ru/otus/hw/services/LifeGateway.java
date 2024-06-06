package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.divine.domain.Soul;
import ru.otus.hw.domain.Intention;

@MessagingGateway
public interface LifeGateway {

    @Gateway(requestChannel = "lifeChannel",
            replyChannel = "lifeReportChannel", replyTimeout = 120000)
    Soul execute(Intention intention);

}
