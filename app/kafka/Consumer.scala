package kafka

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.pekko.Done
import org.apache.pekko.actor.CoordinatedShutdown
import play.api.Logging
import play.api.libs.json.Json

import java.time.Duration
import java.util
import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.util.{Failure, Success}

@Singleton
final class Consumer @Inject() (
    serverUrl: String,
    groupId: String,
    pollInterval: Duration,
    coordinatedShutdown: CoordinatedShutdown
) extends Logging {

  logger.info("Consumer starts")

  private val ctx: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  private val stopConsumer = new AtomicBoolean(false)

  private val consumer = new KafkaConsumer[String, String](
    buildProperties,
    new StringDeserializer,
    new StringDeserializer
  )

  consumer.subscribe(
    util.Arrays.asList(
      "moduleLocation-updated",
      "moduleLanguage-updated"
    )
  )

  Future {
    while (!stopConsumer.get()) {
      consumer
        .poll(pollInterval)
        .asScala
        .foreach { record =>
          logger.info(record.toString)
          handleRecord(record)
        }
    }
    logger.info("stop polling")
  }(ctx)
    .andThen(_ => consumer.close())(ctx)
    .andThen {
      case Success(_) =>
        logger.info("finished")
      case Failure(e) =>
        logger.error("failed", e)
    }(ctx)

  coordinatedShutdown.addTask(
    CoordinatedShutdown.PhaseServiceStop,
    "Consumer-stop"
  ) { () =>
    logger.info("Shutdown-task[Consumer-stop] starts.")
    stopConsumer.set(true)
    Future { Done }(ctx).andThen {
      case Success(_) =>
        logger.info("Shutdown-task[Consumer-stop] succeed.")
      case Failure(e) =>
        logger.error("Shutdown-task[Consumer-stop] fails.", e)
    }(ctx)
  }

  private def handleRecord(r: ConsumerRecord[String, String]): Unit =
    r.topic() match {
      case "moduleLocation-updated" =>
        logger.info(Json.parse(r.value()).toString())
      case "moduleLanguage-updated" =>
        logger.info(Json.parse(r.value()).toString())
      case other =>
        logger.info(s"unknown topic: $other")
    }

  private def buildProperties: Properties = {
    val properties = new Properties
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverUrl)
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    properties
  }
}
