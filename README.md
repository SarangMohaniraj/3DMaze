# 3DMaze

## Installation
```batch
git clone https://github.com/SarangMohaniraj/3DMaze.git
```

## Scripts
### Run Script
```batch
javac MazeProgram.java
java MazeProgram
```

## Controls
1. Pressing the `UP` key to move forward respective to your direction.
2. Use the  `LEFT` and `RIGHT` keys to turn.
3. Press `SPACE` to open up the map.
4. When the map is opened, press `ENTER` to toggle the flashlight's high beam

## Game Mechanics
A maze is randomly generated using a depth first search algorithm. Your vision is limited by your visibility. When the map is open, you can only see your location and a small radius with your flashlight. You lose 2% battery every 5 seconds. Use your flashlight's to see a larger radius on the map. Do not keep high beam on for too long as it drains four times more battery than usual. As you lose battery, the flashlight dims on the map. If your battery reaches zero, you are unable to complete the maze. Additionally, the maze's layout will randomly change when you least expect it, rendering it nearly impossible to complete!
