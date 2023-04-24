package game;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Square;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class GameManager {

  private final Board board;
  private int playerTurn = 1;

  public GameManager(Board board) {
    this.board = board;
    highlightPossibleMoves();
  }

  public GameManager(Board board, int playerTurn) {
    this.board = board;
    this.playerTurn = playerTurn;
    highlightPossibleMoves();
  }

  public int nextTurn(String move) {
    return nextTurn(extractSquareNumber(move));
  }

  public int nextTurn(Pair<Integer, Integer> move) {
    makeMove(move);
    changePlayerTurn();

    board.prepareBoardForNextTurn();
    var nextTurn = isGameFinished() ? 0 : playerTurn;
    highlightPossibleMoves();

    return nextTurn;
  }

  public String getBoardToPrint() {
    return board.toString();
  }

  public Pair<Integer, Integer> getResult() {
    return Pair.with(board.countSquares(Square.BLACK), board.countSquares(Square.WHITE));
  }

  public Board getBoard() {
    return board;
  }

  private void makeMove(Pair<Integer, Integer> move) {
    if (board.getSquare(move.getValue0(), move.getValue1()) == Square.POSSIBLE_MOVE) {
      board.setSquare(Square.forNumber(playerTurn), move.getValue0(), move.getValue1());
      evaluateMove(move.getValue0(), move.getValue1());
    }
  }

  public boolean isGameFinished() {
    //    if (doesPlayerHasNoPossibleMoves(1)) {
    //      System.out.println("Player 1 without moves");
    //    }
    //    if (doesPlayerHasNoPossibleMoves(2)) {
    //      System.out.println("Player 2 without moves");
    //    }
    //    if (isBoardFull()) {
    //      System.out.println("Board is full");
    //    }

    return isBoardFull() || (doesPlayerHasNoPossibleMoves(1) && doesPlayerHasNoPossibleMoves(2));
  }

  public List<Pair<Integer, Integer>> getPossibleMoves(int playerTurn) {
    var playerSquareType = Square.forNumber(playerTurn);
    var opponentSquareType = Square.forNumber(getOtherPlayer(playerTurn));
    var potentialMoves = board.getPotentialMoves(opponentSquareType);
    var possibleMoves = new ArrayList<Pair<Integer, Integer>>();

    potentialMoves.forEach(
        move -> {
          for (var direction : Board.DIRECTIONS) {
            var line =
                board.getLine(
                    move.getValue0(),
                    move.getValue1(),
                    direction.getValue0(),
                    direction.getValue1());

            if (line.isEmpty()) {
              continue;
            }

            if (getScore(line, playerSquareType, opponentSquareType) != 0) {
              possibleMoves.add(move);
              break;
            }
          }
        });

    return possibleMoves;
  }

  private boolean isBoardFull() {
    for (int row = 0; row < Board.BOARD_SIZE; row++) {
      for (int column = 0; column < Board.BOARD_SIZE; column++) {
        if (board.getSquare(row, column).getNumber() == Square.EMPTY.getNumber()) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean doesPlayerHasNoPossibleMoves(int playerTurn) {
    return getPossibleMoves(playerTurn).isEmpty();
  }

  private Pair<Integer, Integer> extractSquareNumber(String move) {
    int column = Character.getNumericValue(move.charAt(0)) - 10;
    int row = Character.getNumericValue(move.charAt(1)) - 1;

    return Pair.with(row, column);
  }

  private void evaluateMove(int row, int column) {
    var playerSquareType = Square.forNumber(playerTurn);
    var opponentSquareType = Square.forNumber(getOtherPlayer(playerTurn));

    for (var direction : Board.DIRECTIONS) {
      var line = board.getLine(row, column, direction.getValue0(), direction.getValue1());

      if (line.isEmpty()) {
        continue;
      }

      int score = getScore(line, playerSquareType, opponentSquareType);

      if (score > 0) {
        for (int i = 0; i < score; i++) {
          var square = line.get(i);
          board.setSquare(playerSquareType, square.getValue1(), square.getValue2());
        }
      }
    }
  }

  private int getScore(
      List<Triplet<Square, Integer, Integer>> line,
      Square playerSquareType,
      Square opponentSquareType) {
    int score = 0;
    Square squareType = Square.EMPTY;

    for (var square : line) {
      squareType = square.getValue0();
      if (squareType != opponentSquareType) {
        break;
      }

      score++;
    }

    if (score > 0 && squareType == playerSquareType) {
      return score;
    } else {
      return 0;
    }
  }

  private void highlightPossibleMoves() {
    getPossibleMoves(playerTurn)
        .forEach(
            square ->
                board.setSquare(Square.POSSIBLE_MOVE, square.getValue0(), square.getValue1()));
  }

  private void changePlayerTurn() {
    int otherPlayer = getOtherPlayer(playerTurn);
    playerTurn = doesPlayerHasNoPossibleMoves(otherPlayer) ? playerTurn : otherPlayer;
  }

  private int getOtherPlayer(int playerTurn) {
    return playerTurn == 1 ? 2 : 1;
  }
}
