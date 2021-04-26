package service.abstracts

import database.cols.IDColumn
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api._

trait Service[Json, Model <: UniqueEntity, T <: Table[Model] with IDColumn]
    extends Core[Model, T]
    with Get[Json, Model]
    with Delete[Model]
    with Create[Json, Model, T]
    with Update[Json, Model]
    with JsonConverter[Json, Model]
