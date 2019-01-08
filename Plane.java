public class Plane{

  private String color; //1 = red  2 = yellow  3 = green  4 = blue
  private Tile tileReference; //should never be null unless the plane hasn't gotten off hangar
  private boolean atHome;
  private int pointValue;
  private boolean hasReachedEnd;
  private int xcor;
  private int ycor;
  private String direction;

  public Plane(String color){
    this.color = color;
    atHome = true;
    hasReachedEnd = false;
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
    atHome = bool;
  }

  public boolean isAtHome(){
    return atHome;
  }

  public String color(){
    return color;
  }

  public void move(int numTiles){
    if(numTiles % 2 == 0 && atHome){ //first case is to get them out of the hangar
      atHome = false;
      if (color.equals("red")){
        xcor = 20 - 1; //specific coords in the grid
        ycor = 30 - 1;
        tileReference = new Tile("red");
        tileReference.setLaunchingTile(true);
        direction = "N";
      }
      if (color.equals("green")){
        xcor = 48 - 1;
        ycor = 30 - 1;
        tileReference = new Tile("green");
        tileReference.setLaunchingTile(true);
        direction = "W";
      }
      if (color.equals("blue")){
        xcor = 48 - 1;
        ycor = 2 - 1;
        tileReference = new Tile("blue");
        tileReference.setLaunchingTile(true);
        direction = "S";
      }
      if (color.equals("yellow")){
        xcor = 20 - 1;
        ycor = 2 - 1;
        tileReference = new Tile("yellow");
        tileReference.setLaunchingTile(true);
        direction = "E";
      }
    } else if (!atHome) { //if not at home, it means you're at the launchingPoint or already on the board
      if (tileReference.isLaunchingTile()){
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
        tileReference.setLaunchingTile(false);
      }
      else {
        tileReference = tileReference.getNextTile();
        xcor = tileReference.getxcor();
        ycor = tileReference.getycor();
        if (tileReference == redEnd && color.equals("red")){

        }
      }
    }
    //not necessarily 1, but 1 tile
  }


}
