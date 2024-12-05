import auth.{Authorization, UserToken}
import com.google.inject.{AbstractModule, TypeLiteral}
import kafka.Consumer
import provider.{AuthorizationProvider, ConsumerProvider}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(new TypeLiteral[Authorization[UserToken]] {})
      .toProvider(classOf[AuthorizationProvider])
      .asEagerSingleton()
//    bind(classOf[Consumer])
//      .toProvider(classOf[ConsumerProvider])
//      .asEagerSingleton()
  }
}
