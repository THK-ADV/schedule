import auth.Authorization
import auth.UserToken
import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import provider.AuthorizationProvider

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
