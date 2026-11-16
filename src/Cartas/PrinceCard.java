package Cartas;


import Board.Board;

public class PrinceCard extends DefaultCard {
	public PrinceCard() {
        super("Príncipe", 8, 6, 4, "/resources/Prince_Card.jpg");
        setDescripcionHabilidad("Otorga escudo temporal (+2 Defensa) a\n2 aliados al azar en otros carriles.");
    }

    @Override
    public void aplicarBufos(Board board, int fila, int col) {
        boolean esAzul = "AZUL".equals(getEquipo());
        java.util.List<DefaultCard> objetivos = new java.util.ArrayList<>();
        java.util.List<String> nombres = new java.util.ArrayList<>();

        for (int f = 0; f < 5; f++) {
            if (f != fila) {
                DefaultCard frenteAliado = board.getFrenteAliado(f, esAzul);
                if (frenteAliado != null) objetivos.add(frenteAliado);
            }
        }

        if (!objetivos.isEmpty()) {
            java.util.Collections.shuffle(objetivos);
            int limite = Math.min(2, objetivos.size());
            for (int i = 0; i < limite; i++) {
                DefaultCard aliado = objetivos.get(i);
                aliado.agregarBonoDefensa(2); 
                nombres.add(aliado.getNombre());
            }
            board.registrarEvento("Príncipe (" + getEquipo() + ") dio escudo temporal +2 DEF a " + String.join(" y ", nombres));
        }
    }
}