package view

import controller.{ControllerMatchInitImpl}
import model.{PlayerFactoryImpl}

@main
def main() = {
  GUIMatchStart(ControllerMatchInitImpl(PlayerFactoryImpl()))
}