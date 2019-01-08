

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


	public static void putTextFromFile(int r, int c, Terminal t, String fileName){
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
			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					System.exit(0);
				}

				putString(1,1,terminal,key+"        ");//to clear leftover letters pad withspaces
			}

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
