public class Tile{

  private String color;
  private boolean planeHere;
  private boolean dangerZone;
  private boolean shortcut;

  public Tile(String color){
    planeHere = false;
  }

  public void setPlaneHere(boolean b){
    planeHere = b;
  }

  public void setDangerZone(boolean b){
    dangerZone = b;
  }

  public void setShortcut(boolean b){
    shortcut = b;
  }

  public String getColor(){
    return color;
  }

  public boolean isPlaneHere(){
    return planeHere;
  }

  public boolean isDangerZone(){
    return dangerZone;
  }

  public boolean isShortcut(){
    return shortcut;
  }

}
