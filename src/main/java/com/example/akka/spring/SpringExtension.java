package com.example.akka.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.Extension;
import akka.actor.Props;

/**
 *  An Akka Extension to provide access to Spring managed Actor Beans.
 */
@Component
public class SpringExtension implements Extension {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private volatile ApplicationContext applicationContext;

  /**
   * Used to initialise the Spring application context for the extension.
   * @param applicationContext
   */
  public void initialize(final ApplicationContext applicationContext) {
    logger.debug("Initialising application context in Akka spring extension ...");
    this.applicationContext = applicationContext;
  }

  /**
   * Create a Props for the specified actorBeanName using SpringActorProducer class.
   *
   * @param actorBeanName  The name of the actor bean to create Props for
   * @return a Props that will create the named actor bean using Spring
   */
  public Props props(final String actorBeanName) {
    logger.debug("Creating 'Props' configuration object for actor bean : {}", actorBeanName);
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
  }

}
