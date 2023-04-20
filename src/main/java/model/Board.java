package model;

import java.util.Arrays;

public class Board {

  public static final int BOARD_SIZE = 8;

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
    board[2][4] = Square.POSSIBLE_MOVE;

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
