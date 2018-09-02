
public enum Direction {
	UP, DOWN, LEFT, RIGHT;

	public boolean opposes(Direction d) {
		return (this == UP && d == DOWN) || (this == DOWN && d == UP) || (this == LEFT && d == RIGHT) || (this == RIGHT && d == LEFT);
	}
}
