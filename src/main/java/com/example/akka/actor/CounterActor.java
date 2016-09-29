package com.example.akka.actor;


import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

@Named("CounterActor")
@Scope("prototype")
public class CounterActor extends UntypedActor {

  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  public enum Message { INCREMENT_COUNTER , GET_CURRENT_VALUE }

  private final AtomicInteger counter = new AtomicInteger(0);

  @Override
  public void onReceive(final Object message) throws Exception {

    if (!(message instanceof Message)) {
      log.debug("CounterActor: Unknown message!");
      unhandled(message);
      return;
    }

    log.debug("CounterActor: received message of type {}", message);
    final Message receivedMessage = (Message) message;

    switch (receivedMessage) {
      case INCREMENT_COUNTER:
        final int currentValue = counter.incrementAndGet();
        log.debug("CounterActor: Incremented counter to {}", currentValue);
        break;
      case GET_CURRENT_VALUE:
        log.debug("CounterActor: Sending current counter value {} to {}",counter.get(), getSender());
        getSender().tell(counter.get(), getSelf());
        break;
      default:
        unhandled(receivedMessage);
    }

  }
}
