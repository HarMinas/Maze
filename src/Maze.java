// Name: Harutyun Minasyan
// USC NetID: Hminasya
// CS 455 PA3
// Spring 2018

import java.util.LinkedList;


/**
   Maze class

   Stores information about a maze and can find a path through the maze
   (if there is one).
   
   Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
   (parameters to constructor), and the path:
     -- no outer walls given in mazeData -- search assumes there is a virtual 
        border around the maze (i.e., the maze path can't go outside of the maze
        boundaries)
     -- start location for a path is maze coordinate startLoc
     -- exit location is maze coordinate exitLoc
     -- mazeData input is a 2D array of booleans, where true means there is a wall
        at that location, and false means there isn't (see public FREE / WALL 
        constants below) 
     -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
     -- only travel in 4 compass directions (no diagonal paths)
     -- can't travel through walls

 */

public class Maze 
{
   
  public static final boolean FREE = false; // weather the location is a wall or a free space
  public static final boolean WALL = true;
  public static final boolean VISITED = true; //weather the location is visited before or not
  public static final boolean NOT_VISITED = false; 
  
  private static final MazeCoord NO_MOVES = new MazeCoord(-2, -2); //a sentinel MazeCoord that is used as a sign that there is no more moves from a location
    
  private boolean[][] mazeData; // The data of the maze
  private boolean[][] visited; //A map corresponding to mazeData that stores whether a location is visited or not priorly.
  private MazeCoord startLocation; //Entry location to the maze
  private MazeCoord exitLocation; //Exit location from the maze
  private LinkedList<MazeCoord> path; //The path object that will store the path from the entryLocation to the exitLocation as a list of MazeCoords.
  private int pathSize; //Stores the number of MazeCoord in the path list.
  
  /**
  *
    Constructs a maze.
    @param mazeData the maze to search.  See general Maze comments above for what
    goes in this array.
    @param startLoc the location in maze to start the search (not necessarily on an edge)
    @param exitLoc the "exit" location of the maze (not necessarily on an edge)
    PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
        and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length
  */
  public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) 
  {
    this.mazeData = new boolean[mazeData.length][mazeData[0].length];
    if (isInMaze(startLoc) && isInMaze(exitLoc)) //Checking if startLocation and exitLocation are in the maze
    {
      startLocation = startLoc;
      exitLocation = exitLoc;
    }
    
    visited = new boolean[mazeData.length][mazeData[0].length];  // Initializing visited array to mazeData array dimensions.
    path = new LinkedList<MazeCoord>(); // Initializing an empty path.
    pathSize = 0; //path size is 0.

    for (int i = 0; i < mazeData.length; i++)  //filling in the mazeData array and the visited array.
    {
      for (int j = 0; j < mazeData[0].length; j++)
      {
        this.mazeData[i][j] = mazeData[i][j];
        visited[i][j] = NOT_VISITED;  
      }
    }   
  }
   
   
  /**
    Returns the number of rows in the maze
    @return number of rows
  */
  public int numRows() 
  {
    int rows = mazeData.length;
    return rows;   
  }

   
  /**
    Returns the number of columns in the maze
    @return number of columns
  */   
  public int numCols() 
  {
    int columns = mazeData[0].length;
    return columns;   
  } 
 
   
  /**
    Returns true if there is a wall at this location or this position is out of the maze
    @param loc the location in maze coordinates
    @return whether there is a wall here
    PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
  */
  public boolean hasWallAt(MazeCoord loc) 
  {
    if (isInMaze(loc))
    {
      if(mazeData[loc.getRow()][loc.getCol()] == FREE)
      {
        return FREE;   
      }
    }
    return WALL;
  }
   

  /**
    Returns the entry location of this maze.
  */
  public MazeCoord getEntryLoc()
  {
    return startLocation;   
  }
   
   
  /**
    Returns the exit location of this maze.
  */
  public MazeCoord getExitLoc() 
  {
    return exitLocation;  
  }

   		
  /**
    Returns the path through the maze. First element is start location, and
    last element is exit location.  If there was not path, or if this is called
    before a call to search, returns empty list.

    @return the maze path
  */
  public LinkedList<MazeCoord> getPath() 
  {
    return path; 
  }


  /**
    Find a path from start location to the exit location (see Maze
    constructor parameters, startLoc and exitLoc) if there is one.
    Client can access the path found via getPath method.

    @return whether a path was found.
  */
  public boolean search()  
  {     
    if (!hasWallAt(startLocation) && !hasWallAt(exitLocation)) //checks for walls at exit/entry location.
    {
      if (startLocation.equals(exitLocation))  //Checks to see if the exit and entry location is the same
      { 
        path.addFirst(startLocation);
        return true;
      }
      path = findPath(startLocation); //returns the found path.
      path.addFirst(startLocation);	//adds the startLocation in the front of the path list.
      if(pathSize < 1)
      {
        return false;
      }
      return true;  
    }
    return false;
  }
   
   
  /*
    returns the coordinate to the down of the location coordinate
  */
  private MazeCoord down(MazeCoord loc)
  {
    return new MazeCoord(loc.getRow() + 1, loc.getCol());
  }
  
  /*
    returns the coordinate to the up of the location coordinate
  */
  private MazeCoord up(MazeCoord loc)
  {
    return new MazeCoord(loc.getRow() - 1, loc.getCol());
  }
   
  /*
    returns the coordinate to the left of the location coordinate
  */
  private MazeCoord left(MazeCoord loc)
  {
    return new MazeCoord(loc.getRow(), loc.getCol() - 1);
  }

  /*
    returns the coordinate to the right of the location coordinate
  */
  private MazeCoord right(MazeCoord loc)
  {  
    return new MazeCoord(loc.getRow(), loc.getCol() + 1);
  }
   
  
  /*
    Returns the next possible location to move. the order of priority for the moves is right, left, down then up.
    For each potential location it is checked to see if there is a wall at that location or whether the location has previously been visited. If both these properties are false
    than that location is returned. The new location can only be one that is adjacent to the starting location.
    Special coordinate, NO_MOVES,is returned if the 4 potential locations (right/left/up/down) are all not valid. 
 	 
    @param loc - the starting location to move from
    @return - the valid location to move to. If none exists return NO_MOVES sentinel.
  */
  private MazeCoord move(MazeCoord loc)
  {
    if ( !hasWallAt(right(loc)) && !isVisited(right(loc)))
    {
      return right(loc);
    }
    else if ( !hasWallAt(left(loc)) && !isVisited(left(loc)))
    {
      return left(loc);
    }
    else if ( !hasWallAt(down(loc)) && !isVisited(down(loc)))
    {		
      return down(loc);
    }
    else if (!hasWallAt(up(loc)) && !isVisited(up(loc)))
    {
      return up(loc);
    }
    return NO_MOVES;
  } 

  
   
  /*
    Checks to see whether there is a path through the maze or not. starts from a location an check to see if there is a legal move that is not visited (the move(loc) function).
    if there is a legal move, it appends the current location to the path list and returns the results of a recursive call from the next legal position.
    if there is no legal move (given by the sentinel NO_MOVES MazeCoord), currentLoc  is update to the previous location using the path and the last element in the path list is removed.
    
    @param currentLoc - the location to start searching from. This location is updated before each recursive call
    @return - returns a LinkedList of MazeCoord objects that represents the path found. If no path is found returns a path with no element in it. 
  */
  private LinkedList<MazeCoord> findPath(MazeCoord currentLoc)
  {
    if (currentLoc.equals(exitLocation)) // Check if the currentLoc is the exitLocation - Base Case
    {
      path.add(currentLoc);
      pathSize++;
      return path;
    }  
    if (move(currentLoc) == NO_MOVES) //Checks if the currentLoc has no moves and backtracks to the previous location.
    {
      if (pathSize > 0)
      {
        path.removeLast();
        pathSize--;
      }
      if (pathSize > 0)
      {
        currentLoc = path.getLast();
        return findPath(currentLoc);
      }
      return path;
    }	    
    currentLoc = move(currentLoc); //moves to the next valid location, marks it visited, adds it to the path and recurses
    visited[currentLoc.getRow()][currentLoc.getCol()] =VISITED; 
    path.add(currentLoc);
    pathSize++;
    return findPath(currentLoc);
  }

   
  /*
    returns true iff the location is visited before and false if the location is not visited before.
    @param loc - the location that need to be checked.
    PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
  */
  private boolean isVisited(MazeCoord loc)
  {
    if (isInMaze(loc))
    {
      if ( visited[loc.getRow()][loc.getCol()] == VISITED)
      {
        return VISITED;
      }
      return NOT_VISITED;
    }
    return VISITED; 
  }
   
   
  /*
    returns true iff the location is inside he maze boundaries.
    @Param loc - the location that one wants to check if its in the maze or not 
    PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
  */
  private boolean isInMaze(MazeCoord loc)
  {
    if (0 <= loc.getRow() && loc.getRow() < numRows() && loc.getCol() < numCols() && loc.getCol() >= 0)   
    {
      return true;
    }
    return false;
  }
  
}
//END OF CLASS




