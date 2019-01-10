public class Tile{

  private String color;
  private boolean planeHere;
  private int numPlanes;
  private String name;
  private Tile nextTile;
  private int xcor;
  private int ycor;


  public Tile(int xcor, int ycor, String color, String name){
    this.xcor = xcor;
    this.ycor = ycor;
    this.color = color;
    this.name = name;
    planeHere = false;
    numPlanes = 0;
  }

  public void setNextTile(Tile next){
    nextTile = next;
  }
  public void setPlaneHere(boolean b){
    planeHere = b;
  }

  public void addPlanes(int n){
    numPlanes += n;
  }

  public String getColor(){
    return color;
  }

  public boolean isPlaneHere(){
    return planeHere;
  }

  public String tileName(){
    return name;
  }

  public int getNumPlanes(){
    return numPlanes;
  }

  public int getxcor(){
    return xcor;
  }

  public int getycor(){
    return ycor;
  }

  public Tile getNextTile(){
    return nextTile;
  }

}
