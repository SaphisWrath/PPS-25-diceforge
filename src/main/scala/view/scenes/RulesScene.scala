package view.scenes

import controller.dto.pathfinders.Paths
import scalafx.scene.Node
import scalafx.scene.control.{Label, ScrollPane}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, Text}
import view.LanguageStrings
import view.ViewComponents.ViewScene
import view.buttons.FxButtonFactory
import view.text.TextFactory
import view.theme.JfxTheme
import view.utils.ViewUtils
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}

import java.nio.file.Files

class RulesScene(hideAction: () => Unit) extends ViewScene[Node]:
  def scene: Node = new VBox {
    val parentWidth: Double = ViewUtils.screenWidth / 3
    val parentHeight: Double = ViewUtils.screenHeight / 1.5
    background = makeBackgroundFill(JfxTheme.tertiary)
    border = makeBorder(JfxTheme.tertiaryBorder)
    maxWidth = parentWidth
    prefWidth = parentWidth
    maxHeight = parentHeight
    prefHeight = parentHeight
    children ++= Seq(
      new ScrollPane {
        private val labelText = Paths.getTextContentsAsString("rules.txt")
        hbarPolicy = ScrollPane.ScrollBarPolicy.Never
        vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
        prefHeight = parentHeight - 20
        maxHeight = parentHeight - 20
        content = TextFactory.makeRulesLabel(labelText, parentWidth, parentHeight)
      },
      FxButtonFactory.makeMenuButton(LanguageStrings.GenericButtonStrings.close, () => hideAction())
    )
  }
