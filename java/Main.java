package pack;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;


class TravelingSalesmanGame extends JFrame {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 850;
    private final int GRID_SIZE = 10;
    private final int CELL_SIZE = 75;
    private JLabel statusLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
    private JLabel walletDisplay = new JLabel("Player 1: $0 | Player 2: $0", SwingConstants.CENTER);
    private JLabel powerDisplay = new JLabel("Player 1 Power: 0 | Player 2 Power: 0", SwingConstants.CENTER);
    private Dice dice = new Dice();
    private GameState gameState;

    public TravelingSalesmanGame() {
        setTitle("Traveling Salesman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setLayout(new BorderLayout()); // Use BorderLayout for the frame

        // Add the labels to the top and bottom of the BorderLayout
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        statusPanel.add(statusLabel);
        statusPanel.add(walletDisplay);
        add(statusPanel, BorderLayout.NORTH);

        add(powerDisplay, BorderLayout.SOUTH);

        gameState = new GameState(walletDisplay, powerDisplay,statusLabel); // Create GameState with references to the labels

        GamePanel gamePanel = new GamePanel(gameState); // GamePanel needs to be modified to accept GameState
        add(gamePanel, BorderLayout.CENTER); // Add the GamePanel to the center

        // Make sure the frame is visible
        setVisible(true);
    }


    // Inside the TravelingSalesmanGame class

    class GamePanel extends JPanel {
        private GameState gameState;
        public GamePanel(GameState gameState) {
            this.gameState = gameState;
            setFocusable(true);
            requestFocusInWindow();
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPress(e);
                }
            });
        }

        private void setupKeyListener() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPress(e);
                    updateStatus(); // Update the status label after handling the key press
                }
            });
        }


        private void handleKeyPress(KeyEvent e) {
            int dx = 0, dy = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    dx = -1;
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    dx = 1;
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    dy = -1;
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    dy = 1;
                    break;
                case KeyEvent.VK_SPACE:
                    if (!gameState.isCurrentPlayerTurn()) {
                        int roll = gameState.rollDice();
                        dice.setText("Dice: " + roll); // Update dice roll visually
                        statusLabel.setText("Player " + gameState.getCurrentPlayerTurn() + " rolls " + roll + "! Moves left: " + gameState.movesLeft);
                    }
                    return;
            }

            if (dx != 0 || dy != 0 && gameState.isCurrentPlayerTurn()) {
                if (gameState.movePlayer(dx, dy)) {
                    repaint();
                    statusLabel.setText("Player " + gameState.getCurrentPlayerTurn() + "'s Turn. Moves left: " + gameState.movesLeft);
                    if (gameState.movesLeft == 0) {
                        statusLabel.setText("Player " + gameState.getCurrentPlayerTurn() + "'s Turn. Press SPACE to roll the dice.");
                    }
                }
            }
        }

        public void updateStatus() {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Player " + gameState.getCurrentPlayerTurn() + "'s Turn");
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int x = i * CELL_SIZE;
                    int y = j * CELL_SIZE;
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    if (gameState.isBuilding(i, j)) {
                        for (Building building : gameState.buildings) {
                            if (building.getPosition().equals(new Point(i, j)) && building.isVisible()) {
                                g.setColor(building.getColor());
                                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                                if (building instanceof Treasure && !gameState.player1Treasures.contains(building) && !gameState.player2Treasures.contains(building)) {
                                    g.setColor(Color.BLACK);
                                    g.drawString(((Treasure) building).getName(), x + 5, y + CELL_SIZE / 2);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            drawPlayer(g, gameState.player1Position, Color.RED, "P1");
            drawPlayer(g, gameState.player2Position, Color.BLUE, "P2");
        }

        private void drawPlayers(Graphics g) {
            drawPlayer(g, gameState.player1Position, Color.RED, "P1");
            drawPlayer(g, gameState.player2Position, Color.BLUE, "P2");
        }

        private void drawPlayer(Graphics g, Point position, Color color, String label) {
            int x = position.x * CELL_SIZE;
            int y = position.y * CELL_SIZE;
            g.setColor(color);
            g.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
            g.setColor(Color.BLACK);
            g.drawString(label, x + (CELL_SIZE / 2) - 5, y + (CELL_SIZE / 2) + 5);
        }

        private void drawBuildings(Graphics g) {
            for (Building building : gameState.buildings) {
                g.setColor(building.getColor());
                int x = building.getPosition().x * CELL_SIZE;
                int y = building.getPosition().y * CELL_SIZE;
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }


        private void drawGrid(Graphics g) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int x = i * CELL_SIZE;
                    int y = j * CELL_SIZE;
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE); // Draw cell borders
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TravelingSalesmanGame game = new TravelingSalesmanGame();
        });
    }
}
