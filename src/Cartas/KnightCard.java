package Cartas;

import Board.Board;

public class KnightCard extends DefaultCard {
	public KnightCard() {
        super("Caballero", 11, 4, 11, "/resources/Knight_Card.jpg");
        setDescripcionHabilidad("70% de probabilidad de absorber un ataque\nenemigo dirigido a un carril adyacente.");
    }

    @Override
    public boolean intentarAbsorberAtaque(Board board, int filaCaballero, int colCaballero, int filaObjetivo, DefaultCard atacante) {
        if (Math.abs(filaCaballero - filaObjetivo) == 1) {
            if (Math.random() < 0.70) {
                board.registrarEvento("Caballero (" + getEquipo() + ") absorbió el ataque de " + atacante.getNombre() + " hacia el carril " + (filaObjetivo + 1));
                return true;
            }
        }
        return false;
    }
}