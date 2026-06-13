package ui;

public class GameBuilder {
    public String buildWhiteBoard() {
        return """
                  a  b  c  d  e  f  g  h

                8 r  n  b  q  k  b  n  r
                7 p  p  p  p  p  p  p  p
                6 .  .  .  .  .  .  .  .
                5 .  .  .  .  .  .  .  .
                4 .  .  .  .  .  .  .  .
                3 .  .  .  .  .  .  .  .
                2 P  P  P  P  P  P  P  P
                1 R  N  B  Q  K  B  N  R

                  a  b  c  d  e  f  g  h
                """;
    }
    public String buildBlackBoard() {
        return """
                  h  g  f  e  d  c  b  a

                1 R  N  B  K  Q  B  N  R
                2 P  P  P  P  P  P  P  P
                3 .  .  .  .  .  .  .  .
                4 .  .  .  .  .  .  .  .
                5 .  .  .  .  .  .  .  .
                6 .  .  .  .  .  .  .  .
                7 p  p  p  p  p  p  p  p
                8 r  n  b  k  q  b  n  r

                  h  g  f  e  d  c  b  a
                """;
    }
}