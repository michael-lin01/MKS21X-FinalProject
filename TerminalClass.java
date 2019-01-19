
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
  private static TilePath redEndLinkedList = new TilePath();
  private static TilePath greenEndLinkedList = new TilePath();
  private static TilePath yellowEndLinkedList = new TilePath();
  private static TilePath blueEndLinkedList = new TilePath();
  
  private static TilePath Tiles = new TilePath();
  private static int shortcutChain = 0;
  private static boolean waiting;
  private static long startMillis;
  private static long endMillis;
  private static long diffMillis;
  
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
      if(colors.charAt(i)=='3') back = Terminal.Color.BLUE;
      if(colors.charAt(i)=='4') back = Terminal.Color.GREEN;
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
              for (int n = 4; n < 15; n+=2){
                blueEndLinkedList.add(new EndTile(x,n,"end",counter));
                counter++;
              }
            }
            if (y == 26){ //if the endTile is on the red branch
              int counter = 1;
              for (int n = 26; n > 15; n-=2){
                redEndLinkedList.add(new EndTile(x,n,"end",counter));
                counter++;
              }
            }
            if (x == 10){ //if the endTile is on the yellow branch
              int counter = 1;
              for (int n = 10; n < 31; n+=4){
                yellowEndLinkedList.add(new EndTile(n,y,"end",counter));
                counter++;
              }
            }
            if (x == 56){ //if the endTile is on the green branch
              int counter = 1;
              for (int n = 56; n > 35; n-=4){
                greenEndLinkedList.add(new EndTile(n,y,"end",counter));
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
    }
    
    catch (FileNotFoundException e){
      //e.printStackTrace();
      System.exit(1);
    }
  }
  
  public static void colorTiles(){
    Tile current = redStart;
    int counter = 3;
    do{
      if(counter%4==0) current.setColor("red");
      if(counter%4==1) current.setColor("yellow");
      if(counter%4==2) current.setColor("blue");
      if(counter%4==3) current.setColor("green");
      counter++;
      current=current.getNextTile();
    } while(current!=redStart);
  }
  
  //preCondition: must be rectangular array with size > 0, and charArray must fit the file text size perfectly
  public static void putFileIntoTerminal(String filename, char[][] charArray, Terminal t){
    try {
      File f = new File(filename);
      Scanner in = new Scanner(f);
      Terminal.Color back;
      Terminal.Color front;
      File colors = new File("AeroplaneChessBoardColors.txt");
      Scanner colorsin = new Scanner(colors);
      for (int y = 0; y < charArray.length; y++){
        String line = in.nextLine();
        String linecolor = colorsin.nextLine();
        for (int x = 0; x < line.length(); x++){
          charArray[y][x] = line.charAt(x); //charArray goes row,col while standard coord grid goes x,y
          t.moveCursor(x,y);
          back = Terminal.Color.DEFAULT; //if not a 1,2,3, or 4, background color is the default color
          front = Terminal.Color.DEFAULT; //if not a 5,6,7,8, foreground color is default
          if(linecolor.charAt(x)=='0') back = Terminal.Color.RED;
          if(linecolor.charAt(x)=='1') back = Terminal.Color.YELLOW;
          if(linecolor.charAt(x)=='2') back = Terminal.Color.BLUE;
          if(linecolor.charAt(x)=='3') back = Terminal.Color.GREEN;
          t.applyBackgroundColor(back);
          if (linecolor.charAt(x)=='5') front = Terminal.Color.RED;
          if (linecolor.charAt(x)=='6') front = Terminal.Color.YELLOW;
          if (linecolor.charAt(x)=='7') front = Terminal.Color.BLUE;
          if (linecolor.charAt(x)=='8') front = Terminal.Color.GREEN;
          t.applyForegroundColor(front);
          t.putCharacter(line.charAt(x));
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
    //System.out.println("number of planes on current tile: "+tile.getNumPlanes());
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
    //System.out.println("newNum: "+newNum);
    charArray[ycor][xcor] = (char)newNum;
    t.moveCursor(xcor,ycor);
    //System.out.println("number of planes on current tile: "+tile.getNumPlanes());
    if (newNum > 1){
      t.putCharacter((char)(newNum+48));
      //+48 is bc ints are -48 when converting to chars
    } else {
      t.putCharacter(' ');
    }
    t.applyForegroundColor(Terminal.Color.DEFAULT);
  }
  
  //rolls a die and displays a dieRoll on the terminal
  public static int rollDie(int numDieSides, Terminal t, String planeTurn){
    Random r = new Random();
    int dieRoll = Math.abs(r.nextInt() % numDieSides) + 1;
    putString(20, 32, t, "Roll: "+ dieRoll);
    return dieRoll;
  }
  
  //if shorthaul shortcut fails due to being busy attacking an enemy plane, shortcut chain resets to 0
  public static Tile shortHaulShortcut(Terminal t, Plane plane, char[][] charArray, ArrayList<Plane> a, String planeTurn){
    Tile tile = plane.getTileReference();
    if (tile.containsAnyInList(a)){ //if you find an enemy plane, you cannot take the shortcut
      returnToHangar(t, tile.planesHere(), charArray, planeTurn); //return enemy planes to hangar
      updateTileNumber(t, planeTurn, charArray, plane.getTileReference());
      updatePlaneLocation(t, plane, charArray);
      shortcutChain = 0;
      return tile;
    } else {
      //if plane is on corresponding end tile (this situation only arises for short haul shortcuts), do nothing
      if(plane.getxcor()==33 && plane.getycor() ==28 && plane.color().equals("red")){
        shortcutChain = 0;
        return tile;
      }
      if(plane.getxcor()==5 && plane.getycor() ==15 && plane.color().equals("yellow")){
        shortcutChain = 0;
        return tile;
      }
      if(plane.getxcor()==33 && plane.getycor() ==2 && plane.color().equals("blue")){
        shortcutChain = 0;
        return tile;
      }
      if(plane.getxcor()==62 && plane.getycor() ==15 && plane.color().equals("green")){
        shortcutChain = 0;
        return tile;
      }
      //shortcut chain represents the # of shortcuts you have taken in one turn
      //if this is your first or second shortcut (represented by shortcutChain < 2), then take the shorthaul shortcut
      if (shortcutChain < 2){
        waiting = true;
        startMillis = System.currentTimeMillis();
        while (waiting){
          endMillis = System.currentTimeMillis();
          diffMillis = endMillis - startMillis;
          if (diffMillis / 1000.0 >= 0.5){
            waiting = false;
          }
        }
        erasePlaneLocation(t, plane, charArray);
        updateTileNumber(t, planeTurn, charArray, tile, -1);
        for (int n = 0; n < 4; n++){
          plane.move(plane.getTileReference().getNextTile());
        }
        tile = plane.getTileReference();
        updatePlaneLocation(t, plane, charArray);
        updateTileNumber(t, planeTurn, charArray, tile);
        shortcutChain++;
        if (tile.containsAnyInList(a)){ //if there's an enemy at the destination of the shortcut
        returnToHangar(t, tile.planesHere(), charArray, planeTurn); //return enemy planes to hangar
        updateTileNumber(t, planeTurn, charArray, plane.getTileReference());
        updatePlaneLocation(t, plane, charArray);
        shortcutChain = 0;
      } //if there is no enemyPlane at the destination of shortcut && ur destination is a long haul shortcut, then ur able to take the long haul shortcut
      else if ((planeTurn.equals("red")&& plane.getxcor()==21 && plane.getycor()==8) //not red cuz planeTurn hasn't updated yet so we have to use the previous planeTurn...
      ||(planeTurn.equals("yellow")&& plane.getxcor()==48 && plane.getycor()==8) //not yellow cuz same reason as above
      ||(planeTurn.equals("blue")&& plane.getxcor()==45 && plane.getycor()==21) //not blue cuz same reason as above
      ||(planeTurn.equals("green")&& plane.getxcor()==18 && plane.getycor()==21)){ //not green cuz same reason as above
        longHaulShortcut(t,plane,charArray,a, planeTurn);
      }
      shortcutChain = 0;
      updatePlaneLocation(t, plane, charArray);
      updateTileNumber(t, planeTurn, charArray, plane.getTileReference());
      return plane.getTileReference();
    } else {
      shortcutChain = 0;
      return tile;
    }
  }
}

//at the end of the day, any long haul shortcuts lead to shortcutChain being reset to 0
public static Tile longHaulShortcut(Terminal t, Plane plane, char[][] charArray, ArrayList<Plane> a, String planeTurn){
  Tile tile = plane.getTileReference();
  if (tile.containsAnyInList(a)){ //if you find an enemy plane, you cannot take the shortcut
    returnToHangar(t, tile.planesHere(), charArray, planeTurn);
    updateTileNumber(t, planeTurn, charArray, plane.getTileReference());
    updatePlaneLocation(t, plane, charArray);
    //System.out.println("num planes on tile should now be 1: "+tile.getNumPlanes());
    shortcutChain = 0;
    return tile;
  } else {
    if (shortcutChain < 2){
      waiting = true;
      startMillis = System.currentTimeMillis();
      while (waiting){
        endMillis = System.currentTimeMillis();
        diffMillis = endMillis - startMillis;
        if (diffMillis / 1000.0 >= 0.5){
          waiting = false;
        }
      }
      erasePlaneLocation(t, plane, charArray);
      updateTileNumber(t, planeTurn, charArray, tile, -1);
      for (int n = 0; n < 12; n++){
        plane.move(plane.getTileReference().getNextTile());
      }
      updatePlaneLocation(t, plane, charArray);
      updateTileNumber(t, planeTurn, charArray, plane.getTileReference());
      shortcutChain++;
      shortHaulShortcut(t, plane, charArray, a, planeTurn);
      shortcutChain = 0;
      return plane.getTileReference();
    } else {
      shortcutChain = 0;
      return tile;
    }
  }
}

public static void returnToHangar(Terminal t, ArrayList<Plane> planesOnTile, char[][] charArray, String planeTurn){
  int counter = 0;
  while (planesOnTile.size() > 1){ //when there's twos+ planes
  Plane indexPlane = planesOnTile.get(counter);
  if (indexPlane.color() != planeTurn){ //save the plane that is destroying the others
    erasePlaneLocation(t, indexPlane, charArray);
    planesOnTile.get(counter).setAtHome(charArray);
    updatePlaneLocation(t, indexPlane, charArray);
    counter--;
  }
  counter++;
}
updateTileNumber(t, planeTurn, charArray, planesOnTile.get(0).getTileReference());
}

public static void instantiatePlaneLocations(Terminal terminal, ArrayList<Plane> planes, char[][] board){
  for (int n = 0; n < planes.size(); n++){
    updatePlaneLocation(terminal, planes.get(n), board);
  }
}


public static void main(String[] args) {
  
  Terminal terminal = TerminalFacade.createTerminal();//TextTerminal();
  terminal.enterPrivateMode();
  
  terminal.setCursorVisible(false);
  
  boolean running = true;
  Key key;
  boolean selecting = false;
  boolean isMessagingTime = false;
  boolean animating = false;

  startMillis = System.currentTimeMillis();
  long lastSecond = 0;
  
  
  char[][] board = new char[31][67]; //size of board
  int numDieSides = 6; //default # of sides for our die
  int x = 0; //default cursor position at (x,y)
  int y = 0;
  
  
  
  //instantiates all the planes, 1 is topleft, 2 is topright, 3 is bottomleft, 4 is bottomright
  Plane red1 = new Plane("red",5-1,26-1);
  Plane red2 = new Plane("red",13-1,26-1);
  Plane red3 = new Plane("red",5-1,29-1);
  Plane red4 = new Plane("red",13-1,29-1);
  
  Plane green1 = new Plane("green",55-1,26-1);
  Plane green2 = new Plane("green",63-1,26-1);
  Plane green3 = new Plane("green",55-1,29-1);
  Plane green4 = new Plane("green",63-1,29-1);
  
  Plane blue1 = new Plane("blue",55-1,3-1);
  Plane blue2 = new Plane("blue",63-1,3-1);
  Plane blue3 = new Plane("blue",55-1,6-1);
  Plane blue4 = new Plane("blue",63-1,6-1);
  
  Plane yellow1 = new Plane("yellow",5-1,3-1);
  Plane yellow2 = new Plane("yellow",13-1,3-1);
  Plane yellow3 = new Plane("yellow",5-1,6-1);
  Plane yellow4 = new Plane("yellow",13-1,6-1);
  
  
  String planeTurn = "red"; //default planeTurn (aka the first color plane that will move)
  Plane plane1 = red1;
  Plane plane2 = red2;
  Plane plane3 = red3;
  Plane plane4 = red4;
  Plane cursorPlane = plane1;
  ArrayList<Plane> otherPlanes = new ArrayList<Plane>();
  //could remove this as cleanup code?
  ArrayList<Plane> planes = new ArrayList<Plane>();
  planes.add(red1);
  planes.add(red2);
  planes.add(red3);
  planes.add(red4);
  planes.add(green1);
  planes.add(green2);
  planes.add(green3);
  planes.add(green4);
  planes.add(blue1);
  planes.add(blue2);
  planes.add(blue3);
  planes.add(blue4);
  planes.add(yellow1);
  planes.add(yellow2);
  planes.add(yellow3);
  planes.add(yellow4);
  

  
  while (running && args.length < 1){
    if (!isMessagingTime && !animating){ //only read key when you are not button mashing
      key = terminal.readInput();
    } else {
      key = null;
    }
    putString(15,20,terminal,"Welcome to Aeroplane Chess!");
    putString(15,23,terminal,"Press spacebar to start the game.");
    putString(15,26,terminal,"Please see the README.md for instructions and how to play this game.");
    if (key != null){
      if (key.getCharacter() == ' '){
        running = false;
      }
      if (key.getKind() == Key.Kind.Escape) {
        
        terminal.exitPrivateMode();
        System.exit(0);
      }
    }
  }
  
  //only happens after you got out of main menu
  putFileIntoTerminal("AeroplaneChessBoard.txt",board,terminal);
  mapTiles();
  colorTiles();
  Tile planeStart = redStart;
  Tile launchingTile = redLaunchingTile; //default
  instantiatePlaneLocations(terminal, planes, board); //could remove this as cleanup code?
  putString(0,32,terminal,"red's Turn!");
  running = true;
  
  while(running){
    endMillis = System.currentTimeMillis();
    diffMillis = endMillis - startMillis;
    if (!isMessagingTime && !animating){ //only read key when you are not button mashing
      key = terminal.readInput();
    } else {
      key = null;
    }
    
    if (key != null)
    {
      
      if (key.getKind() == Key.Kind.Escape) {
        
        terminal.exitPrivateMode();
        System.exit(0);
      }
      
      if (key.getCharacter() == ' '){ //this rolls a die
        //if the player is unfortunate enough to roll an odd number when none of their planes are on board yet...
        int dieRoll;
        dieRoll = 1;
        putString(20,32,terminal,"Roll: "+dieRoll);
        if (args.length > 0){
          if (args[0].equals("dieRollManipulate")){
            boolean dieMode = true;
            while (dieMode){
              if (!isMessagingTime && !animating){ //only read key when you are not button mashing
                key = terminal.readInput();
              } else {
                key = null;
              }
              if (key != null){
                //System.out.println("In deciding dice roll mode. Press Tab to increment numbers on dieRoll. Press spacebar to move the plane");
                if (key.getKind() == Key.Kind.Tab){
                  if (dieRoll == 6){
                    dieRoll = 1;
                  } else {
                    dieRoll++;
                  }
                  putString(20,32,terminal,"Roll: "+dieRoll);
                }
                if (key.getCharacter() == ' '){
                  dieMode = false;
                }
                if (key.getKind() == Key.Kind.Escape) {
                  
                  terminal.exitPrivateMode();
                  System.exit(0);
                }
              }
            }
          }
        } else {
          dieRoll = rollDie(numDieSides, terminal, planeTurn);
        }
        
        
        //if you roll an odd die ------------
        if (dieRoll % 2 == 1 &&
        (plane1.isAtHome() && plane2.isAtHome() &&
        plane3.isAtHome() && plane4.isAtHome())){
          isMessagingTime = true;
          putString(0,34,terminal,"Sorry, but you rolled an odd number!");
          startMillis = System.currentTimeMillis();
          while (isMessagingTime){
            endMillis = System.currentTimeMillis();
            diffMillis = endMillis - startMillis;
            if (diffMillis / 1000 > 2){
              isMessagingTime = false;
            }
          }
          putString(0,34,terminal,"                                    ");
          selecting = false;
        } //---------------------
        
        else { //aka any other case than being stuck in the hangar at the beginning of the game
          //defaulting stuff
          selecting = true;
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
          if (plane1.isAtHome()){ //these if cases are to set which default plane the cursor goes to when selecting
            x = plane1.getxcor();
            y = plane1.getycor();
            cursorPlane = plane1;
          } else if (plane2.isAtHome()){
            x = plane2.getxcor();
            y = plane2.getycor();
            cursorPlane = plane2;
          } else if (plane3.isAtHome()){
            x = plane3.getxcor();
            y = plane3.getycor();
            cursorPlane = plane3;
          } else if (plane4.isAtHome()){
            x = plane4.getxcor();
            y = plane4.getycor();
            cursorPlane = plane4;
          } else { //if none of them are home, default to plane1
            x = plane1.getxcor();
            y = plane1.getycor();
            cursorPlane = plane1;
          }
        }
        
        //this part occurs regardless if it was an odd or even roll (a desired feature)
        terminal.moveCursor(x,y); //default location for cursor after selecting default is top plane)
        terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
        terminal.applyForegroundColor(plane1.R(),plane1.G(),plane1.B()); //so the color of planes don't change when you are moving cursor around
        terminal.putCharacter('P'); //so that the cursor shows up by default
        terminal.moveCursor(x,y); //to reset position
      } //end defaulting ----------------------------------------------------------
      
      
      if (key.getKind() == Key.Kind.Escape){
        terminal.exitPrivateMode();
        System.exit(0);
      }

      while (selecting){
        if (!isMessagingTime && !animating){ //only read key when you are not button mashing
          key = terminal.readInput();
        } else {
          key = null;
        }
        if (key != null){
          if (key.getKind() == Key.Kind.Escape){
            terminal.exitPrivateMode();
            System.exit(0);
          }
          
          if (key.getCharacter() == ' '){ //once we have selected a plane
            //happens regardless of dieRollManipulate
            if (cursorPlane.getTileReference().getNumPlanes() < 2){ //if leaving froma tile with only 1 plane
              erasePlaneLocation(terminal, cursorPlane, board);
              cursorPlane.getTileReference().removePlane(cursorPlane);
            }
            if (cursorPlane.isAtHome()){
              updateTileNumber(terminal, planeTurn, board, cursorPlane.move(launchingTile));
              updatePlaneLocation(terminal, cursorPlane, board);
            } else {
              String colorOfPlaneOnNextTile = " ";
              for (int n = 1; n <= dieRoll; n++){
                animating = true;
                startMillis = System.currentTimeMillis();
                while (animating){
                  endMillis = System.currentTimeMillis();
                  diffMillis = endMillis - startMillis;
                  if (diffMillis / 1000.0 >= 0.5){
                    terminal.applyBackgroundColor(Terminal.Color.DEFAULT); //just to remove the highlights
                    terminal.applyForegroundColor(cursorPlane.R(),cursorPlane.G(),cursorPlane.B());
                    terminal.moveCursor(cursorPlane.getxcor(),cursorPlane.getycor());
                    terminal.putCharacter('P');
                    terminal.moveCursor(cursorPlane.getxcor(),cursorPlane.getycor());
                    if (cursorPlane.getTileReference().getNumPlanes() < 2){ //if only one plane on previous tile
                      erasePlaneLocation(terminal, cursorPlane, board);
                      cursorPlane.getTileReference().removePlane(cursorPlane);
                    } else { //if other planes on previous tile
                      if (!colorOfPlaneOnNextTile.equals(" ")){ //this serves as memory of the plane color on previous tile
                        Plane colorDummy = new Plane(colorOfPlaneOnNextTile,-1,-1);
                        terminal.applyForegroundColor(colorDummy.R(),colorDummy.G(),colorDummy.B());
                        terminal.putCharacter('P');
                        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
                      }
                    }
                    if (cursorPlane.getTileReference() == launchingTile){ //if plane is on launchingTile;
                      if (planeStart.planesHere().size() > 0){
                        colorOfPlaneOnNextTile = planeStart.planesHere().get(0).toString();
                      } else {
                        colorOfPlaneOnNextTile = " ";
                      }
                      //sets tile plane is leaving from to have numPlanes on it -1;
                      updateTileNumber(terminal, planeTurn, board, cursorPlane.getTileReference(), -1);
                      updateTileNumber(terminal, planeTurn, board, cursorPlane.move(planeStart));
                    } else { //if plane is already on the board
                      if (cursorPlane.getTileReference().getNextTile().planesHere().size() > 0){
                        colorOfPlaneOnNextTile = cursorPlane.getTileReference().getNextTile().planesHere().get(0).toString();
                      } else {
                        colorOfPlaneOnNextTile = " ";
                      }
                      //sets tile plane is leaving from to have numPlanes on it -1;
                      updateTileNumber(terminal, planeTurn, board, cursorPlane.getTileReference(), -1);
                      if(cursorPlane.getxcor()==33 && cursorPlane.getycor() ==28 && cursorPlane.color().equals("red")){
                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(redEndLinkedList.start()));
                      }
                      else if(cursorPlane.getxcor()==5 && cursorPlane.getycor() ==15 && cursorPlane.color().equals("yellow")){
                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(yellowEndLinkedList.start()));
                      }
                      else if(cursorPlane.getxcor()==33 && cursorPlane.getycor() ==2 && cursorPlane.color().equals("blue")){
                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(blueEndLinkedList.start()));
                      }
                      else if(cursorPlane.getxcor()==62 && cursorPlane.getycor() ==15 && cursorPlane.color().equals("green")){
                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(greenEndLinkedList.start()));
                      }
                      else {
                        updateTileNumber(terminal, planeTurn, board, cursorPlane.move(cursorPlane.getTileReference().getNextTile()));
                      }
                      //happens regardless of else statements
                      if (true){ //the if case here should be if the cursorPlane has reached the end of the linked lists (can use linkedlist.get(end) or use xcor == coord)
                        cursorPlane.setFinished(true);
                        //return plane to hangar with brown background color with putCharacter(' ') at an available hangar spot (if need help with the available hangar spot logic, ask Victor)
                      }
                      
                    }
                    //occurs regardless of what tile the plane is on
                    updatePlaneLocation(terminal, cursorPlane, board);
                    animating = false;
                  }
                }
              }
              //this code runs at the end of a plane's complete movement (when it has moved through all tiles indicated by its dice roll)
              
              //long haul shortcuts
              if ((planeTurn.equals("red")&& cursorPlane.getxcor()==21 && cursorPlane.getycor()==8)
              ||(planeTurn.equals("yellow")&& cursorPlane.getxcor()==48 && cursorPlane.getycor()==8)
              ||(planeTurn.equals("blue")&& cursorPlane.getxcor()==45 && cursorPlane.getycor()==21)
              ||(planeTurn.equals("green")&& cursorPlane.getxcor()==18 && cursorPlane.getycor()==21)){
                //this little method already takes care of erasePlaneLocation, updateTileNumber, and updatePlaneLocation
                longHaulShortcut(terminal, cursorPlane, board, otherPlanes, planeTurn);
              } //short haul shortcuts
              else if ((planeTurn.equals("red") && cursorPlane.getTileReference().getColor().equals("red"))
              || (planeTurn.equals("blue") && cursorPlane.getTileReference().getColor().equals("blue"))
              || (planeTurn.equals("green") && cursorPlane.getTileReference().getColor().equals("green"))
              || (planeTurn.equals("yellow") && cursorPlane.getTileReference().getColor().equals("yellow"))){
                //this little method already takes care of erasePlaneLocation, updateTileNumber, and updatePlaneLocation
                shortHaulShortcut(terminal, cursorPlane, board, otherPlanes, planeTurn);
              }
              else { //if you DON't land on a shortcut, then u gotta check if you landed on an enemy plane
              if (cursorPlane.getTileReference().containsAnyInList(otherPlanes)){
                returnToHangar(terminal, cursorPlane.getTileReference().planesHere(), board, planeTurn); //return enemy planes to hangar
                //System.out.println("num planes on tile should now be 1: "+cursorPlane.getTileReference().getNumPlanes());
              }
              updatePlaneLocation(terminal, cursorPlane, board);
              updateTileNumber(terminal, planeTurn, board, cursorPlane.getTileReference());
            }
            
          }
          
          //after the plane has finished moving
          selecting = false;
        }
        
        
        if (key.getKind() == Key.Kind.Tab){ //selecting through planes
          terminal.applyBackgroundColor(Terminal.Color.DEFAULT); //to get rid of the background from old select slot
          terminal.putCharacter('P'); 
          if (dieRoll % 2 == 0){
            if (cursorPlane == plane1){
              if (cursorPlane.getTileReference() != plane2.getTileReference()){
                x = plane2.getxcor();
                y = plane2.getycor();
                cursorPlane = plane2;
              } else {
                if (cursorPlane.getTileReference() != plane3.getTileReference()){
                  x = plane3.getxcor();
                  y = plane3.getycor();
                  cursorPlane = plane3;
                } else {
                  x = plane4.getxcor();
                  y = plane4.getycor();
                  cursorPlane = plane4;
                }
              }
            } else if (cursorPlane == plane2){
              if (cursorPlane.getTileReference() != plane3.getTileReference()){
                x = plane3.getxcor();
                y = plane3.getycor();
                cursorPlane = plane3;
              } else {
                if (cursorPlane.getTileReference() != plane4.getTileReference()){
                  x = plane4.getxcor();
                  y = plane4.getycor();
                  cursorPlane = plane4;
                } else {
                  x = plane1.getxcor();
                  y = plane1.getycor();
                  cursorPlane = plane1;
                }
              }
            } else if (cursorPlane == plane3){
              if (cursorPlane.getTileReference() != plane4.getTileReference()){
                x = plane4.getxcor();
                y = plane4.getycor();
                cursorPlane = plane4;
              } else {
                if (cursorPlane.getTileReference() != plane1.getTileReference()){
                  x = plane1.getxcor();
                  y = plane1.getycor();
                  cursorPlane = plane1;
                } else {
                  x = plane2.getxcor();
                  y = plane2.getycor();
                  cursorPlane = plane2;
                }
              }
            } else if (cursorPlane == plane4){
              if (cursorPlane.getTileReference() != plane1.getTileReference()){
                x = plane1.getxcor();
                y = plane1.getycor();
                cursorPlane = plane1;
              } else {
                if (cursorPlane.getTileReference() != plane2.getTileReference()){
                  x = plane2.getxcor();
                  y = plane2.getycor();
                  cursorPlane = plane2;
                } else {
                  x = plane3.getxcor();
                  y = plane3.getycor();
                  cursorPlane = plane3;
                }
              }
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
          terminal.moveCursor(x,y);
          terminal.applyBackgroundColor(Terminal.Color.MAGENTA);
          terminal.putCharacter('P');
          terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
          terminal.moveCursor(x,y); //moves back to position the cursor is over
        }
        
        //if want to put a new key input, put here
        
      }
    }
    
    
    //will happen regardless of what roll is achieved; aka the game moves on despite getting odd or even
    //will add the planes whose turn is over to the category of "otherPlanes"
    otherPlanes.add(plane1); otherPlanes.add(plane2); otherPlanes.add(plane3); otherPlanes.add(plane4);
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
    //the planes with same color as planeTurn are now not part of "otherPlanes"
    otherPlanes.remove(plane1); otherPlanes.remove(plane2); otherPlanes.remove(plane3); otherPlanes.remove(plane4);
    putString(0,32,terminal,"                                  ");
    putString(0,32,terminal,planeTurn + "'s Turn!");
  }
}
}
}
}
