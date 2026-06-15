package client.gameplay;

public class Help {

    public String text() {
        return """
                Gameplay Commands:
                  redraw
                  move <start> <end>
                  resign
                  leave
                  highlight <position>
                  help
                  quit
                """;
    }
}