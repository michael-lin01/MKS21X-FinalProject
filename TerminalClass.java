

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
	public static void putString(int c, int r,Terminal t, String s){
		t.moveCursor(c,r);
		for(int i = 0; i < s.length();i++){
			t.putCharacter(s.charAt(i));
		}
	}

	public static void putTextFromFile(Terminal t, String fileName, char[][] charArray){
		try{
			File f = new File(fileName);
			Scanner in = new Scanner(f);
            for (int row = 0; row < charArray.length; row++){
                String line = in.nextLine();
                for (int col = 0; col < charArray[row].length; col++){
                    char character = line.charAt(col);
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

    public static void updatePlaneLocation(Plane plane, char[][] charArray, Terminal t){
        charArray[plane.getxcor()][plane.getycor()] = 'T';
        t.moveCursor(plane.getxcor(),plane.getycor());
        t.putCharacter('T');
    }

    public static int rollDie(int numDieSides, Terminal t){
        long tStart = System.currentTimeMillis();
        Random r = new Random();
        int dieRoll = Math.abs(r.nextInt() % numDieSides) + 1;
        putString(0, 34, t, "Die Roll: "+ dieRoll);
        boolean timing = true;
        while (timing){
            long tEnd = System.currentTimeMillis();
            long millis = tEnd - tStart;
            if (millis/1000 > 3){
                putString(0,34,t, "           ");
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
        putTextFromFile(terminal,"AeroplaneChessBoard.txt",board);
        Plane red1 = new Plane("red");
        updatePlaneLocation(red1, board, terminal);
        if (rollDie(numDieSides, terminal) % 2 == 0){
            red1.setxcor(20-1);
            red1.setycor(30-1);
            updatePlaneLocation(red1, board, terminal); 
        }

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
