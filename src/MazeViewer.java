// Name: Harutyun Minasyan
// USC NetID: Hminasya
// CS 455 PA3
// Spring 2018


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import javax.swing.JFrame;
import java.util.Scanner;


/**
 * MazeViewer class
 * 
 * Program to read in and display a maze and a path through the maze. At user
 * command displays a path through the maze if there is one.
 * 
 * How to call it from the command line:
 * 
 *      java MazeViewer mazeFile
 * 
 * where mazeFile is a text file of the maze. The format is the number of rows
 * and number of columns, followed by one line per row, followed by the start location, 
 * and ending with the exit location. Each maze location is
 * either a wall (1) or free (0). Here is an example of contents of a file for
 * a 3x4 maze, with start location as the top left, and exit location as the bottom right
 * (we count locations from 0, similar to Java arrays):
 * 
 * 3 4 
 * 0111
 * 0000
 * 1110
 * 0 0
 * 2 3
 * 
 */

public class MazeViewer 
{
  private static final char WALL_CHAR = '1';
  private static final char FREE_CHAR = '0';

  /**
    The main method of the program. Gets the file name, sets the Maze Frame and checks whether the file name was provided and if the file exists.
    @param args - the file name passed into the program. 
  */
  public static void main(String[] args)  
  {
    String fileName = "";
    try 
    {
      if (args.length < 1) 
      {
        System.out.println("ERROR: missing file name command line argument");
      }
      else 
      {
        fileName = args[0];
        JFrame frame = readMazeFile(fileName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
       }
    }
    catch (FileNotFoundException exc)
    {
      System.out.println("ERROR: File not found: " + fileName);
    }
    catch (IOException exc) 
    {
      exc.printStackTrace();
    }
  }

  /**
    readMazeFile reads in maze from the file whose name is given and 
    returns a MazeFrame created from it. 
    Breaks down the file starting from the second line into strings that span each line and uses those string to read characters from them.
   
    @param fileName - the name of a file to read from (file format shown in class comments, above)
    @returns a MazeFrame containing the data from the file.      
    @throws FileNotFoundException - if there's no such file (subclass of IOException)
    @throws IOException - (hook given in case you want to do more error-checking -- that would also involve changing main to catch other exceptions)
  */
  private static MazeFrame readMazeFile(String fileName) throws IOException
  {
    Scanner in; //The scanner to read from the file
    
    try  //Tries to set the file to a one with fileName and passes the file to the scanner
    {
      File file = new File(fileName);
      in = new Scanner(file); 
    }
    catch (IOException e) 
    { 
    	  throw e;
    	}
    
    boolean[][] mazeData = new boolean[in.nextInt()][in.nextInt()]; //creates a boolean 2D array with the size read from the file
    in.nextLine(); //moves the scanner to the second line
    
    for (int i = 0; i < mazeData.length; i++)
    {
      mazeData[i] = createRows(in.nextLine());  
    }
    
    MazeCoord startLoc = new MazeCoord(in.nextInt(), in.nextInt()); //setting the startLoc of the maze read from the file(3rd/4th numbers from the end).
    MazeCoord exitLoc = new MazeCoord(in.nextInt(), in.nextInt()); //setting the exitLoc of the maze read from the file(last two numbers).
    in.close(); 
    
    return new MazeFrame(mazeData, startLoc, exitLoc);
  }
   
   
  /*
    Creates an array of booleans from a string. Takes the string and traverses over its characters and for every WALL_CHAR stores
    true in the boolean arrays corresponding to the index in the string and for every FREE_CHAR in the string stores false in the 
    boolean array.
    
    @param nextLine - the string object, the characters of which need to be translated into a boolean array.
  */
  private static boolean[] createRows(String nextLine)
  {
    char nextChar;
    boolean[] rowData = new boolean[nextLine.length()];
    
    for (int j = 0; j < nextLine.length(); j++) 
    {
      nextChar = nextLine.charAt(j);
      if (nextChar == WALL_CHAR)
      {
        rowData[j] = true;
      }
      else if(nextChar == FREE_CHAR)
      {
        rowData[j] = false;
      }
    }
    
    return rowData;
  }


}