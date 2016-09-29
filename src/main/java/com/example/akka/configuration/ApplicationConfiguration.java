package com.example.akka.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;
import com.example.akka.spring.SpringExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.example.akka" })
public class ApplicationConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  private static final String ACTOR_SYSTEM_NAME = "akka-spring-actor-system";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SpringExtension springExtension;

  /**
   * Actor system singleton for this application.
   */
  @Bean
  public ActorSystem actorSystem() {
    logger.debug("Initialising actor system singleton bean for the application ...");
    ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME, akkaConfiguration());
    springExtension.initialize(applicationContext);
    return system;
  }

  @Bean
  public Config akkaConfiguration() {
    return ConfigFactory.load();
  }

}