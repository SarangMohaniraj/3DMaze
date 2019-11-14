public class Wall extends Location{
	Location location;
	private String key;
	public static final String WALL = "WALL" ,EMPTY = "EMPTY" ,ENTER = "ENTER",EXIT = "EXIT";
	public Wall(int x, int y, String key){
		super(x,y);
		this.key = key;
	}
	public String getKey(){return key;}
  public Location getLocation(){return location;}
}