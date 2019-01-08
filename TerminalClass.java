

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

    public static void updateTerminal(Terminal t, char[][] charArray){
        for (int y = 0; y < charArray.length; y++){
            for (int x = 0; x < charArray[y].length; x++){
                t.moveCursor(x,y);
                t.putCharacter(charArray[y][x]);
            }
        }
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
    public static void putFileInto2dArray(String filename, char[][] charArray){
        try {
            File f = new File(filename);
            Scanner in = new Scanner(f);
            while (in.hasNext()){
                for (int y = 0; y < charArray.length; y++){
                    String line = in.nextLine();
                    for (int x = 0; x < charArray[y].length; x++){
                        charArray[y][x] = line.charAt(x); //charArray goes row,col while standard coord grid goes x,y
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

    public static void changePlaneLocation(Terminal t, Plane plane, char[][] charArray){
        charArray[plane.getycor()][plane.getxcor()] = ' ';
        updateTerminal(t, charArray);
    }

    public static void updatePlaneLocation(Terminal t, Plane plane, char[][] charArray){
        charArray[plane.getycor()][plane.getxcor()] = 'P';
        updateTerminal(t, charArray);
        /*charArray[plane.getxcor()][plane.getycor()] = 'P';
        t.moveCursor(plane.getxcor(),plane.getycor());
        if (plane.color().equals("red")){
            t.applyForegroundColor(Terminal.Color.RED);
        }
        if (plane.color().equals("green")){
            t.applyForegroundColor(Terminal.Color.GREEN);
        }
        if (plane.color().equals("blue")){
            t.applyForegroundColor(Terminal.Color.BLUE);
        }
        if (plane.color().equals("yellow")){
            t.applyForegroundColor(Terminal.Color.YELLOW);
        }
        t.putCharacter('P');
        t.applyForegroundColor(Terminal.Color.DEFAULT);*/
    }

    //rolls a die and displays a dieRoll on the terminal
    public static int rollDie(int numDieSides, Terminal t){
        long tStart = System.currentTimeMillis();
        Random r = new Random();
        int dieRoll = Math.abs(r.nextInt() % numDieSides) + 1;
        putString(0, 35, t, "Die Roll: "+ dieRoll);
        boolean timing = true;
        while (timing){
            long tEnd = System.currentTimeMillis();
            long millis = tEnd - tStart;
            if (millis/1000 > 1){
                putString(0,35,t, "           ");
                timing = false;
            }
        }
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

        char[][] board = new char[31][67];
        int numDieSides = 6;
        int x = 0;
        int y = 0;
        putFileInto2dArray("AeroplaneChessBoard.txt",board);
        updateTerminal(terminal, board);
        Plane red1 = new Plane("red");
        red1.setxcor(5-1);
        red1.setycor(26-1);
        updatePlaneLocation(terminal, red1, board);
        Plane red2 = new Plane("red");
        red2.setxcor(13-1);
        red2.setycor(26-1);
        updatePlaneLocation(terminal, red2, board);
        System.out.println(toString(board));
        Plane red3 = new Plane("red");
        red3.setxcor(5-1);
        red3.setycor(29-1);
        updatePlaneLocation(terminal, red3, board);
        Plane red4 = new Plane("red");
        red4.setxcor(13-1);
        red4.setycor(29-1);
        updatePlaneLocation(terminal, red4, board);

        /*int dieRoll = rollDie(numDieSides, terminal);
        if (dieRoll % 2 == 0){
            changePlaneLocation(terminal, red1, board);
            red1.move(dieRoll);
            updatePlaneLocation(terminal, red1, board); 
        }
        */
        //System.out.println(toString(board));

		while(running){

			//terminal.moveCursor(x,y);
			//terminal.applyBackgroundColor(Terminal.Color.YELLOW);
			//terminal.applyForegroundColor(Terminal.Color.RED);
			//terminal.putCharacter('@');
			//terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
			//terminal.applyForegroundColor(Terminal.Color.DEFAULT);



			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					System.exit(0);
                }
                
                String planeTurn = "red"; //default planeTurn

                if (key.getKind() == Key.Kind.NormalKey){ //the spacebar
                    int dieRoll = rollDie(numDieSides, terminal);
                    if (dieRoll % 2 == 0 &&
                        (red1.isAtHome() || red2.isAtHome() || red3.isAtHome() || red4.isAtHome())){
                        boolean selecting = true;
                        boolean isTopPlane = true; //we start with these values bc our default is topleft plane
                        boolean isRightPlane = false;
                        x = red1.getxcor();
                        y = red1.getycor();
                        terminal.moveCursor(x,y); //default location for cursor after selecting default is top plane)
                        terminal.applyBackgroundColor(Terminal.Color.GREEN);
                        terminal.putCharacter('P');
                        terminal.moveCursor(x,y); //to reset position
                        while (selecting){ //all this code is only accounting if all 4 planes are in the hangar...
                            Key planeSelect = terminal.readInput();
                            if (planeSelect != null){
                                if (planeSelect.getKind() == Key.Kind.Escape){
                                    terminal.exitPrivateMode();
					                System.exit(0);
                                }
                                if (planeSelect.getKind() == Key.Kind.NormalKey){
                                    red1.move(dieRoll); //filler code, in reality we would have the plane selected move
                                    red1.setIsAtHome(false);
                                    selecting = false;
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
                                    //System.out.println(isTopPlane);
                                    terminal.applyBackgroundColor(Terminal.Color.GREEN);
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
                }

				 //if (key.getKind() == Key.Kind.Space) {
				 //	int dieRoll = rollDie(6);
				// 	
				// }

				// if (key.getKind() == Key.Kind.ArrowRight) {
				// 	terminal.moveCursor(x,y);
				// 	terminal.putCharacter(' ');
				// 	x++;
				// }

				// if (key.getKind() == Key.Kind.ArrowUp) {
				// 	terminal.moveCursor(x,y);
				// 	terminal.putCharacter(' ');
				// 	y--;
				// }

				// if (key.getKind() == Key.Kind.ArrowDown) {
				// 	terminal.moveCursor(x,y);
				// 	terminal.putCharacter(' ');
				// 	y++;
				// }
				putString(1,1,terminal,key+"        ");//to clear leftover letters pad withspaces
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
