package Cartas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Board.Board;

public class WizardCard extends DefaultCard {
	public WizardCard() {
        super("Mago", 7, 8, 4, "/resources/Wizard_Card.jpg");
        setDescripcionHabilidad("Resta 2 de vida a una carta enemiga\nal azar en otro carril.");
    }

    @Override
    public void aplicarAtaquesEspeciales(Board board, int fila, int col) {
        boolean esAzul = "AZUL".equals(getEquipo());
        List<Integer> carrilesValidos = new ArrayList<>();

        for (int f = 0; f < 5; f++) {
            if (f != fila && board.getFrenteEnemigo(f, esAzul) != null) {
                carrilesValidos.add(f);
            }
        }

        if (!carrilesValidos.isEmpty()) {
            Collections.shuffle(carrilesValidos);
            int filaObjetivo = carrilesValidos.get(0);
            DefaultCard objetivo = board.getFrenteEnemigo(filaObjetivo, esAzul);
            if (objetivo != null) {
                objetivo.recibirDanioDirecto(2);
                board.registrarEvento("Mago (" + getEquipo() + ") restó 2 de vida a " + objetivo.getNombre() + " en carril " + (filaObjetivo + 1));
                board.verificarMuerteEnPosicion(filaObjetivo, esAzul ? 4 : 3);
            }
        }
    }
}