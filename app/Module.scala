import auth.OAuthAuthorization
import com.google.inject.{AbstractModule, TypeLiteral}
import kafka.Consumer
import provider.{ConsumerProvider, OAuthAuthorizationProvider, UserToken}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(new TypeLiteral[OAuthAuthorization[UserToken]] {})
      .toProvider(classOf[OAuthAuthorizationProvider])
    bind(classOf[Consumer])
      .toProvider(classOf[ConsumerProvider])
      .asEagerSingleton()
  }
}
