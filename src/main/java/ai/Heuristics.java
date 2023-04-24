package ai;

import game.GameManager;

public class Heuristics {

  private static final double CORNER_WEIGHT = 3.5;
  private static final double EDGE_WEIGHT = 2;
  private static final double RESULT_WEIGHT = 0.5;

  public static double edgesStrategy(GameManager gameManager) {
    var result = gameManager.getResult();
    var cornerCount = gameManager.getCornerSquaresCount();
    var edgeCount = gameManager.getEdgeSquaresCount();

    return (result.getValue0() - result.getValue1()) * RESULT_WEIGHT
        + (cornerCount.getValue0() - cornerCount.getValue1()) * CORNER_WEIGHT
        + (edgeCount.getValue0() - edgeCount.getValue1()) * EDGE_WEIGHT;
  }

  public static double naiveStrategy(GameManager gameManager) {
    var result = gameManager.getResult();
    return result.getValue0() - result.getValue1();
  }

  public static double reverseNaiveStrategy(GameManager gameManager) {
    var result = gameManager.getResult();
    return result.getValue1() - result.getValue0();
  }

  private Heuristics() {}
}
