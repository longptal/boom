package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Screen;

/**
 * Bao gồm Bomber và Enemy
 */
public abstract class Character extends AnimatedEntitiy {
	
	protected Board _board;
	protected Direction _direction = Direction.nill;
	protected boolean _alive = true;
	protected boolean _moving = false;
	public int _timeAfter = 40;
	
	public Character(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
	}
	
	@Override
	public abstract void update();
	
	@Override
	public abstract void render(Screen screen);

	/**
	 * Tính toán hướng đi
	 */
	protected abstract void calculateMove();
	
	protected abstract void move(double xa, double ya);

	/**
	 * Được gọi khi đối tượng bị tiêu diệt
	 */
	public abstract void kill();

	/**
	 * Xử lý hiệu ứng bị tiêu diệt
	 */
	protected abstract void afterKill();

	/**
	 * Kiểm tra xem đối tượng có di chuyển tới vị trí đã tính toán hay không
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean canMove(double x, double y){
		double cx1 = _x + x;
		double cy1 = _y + y - Game.TILES_SIZE;
		double cx2 = cx1, cy2 = cy1;
		Entity e1, e2;
		double epsilon = _sprite.SIZE -1 ;
		switch (_direction) {
			case left:

				cy2 = cy1 + epsilon;
				break;
			case right:

				cx1 += epsilon;
				cx2 = cx1;
				cy2 +=epsilon;
				break;

			case down:
				cy1 += epsilon;
				cy2 = cy1;
				cx2 += epsilon;
				break;

			case up:
				cx2 += epsilon;
				break;
			default:

				break;
		}
		e1 = _board.getEntity(cx1 / Game.TILES_SIZE, cy1 / Game.TILES_SIZE, this);
		e2 = _board.getEntity(cx2 / Game.TILES_SIZE, cy2 / Game.TILES_SIZE, this);
		return e1.collide(this) && e2.collide(this);
	}

	protected double getXMessage() {
		return (_x * Game.SCALE) + (_sprite.SIZE / 2 * Game.SCALE);
	}
	
	protected double getYMessage() {
		return (_y* Game.SCALE) - (_sprite.SIZE / 2 * Game.SCALE);
	}
	
}
