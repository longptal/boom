package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Tile {
	protected Board _board;
	public Portal(int x, int y, Board b, Sprite sprite) {
		super(x, y, sprite);
		_board = b;
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi vào
		if(e instanceof Bomber) {
			if(_board.detectNoEnemies()) {
				if(_x == e.getXTile() && _y == e.getYTile()) {
					_board.nextLevel();
				}
				return true;
			}
		}
		return false;
	}

}
