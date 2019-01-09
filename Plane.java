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

  public Plane(String color){
    this.color = color;
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

  public void setAtHome(boolean bool){
    isAtHome = bool;
  }

  public boolean isAtHome(){
    return isAtHome;
  }

  public String color(){
    return color;
  }

  public void move(int numTiles){
    if(numTiles % 2 == 0 && isAtHome){ //first case is to get them out of the hangar
      isAtHome = false;
      if (color.equals("red")){
        xcor = 20 - 1; //specific coords in the grid
        ycor = 30 - 1;

      }
      if (color.equals("green")){
        xcor = 48 - 1;
        ycor = 30 - 1;

      }
      if (color.equals("blue")){
        xcor = 48 - 1;
        ycor = 2 - 1;

      }
      if (color.equals("yellow")){
        xcor = 20 - 1;
        ycor = 2 - 1;

      }
    } else if (!isAtHome) { //if not at home, it means you're at the launchingPoint or already on the board
      if (tileReference.tileName().equals("LaunchingTile")){
        if (color.equals("red")){
          xcor += 2; //moves it to the first jumping spot
          ycor -= 1; // "-1" so that the plane goes up the board, since our origin is at the topleft
        }
        if (color.equals("green")){
          xcor -= 2;
          ycor -= 1;
        }
        if (color.equals("blue")){
          xcor -= 2;
          ycor += 1;
        }
        if (color.equals("yellow")){
          xcor += 2;
          ycor += 1;
        }
      }
      else {
        tileReference = tileReference.getNextTile();
        xcor = tileReference.getxcor();
        ycor = tileReference.getycor();
      }
    }
    //not necessarily 1, but 1 tile
  }
}
