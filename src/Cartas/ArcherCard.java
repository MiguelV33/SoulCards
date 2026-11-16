package Cartas;

import Board.Board;

public class ArcherCard extends DefaultCard {
	public ArcherCard() {
        super("Arquero", 5, 5, 2, "/resources/Archer_Card.jpg");
        setDescripcionHabilidad("Resta 2 de vida a la carta enemiga\nal frente de su mismo carril.");
    }

    @Override
    public void aplicarAtaquesEspeciales(Board board, int fila, int col) {
        boolean esAzul = "AZUL".equals(getEquipo());
        DefaultCard objetivo = board.getFrenteEnemigo(fila, esAzul);
        if (objetivo != null) {
            objetivo.recibirDanioDirecto(2);
            board.registrarEvento("Arquero (" + getEquipo() + ") restó 2 de vida a " + objetivo.getNombre() + " en su carril");
            board.verificarMuerteEnPosicion(fila, esAzul ? 4 : 3);
        }
    }
}