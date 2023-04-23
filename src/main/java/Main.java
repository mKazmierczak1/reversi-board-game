import game.GameConsoleClient;
import game.GameManager;
import java.util.Scanner;
import model.Board;

public class Main {

  public static void main(String[] args) {
    var gameClient = new GameConsoleClient(new GameManager(new Board()), new Scanner(System.in));
    gameClient.gameLoop();
  }
}
