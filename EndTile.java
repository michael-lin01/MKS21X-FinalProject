public class EndTile extends Tile{
  private int numTile;

  public EndTile(int xcor, int ycor, String color, int numTile){
    super(xcor,ycor,color);
    this.numTile = numTile;
  }

  public int getNumTile(){
    return numTile;
  }

}
