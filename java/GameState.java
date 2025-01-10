package pack;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

class GameState {
    Point player1Position = new Point(0, 0);
    Point player2Position = new Point(9, 9);
    Set<Building> buildings = new HashSet<>();
    int currentPlayerTurn = 1;
    int player1Money = 100;
    int player2Money = 100;
    int player1Power = 10;
    int player2Power = 10;
    int movesLeft = 0;
    private JLabel walletDisplay = new JLabel("Player 1: $100 | Player 2: $100", SwingConstants.CENTER);
    private JLabel powerDisplay = new JLabel("Player 1 Power: 10 | Player 2 Power: 10", SwingConstants.CENTER);
    private JLabel statusLabel;
    java.util.List<Treasure> player1Treasures = new ArrayList<Treasure>();
    List<Treasure> player2Treasures = new ArrayList<Treasure>();


    public void playerEarnsMoney(int playerNumber, int amount) {
        if (playerNumber == 1) {
            player1Money += amount;
        } else if (playerNumber == 2) {
            player2Money += amount;
        }
        updateDisplays();
    }

    public void playerUsesPower(int playerNumber, int powerUsed) {
        if (playerNumber == 1) {
            player1Power -= powerUsed;
        } else if (playerNumber == 2) {
            player2Power -= powerUsed;
        }
        updateDisplays();
    }


    public GameState(JLabel walletDisplay, JLabel powerDisplay, JLabel statusLabel) {
        this.walletDisplay = walletDisplay;
        this.powerDisplay = powerDisplay;
        this.statusLabel = statusLabel;
        initializeBuildings();
    }

    private void initializeBuildings() {
        buildings.add(new Castle(new Point(4, 4)));
        // Assuming addRandomBuildings method properly initializes other buildings
        addRandomTreasures();
        addRandomBuildings(Wall.class, 5);
        addRandomBuildings(Market.class, 5);
        addRandomBuildings(LostItem.class, 13);
        addRandomBuildings(Trap.class, 5);
    }

    private void addRandomTreasure(String name, int value) {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            Point position = new Point(x, y);

            if (position.equals(player1Position) || position.equals(player2Position)) continue;

            boolean isOverlapping = buildings.stream().anyMatch(b -> b.getPosition().equals(position));
            if (!isOverlapping) {
                buildings.add(new Treasure(position, name, value));
                break;
            }
        }
    }

    private void addRandomTreasures() {
        String[] treasureNames = {"Diamond Ring", "Jewel-encrusted Sword", "Golden Goblet", "Crystal Goblets", "Wooden Bow", "Paladin’s Shield", "Golden Key", "Dragon’s Scroll"};
        int[] treasureValues = {100, 200, 150, 120, 80, 180, 140, 220}; // example values, adjust as needed

        for (int i = 0; i < treasureNames.length; i++) {
            addRandomTreasure(treasureNames[i], treasureValues[i]);
        }
    }

    private void addRandomBuildings(Class<? extends Building> buildingClass, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            while (true) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Point position = new Point(x, y);

                if (position.equals(player1Position) || position.equals(player2Position)) continue;

                boolean isOverlapping = buildings.stream().anyMatch(b -> b.getPosition().equals(position));
                if (!isOverlapping) {
                    try {
                        buildings.add(buildingClass.getConstructor(Point.class).newInstance(position));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isBuilding(int x, int y) {
        return buildings.stream().anyMatch(b -> b.getPosition().equals(new Point(x, y)));
    }

    public int rollDice() {
        Random random = new Random();
        movesLeft = random.nextInt(6) + 1; // Dice roll between 1 and 6
        return movesLeft;
    }

    public void interactWithBuilding(Building building) {
        building.setVisible(true); // Make the building visible when interacted with
        if (building instanceof Market) {
            interactWithMarket(currentPlayerTurn);
        } else if (building instanceof Treasure) {
            if (currentPlayerTurn == 1) {
                player1Treasures.add((Treasure) building);
            } else {
                player2Treasures.add((Treasure) building);
            }
            buildings.remove(building);
        } else if (building instanceof LostItem) {
            LostItem lostItem = (LostItem) building;
            if (!lostItem.hasGivenMoney()) {
                // Generate a random amount of money between 1 and 50
                Random random = new Random();
                int moneyGiven = random.nextInt(50) + 1; // This will give a value between 1 (inclusive) and 51 (exclusive)
                playerEarnsMoney(currentPlayerTurn, moneyGiven);
                lostItem.giveMoney(); // Prevent further money from being given
                JOptionPane.showMessageDialog(null, "Player " + currentPlayerTurn + " found a lost item and received $" + moneyGiven + "!");
            }
        }
        updateDisplays(); // Update the display after interaction
    }


    public void updateDisplays() {
        // ...
        String player1TreasuresString = player1Treasures.stream().map(Treasure::getName).collect(Collectors.joining(", "));
        String player2TreasuresString = player2Treasures.stream().map(Treasure::getName).collect(Collectors.joining(", "));
        SwingUtilities.invokeLater(() -> {
            walletDisplay.setText(String.format("Player 1: $%d | Player 2: $%d", player1Money, player2Money));
            powerDisplay.setText(String.format("Player 1 Power: %d | Player 2 Power: %d", player1Power, player2Power));
            statusLabel.setText(String.format("Player 1 Treasures: %s | Player 2 Treasures: %s", player1TreasuresString, player2TreasuresString));
        });
    }

    private void interactWithMarket(int playerNumber) {
        List<String> optionsList = new ArrayList<>(Arrays.asList("$50 for 20 Power", "$30 for 10 Power", "$10 for 5 Power", "Buy treasure location for $75", "Cancel"));
        Object[] options = optionsList.toArray();
        int choice = JOptionPane.showOptionDialog(null, "Select an option:",
                "Market", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[optionsList.size() - 1]);
        int moneyCost = 0;
        int powerGain = 0;
        switch (choice) {
            case 0:
                moneyCost = 50;
                powerGain = 20;
                break;
            case 1:
                moneyCost = 30;
                powerGain = 10;
                break;
            case 2:
                moneyCost = 10;
                powerGain = 5;
                break;
            case 3:
                moneyCost = 75;
                // Buying treasure location logic goes here
                if ((playerNumber == 1 && player1Money >= moneyCost) || (playerNumber == 2 && player2Money >= moneyCost)) {
                    revealTreasureLocation(playerNumber, moneyCost);
                    return; // Exit the method to avoid falling into the default case
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough money for this purchase.");
                    return; // Exit the method as no further action is required
                }
            default: // Cancel or close
                return;
        }

        if (playerNumber == 1 && player1Money >= moneyCost) {
            player1Money -= moneyCost;
            player1Power += powerGain;
            JOptionPane.showMessageDialog(null, "Player 1, transaction completed.");
        } else if (playerNumber == 2 && player2Money >= moneyCost) {
            player2Money -= moneyCost;
            player2Power += powerGain;
            JOptionPane.showMessageDialog(null, "Player 2, transaction completed.");
        } else {
            JOptionPane.showMessageDialog(null, "Not enough money for this purchase.");
        }
        updateDisplays();
    }

    private void revealTreasureLocation(int playerNumber, int cost) {
        // Assuming there's a method to get a random hidden treasure
        Optional<Treasure> hiddenTreasure = buildings.stream()
                .filter(b -> b instanceof Treasure && !b.isVisible())
                .map(b -> (Treasure) b)
                .findAny();

        if (hiddenTreasure.isPresent()) {
            Treasure treasure = hiddenTreasure.get();
            Point position = treasure.getPosition();
            treasure.setVisible(true); // Optionally make the treasure visible on the map
            if (playerNumber == 1) {
                player1Money -= cost;
            } else {
                player2Money -= cost;
            }
            JOptionPane.showMessageDialog(null, String.format("Treasure '%s' located at (%d, %d).", treasure.getName(), position.x, position.y));
        } else {
            JOptionPane.showMessageDialog(null, "No hidden treasures left to reveal!");
        }
        updateDisplays();
    }

    public boolean movePlayer(int dx, int dy) {
        if (movesLeft <= 0) return false;

        Point playerPosition = currentPlayerTurn == 1 ? player1Position : player2Position;
        Point newPosition = new Point(playerPosition.x + dx, playerPosition.y + dy);

        if (newPosition.x < 0 || newPosition.x >= 10 || newPosition.y < 0 || newPosition.y >= 10 || isWall(newPosition.x, newPosition.y))
            return false;

        playerPosition.setLocation(newPosition);
        movesLeft--;

        Building building = getBuildingAtPosition(newPosition.x, newPosition.y);
        if (building != null) {
            interactWithBuilding(building);
        }

        if (player1Position.equals(player2Position)) {
            conductBattle();
        }
        if (movesLeft == 0) {
            currentPlayerTurn = currentPlayerTurn == 1 ? 2 : 1;
        }

        updateDisplays(); // Update the display after a player moves
        return true;
    }

    private boolean isWall(int x, int y) {
        return buildings.stream().anyMatch(b -> b instanceof Wall && b.getPosition().equals(new Point(x, y)));
    }

    private void conductBattle() {
        int totalPower = player1Power + player2Power; // This is for calculating the money fraction

        if (player1Power > player2Power) {
            applyBattleOutcome(1, 2, totalPower); // Player 1 wins
        } else if (player2Power > player1Power) {
            applyBattleOutcome(2, 1, totalPower); // Player 2 wins
        } else {
            // Optional: handle a draw condition, decide tiebreaker
            JOptionPane.showMessageDialog(null, "The battle is a draw! Both players hold their ground.");
        }

        updateDisplays(); // Update displays to reflect changes
    }

    private void applyBattleOutcome(int winner, int loser, int totalPower) {
        double moneyWon;

        if (winner == 1) {
            // Calculate the money won based on power differences and total power
            moneyWon = ((double) (player1Power - player2Power) / totalPower) * player2Money;
            // Update the money after battle
            player1Money += moneyWon;
            player2Money -= moneyWon;
            // Apply the reduction of power
            player1Power -= player2Power;
            player2Power = 0;
            // Reset the loser's position
            player2Position.setLocation(9, 9);
        } else {
            // Calculate the money won based on power differences and total power
            moneyWon = ((double) (player2Power - player1Power) / totalPower) * player1Money;
            // Update the money after battle
            player2Money += moneyWon;
            player1Money -= moneyWon;
            // Apply the reduction of power
            player2Power -= player1Power;
            player1Power = 0;
            // Reset the loser's position
            player1Position.setLocation(0, 0);
        }

        // Display the outcome of the battle
        JOptionPane.showMessageDialog(null, "Player " + winner + " wins the battle and takes $" + String.format("%.2f", moneyWon) + " from Player " + loser);
    }

    // Helper method to get the building at a specific position
    private Building getBuildingAtPosition(int x, int y) {
        for (Building building : buildings) {
            if (building.getPosition().equals(new Point(x, y))) {
                return building;
            }
        }
        return null;
    }


    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public boolean isCurrentPlayerTurn() {
        return movesLeft > 0;
    }
}
