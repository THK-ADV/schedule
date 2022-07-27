package suite

import org.scalatest.OptionValues
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

trait UnitSpec extends AnyWordSpec with should.Matchers with OptionValues
