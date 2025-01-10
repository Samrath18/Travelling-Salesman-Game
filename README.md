# 2024/04/04

# Travelling-Salesman-Game

## Overview

This project is a 2-player strategy game set on a grid, where players navigate a board filled with different buildings, interact with them, and collect treasures, power-ups, and money. The players take turns rolling dice to move across the board, engaging with different types of buildings such as markets, treasures, traps, lost items, and more. The game incorporates strategic elements, power management, and random events to create an engaging experience.

## Features

### 1. **2-Player Turn-Based Gameplay**
   - The game alternates turns between two players (Player 1 and Player 2).
   - Players move on a 10x10 grid and interact with buildings located at various positions.
   - Players roll a dice to determine how many steps they can take during their turn.
   - A battle may occur when players land on the same position on the grid.

### 2. **Building Types**
   - The grid contains various types of buildings, each with different functions:
     - **Castle**: The starting point for Player 1 (Position `(0, 0)`).
     - **Wall**: Acts as an obstacle that players cannot pass through.
     - **Market**: Allows players to purchase power-ups and other items.
     - **Treasure**: Hidden items that players can collect for a monetary value.
     - **Lost Item**: A building that gives players money when discovered.
     - **Trap**: Hidden buildings that have negative effects when interacted with.
     - **HiddenCell**: A special building that is visually hidden until discovered.

### 3. **Dice Mechanism**
   - Players roll a dice (between 1 to 6) to determine the number of moves they can make during their turn.
   - The roll is random, and the result is displayed after every dice roll.

### 4. **Player Stats and Resources**
   - Each player has two main resources: **Money** and **Power**.
     - **Money** is used to purchase items from the Market or for other game interactions.
     - **Power** determines a player's strength during a battle.
   - Players can earn money and power in different ways:
     - **Market Purchases**: Buy power-ups or treasure locations for a cost.
     - **Lost Items**: Discover hidden items that grant money.
     - **Battles**: Winning a battle against the opponent results in taking their money.
   - Player stats are updated in real-time and displayed on the UI.

### 5. **Battle System**
   - Battles occur when players land on the same position in the grid.
   - Players' **Power** stats determine the winner:
     - The player with the higher power wins and takes a portion of the loser’s money.
     - The loser’s power is reduced to zero, and their position is reset to the starting point.
     - In case of a draw, no money is exchanged, and both players hold their ground.

### 6. **Market Interaction**
   - Players can interact with the **Market** to buy different items:
     - **Power-ups**: Increase a player’s power by spending money (e.g., $50 for 20 power).
     - **Treasure Location**: Buy information about hidden treasure for $75.
   - The **Market** offers a variety of choices, and players can cancel their purchases.

### 7. **Treasure Collection**
   - Treasures are hidden in random locations on the grid.
   - Players can find treasures and collect them for monetary rewards.
   - Each treasure has a name and a value, and treasures are removed from the grid once collected.

### 8. **Lost Item Discovery**
   - Lost items are hidden until discovered by the players.
   - When a player interacts with a **Lost Item**, they receive a random amount of money between $1 and $50.
   - Once money is given, the **Lost Item** becomes inactive.

### 9. **Turn & Movement System**
   - Players can move horizontally or vertically by one step per move.
   - They can only move if they have moves left (determined by the dice roll).
   - Players are not allowed to move outside the boundaries of the 10x10 grid or through walls.
   - A player’s turn ends when they either run out of moves or choose to cancel further movement.

## Gameplay Instructions

1. **Starting the Game**:
   - The game starts with Player 1 at the `(0, 0)` position and Player 2 at `(9, 9)`.
   - Player 1 takes the first turn.

2. **Dice Roll**:
   - Each player rolls the dice to determine how many spaces they can move during their turn.
   - The dice is rolled by clicking its UI with the mouse and the result is displayed in the same place.

3. **Interacting with Buildings**:
   - As players move around the grid, they will land on various types of buildings.
   - Depending on the building, different actions will occur, such as collecting treasures, gaining money, purchasing items from the market, or triggering traps.

4. **Market Transactions**:
   - When a player lands on a Market, they can choose from several purchasing options (e.g., buying power-ups or treasure locations).
   - Make sure to have enough money to make purchases.

5. **Battles**:
   - When both players land on the same position, a battle occurs.
   - The player with the higher power wins and takes money from the losing player.

6. **End of Turn**:
   - A turn ends when the player has moved as much as possible or chooses to stop.
   - The next player then takes their turn, and the game continues until a winner is determined (either by a player’s victory in a battle or through some other game-ending condition).

## Game Rules

- **Movement**:
  - Players can move in four directions: up, down, left, or right.
  - Players cannot pass through walls, and their movement is restricted by the grid boundaries.

- **Power & Money**:
  - Players start with 100 money and 10 power.
  - Power can be increased by purchasing items at the Market.
  - Money can be spent to purchase power-ups or treasure locations at the Market or earned by discovering lost items or treasures.

- **Battle System**:
  - Battles are resolved by comparing players’ power levels.
  - The winner takes a fraction of the loser’s money, and the loser’s power is reduced to zero.
  - In the case of a draw, no money changes hands, but both players remain where they are.
