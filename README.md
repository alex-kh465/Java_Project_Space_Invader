# Space Invaders Game

A classic 2D Space Invaders game developed in Java with an interactive user interface and engaging gameplay. The player controls a spaceship, shoots bullets to destroy alien invaders, and progresses through increasingly challenging levels. Includes background music for an immersive experience. High scores are now tracked and stored using a MySQL database via JDBC.

---

## Features
- **Player Controls**: Move left, right, and shoot bullets to destroy invaders.
- **Invaders**: Aliens move sideways and down as levels progress.
- **Bullets**: Fired by the player to destroy invaders.
- **Levels**: Difficulty increases with each cleared level.
- **Score Tracking**: Displays current score, high score, and level.
- **High Score Persistence**: Tracks and retrieves high scores from a MySQL database.
- **Game Over**: Detects collisions with invaders.
- **Background Sound**: Loops continuously for a lively gaming experience.

---

## Gameplay
- **Left Arrow Key**: Move spaceship left.
- **Right Arrow Key**: Move spaceship right.
- **Space Key**: Shoot bullets.
- **R Key**: Restart the game after `Game Over`.

---

## Installation

### Prerequisites
1. **Java Requirements**:
   - [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) installed.
   - An Integrated Development Environment (IDE) like [Eclipse](https://www.eclipse.org/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/).

2. **MySQL Database**:
   - [MySQL Server](https://dev.mysql.com/downloads/) installed.
   - A database and table for high scores set up. 

   Example SQL script to set up the table:
   ```sql
   CREATE DATABASE space_invaders;
   USE space_invaders;

   CREATE TABLE high_scores (
       id INT AUTO_INCREMENT PRIMARY KEY,
       player_name VARCHAR(50) NOT NULL,
       score INT NOT NULL,
       date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
