public class Tile{

  private String color;
  private boolean planeHere;
  private int numPlanes;
  private String name;
  private Tile nextTile;
  private Tile prevTile;
  private int xcor;
  private int ycor;

  public Tile(int xcor, int ycor){
    this.xcor = xcor;
    this.ycor = ycor;
    planeHere = false;
    numPlanes=0;
  }
  public Tile(int xcor, int ycor, String color){
    this.xcor = xcor;
    this.ycor = ycor;
    this.color = color;
    planeHere = false;
    numPlanes = 0;
  }

  public String name(){
    return name;
  }
  public void setNextTile(Tile next){
    nextTile = next;
  }
  public void setPrevTile(Tile prev){
    prevTile = prev;
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

  public Tile getPrevTile(){
    return prevTile;
  }

  public String toString(){
    return ""+xcor+", "+ycor;
  }
}
