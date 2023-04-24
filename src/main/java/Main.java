import game.GameConsoleClient;
import game.GameManager;
import java.util.Scanner;
import model.Board;

public class Main {

  public static void main(String[] args) {
    int moveDepth = 3;
    var gameClient =
        new GameConsoleClient(
            new GameManager(new Board(Board.randomStartingBoard())),
            new Scanner(System.in),
            moveDepth);

    // gameClient.multiplayerGameLoop();
    // gameClient.singlePlayerGameLoop();
    gameClient.AIBattleGameLoop();
  }
}
