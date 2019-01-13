

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
import java.util.ArrayList;



public class TerminalClass {
  private static Tile redStart;
  private static Tile greenStart;
  private static Tile yellowStart;
  private static Tile blueStart;
  private static Tile redLaunchingTile;
  private static Tile greenLaunchingTile;
  private static Tile yellowLaunchingTile;
  private static Tile blueLaunchingTile;
  private static Tile redEnd;
  private static Tile greenEnd;
  private static Tile yellowEnd;
  private static Tile blueEnd;
  private static TilePath redEndLinkedList = new TilePath();
  private static TilePath greenEndLinkedList = new TilePath();
  private static TilePath yellowEndLinkedList = new TilePath();
  private static TilePath blueEndLinkedList = new TilePath();

  private static TilePath Tiles = new TilePath();

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
		//Terminal.Color back;
		for(int i = 0; i < s.length();i++){
      /*
			back = Terminal.Color.DEFAULT; //if not a 1,2,3, or 4, background color is the default color
			if(colors.charAt(i)=='1') back = Terminal.Color.RED;
      if(colors.charAt(i)=='2') back = Terminal.Color.YELLOW;
      if(colors.charAt(i)=='3') back = Terminal.Color.GREEN;
      if(colors.charAt(i)=='4') back = Terminal.Color.BLUE;
			t.applyBackgroundColor(back);
      */
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
				//String linecolor = colorsin.nextLine();
				putString(r,lineCounter, t, line);//, linecolor);
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
    
    public static void mapTiles(){
      try {
        File f = new File("AeroplaneChessBoard.txt");
        Scanner in = new Scanner(f);
        TilePath l = new TilePath();
        for (int y = 0; y < 31; y++){
          String line = in.nextLine();
          l.clear();
          for (int x = 0; x < line.length(); x++){
            char c = line.charAt(x);
            if (c=='r'||c=='y'||c=='b'||c=='g'){
              if(c=='r') redLaunchingTile = new Tile(x,y,"red");
              if(c=='y') yellowLaunchingTile = new Tile(x,y,"yellow");
              if(c=='b') blueLaunchingTile = new Tile(x,y,"blue");
              if(c=='g') greenLaunchingTile = new Tile(x,y,"green");
            } 
            else if (c=='E'){ //if the tile is an endTile
                if (y == 4){ //if the endTile is on the blue branch
                    int counter = 1;
                    for (int n = y; n < 13; n+=2){
                        blueEndLinkedList.add(new EndTile(x,n,"blue",counter));
                        counter++;
                    }
                }
                if (y == 26){ //if the endTile is on the red branch
                    int counter = 1;
                    for (int n = y; n > 17; n-=2){
                        redEndLinkedList.add(new EndTile(x,n,"red",counter));
                        counter++;
                    }
                }
                if (x == 10){ //if the endTile is on the yellow branch
                    int counter = 1;
                    for (int n = x; n < 28; n+=4){
                        redEndLinkedList.add(new EndTile(n,y,"yellow",counter));
                        counter++;
                    }
                }
                if (x == 56){ //if the endTile is on the green branch
                    int counter = 1;
                    for (int n = x; n > 39; n-=4){
                        redEndLinkedList.add(new EndTile(n,y,"green",counter));
                        counter++;
                    }
                }
            }
            else if(c=='T'||c=='R'||c=='Y'||c=='B'||c=='G'){
              if(c=='R') redStart = new Tile(x,y,"red");
              if(c=='Y') yellowStart = new Tile(x,y,"yellow");
              if(c=='B') blueStart = new Tile(x,y,"blue");
              if(c=='G') greenStart = new Tile(x,y,"green");

              
              if(y==2){
                if(blueStart != null && x==blueStart.getxcor()) Tiles.add(blueStart);
                else Tiles.add(x,y);  
              }
              else{
                if(y==28){
                  if(redStart != null && x==redStart.getxcor()) Tiles.add(0,redStart);
                  else Tiles.add(0,x,y);
                }
                else{
                  if(y==21){
                    if(x<34){ 
                      Tiles.add(0,x,y);
                    }
                    else{
                      if(greenStart!=null&&x==greenStart.getxcor()) l.add(0,greenStart);
                      else l.add(0,x,y);
                    }
                  }
                  else{
                    if(x<34){
                      if(yellowStart != null && x==yellowStart.getxcor()&& y==yellowStart.getycor()) l.add(yellowStart);
                      else l.add(x,y);
                    }
                    else{
                      Tiles.add(x,y);
                    }
                  }
                }
              }
            }
            if(x==line.length()-1&&y==21&&l.size()>0){

              Tiles.extend(l);
              l.clear();
            }
            if(x==34&&l.size()>0){
              Tiles.attach(l);
            }
          }
        }
        Tiles.close();
        in.close();
        /*Tile nextTile = greenStart;
        for (int n = 0; n < 10; n++){
            System.out.print("nextTile: ");
            System.out.println(nextTile);
            nextTile = nextTile.getNextTile();
        }
        System.out.println(redStart);
        System.out.println(redStart.getNextTile());
        System.out.println(yellowStart);
        System.out.println(yellowStart.getNextTile());
        System.out.println(blueStart);
        System.out.println(blueStart.getNextTile());
        System.out.println(greenStart);
        System.out.println(greenStart.getNextTile());
        */
      }
        
       catch (FileNotFoundException e){
        //e.printStackTrace();
        System.exit(1);
      }
    }

    //preCondition: must be rectangular array with size > 0, and charArray must fit the file text size perfectly
    public static void putFileIntoTerminal(String filename, char[][] charArray, Terminal t){
      try {
        File f = new File(filename);
        Scanner in = new Scanner(f);
        //TilePath l = new TilePath();
        while (in.hasNext()){
          for (int y = 0; y < charArray.length; y++){
            String line = in.nextLine();
            for (int x = 0; x < line.length(); x++){
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
    public static void updateTileNumber(Terminal t, String planeTurn, char[][] charArray, Tile tile){
        int xcor = tile.getxcor();
        int ycor = tile.getycor();
        t.applyBackgroundColor(Terminal.Color.DEFAULT);
        if (planeTurn == "red"){
            t.applyForegroundColor(Terminal.Color.RED);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "green"){
            t.applyForegroundColor(Terminal.Color.GREEN);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "blue"){
            t.applyForegroundColor(Terminal.Color.BLUE);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "yellow"){
            t.applyForegroundColor(Terminal.Color.YELLOW);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        charArray[ycor][xcor] = (char)tile.getNumPlanes();
        t.moveCursor(xcor,ycor);
        System.out.println("number of planes on current tile: "+tile.getNumPlanes());
        if (tile.getNumPlanes() > 1){
            t.putCharacter((char)(tile.getNumPlanes()+48));
            //+48 is bc ints are -48 when converting to chars
        } else {
            t.putCharacter(' ');
        }
        t.applyForegroundColor(Terminal.Color.DEFAULT);
    }

    public static void updateTileNumber(Terminal t, String planeTurn, char[][] charArray, Tile tile, int numChange){
        int xcor = tile.getxcor();
        int ycor = tile.getycor();
        t.applyBackgroundColor(Terminal.Color.DEFAULT);
        if (planeTurn == "red"){
            t.applyForegroundColor(Terminal.Color.RED);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "green"){
            t.applyForegroundColor(Terminal.Color.GREEN);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "blue"){
            t.applyForegroundColor(Terminal.Color.BLUE);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        else if (planeTurn == "yellow"){
            t.applyForegroundColor(Terminal.Color.YELLOW);
            xcor --; //REMOVE LATER, JUST FOR TESTING PURPOSES. WHEN WE HAVE A TILE FOR STORING THE DISPLAY OF NUMBERS, USE numTile as a parameter
        }
        int newNum = tile.getNumPlanes() + numChange;
        charArray[ycor][xcor] = (char)newNum;
        t.moveCursor(xcor,ycor);
        System.out.println("number of planes on current tile: "+newNum);
        if (newNum > 1){
            t.putCharacter((char)(tile.getNumPlanes()+48));
            //+48 is bc ints are -48 when converting to chars
        } else {
            t.putCharacter(' ');
        }
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

        TerminalSize terminalSize = new TerminalSize(31,67); //terminal.getTerminalSize();
		terminal.setCursorVisible(false);

		boolean running = true;

		long tStart = System.currentTimeMillis();
		long lastSecond = 0;


        char[][] board = new char[31][67]; //size of board
        int numDieSides = 6; //default # of sides for our die
        int x = 0; //default cursor position at (x,y)
        int y = 0;
        putFileIntoTerminal("AeroplaneChessBoard.txt",board,terminal);
        mapTiles();
        Tile planeStart = redStart;
        Tile launchingTile = redLaunchingTile; //default
        


        //instantiates all the planes, 1 is topleft, 2 is topright, 3 is bottomleft, 4 is bottomright
        Plane red1 = new Plane("red",5-1,26-1);
        updatePlaneLocation(terminal, red1, board);
        Plane red2 = new Plane("red",13-1,26-1);
        updatePlaneLocation(terminal, red2, board);
        Plane red3 = new Plane("red",5-1,29-1);
        updatePlaneLocation(terminal, red3, board);
        Plane red4 = new Plane("red",13-1,29-1);
        updatePlaneLocation(terminal, red4, board);

        Plane green1 = new Plane("green",55-1,26-1);
        updatePlaneLocation(terminal, green1, board);
        Plane green2 = new Plane("green",63-1,26-1);
        updatePlaneLocation(terminal, green2, board);
        Plane green3 = new Plane("green",55-1,29-1);
        updatePlaneLocation(terminal, green3, board);
        Plane green4 = new Plane("green",63-1,29-1);
        updatePlaneLocation(terminal, green4, board);

        Plane blue1 = new Plane("blue",55-1,3-1);
        updatePlaneLocation(terminal, blue1, board);
        Plane blue2 = new Plane("blue",63-1,3-1);
        updatePlaneLocation(terminal, blue2, board);
        Plane blue3 = new Plane("blue",55-1,6-1);
        updatePlaneLocation(terminal, blue3, board);
        Plane blue4 = new Plane("blue",63-1,6-1);
        updatePlaneLocation(terminal, blue4, board);

        Plane yellow1 = new Plane("yellow",5-1,3-1);
        updatePlaneLocation(terminal, yellow1, board);
        Plane yellow2 = new Plane("yellow",13-1,3-1);
        updatePlaneLocation(terminal, yellow2, board);
        Plane yellow3 = new Plane("yellow",5-1,6-1);
        updatePlaneLocation(terminal, yellow3, board);
        Plane yellow4 = new Plane("yellow",13-1,6-1);
        updatePlaneLocation(terminal, yellow4, board);


        String planeTurn = "red"; //default planeTurn (aka the first color plane that will move)
        Plane plane1 = red1;
        Plane plane2 = red2;
        Plane plane3 = red3;
        Plane plane4 = red4;
        Plane cursorPlane = plane1;
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

                boolean editorMode = false;

                if (key.getCharacter() == ' '){
                    //if the player is unfortunate enough to roll an odd number when none of their planes are on board yet...
                    int dieRoll = rollDie(numDieSides, terminal, planeTurn);

                    ArrayList<Plane> planes = new ArrayList();
                    int editorPlaneNumber = 0;
                    if (args.length > 0){
                        if (args[0].equals("editorMode")) editorMode = true;
                    }


                    if (dieRoll % 2 == 1 &&
                        (plane1.isAtHome() && plane2.isAtHome() &&
                        plane3.isAtHome() && plane4.isAtHome()) && !editorMode){
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
                        putString(40,32,terminal,"                                    ");
                        selecting = false;
                    }

                    else { //aka any other case than being stuck in the hangar at the beginning of the game
                        if (!editorMode){
                            if (dieRoll % 2 == 1){ //don't need to check if all planes are in hangar, since that was the if case before this one
                                if (!plane1.isAtHome()){ //checks which planes are out of the hangar, then moves cursor to them
                                    x = plane1.getxcor();
                                    y = plane1.getycor();
                                    cursorPlane = plane1;
                                } else if (!plane2.isAtHome()){
                                    x = plane2.getxcor();
                                    y = plane2.getycor();
                                    cursorPlane = plane2;
                                } else if (!plane3.isAtHome()){
                                    x = plane3.getxcor();
                                    y = plane3.getycor();
                                    cursorPlane = plane3;
                                } else if (!plane4.isAtHome()){
                                    x = plane4.getxcor();
                                    y = plane4.getycor();
                                    cursorPlane = plane4;
                                }
                            }
                            else{ //if you rolled an even #, use these if cases to decide default placement for cursor
                                //System.out.println("debugging");
                                if (plane1.isAtHome()){ //these if cases are to set which default plane the cursor goes to when selecting
                                    x = plane1.getxcor();
                                    y = plane1.getycor();
                                    cursorPlane = plane1;
                                    //System.out.println("plane1 if?" + "x: " + x + " y: " + y);
                                } else if (plane2.isAtHome()){
                                    x = plane2.getxcor();
                                    y = plane2.getycor();
                                    cursorPlane = plane2;
                                    //System.out.println("plane2 if?" + "x: " + x + " y: " + y);
                                } else if (plane3.isAtHome()){
                                    x = plane3.getxcor();
                                    y = plane3.getycor();
                                    cursorPlane = plane3;
                                    //System.out.println("plane3 if?" + "x: " + x + " y: " + y);
                                } else if (plane4.isAtHome()){
                                    x = plane4.getxcor();
                                    y = plane4.getycor();
                                    cursorPlane = plane4;
                                    //System.out.println("plane4 if?" + "x: " + x + " y: " + y);
                                } else { //if none of them are home, default to plane1
                                    x = plane1.getxcor();
                                    y = plane1.getycor();
                                    cursorPlane = plane1;
                                    //System.out.println("should have worked?" + "plane1: x: " + x + " y: " + y);
                                }
                            }

                            //this part occurs regardless if it was an odd or even roll (a desired feature)
                            //System.out.println("x: "+x + " y: "+y); //debugging
                            terminal.moveCursor(x,y); //default location for cursor after selecting default is top plane)
                            terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
                            terminal.applyForegroundColor(plane1.R(),plane1.G(),plane1.B()); //so the color of planes don't change when you are moving cursor around
                            terminal.putCharacter('P'); //so that the cursor shows up by default
                            terminal.moveCursor(x,y); //to reset position
                        }

                        selecting = true;
                        while (selecting){
                            key = terminal.readInput();
                            if (key != null){

                                while (editorMode){
                                    key = terminal.readInput();
                                    if (key != null){
                                        System.out.println("In placing planes mode. Press 'e' to get out");
                                        if (key.getKind() == Key.Kind.ArrowUp && y-1 >= 0){
                                            //removing background color from old cursor position
                                            terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                            terminal.putCharacter(board[y][x]);
                                            //applying background color to new cursor position
                                            terminal.applyBackgroundColor(255,102,9); //orange
                                            y--;
                                            terminal.moveCursor(x,y);
                                            terminal.putCharacter(board[y][x]);
                                            terminal.moveCursor(x,y); //to reset position
                                        }
                                        if (key.getKind() == Key.Kind.ArrowDown && y+1 <= 30){
                                            //removing background color from old cursor position
                                            terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                            terminal.putCharacter(board[y][x]);
                                            //applying background color to new cursor position
                                            terminal.applyBackgroundColor(255,102,9); //orange
                                            y++;
                                            terminal.moveCursor(x,y);
                                            terminal.putCharacter(board[y][x]);
                                            terminal.moveCursor(x,y); //to reset position
                                        }
                                        if (key.getKind() == Key.Kind.ArrowLeft && x-1 >= 0){
                                            //removing background color from old cursor position
                                            terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                            terminal.putCharacter(board[y][x]);
                                            //applying background color to new cursor position
                                            terminal.applyBackgroundColor(255,102,9); //orange
                                            x--;
                                            terminal.moveCursor(x,y);
                                            terminal.putCharacter(board[y][x]);
                                            terminal.moveCursor(x,y); //to reset position
                                        }
                                        if (key.getKind() == Key.Kind.ArrowRight && x+1 <= 67){
                                            //removing background color from old cursor position
                                            terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                            terminal.putCharacter(board[y][x]);
                                            //applying background color to new cursor position
                                            terminal.applyBackgroundColor(255,102,9); //orange
                                            x++;
                                            terminal.moveCursor(x,y);
                                            terminal.putCharacter(board[y][x]);
                                            terminal.moveCursor(x,y); //to reset position
                                        }
                                        if (key.getCharacter() == ' '){
                                            cursorPlane = new Plane(planeTurn,x,y);
                                            board[y][x] = 'P';
                                            planes.add(cursorPlane);
                                            Tile tile = Tiles.getTileAt(x,y);
                                            if (tile.getxcor() == -1 && tile.getycor() == -1){
                                                System.out.println("tile not found");
                                            }
                                            cursorPlane.setTileReference(tile);
                                            tile.setPlaneHere(true);
                                            tile.addPlanes(1);
                                            updateTileNumber(terminal, planeTurn, board, tile);
                                            editorPlaneNumber++;
                                            cursorPlane.setAtHome(false);
                                            if (planeTurn.equals("red")){
                                                terminal.applyForegroundColor(Terminal.Color.RED);
                                            } else if (planeTurn.equals("green")){
                                                terminal.applyForegroundColor(Terminal.Color.GREEN);
                                            } else if (planeTurn.equals("blue")){
                                                terminal.applyForegroundColor(Terminal.Color.BLUE);
                                            } else if (planeTurn.equals("yellow")){
                                                terminal.applyForegroundColor(Terminal.Color.YELLOW);
                                            }
                                            terminal.putCharacter('P');
                                            terminal.applyForegroundColor(Terminal.Color.DEFAULT);
                                        }
                                        if (key.getCharacter() == 'e'){
                                            editorMode = false;
                                            System.out.println("Exiting placing planes mode. Press Tab to select planes or spacebar to move planes.");
                                        }
                                    }
                                }



                                if (key.getKind() == Key.Kind.Escape){
                                    terminal.exitPrivateMode();
                                    System.exit(0);
                                }


                                if (key.getCharacter() == ' '){ //once we have selected a plane
                                    //System.out.println("planeTurn: "+planeTurn);
                                    if (args.length > 0){
                                        editorMode = true;
                                        dieRoll = 0;
                                        while (editorMode){
                                            key = terminal.readInput();
                                            if (key != null){
                                                System.out.println("In deciding dice roll mode. Press 'i' to increment numbers on dieRoll. Press 'e' to get out");
                                                if (key.getCharacter() == 'i'){
                                                    if (dieRoll == 6){
                                                        dieRoll = 1;
                                                    } else {
                                                        dieRoll++;
                                                    }
                                                    putString(20,32,terminal,"Roll: "+dieRoll);
                                                }
                                                if (key.getCharacter() == 'e'){
                                                    editorMode = false;
                                                    System.out.println("Exiting deciding dice mode. Press spacebar to place planes again.");
                                                }
                                            }
                                        }
                                    }
                                    //happens regardless of editorMode or not
                                    if (cursorPlane.getTileReference().getNumPlanes() < 2){
                                        erasePlaneLocation(terminal, cursorPlane, board);
                                        cursorPlane.getTileReference().setPlaneHere(false);
                                    }
                                    if (cursorPlane.isAtHome()){
                                      updateTileNumber(terminal, planeTurn, board, cursorPlane.move(launchingTile));
                                      updatePlaneLocation(terminal, cursorPlane, board); 
                                    } else {
                                        for (int n = 1; n <= dieRoll; n++){
                                            boolean animating = true;
                                            long milliStart = System.currentTimeMillis();
                                            while (animating){
                                                long milliEnd = System.currentTimeMillis();
                                                long milliDiff = milliEnd - milliStart;
                                                //if (milliDiff % 100 == 0) System.out.println(milliDiff);
                                                if (milliDiff / 1000.0 >= 0.5){
                                                    //just to remove the highlights
                                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                                    terminal.applyForegroundColor(cursorPlane.R(),cursorPlane.G(),cursorPlane.B());
                                                    terminal.moveCursor(cursorPlane.getxcor(),cursorPlane.getycor());
                                                    terminal.putCharacter('P');
                                                    if (cursorPlane.getTileReference().getNumPlanes() < 2){
                                                        erasePlaneLocation(terminal, cursorPlane, board);
                                                        cursorPlane.getTileReference().setPlaneHere(false);
                                                    }
                                                    if (cursorPlane.getTileReference() == launchingTile){ //if plane is on launchingTile;
                                                        //sets tile plane is leaving from to have numPlanes on it -1;
                                                        updateTileNumber(terminal, planeTurn, board, cursorPlane.getTileReference(), -1);
                                                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(planeStart));
                                                    } else { //if plane is already on the board
                                                        //sets tile plane is leaving from to have numPlanes on it -1;
                                                        updateTileNumber(terminal, planeTurn, board, cursorPlane.getTileReference(), -1);
                                                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(cursorPlane.getTileReference().getNextTile()));
                                                    } //else {
                                                        //this section will be for if the plane is on an endTile
                                                    //}
                                                    //occurs regardless of what tile the plane is on
                                                    updatePlaneLocation(terminal, cursorPlane, board);
                                                    if (cursorPlane.getTileReference().isPlaneHere()){ //if it encounters an enemy plane

                                                    }
                                                    animating = false;
                                                }
                                            }
                                        }
                                    }
                                    selecting = false;
                                }

                                editorPlaneNumber = 0;

                                if (key.getKind() == Key.Kind.Tab){ //selecting through planes
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT); //to get rid of the background from old select slot
                                    terminal.putCharacter('P');
                                    if (args.length > 0){ //if in editorMode
                                        System.out.println("In selecting planes mode. Press spacebar when you want to move planes.");
                                        cursorPlane = planes.get(editorPlaneNumber);
                                        if (editorPlaneNumber == planes.size() - 1){
                                            editorPlaneNumber = 0;
                                        } else {
                                            editorPlaneNumber++;
                                        }
                                        x = cursorPlane.getxcor();
                                        y = cursorPlane.getycor();
                                    } else {
                                        if (dieRoll % 2 == 0){
                                            if (cursorPlane == plane1){
                                                x = plane2.getxcor();
                                                y = plane2.getycor();
                                                cursorPlane = plane2;
                                            } else if (cursorPlane == plane2){
                                                x = plane3.getxcor();
                                                y = plane3.getycor();
                                                cursorPlane = plane3;
                                            } else if (cursorPlane == plane3){
                                                x = plane4.getxcor();
                                                y = plane4.getycor();
                                                cursorPlane = plane4;
                                            } else if (cursorPlane == plane4){
                                                x = plane1.getxcor();
                                                y = plane1.getycor();
                                                cursorPlane = plane1;
                                            }
                                        }
                                        if (dieRoll % 2 == 1){
                                            if (cursorPlane == plane1){
                                                if (!plane2.isAtHome()){
                                                    x = plane2.getxcor();
                                                    y = plane2.getycor();
                                                    cursorPlane = plane2;
                                                } else if (!plane3.isAtHome()){
                                                    x = plane3.getxcor();
                                                    y = plane3.getycor();
                                                    cursorPlane = plane3;
                                                } else if (!plane4.isAtHome()){
                                                    x = plane4.getxcor();
                                                    y = plane4.getycor();
                                                    cursorPlane = plane4;
                                                }
                                            } else if (cursorPlane == plane2){
                                                if (!plane3.isAtHome()){
                                                    x = plane3.getxcor();
                                                    y = plane3.getycor();
                                                    cursorPlane = plane3;
                                                } else if (!plane4.isAtHome()){
                                                    x = plane4.getxcor();
                                                    y = plane4.getycor();
                                                    cursorPlane = plane4;
                                                } else if (!plane1.isAtHome()){
                                                    x = plane1.getxcor();
                                                    y = plane1.getycor();
                                                    cursorPlane = plane4;
                                                }
                                            } else if (cursorPlane == plane3){
                                                if (!plane4.isAtHome()){
                                                    x = plane4.getxcor();
                                                    y = plane4.getycor();
                                                    cursorPlane = plane4;
                                                } else if (!plane1.isAtHome()){
                                                    x = plane1.getxcor();
                                                    y = plane1.getycor();
                                                    cursorPlane = plane1;
                                                } else if (!plane2.isAtHome()){
                                                    x = plane2.getxcor();
                                                    y = plane2.getycor();
                                                    cursorPlane = plane2;
                                                }
                                            } else if (cursorPlane == plane4){
                                                if (!plane1.isAtHome()){
                                                    x = plane1.getxcor();
                                                    y = plane1.getycor();
                                                    cursorPlane = plane1;
                                                } else if (!plane2.isAtHome()){
                                                    x = plane2.getxcor();
                                                    y = plane2.getycor();
                                                    cursorPlane = plane2;
                                                } else if (!plane3.isAtHome()){
                                                    x = plane3.getxcor();
                                                    y = plane3.getycor();
                                                    cursorPlane = plane3;
                                                }
                                            }
                                        }
                                    }

                                    //happens regardless of whether in editorMode or not
                                    terminal.moveCursor(x,y);
                                    terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
                                    terminal.putCharacter('P');
                                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
                                    terminal.moveCursor(x,y); //moves back to position the cursor is over
                                }
                            }
                        }
                    }
                }


                //will happen regardless of what roll is achieved; aka the game moves on despite getting odd or even
                if (planeTurn.equals("red")){ //once you have selected, then switch plane turns
                    launchingTile = greenLaunchingTile; //switches over to greenLaunchingTile
                    planeStart = greenStart;
                    planeTurn = "green";
                    plane1 = green1;
                    plane2 = green2;
                    plane3 = green3;
                    plane4 = green4;
                } else if (planeTurn.equals("green")){
                    launchingTile = blueLaunchingTile;
                    planeStart =  blueStart;
                    planeTurn = "blue";
                    plane1 = blue1;
                    plane2 = blue2;
                    plane3 = blue3;
                    plane4 = blue4;
                } else if (planeTurn.equals("blue")){
                    launchingTile = yellowLaunchingTile;
                    planeStart = yellowStart;
                    planeTurn = "yellow";
                    plane1 = yellow1;
                    plane2 = yellow2;
                    plane3 = yellow3;
                    plane4 = yellow4;
                } else if (planeTurn.equals("yellow")){
                    launchingTile = redLaunchingTile;
                    planeStart = redStart;
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
