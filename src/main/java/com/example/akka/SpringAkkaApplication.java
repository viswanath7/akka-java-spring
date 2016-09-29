package com.example.akka;

import static com.example.akka.actor.CounterActor.Message.GET_CURRENT_VALUE;
import static com.example.akka.actor.CounterActor.Message.INCREMENT_COUNTER;

import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.example.akka.spring.SpringExtension;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@SpringBootApplication
public class SpringAkkaApplication implements CommandLineRunner {

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private SpringExtension springExtension;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static void main(final String[] args) {
    SpringApplication.run(SpringAkkaApplication.class, args);
    System.exit(0);
  }

  /**
   * Callback used to run the bean.
   *
   * @param args incoming main method arguments
   * @throws Exception on error
   */
  @Override
  public void run(final String... args) throws Exception {
    try {
      logger.debug("Creating a CounterActor to perform actual work ...");
      ActorRef counterActor = createActor("CounterActor");

      logger.debug("Asyncronously sending messages to CounterActor to increment count 5 times ...");
      for(int count = 0; count < 5; count ++) counterActor.tell(INCREMENT_COUNTER, ActorRef.noSender());


      FiniteDuration timeoutDuration = FiniteDuration.create(1, TimeUnit.SECONDS);
      Future<Object> awaitable = Patterns.ask(counterActor, GET_CURRENT_VALUE, Timeout.durationToTimeout(timeoutDuration));
      logger.debug("Response: " + Await.result(awaitable, timeoutDuration));
    } finally {
      actorSystem.terminate();
      Await.result(actorSystem.whenTerminated(), Duration.Inf());
    }
    logger.debug("Terminated actor system: {}", actorSystem.name());
  }

  private ActorRef createActor(@NonNull final String actorBeanName) {
    return actorSystem.actorOf(springExtension.props(actorBeanName), actorBeanName);
  }

}
