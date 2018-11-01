package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.character.Direction;

import java.util.Random;

public class AILow extends AI {

	@Override
	public Direction calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		return Direction.getDirectionFromInt(new Random().nextInt(4));
	}

}
