import auth.OAuthAuthorization
import com.google.inject.{AbstractModule, TypeLiteral}
import provider.{OAuthAuthorizationProvider, UserToken}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(new TypeLiteral[OAuthAuthorization[UserToken]] {})
      .toProvider(classOf[OAuthAuthorizationProvider])
  }
}
