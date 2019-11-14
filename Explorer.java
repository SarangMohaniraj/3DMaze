import java.time.*;
public class Explorer extends Location{

	private Location location;
	private int direction = 0; //0 is north, 1 is east, 2 is south, 3 is west
  public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	public static final String LEFT = "LEFT", RIGHT = "RIGHT";
  int visiblity = 5, steps = 0, flashlightBattery = 100;
  Instant startTime;


	public Explorer(int x, int y){
		super(x,y);
	}
  // public Explorer(Explorer explorer){
  //   this.explorer = explorer;
  // }


	public void move(){
		int dx = 0;
		int dy = 0;

		switch(direction){
			case NORTH : dy--;
					 break;
			case EAST : dx++;
					 break;
			case SOUTH : dy++;
					 break;
			case WEST : dx--;
					 break;
		}
		
		if(getX() + dx < 0 || getY() + dy < 0 || getX() + dx >= Maze.walls.length || getY() + dy >= Maze.walls[0].length || Maze.walls[getX() + dx][getY() + dy].getKey().equals(Wall.WALL)){
			dx = 0;
			dy = 0;
		}else
      steps++;

		setX(getX() + dx);
		setY(getY() + dy);
		location = new Location(getX(),getY());
	}
  public int getSteps(){return steps;}
  public Location getLocation(){return location;}
	public int getDirection(){return direction;}
	public void setDirection(String d){
		if(d.equals(LEFT)){
			direction = turnLeft(direction);
		}else if(d.equals(RIGHT)){
			direction = turnRight(direction);
		}
	}
  public int getVisibility(){return visiblity;}
  public void setVisibility(int visiblity){
    this.visiblity = visiblity; 
  }


    public int turnLeft(int direction) {
      switch(direction){
        case NORTH : direction = WEST;
             break;
        case EAST : direction = NORTH;
             break;
        case SOUTH : direction = EAST;
             break;
        case WEST : direction = SOUTH;
             break;
      }
      return direction;
    }

    public int turnRight(int direction) {
      switch(direction){
        case NORTH : direction = EAST;
             break;
        case EAST : direction = SOUTH;
             break;
        case SOUTH : direction = WEST;
             break;
        case WEST : direction = NORTH;
             break;
      }
      return direction;
    }

  public Location nextLocation(int spacesAhead){
    Location checkLocation = new Location(getX(), getY());        

    //check if the space ahead exists
    switch(direction) {
      case NORTH : checkLocation.setY(checkLocation.getY() - spacesAhead);
          break;
      case EAST : checkLocation.setX(checkLocation.getX() + spacesAhead);     
          break;
      case SOUTH : checkLocation.setY(checkLocation.getY() + spacesAhead);
          break;
      case WEST : checkLocation.setX(checkLocation.getX() - spacesAhead);
          break; 
    }

    if(Maze.contains(checkLocation))
      return checkLocation;
    return null;
  }
  
  public boolean checkLeft(int spacesAhead){
    Location checkLocation = nextLocation(spacesAhead);
    if(checkLocation == null)
      return false;
    //check if the space ahead to the left exists
    switch(direction){
      case NORTH : checkLocation.setX(checkLocation.getX() - 1);
              break;
      case EAST : checkLocation.setY(checkLocation.getY() - 1);
              break;
      case SOUTH : checkLocation.setX(checkLocation.getX() + 1);
              break;
      case WEST : checkLocation.setY(checkLocation.getY() + 1);
              break;
    }
    if(Maze.contains(checkLocation) && !Maze.walls[checkLocation.getX()][checkLocation.getY()].getKey().equals("WALL"))
      return true;
    return false;
  }

  public boolean checkRight(int spacesAhead){
    Location checkLocation = nextLocation(spacesAhead);
    if(checkLocation == null)
      return false;
    //check if the space ahead to the right exists
    switch(direction){
      case NORTH : checkLocation.setX(checkLocation.getX() + 1);
              break;
      case EAST : checkLocation.setY(checkLocation.getY() + 1);
              break;
      case SOUTH : checkLocation.setX(checkLocation.getX() - 1);
              break;
      case WEST : checkLocation.setY(checkLocation.getY() - 1);
              break;
    }
    if(Maze.contains(checkLocation) && !Maze.walls[checkLocation.getX()][checkLocation.getY()].getKey().equals("WALL"))
      return true;
    return false;
  }

    public void startTime(){
      startTime = Instant.now();
    }
    public long getSecondsPassed(){return Duration.between(startTime, Instant.now()).toMillis()/1000;}

    public void useFlashlightBattery(int scale){ //scale is for highbeam
      if(getSecondsPassed() % 5 == 0){
        flashlightBattery -= 2*scale;
      }
    }
    public int getFlashlightBattery(){return flashlightBattery;}



}