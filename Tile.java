import java.util.ArrayList;

public class Tile{

  private String color;
  private ArrayList<Plane> planesHere;
  private String name;
  private Tile nextTile;
  private Tile prevTile;
  private int xcor;
  private int ycor;

  public Tile(int xcor, int ycor){
    this.xcor = xcor;
    this.ycor = ycor;
    planesHere = new ArrayList<Plane>();
  }
  public Tile(int xcor, int ycor, String color){
    this.xcor = xcor;
    this.ycor = ycor;
    this.color = color;
    planesHere = new ArrayList<Plane>();
  }

  public ArrayList<Plane> planesHere(){
    return planesHere;
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

  public void addPlane(Plane plane){
    planesHere.add(plane);
  }

  public boolean removePlane(Plane plane){
    return planesHere.remove(plane);
  }

  public String getColor(){
    return color;
  }
  
  public void setColor(String color){
    this.color = color;
  }

  public boolean isPlaneHere(){
    return (planesHere.size() > 0);
  }

  public boolean containsAnyInList(ArrayList<Plane> a){
    for (int n = 0; n < a.size(); n++){
      if (planesHere.contains(a.get(n))){
        return true;
      }
    }
    return false;
  }

  public String tileName(){
    return name;
  }

  public int getNumPlanes(){
    return planesHere.size();
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
