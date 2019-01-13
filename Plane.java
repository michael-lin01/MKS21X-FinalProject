public class Plane{

  private String color;
  private int R; //variables in RBG for TerminalClass applicability ease
  private int G;
  private int B;
  private Tile tileReference; //should never be null unless the plane hasn't gotten off hangar
  private boolean isAtHome;
  private int pointValue;
  private boolean hasReachedEnd;
  private int xcor;
  private int ycor;

  public Plane(String color, int xcor, int ycor){
    this.color = color;
    this.xcor = xcor;
    this.ycor = ycor;
    if (color.equals("red")){
      R = 255;
      G = 0;
      B = 0;
    } else if (color.equals("blue")){
      R = 0;
      G = 0;
      B = 255;
    } else if (color.equals("yellow")){
      R = 255;
      G = 255;
      B = 0;
    } else if (color.equals("green")){
      R = 0;
      G = 255;
      B = 0;
    } else {
      throw new IllegalArgumentException("inputted non-valid color... only valid colors are red, green, blue, and yellow");
    }
    isAtHome = true;
    hasReachedEnd = false;
    tileReference = new Tile(-1,-1,"filler"); //is here just to avoid NullPointerException
  }

  public int R(){
    return R;
  }

  public int G(){
    return G;
  }

  public int B(){
    return B;
  }

  public int getxcor(){
    return xcor;
  }

  public int getycor(){
    return ycor;
  }

  public void setxcor(int x){
    xcor = x;
  }

  public void setycor(int y){
    ycor = y;
  }

  //returns true if plane has successfully been set at home
  //returns false if no spots available at home
  public boolean setAtHome(char[][] charArray){
    tileReference.removePlane(this);
    if (color.equals("red")){
      if (charArray[26-1][5-1] == ' '){
        xcor = 5-1;
        ycor = 26-1;
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        charArray[25][4] = 'P';
        return true;
      } else if (charArray[26-1][13-1] == ' '){
        xcor = 13-1;
        ycor = 26-1;
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        charArray[25][12] = 'P';
        return true;
      } else if (charArray[29-1][5-1] == ' '){
        xcor = 5-1;
        ycor = 29-1;
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        charArray[28][4] = 'P';
        return true;
      } else if (charArray[29-1][13-1] == ' '){
        xcor = 13-1;
        ycor = 29-1;
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        charArray[28][12] = 'P';
        return true;
      }
    } else if (color.equals("green")){
      if (charArray[25][54] == ' '){
        xcor = 54;
        ycor = 25;
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        charArray[25][54] = 'P';
        return true;
      } else if (charArray[25][62] == ' '){
        xcor = 62;
        ycor = 25;
        charArray[25][62] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[28][54] == ' '){
        xcor = 54;
        ycor = 28;
        charArray[28][54] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[28][62] == ' '){
        xcor = 62;
        ycor = 28;
        charArray[28][62] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      }
    } else if (color.equals("blue")){
      if (charArray[2][54] == ' '){
        xcor = 54;
        ycor = 2;
        charArray[2][54] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[2][62] == ' '){
        xcor = 62;
        ycor = 2;
        charArray[2][62] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[5][54] == ' '){
        xcor = 54;
        ycor = 5;
        charArray[5][54] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[5][62] == ' '){
        xcor = 62;
        ycor = 5;
        charArray[5][62] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      }
    } else if (color.equals("yellow")){
      if (charArray[2][4] == ' '){
        xcor = 4;
        ycor = 2;
        charArray[2][4] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[2][12] == ' '){
        xcor = 12;
        ycor = 2;
        charArray[2][12] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[5][4] == ' '){
        xcor = 4;
        ycor = 5;
        charArray[5][4] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      } else if (charArray[5][12] == ' '){
        xcor = 12;
        ycor = 5;
        charArray[5][12] = 'P';
        tileReference = new Tile(-1, -1, "filler");
        isAtHome = true;
        return true;
      }
    }
    return false;
  }

  public boolean isAtHome(){
    return isAtHome;
  }

  public String color(){
    return color;
  }

  public Tile getTileReference(){
    return tileReference;
  }

  public void setTileReference(Tile tile){
    tileReference = tile;
  }

  //method does two functions: moves the plane AND returns the tile that the plane has moved to
  public Tile move(Tile tile){
    if(isAtHome){ //first case is to get them out of the hangar (the only way to move
      //them out of hangar is if you select it, and you can only select planes atHome in that case)
      isAtHome = false;
      tileReference = tile;
      xcor = tileReference.getxcor();
      ycor = tileReference.getycor();
      tileReference.addPlane(this);
      return tileReference;
    } else if (!isAtHome) { //if not at home, it means you're at the launchingTile or already on the board
        tileReference.removePlane(this);
        tileReference = tile;
        xcor = tileReference.getxcor();
        ycor = tileReference.getycor();
        tileReference.addPlane(this);
        return tileReference;
    }

    return tileReference; //here just to compile?
  }

  public String toString(){
    return color;
  }
}
