/**
 * Represents one of the two tic-tac-toe players.
 * Stores the player's display name and their board marker ('X' or 'O').
 */
public class Player {

    private final String name;
    private final char marker;

    public Player(String name, char marker) {
        this.name   = name;
        this.marker = marker;
    }

    public String getName()  { return name;   }
    public char   getMarker(){ return marker; }

    @Override
    public String toString() {
        return name + " (" + marker + ")";
    }
}
