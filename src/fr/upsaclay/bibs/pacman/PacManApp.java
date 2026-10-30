package fr.upsaclay.bibs.pacman;

import fr.upsaclay.bibs.pacman.control.Controller;
import fr.upsaclay.bibs.pacman.control.InterfaceMode;
import fr.upsaclay.bibs.pacman.control.SimpleController;
import fr.upsaclay.bibs.pacman.control.VisualController;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The main application
 */
public class PacManApp {

    public static void main(String[] args) throws PacManException, IOException, URISyntaxException {

        SwingUtilities.invokeLater(() -> {
            try {
                Controller controller = Controller.getController(InterfaceMode.VISUAL);
                controller.setGameType(GameType.CLASSIC);
                //controller.setGameType(GameType.TEST);
                //controller.setGameType(GameType.CLASSIC_TEST);
                controller.initialize();
            } catch (PacManException | IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
