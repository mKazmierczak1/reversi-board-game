package ai;

import static java.lang.Math.max;
import static java.lang.Math.min;

import game.GameManager;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import model.Board;
import model.Square;
import org.javatuples.Pair;
import org.javatuples.Triplet;

@RequiredArgsConstructor
public class AIOpponent {

  private final int player;
  private final int initialDepth;

  public Pair<Integer, Integer> makeMove(Board board) {
    var decision =
        miniMax(
            null,
            initialDepth,
            -Double.MAX_VALUE,
            Double.MAX_VALUE,
            player,
            board,
            gameManager -> {
              var result = gameManager.getResult();
              return (double) (result.getValue0() - result.getValue1());
            });

    var decisionRow = decision.getValue1();
    var decisionColumn = decision.getValue2();

    return decisionRow != null && decisionColumn != null
        ? Pair.with(decisionRow, decisionColumn)
        : null;
  }

  private Triplet<Double, Integer, Integer> miniMax(
      Pair<Integer, Integer> move,
      int depth,
      double alpha,
      double beta,
      int player,
      Board board,
      Function<GameManager, Double> heuristic) {
    var copiedBoard = new Board(copyBoard(board.getBoard()));
    var gameManager = new GameManager(copiedBoard, player);
    copiedBoard.prepareBoardForNextTurn();

    if (gameManager.isGameFinished()) {
      var result = gameManager.getResult();
      return (Triplet.with(
          result.getValue0() - result.getValue1() > 0
              ? Double.MAX_VALUE
              : result.getValue0() - result.getValue1() < 0 ? -Double.MAX_VALUE : 0,
          move.getValue0(),
          move.getValue1()));
    }

    if (depth == 0) {
      return (Triplet.with(heuristic.apply(gameManager), move.getValue0(), move.getValue1()));
    }

    if (isMaximizing(player)) {
      double maxEval = -Double.MAX_VALUE;
      Pair<Integer, Integer> bestMove = null;

      for (var possibleMove : gameManager.getPossibleMoves(player)) {
        var game = new GameManager(new Board(copyBoard(board.getBoard())), player);
        int nextPlayer = game.nextTurn(possibleMove);

        double eval =
            miniMax(possibleMove, depth - 1, alpha, beta, nextPlayer, game.getBoard(), heuristic)
                .getValue0();
        maxEval = max(eval, maxEval);
        bestMove = maxEval == eval ? possibleMove : bestMove;
        alpha = max(alpha, eval);

        if (beta <= alpha) {
          break;
        }
      }

      return bestMove != null
          ? Triplet.with(maxEval, bestMove.getValue0(), bestMove.getValue1())
          : Triplet.with(maxEval, null, null);
    } else {
      double minEval = Double.MAX_VALUE;
      Pair<Integer, Integer> bestMove = null;

      for (var possibleMove : gameManager.getPossibleMoves(player)) {
        var game = new GameManager(new Board(copyBoard(board.getBoard())), player);
        int nextPlayer = game.nextTurn(possibleMove);

        double eval =
            miniMax(possibleMove, depth - 1, alpha, beta, nextPlayer, game.getBoard(), heuristic)
                .getValue0();
        minEval = min(eval, minEval);
        bestMove = minEval == eval ? possibleMove : bestMove;
        beta = min(beta, eval);

        if (beta <= alpha) {
          break;
        }
      }

      return bestMove != null
          ? Triplet.with(minEval, bestMove.getValue0(), bestMove.getValue1())
          : Triplet.with(minEval, null, null);
    }
  }

  private Square[][] copyBoard(Square[][] board) {
    var copiedBoard = Board.emptyBoard();

    for (int i = 0; i < Board.BOARD_SIZE; i++) {
      System.arraycopy(board[i], 0, copiedBoard[i], 0, Board.BOARD_SIZE);
    }

    return copiedBoard;
  }

  private boolean isMaximizing(int player) {
    return player == 1;
  }
}
