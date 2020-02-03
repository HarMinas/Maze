//Name: Harutyun Minasyan
// USC NetID: Hminasya
// CS 455 PA3
// Spring 2018

import java.awt.Graphics;
import javax.swing.JComponent;
import java.awt.Color;
import java.util.LinkedList;

/**
   MazeComponent class
   
   A component that displays the maze and path through it if one has been found.
*/
public class MazeComponent extends JComponent
{
  //Constants
  private static final int START_X = 10; // top left of corner of maze in frame
  private static final int START_Y = 10;
  private static final int BOX_WIDTH = 20;  // width and height of one maze "location"
  private static final int BOX_HEIGHT = 20;
  private static final int INSET = 2;  // how much smaller on each side to make entry/exit inner box 
  private static final Color WALL_COLOR = Color.BLACK; //draw colors for various objects
  private static final Color FREE_SPACE_COLOR = Color.WHITE;
  private static final Color ENTRY_COLOR = Color.YELLOW;
  private static final Color EXIT_COLOR = Color.GREEN;
  private static final Color PATH_COLOR = Color.BLUE;
  private static final Color BORDER_COLOR = Color.BLACK;
  //Variables
  private int pathSize; //The size of the path found
  private Maze maze; //The maze that contains the data 
   
  /**
    Constructs the component.
    @param maze   the maze to display
  */
  public MazeComponent(Maze maze) 
  {   
    this.maze = maze;
    pathSize = 0;
  }
  
  /**
    Draws the current state of maze including the path through it if one has been found.
    @param g the graphics context
  */
  public void paintComponent(Graphics g)
  {
	
    pathSize = maze.getPath().size(); //sets the path size to the size of the path from the maze object
    if (pathSize > 0) //check if the path is not empty
    {
      drawMaze(g);     //draws the maze 
      drawPath(maze.getPath(), g); //draws the path through the maze
    }
    else
    {
      drawMaze(g);
    }  
    
    drawBorders(g);
    
  }
   
  /*
    Draws a maze from starting from the upper left point in the screen. 
    Fills in the squares that correspond to walls in the maze with black squares and the free spaces with white squares. 
    Marks the exit location with a green rectangle and the entry location with yellow. The exit/entry location marks are smaller 
      rectangles than the regular black and white boxes.
      
    @param g - the Graphics object
   */
  private void drawMaze(Graphics g)
  {
    int x = START_X;
    int y = START_Y;
    
    for (int i = 0; i < maze.numRows(); i++)
    {
      for (int j = 0; j < maze.numCols(); j++)
      {
        if (maze.hasWallAt(new MazeCoord(i,j)))
        {
          drawRect(g, x, y, WALL_COLOR);
        }
        else
        {
          drawRect(g, x, y, FREE_SPACE_COLOR);
        }	   
        if(maze.getExitLoc().equals(new MazeCoord(i,j)))
        {
          markLocation(g, x, y, EXIT_COLOR);
        }
        if (maze.getEntryLoc().equals(new MazeCoord(i,j)))
        {
          markLocation(g, x, y, ENTRY_COLOR);
        }
        x += BOX_WIDTH;
      }
      y += BOX_HEIGHT;
      x = START_X;
    }
  }

  /*
    Draws a Rectangle of a given color at a given coordinate with the size of BOX_WIDTH and BOX_HEIGHT.
    
    @param g - The Graphics object
    @param x - The x coordinate of the squares left upper corner.
    @param x - The y coordinate of the squares left upper corner.
    @param color -The color of the rectangle to be painted.
  */
  private void drawRect(Graphics g, int x, int y, Color color) 
  {
    g.setColor(color);
    g.fillRect(x, y, BOX_WIDTH, BOX_HEIGHT);
  }

  /*
    Draws a Rectangle that is smaller than the rectangles drawn by drawRect method.
    Used for exit and entry location marks. 

    @param g - The Graphics object
    @param x - The x coordinate of the squares left upper corner.
    @param y - The y coordinate of the squares left upper corner.
    @param color - the color of the rectangle to be drawn
  */
  private void markLocation(Graphics g, int x, int y, Color color) 
  {
    g.setColor(color);
    g.fillRect(x + INSET, y + INSET, BOX_WIDTH - 2*INSET, BOX_HEIGHT - 2*INSET);
  }
   
  /*
    Draws a path between two coordinates in the maze. The path is drawn from the center of one rectangle to the center of the 
    next rectangle. The path does not go diagonally through any two rectangles. It goes through centers of all rectangles that 
    lie on the path.
    
    @param path - the path represented as a LinkedList of MazeCoords. used to draw the path line.
    @param g - the Graphics object.
  */
  private void drawPath(LinkedList<MazeCoord> path, Graphics g)
  {
    int x1, y1, x2, y2; //the coordinates of the two points between which there should be a line drawn.
    int lengthOfPathToDraw = pathSize; //The length of the path that will be decremented.
    
    while(lengthOfPathToDraw > 0) 
    {
      x1 = (path.getLast().getCol() * BOX_WIDTH) + BOX_WIDTH; 
      y1 = (path.getLast().getRow() * BOX_HEIGHT)  + BOX_HEIGHT;
      path.addFirst(path.getLast());
      path.removeLast();
      lengthOfPathToDraw --;
      if (lengthOfPathToDraw > 0) 
      {
        x2 = (path.getLast().getCol() * BOX_WIDTH) + BOX_WIDTH;
        y2 = (path.getLast().getRow() * BOX_HEIGHT) + BOX_HEIGHT;
      }
      else
      {
        x2 = x1;
        y2 = y1;
      }
      g.setColor(PATH_COLOR); //setting the color of the line.
      g.drawLine(x1, y1, x2, y2);  //drawing the line between the two points. 
    }   
  }
  
  
  private void drawBorders(Graphics g)
  {
	  int leftX = START_X;
	  int upperY = START_Y;
	  int rightX = leftX + (maze.numCols() * BOX_WIDTH);
	  int lowerY = upperY + (maze.numRows() * BOX_HEIGHT);
	  
	  
	 
	  g.setColor(BORDER_COLOR);
	  g.drawLine(leftX, upperY, rightX, upperY); //draws the top border
	  g.drawLine(leftX, upperY, leftX, lowerY);  //draws the left border
	  g.drawLine(leftX, lowerY, rightX, lowerY);  //draws the bottom border
	  g.drawLine(rightX, upperY, rightX, lowerY);  //draws


  }
  
}