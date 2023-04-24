package game;

import ai.AIOpponent;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;

@RequiredArgsConstructor
public class GameConsoleClient {

  private final GameManager gameManager;
  private final Scanner scanner;
  private final int AIMoveDepth;

  public void multiplayerGameLoop() {
    int player = 1;

    do {
      System.out.println(gameManager.getBoardToPrint());
      System.out.printf("Player %s turn - move: ", player);
      var move = scanner.next();

      player = gameManager.nextTurn(move);
    } while (player != 0);

    printResult(gameManager);
  }

  public void singlePlayerGameLoop() {
    var ai = new AIOpponent(2, 1);
    int player = 1;

    do {
      System.out.println(gameManager.getBoardToPrint());
      System.out.printf("Player %s turn - move: ", player);

      if (player == 1) {
        var move = scanner.next();
        player = gameManager.nextTurn(move);
      } else {
        var move = ai.makeMove(gameManager.getBoard());
        System.out.println(move);
        player = gameManager.nextTurn(move);
      }
    } while (player != 0);

    printResult(gameManager);
  }

  public void AIBattleGameLoop() {
    var aiBlack = new AIOpponent(1, AIMoveDepth);
    var aiWhite = new AIOpponent(2, AIMoveDepth);
    int player = 1;

    do {
      Pair<Integer, Integer> move;
      System.out.println(gameManager.getBoardToPrint());
      System.out.printf("Player %s turn - move: ", player);

      if (player == 1) {
        move = aiBlack.makeMove(gameManager.getBoard());
      } else {
        move = aiWhite.makeMove(gameManager.getBoard());
      }

      if (move != null) {
        System.out.println(move);
        player = gameManager.nextTurn(move);
      } else {
        System.out.println("No moves available! Switching players.");
        player = switchPlayer(player);
      }
    } while (player != 0);

    printResult(gameManager);
  }

  private void printResult(GameManager gameManager) {
    var gameResult = gameManager.getResult();
    System.out.println(gameManager.getBoardToPrint());
    System.out.printf(
        "Game result: \nPlayer 1 - %s \nPlayer 2 - %s",
        gameResult.getValue0(), gameResult.getValue1());
  }

  private int switchPlayer(int player) {
    return player == 1 ? 2 : 1;
  }
}
