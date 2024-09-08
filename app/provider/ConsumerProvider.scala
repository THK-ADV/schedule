package provider

import database.repos.*
import kafka.{Consumer, DatabaseActor}
import ops.ConfigurationOps.Ops
import org.apache.pekko.actor.{ActorSystem, CoordinatedShutdown}
import play.api.Configuration

import java.time.Duration
import javax.inject.{Inject, Provider, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class ConsumerProvider @Inject() (
    coordinatedShutdown: CoordinatedShutdown,
    config: Configuration,
    system: ActorSystem,
    moduleRepo: ModuleRepository,
    moduleRelationRepo: ModuleRelationRepository,
    moduleSupervisorRepo: ModuleSupervisorRepository,
    moduleInStudyProgramRepo: ModuleInStudyProgramRepository,
    facultyRepo: FacultyRepository,
    degreeRepo: DegreeRepository,
    studyProgramRepository: StudyProgramRepository,
    ctx: ExecutionContext
) extends Provider[Consumer]:

  override def get() = new Consumer(
    config.nonEmptyString("kafka.serverUrl"),
    config.nonEmptyString("kafka.groupId"),
    Duration.ofSeconds(10),
    topics,
    DatabaseActor(
      moduleRepo,
      moduleRelationRepo,
      moduleSupervisorRepo,
      moduleInStudyProgramRepo,
      facultyRepo,
      degreeRepo,
      studyProgramRepository,
      system,
      ctx
    ),
    coordinatedShutdown
  )

  def topics = List(
    "module-created",
    "module-deleted",
    "module-updated",
    "moduleLocation-created",
    "moduleLocation-deleted",
    "moduleLocation-updated"
  )
