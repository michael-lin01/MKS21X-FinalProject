public class Plane extends TerminalClass{

  private String color;
  private Tile tileReference;
  private boolean isAtHome;
  private int pointValue;
  private boolean hasReachedEnd;
  private int xcor;
  private int ycor;
  private String direction;

  public Plane(String color){
    this.color = color;
    isAtHome = true;
    hasReachedEnd = false;
    if (color == "red"){
      xcor = 5 - 1;
      ycor = 29 - 1;
    }
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
  
  private void move(int numTiles){
    if(numTiles == 0){
      isAtHome = false;
      //other stuff depending on color
    }
    //Terminal.putString();
  
    
    //not necessarily 1, but 1 tile
    if(direction.equals("N")) ycor -= 1;
    if(direction.equals("S")) ycor += 1;
    if(direction.equals("E")) xcor += 1;
    if(direction.equals("W")) xcor -= 1;
    if(direction.equals("NE")) {
      ycor -= 1;
      xcor += 1;
    }
    if(direction.equals("NW")) {
      ycor -= 1;
      xcor -= 1;
    }
    if(direction.equals("SW")) {
      ycor += 1;
      xcor -= 1;
    }
    if(direction.equals("SE")) {
      ycor += 1;
      xcor += 1;
    }
  }


}