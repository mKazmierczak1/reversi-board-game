package game;

import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameConsoleClient {

  private final GameManager gameManager;
  private final Scanner scanner;

  public void gameLoop() {
    int player = 1;

    do {
      System.out.println(gameManager.getBoardToPrint());
      System.out.printf("Player %s turn - move: ", player);
      var move = scanner.next();

      player = gameManager.nextTurn(move);
    } while (player != 0);

    var gameResult = gameManager.getResult();
    System.out.println(gameManager.getBoardToPrint());
    System.out.printf(
        "Game result: \nPlayer 1 - %s \nPlayer 2 - %s",
        gameResult.getValue0(), gameResult.getValue1());
  }
}
