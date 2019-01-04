public class Plane{

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
  }
  
  private void move(int numTiles){
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
