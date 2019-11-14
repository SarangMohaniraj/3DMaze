import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.*;
public class MazeProgram extends JPanel implements KeyListener,MouseListener
{
	JFrame frame;
	//declare an array to store the maze - Store Wall(s) in the array
	int screenWidth=1000,screenHeight=800;
	Maze maze;
	Wall[][]walls;
	Explorer explorer;
	ArrayList<Polygon> walls3D;
  boolean toggle2DMap = false;
  boolean toggleHighBeam = false;
  boolean run = true;

	public MazeProgram()
	{
    maze = new Maze(new Wall[1][1]);
		setBoard(Maze.rows,Maze.columns);
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenWidth,screenHeight);
		frame.setVisible(true);
		frame.addKeyListener(this);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);	//this will set the background color
		g.fillRect(0,0,screenWidth,screenHeight);  //since the screen size is 1000x800
									//it will fill the whole visible part
									//of the screen with a black rectangle

    

    if(run){
      //2D
      if(toggle2DMap){
        for(int i = 0; i < walls.length; i ++){
          for(int j = 0; j < walls[0].length; j++){
            switch(walls[i][j].getKey()){
              case "WALL" : g.setColor(Color.YELLOW);
                      g.drawRect(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      break;
              case "ENTER" : g.setColor(Color.CYAN);
                      g.fillRect(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      // g.setColor(Color.BLUE);
                      // g.fillOval(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      break;
              case "EXIT" : g.setColor(Color.GREEN);
                      g.fillRect(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      // g.setColor(Color.GREEN);
                      // g.fillOval(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      break;
              case "EMPTY" : g.setColor(Color.BLACK);
                      g.fillRect(walls[i][j].getX()*20,walls[i][j].getY()*20,20,20);
                      break;
            }
          }
        }

        g.setColor(Color.YELLOW);
        g.fillOval(explorer.getX()*20,explorer.getY()*20,20,20);

        
        //flashlight
        Graphics2D g2d = (Graphics2D) g;
        Area outside = new Area(new Rectangle(0,0,screenWidth,screenHeight));
        Area inside = new Area(new Ellipse2D.Double(explorer.getX()*20-(20*explorer.getVisibility()/2-20/2),explorer.getY()*20-(20*explorer.getVisibility()/2-20/2),20*explorer.getVisibility(),20*explorer.getVisibility()));
        outside.subtract(inside);
        g2d.setColor(Color.BLACK);
        g2d.fill(outside);

        g2d.setColor(Color.YELLOW);
        g2d.draw(new RoundRectangle2D.Double(screenWidth*10/13,screenHeight*10/15,150,40,10,10));
        g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+150,screenHeight*10/15+14,7,10,2,2));

        if(explorer.getFlashlightBattery() >= 90){
          g2d.setColor(Color.YELLOW);
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+5,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+40+1,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+70+8,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+100+15,screenHeight*10/15+5,30,30,5,5));
        }
        if(explorer.getFlashlightBattery() >= 75){
          g2d.setColor(new Color(0,0,0,0));
          g2d.fill(inside);
          g2d.setColor(Color.YELLOW);
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+5,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+40+1,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+70+8,screenHeight*10/15+5,30,30,5,5));
        }else if(explorer.getFlashlightBattery() >= 50){
          g2d.setColor(new Color(0,0,0,.5f));
          g2d.fill(inside);
          g2d.setColor(Color.YELLOW);
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+5,screenHeight*10/15+5,30,30,5,5));
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+40+1,screenHeight*10/15+5,30,30,5,5));
        }else if(explorer.getFlashlightBattery() >= 25){
          g2d.setColor(new Color(0,0,0,.8f));
          g2d.fill(inside);
          g2d.setColor(Color.YELLOW);
          g2d.fill(new RoundRectangle2D.Double(screenWidth*10/13+5,screenHeight*10/15+5,30,30,5,5));
        }else{
          g2d.setColor(new Color(0,0,0,.9f));
          g2d.fill(inside);
        }
        g.setFont(new Font("Lato",Font.PLAIN,18));
        g.drawString("Steps: " + explorer.getSteps(), screenWidth*10/13+40,screenHeight*9/15+30);
      }else{ //3D
        for(Polygon p : walls3D){
          g.setColor(Color.YELLOW);
          g.drawPolygon(p);
        }
      }
      //compass
      g.setColor(Color.YELLOW);
      g.drawOval(screenWidth*10/13,screenHeight*11/15,150,150);
      switch(explorer.getDirection()){
        case 0 : g.drawPolygon(
                    new int[] {screenWidth*10/13+75,screenWidth*10/13+75+5,screenWidth*10/13+75-5},
                    new int[] {screenHeight*11/15+5,screenHeight*11/15+75,screenHeight*11/15+75},3);
                  break;
        case 1 : g.drawPolygon(
                    new int[] {screenWidth*10/13+75,screenWidth*10/13+150-5,screenWidth*10/13+75},
                    new int[] {screenHeight*11/15+75-5,screenHeight*11/15+75,screenHeight*11/15+75+5},3);
                  break;
        case 2 : g.drawPolygon(
                    new int[] {screenWidth*10/13+75-5,screenWidth*10/13+75+5,screenWidth*10/13+75},
                    new int[] {screenHeight*11/15+75,screenHeight*11/15+75,screenHeight*11/15+150-5},3);
                  break;
        case 3 : g.drawPolygon(
                    new int[] {screenWidth*10/13+5,screenWidth*10/13+75,screenWidth*10/13+75},
                    new int[] {screenHeight*11/15+75,screenHeight*11/15+75-5,screenHeight*11/15+75+5},3);
                  break;
      }
    }else if(explorer.getFlashlightBattery() <= 0){
      g.setColor(Color.YELLOW);
      g.setFont(new Font("Lato",Font.BOLD,48));
      g.drawString("You ran out of battery!", screenWidth/4-70,screenHeight/2-60);
      g.drawString("Steps: " + explorer.getSteps(), screenWidth/2-100,screenHeight/2+60);
    }else{
      g.setColor(Color.YELLOW);
      g.setFont(new Font("Lato",Font.BOLD,48));
      g.drawString("You blew up the Death Star!", screenWidth/4-100,screenHeight/2-60);
      g.drawString("Steps: " + explorer.getSteps(), screenWidth/2-100,screenHeight/2+60);
    }



		
	}
  public void setBoard(int rows, int columns){
    ArrayList<String> mazeList = maze.make(rows,columns);

    walls = new Wall[mazeList.size()][mazeList.get(0).length()];
      for(int i = 0; i < walls.length; i ++){
        for(int j = 0; j < walls[0].length; j++){
          switch(mazeList.get(i).charAt(j)){
            case '▀' : walls[i][j] = new Wall(i,j, Wall.WALL);
                  break;
            case 'A' : walls[i][j] = new Wall(i,j, Wall.ENTER);
                  if(explorer == null){
                    explorer = new Explorer(i,j);
                    explorer.startTime();
                    new Thread(() -> {
                      while(true){
                        try{
                          Thread.sleep(1000);
                        }catch(Exception e){}
                        explorer.useFlashlightBattery(toggleHighBeam ? 4 : 1);
                        if(explorer.getFlashlightBattery() <= 0)
                          run = false;
                      }
                    }).start();
                  }
                  break;
            case 'Z' : walls[i][j] = new Wall(i,j, Wall.EXIT);
                  break;
            default  : walls[i][j] = new Wall(i,j, Wall.EMPTY);
                  break;
          }
        }
      }
      maze = new Maze(walls);
      draw3D();
  }

	public void draw3D()
	{

		walls3D = new ArrayList<Polygon>();

    int intervalX = screenWidth/13, intervalY = screenHeight/15;

    for(int i = 0; i < explorer.getVisibility(); i++){
      if(explorer.nextLocation(i) == null || Maze.getKey(explorer.nextLocation(i)).equals("WALL"))
        break;

      //if the turns are difficult to see, comment out the ceiling (if it is still too hard comment out the floor)
      //ceiling
      int[]floorAndCeilingX = {intervalX*i,screenWidth-(intervalX*i),screenWidth-(intervalX+intervalX*i),intervalX+intervalX*i};
      int[]ceilingY = {intervalY*i,intervalY*i,intervalY+intervalY*i,intervalY+intervalY*i};
      walls3D.add(new Polygon(floorAndCeilingX,ceilingY,4));

      //floor
      int[]floorY = {screenHeight-intervalY*i,screenHeight-intervalY*i,screenHeight-intervalY-intervalY*i,screenHeight-intervalY-intervalY*i};
      walls3D.add(new Polygon(floorAndCeilingX,floorY,4));


      //left
      int[]leftX = {intervalX*i,intervalX+intervalX*i,intervalX+intervalX*i,intervalX*i};
      int[]y = {intervalY*i,intervalY+intervalY*i,screenHeight-intervalY-intervalY*i,screenHeight-intervalY*i};
      
      if(explorer.checkLeft(i))
        y = new int[]{intervalY+intervalY*i,intervalY+intervalY*i,screenHeight-intervalY-intervalY*i,screenHeight-intervalY-intervalY*i};
      walls3D.add(new Polygon(leftX,y,4));

      //right
      y = new int[]{intervalY*i,intervalY+intervalY*i,screenHeight-intervalY-intervalY*i,screenHeight-intervalY*i};
      if(explorer.checkRight(i))
        y = new int[]{intervalY+intervalY*i,intervalY+intervalY*i,screenHeight-intervalY-intervalY*i,screenHeight-intervalY-intervalY*i};
      walls3D.add(new Polygon(flipX(leftX),y,4));

    }
		
	}
  //flip x coordinates from left to right of screen
  public int[] flipX(int[] x) {
        int[] x2 = new int[x.length];
        for(int i = 0; i < x.length; i++)
            x2[i] = screenWidth-x[i];
        return x2;
    }



	public void keyPressed(KeyEvent e)
	{
    if(run){
  		switch(e.getKeyCode()){
  			case 38 : explorer.move(); //up
                  if(Maze.getKey(explorer.getLocation()).equals("EXIT"))
                    run = false;
              break;
        case 87 : explorer.move(); //up
                  if(Maze.getKey(explorer.getLocation()).equals("EXIT"))
                    run = false;
              break;
  			case 37 : explorer.setDirection(Explorer.LEFT); //left
  						break;
        case 65 : explorer.setDirection(Explorer.LEFT); //left
              break;
  			case 39 : explorer.setDirection(Explorer.RIGHT); //right
  						break;
        case 68 : explorer.setDirection(Explorer.RIGHT); //right
              break;
        case 32 : toggle2DMap = !toggle2DMap; //space
                  if(!toggle2DMap){
                    toggleHighBeam = false;
                    explorer.setVisibility(5);
                  }
              break;
        case 10 : if(toggle2DMap){ //enter
                    toggleHighBeam = !toggleHighBeam;
                    if(toggleHighBeam)
                      explorer.setVisibility(10);
                    else
                      explorer.setVisibility(5);
                  }
              break;
  		}
    }
    draw3D();
    if(explorer.getSteps() != 0 && explorer.getSteps() % (int)((Math.random()*50)+20) == 0)
      setBoard(Maze.rows,Maze.columns); //generate random maze every random steps
		repaint();

	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}