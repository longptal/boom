package uet.oop.bomberman.entities.character;

import sun.util.resources.cldr.bas.CalendarData_bas_CM;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.debug.Debug;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;

import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    protected Keyboard _input;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0

        if (_input.space && Game.getBombRate() >= 1 && _timeBetweenPutBombs <= 0) {
            placeBomb(Coordinates.pixelToTile(_x), Coordinates.pixelToTile(_y - Game.TILES_SIZE));
            Game.addBombRate(-1);
            _timeBetweenPutBombs = 30;
            Debug.Log("booms number: " + Game.getBombRate());
        }
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        _board.addBomb(new Bomb(x, y, _board));
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            if (_bombs.size() == 0) {

//                if (_board.getLives() == 0)
//                    _board.endGame();
//                else
                    _board.restartLevel();
            }
        }
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
        if (_input.up) ya--;
        if (_input.down) ya++;
        if (_input.left) xa--;
        if (_input.right) xa++;

        if (xa != 0 || ya != 0) {
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
            _moving = true;
        } else {
            _moving = false;
        }
    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        if (xa > 0) _direction = Direction.right;
        if (xa < 0) _direction = Direction.left;
        if (ya > 0) _direction = Direction.down;
        if (ya < 0) _direction = Direction.up;

        if (canMove(xa, ya)) { //separate the moves for the player can slide when is colliding
            _x += xa;
            _y += ya;
        } else {
            specialMove(xa, ya);
        }
    }

    private void specialMove(double x, double y) {
        double cx1 = _x + x;
        double cy1 = _y + y - Game.TILES_SIZE;
        double cx2 = cx1, cy2 = cy1;
        double epsilon = _sprite.SIZE;
        int deltaEpsilon = 8;
        switch (_direction) {
            case left:

                cy2 = cy1 + epsilon;
                if (((int) cy1) % 16 >= (Game.TILES_SIZE - deltaEpsilon)) {
                    _y = (((int) cy1) / 16 + 2) * Game.TILES_SIZE;
                }
                if (((int) cy2) % 16 <= deltaEpsilon) {
                    _y = ((int) cy2) / 16 * Game.TILES_SIZE;
                }
                break;
            case right:

                cx1 += epsilon;
                cx2 = cx1;
                cy2 += epsilon;
                if (((int) cy1) % 16 >= (Game.TILES_SIZE - deltaEpsilon)) {
                    _y = (((int) cy1) / 16 + 2) * Game.TILES_SIZE;
                }
                if (((int) cy2) % 16 <= deltaEpsilon) {
                    _y = ((int) cy2) / 16 * Game.TILES_SIZE;
                }
                break;

            case down:
                cy1 += epsilon;
                cy2 = cy1;
                cx2 += epsilon;
                if (((int) cx1) % 16 >= (Game.TILES_SIZE - deltaEpsilon)) {
                    _x = (((int) cx1) / 16 + 1) * Game.TILES_SIZE;
                }
                if (((int) cx2) % 16 <= deltaEpsilon) {
                    _x = (((int) cx2) / 16 - 1) * Game.TILES_SIZE;
                }
                break;

            case up:
                cx2 += epsilon;
                if (((int) cx1) % 16 >= (Game.TILES_SIZE - deltaEpsilon)) {
                    _x = (((int) cx1) / 16 + 1) * Game.TILES_SIZE;
                }
                if (((int) cx2) % 16 <= deltaEpsilon) {
                    _x = (((int) cx2) / 16 - 1) * Game.TILES_SIZE;
                }
                break;
            default:

                break;
        }
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if (e instanceof FlameSegment) {
            kill();
        }
        if (e instanceof Enemy) {
            kill();
        }
        return true;
    }

    private void chooseSprite() {
        int dir = Direction.getIntFromDirection(_direction);
        switch (dir) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
