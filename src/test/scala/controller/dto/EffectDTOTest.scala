package controller.dto

import controller.converters.ResourceConverters
import controller.converters.ResourceConverters.resourceWithAmountToString
import controller.dto.EffectDTO.unapply
import model.effects.ResourceEffect
import model.resource.*
import org.scalatest.funsuite.AnyFunSuite

class EffectDTOTest extends AnyFunSuite {
  val goldRes = Gold(10)
  val gpRes = GloryPoint(9)
  val sunRes = SunCrystal(8)
  val moonRes = MoonCrystal(7)
  //
  //  test("apply"):
  //    var effectDTO = EffectDTO(ResourceEffect(goldRes, None))
  //    assert(effectDTO.label.get == goldRes.amount.toString)
  //    effectDTO = EffectDTO(ResourceEffect(gpRes, None))
  //    assert(effectDTO.label.get == gpRes.amount.toString)
  //    effectDTO = EffectDTO(ResourceEffect(sunRes, None))
  //    assert(effectDTO.label.get == sunRes.amount.toString)
  //    effectDTO = EffectDTO(ResourceEffect(moonRes, None))
  //    assert(effectDTO.label.get == moonRes.amount.toString)
}
