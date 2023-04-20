package model;

public enum Square {
  EMPTY(0),
  BLACK(1),
  WHITE(2),
  POSSIBLE_MOVE(3);

  public static Square forNumber(int value) {
    return switch (value) {
      case 1 -> BLACK;
      case 2 -> WHITE;
      case 3 -> POSSIBLE_MOVE;
      default -> EMPTY;
    };
  }

  public int getNumber() {
    return value;
  }

  @Override
  public String toString() {
    return switch (this) {
      case EMPTY -> "  ";
      case BLACK -> "⚫";
      case WHITE -> "⚪";
      case POSSIBLE_MOVE -> "\uD83D\uDD39";
    };
  }

  private final int value;

  Square(int value) {
    this.value = value;
  }
}
