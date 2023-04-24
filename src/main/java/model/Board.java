package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.javatuples.Triplet;

@RequiredArgsConstructor
public class Board {

  public static final int BOARD_SIZE = 8;
  public static final List<Pair<Integer, Integer>> DIRECTIONS =
      List.of(
          Pair.with(-1, -1),
          Pair.with(0, -1),
          Pair.with(1, -1),
          Pair.with(1, 0),
          Pair.with(1, 1),
          Pair.with(0, 1),
          Pair.with(-1, 1),
          Pair.with(-1, 0));

  private final Square[][] squares;

  public Board() {
    squares = startingBoard();
  }

  public static Square[][] emptyBoard() {
    var board = new Square[BOARD_SIZE][BOARD_SIZE];
    Arrays.stream(board).forEach(s -> Arrays.fill(s, Square.EMPTY));

    return board;
  }

  public static Square[][] startingBoard() {
    var board = emptyBoard();
    board[3][3] = Square.BLACK;
    board[4][4] = Square.BLACK;
    board[3][4] = Square.WHITE;
    board[4][3] = Square.WHITE;

    return board;
  }

  public static Square[][] randomStartingBoard() {
    var board = emptyBoard();
    var random = new Random();
    int row = random.nextInt(7);
    int column = random.nextInt(7);

    board[row][column] = Square.BLACK;
    board[row + 1][column + 1] = Square.BLACK;
    board[row][column + 1] = Square.WHITE;
    board[row + 1][column] = Square.WHITE;

    return board;
  }

  public Square[][] getBoard() {
    return squares;
  }

  public Square getSquare(int row, int column) {
    return squares[row][column];
  }

  public void setSquare(Square square, int row, int column) {
    squares[row][column] = square;
  }

  public List<Triplet<Square, Integer, Integer>> getLine(
      int row, int column, int xDirection, int yDirection) {
    var line = new ArrayList<Triplet<Square, Integer, Integer>>();
    int x = row + xDirection;
    int y = column + yDirection;

    while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
      line.add(Triplet.with(getSquare(x, y), x, y));
      x += xDirection;
      y += yDirection;
    }

    return line;
  }

  public List<Pair<Integer, Integer>> getPotentialMoves(Square opponentSquare) {
    var potentialMoves = new ArrayList<Pair<Integer, Integer>>();

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int column = 0; column < BOARD_SIZE; column++) {
        if (getSquare(row, column) == opponentSquare) {
          int i = row;
          int j = column;
          DIRECTIONS.forEach(
              direction -> {
                int moveRow = i + direction.getValue0();
                int moveColumn = j + direction.getValue1();

                if (moveRow >= 0
                    && moveRow < BOARD_SIZE
                    && moveColumn >= 0
                    && moveColumn < BOARD_SIZE
                    && getSquare(moveRow, moveColumn) == Square.EMPTY) {
                  potentialMoves.add(Pair.with(moveRow, moveColumn));
                }
              });
        }
      }
    }

    return potentialMoves;
  }

  public void prepareBoardForNextTurn() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int column = 0; column < BOARD_SIZE; column++) {
        if (getSquare(row, column) == Square.POSSIBLE_MOVE) {
          squares[row][column] = Square.EMPTY;
        }
      }
    }
  }

  public int countSquares(Square squareType) {
    int count = 0;

    for (var row : squares) {
      for (var square : row) {
        if (square == squareType) {
          count++;
        }
      }
    }

    return count;
  }

  @Override
  public String toString() {
    StringBuilder boardToPrint = new StringBuilder("    A  B  C  D  E  F  G  H\n");
    int rowNumber = 1;

    for (var row : squares) {
      boardToPrint.append(rowNumber).append(" |");

      for (var square : row) {
        boardToPrint.append(square).append("|");
      }

      boardToPrint.append("\n");
      rowNumber++;
    }

    return boardToPrint.toString();
  }
}
