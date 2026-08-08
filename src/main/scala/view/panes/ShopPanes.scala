package view.panes

import controller.dto.ItemDTO
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import view.buttons.ButtonFactory.makeBoardButton
import view.panes.EffectPanes.*

object ShopPanes:
  private class ItemPane(itemDTO: ItemDTO) extends VBox:
    private def buyItemButton(itemDTO: ItemDTO): Button =
      //  TODO: Aggiungi a LanguageStrings
      makeBoardButton("Compra", itemDTO.onClick, () => !itemDTO.clickable())

    children = Seq(
      EffectPane(itemDTO.item),
      EffectPane(itemDTO.cost),
      buyItemButton(itemDTO)
    )

  class ShopPane(items: Seq[ItemDTO]) extends BorderPane:
    private val (topRow, bottomRow) = items.splitAt(items.size / 2)

    top = new HBox {
      children = topRow.map(ItemPane(_))
    }
    bottom = new HBox {
      children = bottomRow.map(ItemPane(_))
    }
