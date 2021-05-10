package service.abstracts

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api._

trait Service[Json, Model <: UniqueEntity, DbEntry <: UniqueDbEntry, T <: Table[
  DbEntry
] with UniqueEntityColumn]
    extends Core[Model, DbEntry, T]
    with Get[Json, Model, DbEntry]
    with Delete[Model, DbEntry]
    with Create[Json, Model, DbEntry, T]
    with Update[Json, Model, DbEntry]
    with JsonConverter[Json, DbEntry]
