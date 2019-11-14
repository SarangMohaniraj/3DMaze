import java.util.*;
public class Maze{
	public static Wall[][] walls;
  public static int rows = 35, columns = 35;
	public Maze(Wall[][] walls){
		this.walls = walls;
	}
	public Wall[][] getWalls(){return walls;}
  public static boolean contains(Location location){return !(location.getX() >= columns || location.getX() < 0 || location.getY() >= rows || location.getY() < 0);}
  public static String getKey(Location location){return walls[location.getX()][location.getY()].getKey();}
  //generate maze
  //depth first search
  public ArrayList<String> generate(String[][]maze,int x,int y){
    int[][] dirs = {new int[]{-1,0},new int[]{1,0},new int[]{0,-1},new int[]{0,1}};
    int width = maze[0].length, height = maze.length;

    maze[y][x] = " ";
    List<int[]> tempList = Arrays.asList(dirs);
    Collections.shuffle(tempList);
    tempList.toArray(dirs);

    for(int[] tuple : dirs){
      int dx = tuple[0], dy = tuple[1];
      int nx = x + 2 * dx, ny =  y + 2 * dy;
      if (0 < nx  && nx < width-1 && 0 < ny && ny < height-1 && maze[ny][nx].equals("▀")){
        maze[y + dy][x + dx] = " ";
        generate(maze, nx, ny);
      }
    }

    //convert 2D array to arraylist
    ArrayList<String> mazeList = new ArrayList<>();
    for(String[] row : maze){
      String line = "";
      for(String s : row)
        line += s;
      mazeList.add(line);
    }
    return mazeList;
  }
  public ArrayList<String> make(int rows, int columns){
    String[][]maze = new String[rows][columns];
    //fill maze with
    for(int i = 0; i <columns; i++){
      for(int j = 0; j <rows; j++){
        maze[i][j] = "▀";
      }
    }
    maze[1][0] = "A";
    maze[rows-2][columns-1] = "Z";
    return generate(maze,1,1);
  }
}