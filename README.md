# Tic-Tac-Toe

## How to Compile and Run

Navigate into the project folder:

```bash
cd tictactoe
```

Compile:

```bash
javac App.java Board.java Game.java GameLog.java InputValidator.java Player.java
```

Run:

```bash
java App
```

---

## How to Play

Cells are numbered 1–9, left to right, top to bottom:

```
 1 | 2 | 3
---+---+---
 4 | 5 | 6
---+---+---
 7 | 8 | 9
```

- Player 1 is **X**, Player 2 is **O**
- On your turn, type the number of the cell you want and press Enter
- First player to get three in a row (across, down, or diagonal) wins
- If all 9 cells fill up with no winner, it's a draw
- After each round you'll see a scoreboard with wins and draws
- If you play again, the player who just lost goes first next round
