

//API : http://mabe02.github.io/lanterna/apidocs/2.1/
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.LanternaException;
import com.googlecode.lanterna.input.CharacterPattern;
import com.googlecode.lanterna.input.InputDecoder;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.KeyMappingProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class TerminalClass {
    //prints out a 2d array for debugging purposes
    public static String toString(char[][] charArray){
        String ans = "";
        for (int y = 0; y < charArray.length; y++){
            for (int x = 0; x < charArray[y].length; x++){
                ans+= charArray[y][x];
            }
            ans += "\n";
        }
        return ans;
    }

    //puts a string on the terminal at position (x,y)
	public static void putString(int x, int y,Terminal t, String s){
		t.moveCursor(x,y);
		for(int i = 0; i < s.length();i++){
			t.putCharacter(s.charAt(i));
		}
	}

	public static void putString(int r, int c,Terminal t, String s, String colors){
		t.moveCursor(r,c);
		Terminal.Color back;
		for(int i = 0; i < s.length();i++){
			back = Terminal.Color.DEFAULT; //if not a 1,2,3, or 4, background color is the default color
			if(colors.charAt(i)=='1') back = Terminal.Color.RED;
      if(colors.charAt(i)=='2') back = Terminal.Color.YELLOW;
      if(colors.charAt(i)=='3') back = Terminal.Color.GREEN;
      if(colors.charAt(i)=='4') back = Terminal.Color.BLUE;
			t.applyBackgroundColor(back);
			t.putCharacter(s.charAt(i));
		}
	}

  //puts text from a file into (r,c) on the terminal, where (r,c) is the topleft most point
	public static void putTextFromFile(int r, int c, Terminal t, String fileName, char[][] charArray){
		try{
			File f = new File(fileName);
			File colors = new File("AeroplaneChessBoardColors.txt");
			Scanner colorsin = new Scanner(colors);

			Scanner in = new Scanner(f);
			int lineCounter = c;
			while (in.hasNext()){
				String line = in.nextLine();
				String linecolor = colorsin.nextLine();
				putString(r,lineCounter, t, line, linecolor);
				for (int index = 0; index < line.length(); index++){
					char character = line.charAt(index);
					charArray[lineCounter][index] = character;
				}
				lineCounter++;
      }
    }catch(FileNotFoundException e){
      System.out.println("File not found: " + fileName);
      //e.printStackTrace();
      System.exit(1);
		}
	}

    //removes the character at the old Plane location & updates terminal
    //postCondition: resets background color of cursor character
    public static void erasePlaneLocation(Terminal t, Plane plane, char[][] charArray){
        int planeX = plane.getxcor();
        int planeY = plane.getycor();
        charArray[planeY][planeX] = ' ';
        t.moveCursor(planeX,planeY);
        t.applyBackgroundColor(Terminal.Color.DEFAULT);
        t.applyForegroundColor(Terminal.Color.DEFAULT);
        t.putCharacter(' ');
    }

    //puts the character at the new Plane location & updates terminal
    //postCondition: resets background color of the character
    public static void updatePlaneLocation(Terminal t, Plane plane, char[][] charArray){
        int planeX = plane.getxcor();
        int planeY = plane.getycor();
        charArray[planeY][planeX] = 'P';
        t.moveCursor(planeX,planeY);
        t.applyForegroundColor(plane.R(), plane.G(), plane.B());
        t.applyBackgroundColor(Terminal.Color.DEFAULT);
        t.putCharacter('P');
        t.applyForegroundColor(Terminal.Color.DEFAULT);
    }

    //puts the correct number of planes on each color's respective launching tile
    //postCondition: resets background color of the character
    public static void updateLaunchingTiles(Terminal t, String planeTurn, char[][] charArray, int numPlanesOnLaunchingTile){
        if (planeTurn == "red"){
            charArray[30-1][19-1] = (char)numPlanesOnLaunchingTile;
            t.moveCursor(19-1,30-1);
            t.applyForegroundColor(Terminal.Color.RED);
        }
        if (planeTurn == "green"){
            charArray[30-1][49-1] = (char)numPlanesOnLaunchingTile;
            t.moveCursor(49-1,30-1);
            t.applyForegroundColor(Terminal.Color.GREEN);
        }
        if (planeTurn == "blue"){
            charArray[2-1][49-1] = (char)numPlanesOnLaunchingTile;
            t.moveCursor(49-1,2-1);
            t.applyForegroundColor(Terminal.Color.BLUE);
        }
        if (planeTurn == "yellow"){
            charArray[2-1][19-1] = (char)numPlanesOnLaunchingTile;
            t.moveCursor(19-1,2-1);
            t.applyForegroundColor(Terminal.Color.YELLOW);
        }
        //-48 is bc ints are +48 when converting to chars
        t.putCharacter((char)(numPlanesOnLaunchingTile+48));
        t.applyForegroundColor(Terminal.Color.DEFAULT);
    }

    //rolls a die and displays a dieRoll on the terminal
    public static int rollDie(int numDieSides, Terminal t, String planeTurn){
        long tStart = System.currentTimeMillis();
        Random r = new Random();
        int dieRoll = Math.abs(r.nextInt() % numDieSides) + 1;
        putString(20, 32, t, "Roll: "+ dieRoll);
        return dieRoll;
	}

	public static void main(String[] args) {


		Terminal terminal = TerminalFacade.createTerminal();
		terminal.enterPrivateMode();

		TerminalSize terminalSize = terminal.getTerminalSize();
		terminal.setCursorVisible(false);

		boolean running = true;

		long tStart = System.currentTimeMillis();
		long lastSecond = 0;

        char[][] board = new char[31][67]; //size of board
        int numDieSides = 6; //default # of sides for our die
        int x = 0; //default cursor position at (x,y)
        int y = 0;
        putTextFromFile(x, y, terminal, "AeroplaneChessBoard.txt", board);

        //instantiates all the planes, 1 is topleft, 2 is topright, 3 is bottomleft, 4 is bottomright
        Plane red1 = new Plane("red");
        red1.setxcor(5-1);
        red1.setycor(26-1);
        updatePlaneLocation(terminal, red1, board);
        Plane red2 = new Plane("red");
        red2.setxcor(13-1);
        red2.setycor(26-1);
        updatePlaneLocation(terminal, red2, board);
        Plane red3 = new Plane("red");
        red3.setxcor(5-1);
        red3.setycor(29-1);
        updatePlaneLocation(terminal, red3, board);
        Plane red4 = new Plane("red");
        red4.setxcor(13-1);
        red4.setycor(29-1);
        updatePlaneLocation(terminal, red4, board);

        Plane green1 = new Plane("green");
        green1.setxcor(55-1);
        green1.setycor(26-1);
        updatePlaneLocation(terminal, green1, board);
        Plane green2 = new Plane("green");
        green2.setxcor(63-1);
        green2.setycor(26-1);
        updatePlaneLocation(terminal, green2, board);
        Plane green3 = new Plane("green");
        green3.setxcor(55-1);
        green3.setycor(29-1);
        updatePlaneLocation(terminal, green3, board);
        Plane green4 = new Plane("green");
        green4.setxcor(63-1);
        green4.setycor(29-1);
        updatePlaneLocation(terminal, green4, board);

        Plane blue1 = new Plane("blue");
        blue1.setxcor(55-1);
        blue1.setycor(3-1);
        updatePlaneLocation(terminal, blue1, board);
        Plane blue2 = new Plane("blue");
        blue2.setxcor(63-1);
        blue2.setycor(3-1);
        updatePlaneLocation(terminal, blue2, board);
        Plane blue3 = new Plane("blue");
        blue3.setxcor(55-1);
        blue3.setycor(6-1);
        updatePlaneLocation(terminal, blue3, board);
        Plane blue4 = new Plane("blue");
        blue4.setxcor(63-1);
        blue4.setycor(6-1);
        updatePlaneLocation(terminal, blue4, board);

        Plane yellow1 = new Plane("yellow");
        yellow1.setxcor(5-1);
        yellow1.setycor(3-1);
        updatePlaneLocation(terminal, yellow1, board);
        Plane yellow2 = new Plane("yellow");
        yellow2.setxcor(13-1);
        yellow2.setycor(3-1);
        updatePlaneLocation(terminal, yellow2, board);
        Plane yellow3 = new Plane("yellow");
        yellow3.setxcor(5-1);
        yellow3.setycor(6-1);
        updatePlaneLocation(terminal, yellow3, board);
        Plane yellow4 = new Plane("yellow");
        yellow4.setxcor(13-1);
        yellow4.setycor(6-1);
        updatePlaneLocation(terminal, yellow4, board);

        Tile redLaunchingTile = new Tile(20-1,30-1,"red","LaunchingTile"); //red's launchingTile's coords
        Tile greenLaunchingTile = new Tile(48-1,30-1,"green","LaunchingTile");
        Tile blueLaunchingTile = new Tile(48-1,2-1,"blue","LaunchingTile");
        Tile yellowLaunchingTile = new Tile(20-1,2-1,"yellow","LaunchingTile");
        Tile launchingTile = redLaunchingTile; //default

        String planeTurn = "red"; //default planeTurn (aka the first color plane that will move)
        Plane plane1 = red1;
        Plane plane2 = red2;
        Plane plane3 = red3;
        Plane plane4 = red4;
        putString(0,32,terminal,"red's Turn!");
        boolean selecting = true;

		while(running){

			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					System.exit(0);
                }

                if (key.getKind() == Key.Kind.NormalKey){
                    //if the player is unfortunate enough to roll an odd number when none of their planes are on board yet...
                    int dieRoll = rollDie(numDieSides, terminal, planeTurn);
                    if (dieRoll % 2 == 1 &&
                        (plane1.isAtHome() && plane2.isAtHome() &&
                        plane3.isAtHome() && plane4.isAtHome())){
                        boolean isMessagingTime = true;
                        putString(40,32,terminal,"Sorry, but you rolled an odd number!");
                        long timerStartMillis = System.currentTimeMillis();
                        while (isMessagingTime){
                            long timerEndMillis = System.currentTimeMillis();
                            long diffMillis = timerEndMillis - timerStartMillis;
                            if (diffMillis / 1000 > 2){
                                isMessagingTime = false;
                            }
                        }
                        putString(40,34,terminal,"                                    ");
                        selecting = false;
                    }

                    else { //aka any other case than being stuck in the hangar at the beginning of the game
                        if (dieRoll % 2 == 1){ //don't need to check if all planes are in hangar, since that was the if case before this one
                            if (!plane1.isAtHome()){ //checks which planes are out of the hangar, then moves cursor to them
                                x = plane1.getxcor();
                                y = plane1.getycor();
                            } else if (!plane2.isAtHome()){
                                x = plane2.getxcor();
                                y = plane2.getycor();
                            } else if (!plane3.isAtHome()){
                                x = plane3.getxcor();
                                y = plane3.getycor();
                            } else if (!plane4.isAtHome()){
                                x = plane4.getxcor();
                                y = plane4.getycor();
                            }
                        } 
                        else { //if you rolled an even #
                            if (plane1.isAtHome()){ //these if cases are to set which default plane the cursor goes to when selecting
                                x = plane1.getxcor();
                                y = plane1.getycor();
                            } else if (plane2.isAtHome()){
                                x = plane2.getxcor();
                                y = plane2.getxcor();
                            } else if (plane3.isAtHome()){
                                x = plane3.getxcor();
                                y = plane3.getycor();
                            } else if (plane4.isAtHome()){
                                x = plane4.getxcor();
                                y = plane4.getycor();
                            } else { //if none of them are home, default to plane1
                                x = plane1.getxcor();
                                y = plane1.getycor();
                            }
                        }
                        terminal.moveCursor(x,y); //default location for cursor after selecting default is top plane)
                        terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
                        terminal.applyForegroundColor(plane1.R(),plane1.G(),plane1.B()); //so the color of planes don't change when you are moving cursor around
                        terminal.putCharacter('P'); //so that the cursor shows up by default
                        terminal.moveCursor(x,y); //to reset position

                        selecting = true;
                        while (selecting){
                            key = terminal.readInput();
                            if (key != null){
                                if (key.getKind() == Key.Kind.Escape){
                                    terminal.exitPrivateMode();
                                    System.exit(0);
                                }

                                if (key.getKind() == Key.Kind.NormalKey){ //once we have selected a plane
                                    if (x == plane1.getxcor() && y == plane1.getycor()){ //if cases used to see which plane to move
                                    erasePlaneLocation(terminal, plane1, board);
                                    updateLaunchingTiles(terminal, planeTurn, board, plane1.move(dieRoll, launchingTile));
                                    updatePlaneLocation(terminal, plane1, board);
                                } else if (x == plane2.getxcor() && y == plane2.getycor()){
                                    erasePlaneLocation(terminal, plane2, board);
                                    updateLaunchingTiles(terminal, planeTurn, board, plane2.move(dieRoll, launchingTile));
                                    updatePlaneLocation(terminal, plane2, board);
                                } else if (x == plane3.getxcor() && y == plane3.getycor()){
                                    erasePlaneLocation(terminal, plane3, board);
                                    updateLaunchingTiles(terminal, planeTurn, board, plane3.move(dieRoll, launchingTile));
                                    updatePlaneLocation(terminal, plane3, board);
                                } else if (x == plane4.getxcor() && y == plane4.getycor()){
                                    erasePlaneLocation(terminal, plane4, board);
                                    updateLaunchingTiles(terminal, planeTurn, board, plane4.move(dieRoll, launchingTile));
                                    updatePlaneLocation(terminal, plane4, board);
                                }
                                    selecting = false;
                                }


                                if (key.getKind() == Key.Kind.Tab){
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT); //to get rid of the background from old select slot
                                    terminal.putCharacter('P');
                                    if (x == plane1.getxcor() && y == plane1.getycor()){
                                        if (plane2.isAtHome() && dieRoll % 2 == 0){
                                            x = plane2.getxcor();
                                            y = plane2.getycor();
                                        }
                                    } else if (x == plane2.getxcor() && y == plane2.getycor()){
                                        if (plane2.isAtHome() && dieRoll % 2 == 0){
                                            x = plane3.getxcor();
                                            y = plane3.getycor();
                                        }
                                    } else if (x == plane3.getxcor() && y == plane3.getycor()){
                                        if (plane2.isAtHome() && dieRoll % 2 == 0){
                                            x = plane4.getxcor();
                                            y = plane4.getycor();
                                        }
                                    } else if (x == plane4.getxcor() && y == plane4.getycor()){
                                        if (plane2.isAtHome() && dieRoll % 2 == 0){
                                            x = plane1.getxcor();
                                            y = plane1.getycor();
                                        }
                                    }
                                    terminal.moveCursor(x,y);
                                    terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y); //moves back to position the cursor is over
                                }
                            }
                        }
                    }


                    //will happen regardless of what roll is achieved; aka the game moves on despite getting odd or even
                    if (planeTurn.equals("red")){ //once you have selected, then switch plane turns
                        launchingTile = greenLaunchingTile; //switches over to greenLaunchingTile
                        planeTurn = "green";
                        plane1 = green1;
                        plane2 = green2;
                        plane3 = green3;
                        plane4 = green4;
                    } else if (planeTurn.equals("green")){
                        launchingTile = blueLaunchingTile;
                        planeTurn = "blue";
                        plane1 = blue1;
                        plane2 = blue2;
                        plane3 = blue3;
                        plane4 = blue4;
                    } else if (planeTurn.equals("blue")){
                        launchingTile = yellowLaunchingTile;
                        planeTurn = "yellow";
                        plane1 = yellow1;
                        plane2 = yellow2;
                        plane3 = yellow3;
                        plane4 = yellow4;
                    } else if (planeTurn.equals("yellow")){
                        launchingTile = redLaunchingTile;
                        planeTurn = "red";
                        plane1 = red1;
                        plane2 = red2;
                        plane3 = red3;
                        plane4 = red4;
                    }
                    putString(0,32,terminal,"                                  ");
                    putString(0,32,terminal,planeTurn + "'s Turn!");
                }
			}
		}
	}
}
