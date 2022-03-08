import collision.{Collision, CollisionType}
import models.Schedule.ScheduleDefault
import models.ScheduleEntryStatus.Draft
import models.{Schedule, ScheduleEntryStatus}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID
import scala.language.{implicitConversions, reflectiveCalls}

class CollisionCheckSpec extends UnitSpec {

  import collision.CollisionCheck._

  implicit def dateFromString(string: String): LocalDate =
    LocalDate.parse(string, DateTimeFormat.forPattern("dd.MM.yyyy"))

  implicit def timeFromString(string: String): LocalTime =
    LocalTime.parse(string, DateTimeFormat.forPattern("HH:mm"))

  "A Collision check" should {
    "detect collisions if two dates overlap" in {
      def schedule(
          date: LocalDate,
          start: LocalTime,
          end: LocalTime
      ): Schedule =
        ScheduleDefault(
          UUID.randomUUID,
          UUID.randomUUID,
          UUID.randomUUID,
          date,
          start,
          end,
          ScheduleEntryStatus.Draft,
          UUID.randomUUID
        )

      datePred.apply(
        schedule("01.01.2022", "09:00", "11:00"),
        schedule("01.01.2022", "11:00", "13:00")
      ) shouldBe false

      datePred.apply(
        schedule("01.01.2022", "09:00", "11:00"),
        schedule("02.01.2022", "09:00", "11:00")
      ) shouldBe false

      datePred.apply(
        schedule("01.01.2022", "09:00", "11:00"),
        schedule("01.01.2022", "10:00", "12:00")
      ) shouldBe true

      datePred.apply(
        schedule("01.01.2022", "09:00", "11:15"),
        schedule("01.01.2022", "11:14", "12:00")
      ) shouldBe true
    }

    "detect collisions if two rooms overlap" in {
      def schedule(
          room: UUID
      ): Schedule =
        ScheduleDefault(
          UUID.randomUUID,
          room,
          UUID.randomUUID,
          "01.01.2022",
          "09:00",
          "11:00",
          ScheduleEntryStatus.Draft,
          UUID.randomUUID
        )

      roomPred.apply(
        schedule(UUID.randomUUID),
        schedule(UUID.randomUUID)
      ) shouldBe false

      val room1 = UUID.randomUUID
      roomPred.apply(
        schedule(room1),
        schedule(room1)
      ) shouldBe true
    }

    "detect collisions if two study paths overlap" in {
      def schedule(
          moduleExam: UUID
      ): Schedule =
        ScheduleDefault(
          UUID.randomUUID,
          UUID.randomUUID,
          moduleExam,
          "01.01.2022",
          "09:00",
          "11:00",
          ScheduleEntryStatus.Draft,
          UUID.randomUUID
        )

      studyPathPred.apply(
        schedule(UUID.randomUUID),
        schedule(UUID.randomUUID)
      ) shouldBe false

      val exam1 = UUID.randomUUID
      studyPathPred.apply(
        schedule(exam1),
        schedule(exam1)
      ) shouldBe true
    }

    "detect collisions if two courses overlap" in {
      def schedule(
          course: UUID
      ): Schedule =
        ScheduleDefault(
          course,
          UUID.randomUUID,
          UUID.randomUUID,
          "01.01.2022",
          "09:00",
          "11:00",
          ScheduleEntryStatus.Draft,
          UUID.randomUUID
        )

      coursePred.apply(
        schedule(UUID.randomUUID),
        schedule(UUID.randomUUID)
      ) shouldBe true

      val course1 = UUID.randomUUID
      coursePred.apply(
        schedule(course1),
        schedule(course1)
      ) shouldBe false
    }

    "detect collisions if a date is blocked" in {
      def schedule(
          date: LocalDate,
          start: LocalTime,
          end: LocalTime
      ): Schedule =
        ScheduleDefault(
          UUID.randomUUID,
          UUID.randomUUID,
          UUID.randomUUID,
          date,
          start,
          end,
          ScheduleEntryStatus.Draft,
          UUID.randomUUID
        )

      blockedPred(Set.empty).apply(
        schedule("01.01.2022", "09:00", "11:00")
      ) shouldBe false

      blockedPred(Set("02.01.2022", "03.01.2022")).apply(
        schedule("01.01.2022", "09:00", "11:00")
      ) shouldBe false

      blockedPred(Set("02.01.2022", "03.01.2022")).apply(
        schedule("02.01.2022", "09:00", "11:00")
      ) shouldBe true

      blockedPred(Set("02.01.2022", "03.01.2022")).apply(
        schedule("03.01.2022", "09:00", "11:00")
      ) shouldBe true
    }

    def single(
        c: UUID,
        r: UUID,
        mer: UUID,
        d: LocalDate,
        s: LocalTime,
        e: LocalTime
    ): Schedule =
      ScheduleDefault(c, r, mer, d, s, e, Draft, UUID.randomUUID)

    def multiple(
        c: UUID,
        r: UUID,
        mers: Vector[UUID],
        d: String,
        s: String,
        e: String
    ): Vector[Schedule] =
      mers.map(mer => single(c, r, mer, d, s, e))

    val ai = UUID.randomUUID
    val wi = UUID.randomUUID
    val mi = UUID.randomUUID
    val itm = UUID.randomUUID

    val ap1V = UUID.randomUUID
    val ap1P = UUID.randomUUID
    val r1 = UUID.randomUUID
    val r2 = UUID.randomUUID
    val r3 = UUID.randomUUID

    val ma1V = UUID.randomUUID
    val ma1P = UUID.randomUUID
    val r4 = UUID.randomUUID
    val r5 = UUID.randomUUID
    val r6 = UUID.randomUUID

    val ebrV = UUID.randomUUID
    val ebrUe = UUID.randomUUID
    val r7 = UUID.randomUUID

    val emi = UUID.randomUUID
    val ewi = UUID.randomUUID
    val eai = UUID.randomUUID
    val eitm = UUID.randomUUID
    val r8 = UUID.randomUUID
    val r9 = UUID.randomUUID
    val r10 = UUID.randomUUID
    val r11 = UUID.randomUUID

    "detect room collisions" in {
      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r1, mi, "28.02.2022", "13:00", "13:00")
      ) shouldBe None

      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r1, ai, "28.02.2022", "13:00", "15:00")
      ) shouldBe None

      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r1, ai, "28.02.2022", "12:00", "13:00")
      ) shouldBe None

      val s1 = single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00")
      val s2 = single(ma1V, r1, ai, "28.02.2022", "12:00", "13:00")
      roomCollision.apply(s1, s2) shouldBe Some(
        Collision(CollisionType.Room, s1, s2)
      )

      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ma1V, r1, ai, "28.02.2022", "13:00", "15:00")
      ) shouldBe None

      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r2, ai, "28.02.2022", "13:00", "15:00")
      ) shouldBe None

      roomCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r2, ai, "28.02.2022", "12:00", "15:00")
      ) shouldBe None // TODO Ap1 zeitglich in 2 räumen?
    }

    "detect study path collisions" in {
      studyPathCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r1, ai, "28.02.2022", "13:00", "15:00")
      ) shouldBe None

      studyPathCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r1, mi, "28.02.2022", "11:00", "13:00")
      ) shouldBe None

      studyPathCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r2, mi, "28.02.2022", "11:00", "13:00")
      ) shouldBe None // TODO ???

      studyPathCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ap1V, r2, ai, "28.02.2022", "12:00", "15:00")
      ) shouldBe None // TODO ???

      studyPathCollision.apply(
        single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
        single(ma1V, r1, mi, "28.02.2022", "11:00", "13:00")
      ) shouldBe None

      val s1 = single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00")
      val s2 = single(ma1V, r2, ai, "28.02.2022", "11:00", "13:00")
      studyPathCollision.apply(s1, s2) shouldBe Some(
        Collision(CollisionType.StudyPathTime, s1, s2)
      )
    }

    "detect multiple collisions" in {
      binaryEval(
        Vector(
          single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
          single(ap1V, r2, mi, "28.02.2022", "11:00", "13:00")
        ),
        List(roomCollision, studyPathCollision)
      ) shouldBe Nil // TODO ???

      binaryEval(
        Vector(
          single(ap1V, r1, ai, "28.02.2022", "11:00", "13:00"),
          single(ap1V, r2, ai, "28.02.2022", "12:00", "15:00")
        ),
        List(roomCollision, studyPathCollision)
      ) shouldBe Nil // TODO ???

      val blockedSchedule = single(ap1V, r1, ai, "03.01.2022", "12:00", "15:00")
      unaryEval(
        Vector(
          single(ap1V, r1, ai, "01.01.2022", "11:00", "13:00"),
          single(ap1V, r1, ai, "02.01.2022", "12:00", "15:00"),
          blockedSchedule,
          single(ap1V, r1, ai, "04.01.2022", "12:00", "15:00")
        ),
        List(blockedCollision(Set("03.01.2022")))
      ) shouldBe Vector(
        Collision(CollisionType.BlockedDay, blockedSchedule, blockedSchedule)
      )
    }

    "detect no collisions in an example schedule" in {
      val schedules = Vector(
        // Monday
        multiple(
          ap1V,
          r1,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "11:00",
          "13:00"
        ),
        multiple(
          ebrV,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "14:00",
          "16:00"
        ),
        multiple(
          ebrUe,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "16:00",
          "18:00"
        ),
        // Tuesday
        multiple(
          ma1V,
          r4,
          Vector(ai, wi, mi, itm),
          "01.03.2022",
          "09:00",
          "11:00"
        ),
        multiple(ma1P, r5, Vector(ai), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r6, Vector(mi), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r5, Vector(wi), "01.03.2022", "14:00", "16:00"),
        multiple(ma1P, r6, Vector(itm), "01.03.2022", "14:00", "16:00"),
        // Wednesday
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "11:00", "13:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "11:00", "13:00"),
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "13:00", "15:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "13:00", "15:00"),
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "15:00", "17:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "15:00", "17:00"),
        // Thursday
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "15:00", "17:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "15:00", "17:00"),
        // Friday
        multiple(emi, r8, Vector(mi), "04.03.2022", "11:00", "17:00"),
        multiple(ewi, r9, Vector(wi), "04.03.2022", "11:00", "17:00"),
        multiple(eai, r10, Vector(ai), "04.03.2022", "11:00", "17:00"),
        multiple(eitm, r11, Vector(itm), "04.03.2022", "11:00", "17:00")
      ).flatten

      val binaryRes =
        binaryEval(schedules, List(roomCollision, studyPathCollision))
      binaryRes shouldBe Nil

      val unaryRes = unaryEval(
        schedules,
        List(blockedCollision(Set("05.03.2022", "06.03.2022")))
      )
      unaryRes shouldBe Nil
    }

    "detect blocked collisions in an example schedule" in {
      val wednesday1 = single(ap1P, r2, ai, "02.03.2022", "11:00", "13:00")
      val wednesday2 = single(ap1P, r3, mi, "02.03.2022", "11:00", "13:00")
      val wednesday3 = single(ap1P, r2, ai, "02.03.2022", "13:00", "15:00")
      val wednesday4 = single(ap1P, r3, mi, "02.03.2022", "13:00", "15:00")
      val wednesday5 = single(ap1P, r2, ai, "02.03.2022", "15:00", "17:00")
      val wednesday6 = single(ap1P, r3, mi, "02.03.2022", "15:00", "17:00")

      val schedules = Vector(
        // Monday
        multiple(
          ap1V,
          r1,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "11:00",
          "13:00"
        ),
        multiple(
          ebrV,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "14:00",
          "16:00"
        ),
        multiple(
          ebrUe,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "16:00",
          "18:00"
        ),
        // Tuesday
        multiple(
          ma1V,
          r4,
          Vector(ai, wi, mi, itm),
          "01.03.2022",
          "09:00",
          "11:00"
        ),
        multiple(ma1P, r5, Vector(ai), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r6, Vector(mi), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r5, Vector(wi), "01.03.2022", "14:00", "16:00"),
        multiple(ma1P, r6, Vector(itm), "01.03.2022", "14:00", "16:00"),
        // Wednesday
        Vector(wednesday1),
        Vector(wednesday2),
        Vector(wednesday3),
        Vector(wednesday4),
        Vector(wednesday5),
        Vector(wednesday6),
        // Thursday
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "15:00", "17:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "15:00", "17:00"),
        // Friday
        multiple(emi, r8, Vector(mi), "04.03.2022", "11:00", "17:00"),
        multiple(ewi, r9, Vector(wi), "04.03.2022", "11:00", "17:00"),
        multiple(eai, r10, Vector(ai), "04.03.2022", "11:00", "17:00"),
        multiple(eitm, r11, Vector(itm), "04.03.2022", "11:00", "17:00")
      ).flatten

      val binaryRes =
        binaryEval(schedules, List(roomCollision, studyPathCollision))
      binaryRes shouldBe Nil

      val unaryRes = unaryEval(
        schedules,
        List(blockedCollision(Set("05.03.2022", "06.03.2022", "02.03.2022")))
      )
      unaryRes.size shouldBe 6
      unaryRes(0) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday1,
        wednesday1
      )
      unaryRes(1) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday2,
        wednesday2
      )
      unaryRes(2) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday3,
        wednesday3
      )
      unaryRes(3) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday4,
        wednesday4
      )
      unaryRes(4) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday5,
        wednesday5
      )
      unaryRes(5) shouldBe Collision(
        CollisionType.BlockedDay,
        wednesday6,
        wednesday6
      )
    }

    "detect collisions in an example schedule" in {
      val schedules = Vector(
        // Monday
        multiple(
          ap1V,
          r1,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "11:00",
          "13:00"
        ),
        multiple(
          ebrV,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "14:00",
          "16:00"
        ),
        multiple(
          ebrUe,
          r7,
          Vector(ai, wi, mi, itm),
          "28.02.2022",
          "15:00",
          "18:00"
        ),
        // Tuesday
        multiple(
          ma1V,
          r4,
          Vector(ai, wi, mi, itm),
          "01.03.2022",
          "09:00",
          "11:00"
        ),
        multiple(ma1P, r5, Vector(ai), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r6, Vector(mi), "01.03.2022", "12:00", "14:00"),
        multiple(ma1P, r5, Vector(wi), "01.03.2022", "14:00", "16:00"),
        multiple(ma1P, r6, Vector(itm), "01.03.2022", "14:00", "16:00"),
        // Wednesday
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "11:00", "13:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "11:00", "13:00"),
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "13:00", "15:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "13:00", "15:00"),
        multiple(ap1P, r2, Vector(ai), "02.03.2022", "15:00", "17:00"),
        multiple(ap1P, r3, Vector(mi), "02.03.2022", "15:00", "17:00"),
        // Thursday
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "11:00", "13:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "13:00", "15:00"),
        multiple(ap1P, r2, Vector(wi), "03.03.2022", "15:00", "17:00"),
        multiple(ap1P, r3, Vector(itm), "03.03.2022", "15:00", "17:00"),
        // Friday
        multiple(emi, r8, Vector(mi), "04.03.2022", "11:00", "17:00"),
        multiple(ewi, r9, Vector(wi), "04.03.2022", "11:00", "17:00"),
        multiple(eai, r10, Vector(ai), "04.03.2022", "11:00", "17:00"),
        multiple(eitm, r11, Vector(itm), "04.03.2022", "11:00", "17:00")
      ).flatten

      val binaryRes =
        binaryEval(schedules, List(roomCollision, studyPathCollision))
          .groupBy(_.kind)
      binaryRes.size shouldBe 2
      val spColl = binaryRes.find(_._1 == CollisionType.StudyPathTime).get
      spColl._2.size shouldBe 4
      val rColl = binaryRes.find(_._1 == CollisionType.Room).get
      rColl._2.size shouldBe 16

      val unaryRes = unaryEval(
        schedules,
        List(blockedCollision(Set("05.03.2022", "06.03.2022")))
      )
      unaryRes shouldBe Nil
    }
  }
}
