package service.abstracts

import database.cols.IDColumn
import database.repos.Repository
import models.UniqueEntity
import slick.jdbc.PostgresProfile.api.Table

trait Core[Model <: UniqueEntity, T <: Table[Model] with IDColumn] {
  protected def repo: Repository[Model, T]
}
