public class TilePath{
  private int size;
  private Tile start,end;

  public TilePath(){
    size = 0;
  }

  public int size(){
    return size;
  }

  private Tile getTile(int n){
    Tile current = start;
    for (int i = 0; i < n; i++){
      current = current.getNextTile();
    }
    return current;
  }

  public Tile start(){
    return start;
  }

  public Tile end(){
    return end;
  }

  public void add(Tile n){
    end.setNextTile(n);
    end = n;
    size++;
  }

  public boolean add(int xcor, int ycor){
    Tile n = new Tile(xcor, ycor);
    //special case if list is empty - node would be both start and end node
    if (size == 0){
      start = n;
      end = n;
    }
    else{
      //if not empty, change the end value to be the new node
      end.setNextTile(n);
      end = n;
    }
    size++;
    return true;
  }

  public void add(int index, int xcor, int ycor){
    if (size == 0) add(xcor, ycor);
    Tile t = new Tile(xcor, ycor);
    t.setNextTile(start);
    start = t;
    size++;
  }

  public void attach(TilePath l){
    l.end().setNextTile(start);
    start = l.start();
    size += l.size();
  }

  public void clear(){
    size = 0;
  }

  public String toString(){
    if (size == 0){
      return "[]";
    }
    String ans = "[";
    //transversing list
    Tile current = start;
    System.out.println(current);
    int counter = 0;
    while(current != null && counter != 50){
      System.out.println(current);
      ans += ("("+current.getxcor()+", "+current.getycor()+") ");
      current = current.getNextTile();
      counter++;
      //System.out.println(current);
    }
    return ans;
    //return ans.substring(0,ans.length()-2)+"]";
  }


}
