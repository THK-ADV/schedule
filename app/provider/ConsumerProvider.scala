package provider

import akka.actor.CoordinatedShutdown
import kafka.Consumer
import ops.ConfigurationOps.Ops
import play.api.Configuration

import java.time.Duration
import javax.inject.{Inject, Provider, Singleton}

@Singleton
final class ConsumerProvider @Inject() (
    coordinatedShutdown: CoordinatedShutdown,
    config: Configuration
) extends Provider[Consumer] {

  override def get() = new Consumer(
    config.nonEmptyString("kafka.serverUrl"),
    config.nonEmptyString("kafka.groupId"),
    Duration.ofSeconds(10),
    coordinatedShutdown
  )
}
