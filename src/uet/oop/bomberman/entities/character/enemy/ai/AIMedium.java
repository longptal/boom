package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Direction;
import uet.oop.bomberman.entities.character.enemy.Enemy;

import java.util.Random;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	
	public AIMedium(Bomber bomber, Enemy e) {
		_bomber = bomber;
		_e = e;
	}

	@Override
	public Direction calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		Direction d = Direction.nill;

		if(_bomber == null)
			return randomDir();

		Random r = new Random();
		int rowOrColoumn = r.nextInt(2);
		if(rowOrColoumn == 0) {
			// calculate row
			 d = calculateRowDirection();
			if(d == Direction.nill) {
				d = calculateColumnDirection();
			}
		} else if(rowOrColoumn == 1) {
			d = calculateColumnDirection();
			if(d == Direction.nill) {
				d = calculateRowDirection();
			}
		}
		if(d == Direction.nill) {
			return randomDir();
		} else {
			return d;
		}



	}

	private Direction randomDir() {
		return Direction.getDirectionFromInt(new Random().nextInt(4));
	}

	private Direction calculateRowDirection() {
		if(_bomber.getXTile() < _e.getXTile()) {
			return Direction.left;
		} else if(_bomber.getXTile() > _e.getXTile()) {
			return Direction.right;
		} else {
			return Direction.nill;
		}
	}
	private Direction calculateColumnDirection() {
		if(_bomber.getYTile() < _e.getYTile()) {
			return Direction.up;
		} else if(_bomber.getYTile() > _e.getYTile()) {
			return Direction.down;
		} else {
			return Direction.nill;
		}
	}


}
