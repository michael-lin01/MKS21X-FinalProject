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

  public void setAtHome(boolean bool){
    isAtHome = bool;
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
      tileReference.addPlanes(1);
      return tileReference;

    } else if (!isAtHome) { //if not at home, it means you're at the launchingTile or already on the board
        tileReference.addPlanes(-1);
        tileReference = tile;
        xcor = tileReference.getxcor();
        ycor = tileReference.getycor();
        return tileReference;
    }
    //shortcuts
    if ((color.equals("red")&&xcor=21 &&ycor=8)||(color.equals("yellow")&&xcor=48 &&ycor=8)||
         (color.equals("blue")&&xcor=45 &&ycor=21)||(color.equals("green")&&xcor=18 &&ycor=21)){
      for(int i = 0; i < 12; i++){
        move(tileReference.getNextTile());
      }
    }

    return tileReference; //here just to compile?
  }
}
