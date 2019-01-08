public class Tile{

  private String color;
  private boolean isPlaneHere;
  private boolean isDangerZone;
  private boolean isShortcut;
  private boolean isLaunchingTile;

  public Tile(String color){
    isPlaneHere = false; //default values
    isLaunchingTile = false;
    isDangerZone = false;
    isShortcut = false;
  }

  public String color(){
    return color;
  }
  
  public void setIsLaunchingTile(boolean bool){
    isLaunchingTile = bool;
  }
  public boolean isLaunchingTile(){
    return isLaunchingTile;
  }


  public void setIsPlaneHere(boolean bool){
    isPlaneHere = bool;
  }
  public boolean isPlaneHere(){
    return isPlaneHere;
  }


  public void setIsDangerZone(boolean bool){
    isDangerZone = bool;
  }
  public boolean isDangerZone(){
    return isDangerZone;
  }


  public void setIsShortcut(boolean bool){
    isShortcut = bool;
  }
  public boolean isShortcut(){
    return isShortcut;
  }
}
