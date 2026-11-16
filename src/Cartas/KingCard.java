package Cartas;

import Board.Board;

public class KingCard extends DefaultCard {
	public KingCard() {
        super("Rey", 13, 5, 2, "/resources/King_Card.jpg");
        setDescripcionHabilidad("Aura pasiva: +2 de Ataque a las cartas\naliadas en su mismo carril mientras viva.");
    }

    @Override
    public int getBonoAtaqueCarril(Board board, int fila, int col) {
        return 2;
    }
    
    
}