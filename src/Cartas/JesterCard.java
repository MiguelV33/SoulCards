package Cartas;

import Board.Board;

public class JesterCard extends DefaultCard {
	public JesterCard() {
        super("Bufón", 10, 4, 3, "/resources/Jester_Card.jpg");
        setDescripcionHabilidad("50% de probabilidad de anular\ncompletamente el ataque frontal enemigo.");
    }

    @Override
    public boolean intentarAnularAtaque(Board board, int filaBufon, int colBufon, DefaultCard atacante) {
        if (Math.random() < 0.50) {
            board.registrarEvento("Bufón (" + getEquipo() + ") anuló completamente el ataque de " + atacante.getNombre());
            return true;
        }
        return false;
    }
}