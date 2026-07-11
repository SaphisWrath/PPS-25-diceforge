package controller.dto

import controller.converters.ResourceConverters
import controller.converters.ResourceConverters.resourceWithAmountToString
import controller.dto.EffectDTO.unapply
import model.effects.ResourceEffect
import model.resource.{GloryPoint, Gold, MoonCrystal, PlayerBoard, SunCrystal}
import org.scalatest.funsuite.AnyFunSuite

class EffectDTOTest extends AnyFunSuite{
  val goldRes = Gold(10)
  val gpRes = GloryPoint(9)
  val sunRes = SunCrystal(8)
  val moonRes = MoonCrystal(7)

  test("unapply"):
    var res = EffectDTO("+", Option(resourceWithAmountToString(goldRes)))
    assert(unapply(res) == ResourceEffect(goldRes, None))
    res = EffectDTO("+", Option(resourceWithAmountToString(gpRes)))
    assert(unapply(res) == ResourceEffect(gpRes, None))
    res = EffectDTO("+", Option(resourceWithAmountToString(sunRes)))
    assert(unapply(res) == ResourceEffect(sunRes, None))
    res = EffectDTO("+", Option(resourceWithAmountToString(moonRes)))
    assert(unapply(res) == ResourceEffect(moonRes, None))

  test("apply"):
    var effectDTO = EffectDTO(ResourceEffect(goldRes, None))
    assert(effectDTO.effectType == "+")
    assert(effectDTO.resource.get == resourceWithAmountToString(goldRes))
    effectDTO = EffectDTO(ResourceEffect(gpRes, None))
    assert(effectDTO.resource.get == resourceWithAmountToString(gpRes))
    effectDTO = EffectDTO(ResourceEffect(sunRes, None))
    assert(effectDTO.resource.get == resourceWithAmountToString(sunRes))
    effectDTO = EffectDTO(ResourceEffect(moonRes, None))
    assert(effectDTO.resource.get == resourceWithAmountToString(moonRes))
}
