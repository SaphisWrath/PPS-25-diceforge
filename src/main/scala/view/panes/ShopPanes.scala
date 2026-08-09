package view.panes

import controller.dto.ItemDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import view.LanguageStrings
import view.buttons.ButtonFactory.makeBoardButton
import view.panes.EffectPanes.*
import view.theme.JfxTheme.*
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}

object ShopPanes:
  private class ItemPane(itemDTO: ItemDTO) extends VBox:
    private def buyItemButton(itemDTO: ItemDTO): Button =
      makeBoardButton(LanguageStrings.ShopStrings.purchase, itemDTO.onClick, () => !itemDTO.clickable())

    border = makeBorder(primaryBorder)
    background = makeBackgroundFill(primaryContainer)
    alignment = Center
    padding = Insets(10)
    spacing = 10
    children = Seq(
      EffectWrapperPane(LanguageStrings.ShopStrings.cost, Seq(itemDTO.cost), primaryBorder),
      EffectWrapperPane(LanguageStrings.ShopStrings.item, Seq(itemDTO.item), primaryBorder),
      buyItemButton(itemDTO)
    )

  class ShopPane(items: Seq[ItemDTO]) extends BorderPane:
    private val spacingValue: Int = 20
    private val (topRow, bottomRow) = items.splitAt(items.size / 2)

    private def shopRow(items: Seq[ItemDTO]): HBox = new HBox {
        spacing = spacingValue
        alignment = Center
        children = items.map(ItemPane(_))
      }

    center = new VBox {
      alignment = Center
      spacing = spacingValue
      children = Seq(
        shopRow(topRow),
        shopRow(bottomRow)
      )
    }
