package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Direction;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.entities.tile.Tile;

public class Flame extends Entity {

	protected Board _board;
	protected Direction _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments = new FlameSegment[0];

	/**
	 *
	 * @param x hoành độ bắt đầu của Flame
	 * @param y tung độ bắt đầu của Flame
	 * @param direction là hướng của Flame
	 * @param radius độ dài cực đại của Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = Direction.getDirectionFromInt(direction);
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

	/**
	 * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán độ dài Flame, tương ứng với số lượng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()];

		/**
		 * biến last dùng để đánh dấu cho segment cuối cùng
		 */
		boolean last = false;

		// TODO: tạo các segment dưới đây
		int  x = (int) _x;
		int y = (int) _y;
		for(int i = 0; i < _flameSegments.length; ++i) {
			switch (_direction) {
				case left:
					x -= 1;
					break;
				case right:
					x += 1;
					break;
				case up:
					y -= 1;
					break;
				case down:
					y +=1;
					break;
			}
			if(i == _flameSegments.length -1) {
				last = true;
			}
			Entity e = _board.getEntityAt(x, y);

			_flameSegments[i] = new FlameSegment(x, y, Direction.getIntFromDirection(_direction), last);

			e.collide(_flameSegments[i]);

			Bomb b = _board.getBombAt(x, y);
			if(b != null) {
				b.explodeAuto();
			}

			Bomber bomber = _board.getBomber();

			if(x == bomber.getXTile() && y== bomber.getYTile()) {
				bomber.kill();
			}

		}


	}

	/**
	 * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị cắt ngắn
	 * @return
	 */
	private int calculatePermitedDistance() {
		// TODO: thực hiện tính toán độ dài của Flame
		int r = 0;
		int x = (int) _x;
		int y = (int) _y;
		while(r < _radius) {
			switch (_direction) {
				case left: --x;break;
				case right:++x; break;
				case down: ++y; break;
				case up: --y; break;
			}

			Entity e = _board.getEntityAt(x, y);
			if(!e.collide(this)) {
				break;
			}
			++r;
		}
		return r;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {}
	
	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Bomber, Enemy. Chú ý đối tượng này có vị trí chính là vị trí của Bomb đã nổ
		if(e instanceof Bomber) {
			((Bomber) e).kill();
		}
		if(e instanceof Enemy) {
			((Enemy) e).kill();
		}
		return true;
	}
}
