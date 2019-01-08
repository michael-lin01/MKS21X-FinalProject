

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

public class TerminalClass {

	public static void putString(int r, int c,Terminal t, String s){
		t.moveCursor(r,c);
		for(int i = 0; i < s.length();i++){
			t.putCharacter(s.charAt(i));
		}
	}

	public static void putTextFromFile(int r, int c, Terminal t, String fileName){
		try{
			File f = new File(fileName);
			Scanner in = new Scanner(f);
			int lineCounter = c;
			while (in.hasNext()){
				String line = in.nextLine();
				putString(r,lineCounter, t, line);
				lineCounter++;
			}
    	}catch(FileNotFoundException e){
      		System.out.println("File not found: " + fileName);
      		//e.printStackTrace();
      		System.exit(1);
    	}
	}


	public static void main(String[] args) {


		int x = 10;
		int y = 10;

		Terminal terminal = TerminalFacade.createTerminal();
		terminal.enterPrivateMode();

		TerminalSize terminalSize = terminal.getTerminalSize();
		terminal.setCursorVisible(false);

		boolean running = true;

		long tStart = System.currentTimeMillis();
		long lastSecond = 0;

		while(running){

			terminal.moveCursor(x,y);
			terminal.applyBackgroundColor(Terminal.Color.YELLOW);
			terminal.applyForegroundColor(Terminal.Color.RED);
			terminal.putCharacter('@');
			terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
			terminal.applyForegroundColor(Terminal.Color.DEFAULT);



			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					System.exit(0);
				}

				if (key.getKind() == Key.Kind.Spacebar) {
					System.out.println("1");
				}


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

			putTextFromFile(2,1,terminal, "AeroplaneChessBoard.txt");
/*
			putString(1,1,terminal,"                       +---+---+---+---+---+");
			putString(1,2,terminal,"+-------+-------+    / |   |   |   |   |   | \\    +-------+-------+");
			putString(1,3,terminal,"|   P   |   P   |  /   |   |   |   |   |   |   \\  |   P   |   P   |");
			putString(1,4,terminal,"|       |       |/-----+---+---+---+---+---+-----\\|       |       |");
			putString(1,5,terminal,"+-------+-------+      |       |   |       |      +-------+-------+");
			putString(1,6,terminal,"|   P   |   P   |------+       +---+       +------|   P   |   P   |");
			putString(1,7,terminal,"|       |       |      |       |   |       |      |       |       |");
			putString(1,8,terminal,"+-------+---+---+------+       +---+       +------+---+---+-------+");
			putString(1,9,terminal,"      / |   |   | \\    |= = = =|= =|= = = =|    / |   |   | \\");
			putString(1,10,terminal,"    /   |   |   |   \\  |       +---+       |  /   |   |   |   \\");
			putString(1,11,terminal," +/-----+---+---+-----\\+       |   |       +/-----+---+---+-----\\+");
			putString(1,12,terminal," |      |         =            +---+            =         |      |");
			putString(1,13,terminal," +------+         =            |   |            =         +------+");
			putString(1,14,terminal," |      |         =         +\\-------/+         =         |      |");
			putString(1,15,terminal," +------+---+---+---+---+---| \\ ___ / |---+---+---+---+---+------+");
			putString(1,16,terminal," |      |   |   | = |   |   |  |___|  |   |   | = |   |   |      |");
			putString(1,17,terminal," +------+---+---+---+---+---| /     \\ |---+---+---+---+---+------+");
			putString(1,18,terminal," |      |         =         +/-------\\+         =         |      |");
			putString(1,19,terminal," +------+         =            |   |            =         +------+");
			putString(1,20,terminal," |      |         =            +---+            =         |      |");
			putString(1,21,terminal," +\\-----+---+---+-----/+       |   |       +\\-----+---+---+-----/+");
			putString(1,22,terminal,"    \\   |   |   |   /  |       +---+       |  \\   |   |   |   /");
			putString(1,23,terminal,"      \\ |   |   | /    |= = = =|= =|= = = =|    \\ |   |   | /");
			putString(1,24,terminal,"+-------+---+---+------+       +---+       +------+---+---+------+");
			putString(1,25,terminal,"|       |       |      |       |   |       |      |       |      |");
			putString(1,26,terminal,"|   P   |   P   |------+       +---+       +------|   P   |   P  |");
			putString(1,27,terminal,"+-------+-------+      |       |   |       |      +-------+------+");
			putString(1,28,terminal,"|       |       |\\-----+---+---+---+---+---+-----/|       |      |");
			putString(1,29,terminal,"|   P   |   P   |  \\   |   |   |   |   |   |   /  |   P   |   P  |");
			putString(1,30,terminal,"+-------+-------+    \\ |   |   |   |   |   | /    +-------+------+");
			putString(1,31,terminal,"                       +---+---+---+---+---+");
*/


		}
	}
}
