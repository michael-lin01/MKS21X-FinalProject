

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

    //puts text from a file into (0,0) on the terminal, where (0,0) is the topleft most point
	public static void putTextFromFile(Terminal t, String fileName, char[][] charArray){
		try{
			File f = new File(fileName);
			Scanner in = new Scanner(f);
            for (int row = 0; row < charArray.length; row++){
                String line = in.nextLine();
                for (int col = 0; col < charArray[row].length; col++){
                    char character = line.charAt(col);
                    /*if (character == 'P'){
                        t.applyForegroundColor(Terminal.Color.RED);
                    } else {
                        t.applyForegroundColor(Terminal.Color.DEFAULT);
                    }*/
                    charArray[row][col] = character;
                    t.moveCursor(col,row);
                    t.putCharacter(character);
                }
            }
    	}catch(FileNotFoundException e){
      		System.out.println("File not found: " + fileName);
      		//e.printStackTrace();
      		System.exit(1);
    	}
    }


    //preCondition: must be rectangular array with size > 0, and charArray must fit the file text size perfectly
    public static void putFileIntoTerminal(String filename, char[][] charArray, Terminal t){
        try {
            File f = new File(filename);
            Scanner in = new Scanner(f);
            while (in.hasNext()){
                for (int y = 0; y < charArray.length; y++){
                    String line = in.nextLine();
                    for (int x = 0; x < charArray[y].length; x++){
                        charArray[y][x] = line.charAt(x); //charArray goes row,col while standard coord grid goes x,y
                        t.moveCursor(x,y);
                        t.putCharacter(line.charAt(x));
                    }
                }
            }
            in.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found: " + filename);
            //e.printStackTrace();
            System.exit(1);
        }
    }

    //removes the character at the old Plane location & updates terminal
    //postCondition: resets background color of cursor character
    public static void changePlaneLocation(Terminal t, Plane plane, char[][] charArray){
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
    public static void updateLaunchingTiles(Terminal terminal, String planeTurn, char[][] charArray, int numPlanesOnLaunchingTile){
        if (planeTurn == "red"){
            charArray[30-1][19-1] = numPlanesOnLaunchingTile;
        }
        if (planeTurn == "green"){
            charArray[49-1][19-1] = numPlanesOnLaunchingTile;
        }
        if (planeTurn == "blue"){
            charArray[49-1][2-1] = numPlanesOnLaunchingTile;
        }
        if (planeTurn == "yellow"){
            charArray[30-1][2-1] = numPlanesOnLaunchingTile;
        }
    }

    //rolls a die and displays a dieRoll on the terminal
    public static int rollDie(int numDieSides, Terminal t, String planeTurn){
        long tStart = System.currentTimeMillis();
        Random r = new Random();
        int dieRoll = Math.abs(r.nextInt() % numDieSides) + 1;
        putString(20, 32, t, planeTurn + "'s Roll: "+ dieRoll);
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
        putFileIntoTerminal("AeroplaneChessBoard.txt",board,terminal);

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

        String planeTurn = "red"; //default planeTurn (aka the first color plane that will move)
        Plane plane1 = red1;
        Plane plane2 = red2;
        Plane plane3 = red3;
        Plane plane4 = red4;
        putString(0,32,terminal,"red's Turn!");
        int redPlanesOnLaunchingTile = 0;
        int greenPlanesOnLaunchingTile = 0;
        int bluePlanesOnLaunchingTile = 0;
        int yellowPlanesOnLaunchingTile = 0;
        int numPlanesOnLaunchingTile = redPlanesOnLaunchingTile;


		while(running){

			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					System.exit(0);
                }

                if (key.getKind() == Key.Kind.NormalKey){ //if the spacebar is pressed
                    int dieRoll = rollDie(numDieSides, terminal, planeTurn);
                    if (dieRoll % 2 == 0 &&
                        (plane1.isAtHome() && plane2.isAtHome() && plane3.isAtHome() && plane4.isAtHome())){
                        boolean selecting = true; //selecting runs while the player is selecting which plane to move
                        boolean isTopPlane = true; //these values are default for plane1
                        boolean isRightPlane = false;
                        if (plane1.isAtHome()){ //these if cases are to set which default plane the cursor goes to when selecting
                          x = plane1.getxcor();
                          y = plane1.getycor();
                        } else if (plane2.isAtHome()){
                          isTopPlane = true;
                          isRightPlane = true;
                          x = plane2.getxcor();
                          y = plane2.getxcor();
                        } else if (plane3.isAtHome()){
                          isTopPlane = false;
                          isRightPlane = false;
                          x = plane3.getxcor();
                          y = plane3.getycor();
                        } else if (plane4.isAtHome()){
                          isTopPlane = false;
                          isRightPlane = false;
                          x = plane4.getxcor();
                          y = plane4.getycor();
                        }
                        terminal.moveCursor(x,y); //default location for cursor after selecting default is top plane)
                        terminal.applyBackgroundColor(Terminal.Color.GREEN);
                        terminal.applyForegroundColor(plane1.R(),plane1.G(),plane1.B()); //so the color of planes don't change when you are moving cursor around
                        terminal.putCharacter('P');
                        terminal.moveCursor(x,y); //to reset position
                        while (selecting){ 
                            Key planeSelect = terminal.readInput();
                            if (planeSelect != null){
                                if (planeSelect.getKind() == Key.Kind.Escape){
                                    terminal.exitPrivateMode();
					                          System.exit(0);
                                }

                                if (planeSelect.getKind() == Key.Kind.NormalKey){ //once we have selected a plane
                                  if (x == plane1.getxcor() && y == plane1.getycor()){ //if cases used to see which plane to move
                                    changePlaneLocation(terminal, plane1, board);
                                    plane1.move(dieRoll);
                                    plane1.setIsAtHome(false);
                                    updatePlaneLocation(terminal, plane1, board);
                                  } else if (x == plane2.getxcor() && y == plane2.getycor()){
                                    changePlaneLocation(terminal, plane2, board);
                                    plane2.move(dieRoll);
                                    plane2.setIsAtHome(false);
                                    updatePlaneLocation(terminal, plane2, board);
                                  } else if (x == plane3.getxcor() && y == plane3.getycor()){
                                    changePlaneLocation(terminal, plane3, board);
                                    plane3.move(dieRoll);
                                    plane3.setIsAtHome(false);
                                    updatePlaneLocation(terminal, plane3, board);
                                  } else if (x == red4.getxcor() && y == red4.getycor()){
                                    changePlaneLocation(terminal, plane4, board);
                                    plane4.move(dieRoll);
                                    plane4.setIsAtHome(false);
                                    updatePlaneLocation(terminal, plane4, board);
                                  }
                                  selecting = false;
                                  numPlanesOnLaunchingTile++;
                                  updateLaunchingTiles(terminal, planeTurn, board, numPlanesOnLaunchingTile);
                                }

                                if (planeSelect.getKind() == Key.Kind.ArrowUp){ //if you press up
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT); //to get rid of the background from old select slot
                                    terminal.putCharacter('P');
                                    terminal.moveCursor(x,y);
                                    if (isTopPlane){ //if is a top plane
                                        if (board[y+3][x] != ' '){ //checks if there is a plane in hangar below
                                            y += 3; //moves y to bottom row of planes
                                            terminal.moveCursor(x,y);
                                            isTopPlane = false;
                                        }
                                    } else {
                                        if (board[y-3][x] != ' '){
                                            y -= 3; //moves y to top row of planes
                                            terminal.moveCursor(x,y);
                                            isTopPlane = true;
                                        }
                                    }
                                    terminal.applyBackgroundColor(Terminal.Color.GREEN); //to highlight the plane the cursor is hovering over
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y);
                                }

                                if (planeSelect.getKind() == Key.Kind.ArrowDown){ //if you press down
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.putCharacter('P');
                                    terminal.moveCursor(x,y);
                                    if (!isTopPlane){ //if is a bottom plane
                                        if (board[y-3][x] != ' '){
                                            y -= 3; //moves y to top row of planes
                                            terminal.moveCursor(x,y); //the upper plane
                                            isTopPlane = true;
                                        }
                                    } else {
                                        if (board[y+3][x] != ' '){
                                            y += 3; //moves y to bottom row of planes
                                            terminal.moveCursor(x,y);
                                            isTopPlane = false;
                                        }
                                    }
                                    terminal.applyBackgroundColor(Terminal.Color.GREEN);
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y);
                                }

                                if (planeSelect.getKind() == Key.Kind.ArrowRight){
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.putCharacter('P');
                                    terminal.moveCursor(x,y);
                                    if (isRightPlane){ //if is a right plane
                                        if (board[y][x-8] != ' '){
                                            x -= 8; //moves x to left row of planes
                                            terminal.moveCursor(x,y);
                                            isRightPlane = false;
                                        }
                                    } else {
                                        if (board[y][x+8] != ' '){
                                            x += 8; //moves x to right row of planes
                                            terminal.moveCursor(x,y);
                                            isRightPlane = true;
                                        }
                                    }
                                    terminal.applyBackgroundColor(Terminal.Color.GREEN);
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y);
                                }

                                if (planeSelect.getKind() == Key.Kind.ArrowLeft){
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.putCharacter('P');
                                    terminal.moveCursor(x,y);
                                    if (!isRightPlane){ //if is a left plane
                                        if (board[y][x+8] != ' '){
                                            x += 8; //moves x to right row of planes
                                            terminal.moveCursor(x,y);
                                            isRightPlane = true;
                                        }
                                    } else {
                                        if (board[y][x-8] != ' '){
                                            x -= 8; //moves x to left row of planes
                                            terminal.moveCursor(x,y);
                                            isRightPlane = false;
                                        }
                                    }
                                    terminal.applyBackgroundColor(Terminal.Color.GREEN);
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y);
                                }
                            }
                        }
                    }
                    if (dieRoll % 2 == 1){
                        boolean isMessagingTime = true;
                        putString(40,32,terminal,"Do this odd case later");
                        long timerStartMillis = System.currentTimeMillis();
                        while (isMessagingTime){
                            long timerEndMillis = System.currentTimeMillis();
                            long diffMillis = timerEndMillis - timerStartMillis;
                            if (diffMillis / 1000 > 2){
                                isMessagingTime = false;
                            }
                        }
                        putString(40,34,terminal,"                                  ");
                    }
                    if (planeTurn.equals("red")){ //once you have selected, then switch plane turns
                        redPlanesOnLaunchingTile = numPlanesOnLaunchingTile; //stores redPlanesOnLaunchingTile
                        numPlanesOnLaunchingTile = greenPlanesOnLaunchingTile; //switches over to greenPlanesOnLaunchingTile
                        planeTurn = "green";
                        plane1 = green1;
                        plane2 = green2;
                        plane3 = green3;
                        plane4 = green4;
                    } else if (planeTurn.equals("green")){
                        planeTurn = "blue";
                        plane1 = blue1;
                        plane2 = blue2;
                        plane3 = blue3;
                        plane4 = blue4;
                    } else if (planeTurn.equals("blue")){
                        planeTurn = "yellow";
                        plane1 = yellow1;
                        plane2 = yellow2;
                        plane3 = yellow3;
                        plane4 = yellow4;
                    } else if (planeTurn.equals("yellow")){
                        planeTurn = "red";
                        plane1 = red1;
                        plane2 = red2;
                        plane3 = red3;
                        plane4 = red4;
                    }
                    putString(0,32,terminal,"                                     ");
                    putString(0,32,terminal,planeTurn + "'s Turn!");
                }
			}

			//DO EVEN WHEN NO KEY PRESSED:
			//long tEnd = System.currentTimeMillis();
			//long millis = tEnd - tStart;
			//putString(1,2,terminal,"Milliseconds since start of program: "+millis);
			//if(millis/1000 > lastSecond){
			//	lastSecond = millis / 1000;
			//	//one second has passed.
			//	putString(1,3,terminal,"Seconds since start of program: "+lastSecond);

			//}

		}
	}
}