package service.abstracts

import database.UniqueDbEntry
import database.cols.UniqueEntityColumn
import database.repos.Repository
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api.Table

trait Core[Model <: UniqueEntity, DbEntry <: UniqueDbEntry, T <: Table[
  DbEntry
] with UniqueEntityColumn] {
  protected def repo: Repository[Model, DbEntry, T]
}
