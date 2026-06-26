package view;

import controller.ControllerMatchInit;

import javax.swing.*;
import java.awt.*;

public class GUIMatchStart extends JFrame {

    public GUIMatchStart(ControllerMatchInit controller) {
        this.setSize(400, 150);
        this.getContentPane().add(BorderLayout.CENTER, new PanelInitMatch(controller));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
