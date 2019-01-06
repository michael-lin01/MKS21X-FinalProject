

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
/*
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
                    }
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
    */

    //preCondition: must be rectangular array with size > 0, and charArray must fit the file text size perfectly
    public static void putFileInto2dArray(String filename, char[][] charArray){
        try {
            File f = new File(filename);
            Scanner in = new Scanner(f);
            while (in.hasNext()){
                for (int y = 0; y < charArray.length; y++){
                    String line = in.nextLine();
                    for (int x = 0; x < charArray[y].length; x++){
                        charArray[y][x] = line.charAt(x);
                    }
                }
            }
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
            if (millis/1000 > 3){
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
        putFileInto2dArray("AeroplaneChessBoard.txt",board);
        Plane red1 = new Plane("red");
        red1.setxcor(5-1);
        red1.setycor(26-1);
        updatePlaneLocation(terminal, red1, board);
        Plane red2 = new Plane("red");
        red1.setxcor(13-1);
        red1.setycor(26-1);
        updatePlaneLocation(terminal, red2, board);
        Plane red3 = new Plane("red");
        red1.setxcor(5-1);
        red1.setycor(29-1);
        updatePlaneLocation(terminal, red3, board);
        Plane red4 = new Plane("red");
        red1.setxcor(13-1);
        red1.setycor(29-1);
        updatePlaneLocation(terminal, red4, board);

        int dieRoll = rollDie(numDieSides, terminal);
        if (dieRoll % 2 == 0){
            changePlaneLocation(terminal, red1, board);
            red1.move(dieRoll);
            updatePlaneLocation(terminal, red1, board); 
        }
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
