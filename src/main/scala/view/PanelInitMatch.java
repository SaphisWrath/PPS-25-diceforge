package view;

import controller.ControllerMatchInit;
import model.Color;

import javax.swing.*;
import java.awt.*;

public class PanelInitMatch extends JPanel {
    public PanelInitMatch(ControllerMatchInit controller) {
        JPanel panelPlayerAmount = new JPanel(new GridLayout(1, 2));
        Choice amountChoice = new Choice();
        amountChoice.addItem("2");
        amountChoice.addItem("3");
        amountChoice.addItem("4");
        panelPlayerAmount.add(new JLabel("How many players?"));
        panelPlayerAmount.add(amountChoice);

        JPanel panelName = new JPanel(new GridLayout(1, 2));
        JTextField nameField = new JTextField();
        panelName.add(new JLabel("Player 1 name:"));
        panelName.add(nameField);

        JPanel panelColor = new JPanel(new GridLayout(1, 2));
        Choice colorChoice = new Choice();
        var iterator = Color.colorMap().keys().iterator();
        while (iterator.hasNext()) {
            colorChoice.addItem(iterator.next());
        }
        panelColor.add(new JLabel("Player color:"));
        panelColor.add(colorChoice);

        this.setLayout(new GridLayout(5, 1));
        JButton confirmButton = new JButton("Add player");
        JLabel feedbackLabel = new JLabel();
        this.add(panelPlayerAmount);
        this.add(panelName);
        this.add(panelColor);
        this.add(confirmButton);
        this.add(feedbackLabel);

        confirmButton.addActionListener(e -> {
            if (!controller.isPlayerAmountSet()) {
                controller.setPlayerAmount(Integer.parseInt(amountChoice.getSelectedItem()));
                amountChoice.setEnabled(false);
            }
            controller.updateMatchInfo(nameField.getText(),
                    colorChoice.getSelectedItem());

            feedbackLabel.setText(controller.isLastPlayerValid()
                    ? "Player Added!"
                    : "Name or color already picked...");

            if (controller.allPlayersSet()) {
                feedbackLabel.setText("Ready to start the game!");
            }
        });
    }
}
