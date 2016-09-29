package com.example.akka.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

/**
 * SpringActorProducer defines how to instantiate an actor within a dependency injection framework.
 * In other words, the actor producer lets Spring create proper Actor instances.
 */
public class SpringActorProducer implements IndirectActorProducer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ApplicationContext applicationContext;
  private final String actorBeanName;

  public SpringActorProducer(final ApplicationContext applicationContext, final String actorBeanName) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
  }

  @Override
  public Actor produce() {
    logger.debug("Producing {} actor bean with spring's application context", actorBeanName);
    return (Actor) applicationContext.getBean(actorBeanName);
  }

  @Override
  public Class<? extends Actor> actorClass() {
    return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
  }
}
