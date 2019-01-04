public class Plane{

  private String color;
  private Tile tileReference;
  private boolean isAtHome;
  private int pointValue;
  private boolean hasReachedEnd;

  public Plane(String color){
    this.color = color;
    isAtHome = true;
    hasReachedEnd = false;
  }

}
