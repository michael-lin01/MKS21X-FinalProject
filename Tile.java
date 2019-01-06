public class Tile{

  private String color;
  private boolean PlaneHere;
  private boolean DangerZone;
  private boolean Shortcut;

  public Tile(String color){
    PlaneHere = false;
  }

  public void setPlaneHere(boolean b){
    PlaneHere = b;
  }

  public void setDangerZone(boolean b){
    DangerZone = b;
  }

  public void setShortcut(boolean b){
    Shortcut = b;
  }

  public String getColor(){
    return color;
  }

  public boolean isPlaneHere(){
    return PlaneHere;
  }

  public boolean isDangerZone(){
    return DangerZone;
  }

  public boolean isShortcut(){
    return Shortcut;
  }
  
}
