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
		return Direction.getDirectionFromInt(new Random().nextInt(4));
	}

}
