/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {

    public void run() {

        // Top-level frame in which game components live
        JFrame frame = new JFrame("Rocketman");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(frame, status);
        frame.add(court, BorderLayout.CENTER);

        GameFileParser parser = new GameFileParser();

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton help = new JButton("Instructions");
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Welcome to Rocketman. "
                                + "\nUse WASD or arrow keys to move. \nPress SPACE to attack. "
                                + "\nPress E while near a space station to enter the bazaar.",
                                "Controls", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null,
                        "You are lost in space with nothing but your ship and a few credits."
                                + "\n You must find your way back home."
                                + "\n There are rumors that you can hire a "
                                + "navigator for 50,000 credits to help you get home.",
                                "Story", JOptionPane.PLAIN_MESSAGE);
                JOptionPane.showMessageDialog(null,
                        "Destroy asteroids for raw materials."
                                + "\nVisit outposts to trade, sell, and upgrade your ship."
                                + "\nBe careful out there!",
                                "Gameplay",
                                JOptionPane.PLAIN_MESSAGE);
                court.requestFocusInWindow();

            }
        });
        control_panel.add(help);

        // Note here that when we add an action listener to the reset button, we define
        // it as an
        // anonymous inner class that is an instance of ActionListener with its
        // actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be
        // called.
        final JButton reset = new JButton("New Game");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (court.getState() == GameState.DEAD) {
                    JOptionPane.showMessageDialog(null, "You can't save when you're dead!",
                            "Can't Save", JOptionPane.ERROR_MESSAGE);
                    court.requestFocusInWindow();
                    return;
                }
                
                if (court.getState() == GameState.WIN) {
                    JOptionPane.showMessageDialog(null, "No need to save, you beat the game!",
                            "Can't Save", JOptionPane.ERROR_MESSAGE);
                    court.requestFocusInWindow();
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                int selection = fileChooser.showSaveDialog(null);

                if (selection == JFileChooser.APPROVE_OPTION) {
                    status.setText(fileChooser.getSelectedFile().getAbsolutePath());

                    String data = parser.gameDataToText(court.getGalaxy(), court.getCurrentX(),
                            court.getCurrentY(), court.getPlayerShip());
                    parser.saveGameDataToFile(data,
                            fileChooser.getSelectedFile().getAbsolutePath());
                }

                court.requestFocusInWindow();
            }
        });
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int selection = fileChooser.showSaveDialog(null);

                if (selection == JFileChooser.APPROVE_OPTION) {
                    status.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    String txt = parser
                            .getTextFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        parser.loadTextToGameData(txt, court);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null,
                                "Error while reading save file. Resetting game", "Loading Error",
                                JOptionPane.ERROR_MESSAGE);
                        court.requestFocusInWindow();
                        court.reset();
                    }
                }

                court.requestFocusInWindow();
            }
        });
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Game());
    }
}