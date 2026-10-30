package fr.upsaclay.bibs.pacman.view;


import fr.upsaclay.bibs.pacman.PacManException;
import fr.upsaclay.bibs.pacman.control.Controller;
import fr.upsaclay.bibs.pacman.control.GameAction;
import fr.upsaclay.bibs.pacman.control.SimpleController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implementations of the app Action Listener for button
 * A different listener is created for each action to be sent to the controller
 */
public class ButtonListener implements ActionListener {

    private final Controller controller;
    private final GameAction action;

    public ButtonListener(Controller controller, GameAction action) {
        this.controller = controller;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        try {
            controller.receiveAction(action);

        } catch (PacManException e) {

            JOptionPane.showMessageDialog(null, "Erreur", "Erreur PacMan", JOptionPane.ERROR_MESSAGE);
        }

    }
}
