package provider

import java.time.Duration
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

import kafka.Consumer
import ops.ConfigurationOps.Ops
import org.apache.pekko.actor.CoordinatedShutdown
import play.api.Configuration

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
