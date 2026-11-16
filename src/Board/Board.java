package Board;

import Cartas.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private DefaultCard[][] tablero;
    private player jugador1;
    private player jugador2;
    private boolean turnoAzul;
    private int energiaActual;
    private boolean juegoTerminado;
    private String mensajeEstado;
    private List<String> historialEventos;

    public Board(player j1, player j2) {
        this.tablero = new DefaultCard[5][8];
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.turnoAzul = true;
        this.energiaActual = 5;
        this.juegoTerminado = false;
        this.historialEventos = new ArrayList<>();
        this.mensajeEstado = "Inicio de partida. Turno Azul.";
        registrarEvento("Partida iniciada. Turno de " + j1.getNombre());
    }

    private player getJugadorActivo() {
        if (turnoAzul) {
            return jugador1;
        }
        return jugador2;
    }

    public void registrarEvento(String evento) {
        historialEventos.add(evento);
        if (historialEventos.size() > 30) {
            historialEventos.remove(0);
        }
        System.out.println("[LOG] " + evento);
    }

    public boolean accionRobarCarta() {
        if (energiaActual < 1 || juegoTerminado) return false;
        player actual = getJugadorActivo();
        if (actual.getCartasMano().size() < 5 && actual.getObjetoMazo().getCartasRestantes() > 0) {
            actual.tomarCartaDelMazo();
            energiaActual--;
            mensajeEstado = actual.getEquipo() + " roba carta.";
            return true;
        }
        return false;
    }

    public boolean accionColocarCarta(int fila, int indiceMano) {
        if (energiaActual < 1 || fila < 0 || fila > 4 || juegoTerminado) return false;
        player actual = getJugadorActivo();
        if (indiceMano < 0 || indiceMano >= actual.getCartasMano().size()) return false;

        int numCartas = contarCartasCarril(fila, turnoAzul);
        if (numCartas >= 4) return false;

        DefaultCard carta = actual.getCartasMano().remove(indiceMano);

        if (turnoAzul) {
            tablero[fila][3 - numCartas] = carta;
        } else {
            tablero[fila][4 + numCartas] = carta;
        }
        energiaActual--;
        mensajeEstado = actual.getEquipo() + " coloca " + carta.getNombre() + " en carril " + (fila + 1);
        return true;
    }

    public boolean accionReposicionarCarta(int filaOrigen, int colOrigen, int filaDestino) {
        if (energiaActual < 1 || juegoTerminado) return false;
        DefaultCard carta = tablero[filaOrigen][colOrigen];
        String equipoActivo = turnoAzul ? "AZUL" : "ROJO";
        if (carta == null || !equipoActivo.equals(carta.getEquipo())) return false;

        int numDestino = contarCartasCarril(filaDestino, turnoAzul);
        int numOrigen = contarCartasCarril(filaOrigen, turnoAzul);

        if (filaOrigen != filaDestino) {
            if (numDestino >= 4) return false;
        } else {
            if (numOrigen < 2) return false;
            int colUltima = turnoAzul ? (3 - (numOrigen - 1)) : (4 + (numOrigen - 1));
            if (colOrigen == colUltima) return false;
        }

        tablero[filaOrigen][colOrigen] = null;
        compactarCarril(filaOrigen, turnoAzul);

        int nuevosEnDestino = contarCartasCarril(filaDestino, turnoAzul);
        if (turnoAzul) {
            tablero[filaDestino][3 - nuevosEnDestino] = carta;
        } else {
            tablero[filaDestino][4 + nuevosEnDestino] = carta;
        }
        energiaActual--;
        mensajeEstado = equipoActivo + " reposiciona " + carta.getNombre() + " hacia carril " + (filaDestino + 1);
        return true;
    }

    public void finalizarTurno() {
        if (juegoTerminado) return;
        ejecutarCombate();
        verificarVictoria();

        if (!juegoTerminado) {
            turnoAzul = !turnoAzul;
            energiaActual = 5;
            mensajeEstado = "Turno de " + (turnoAzul ? "AZUL" : "ROJO") + " | Energía: 5";
        }
    }

    private void ejecutarCombate() {
        int colInicio = turnoAzul ? 0 : 4;
        int colFin = turnoAzul ? 3 : 7;

        // Fase 1: Organización (Ya resuelta en el turno del jugador)

        // Fase 2: Aplicación de Bufos temporales / bufos de atque defensa y vida
        for (int f = 0; f < 5; f++) {
            for (int c = colInicio; c <= colFin; c++) {
                DefaultCard carta = tablero[f][c];
                if (carta != null) carta.aplicarBufos(this, f, c);
            }
        }

        // Fase 3: Habilidades Especiales de Ataque a distancia 
        for (int f = 0; f < 5; f++) {
            for (int c = colInicio; c <= colFin; c++) {
                DefaultCard carta = tablero[f][c];
                if (carta != null) carta.aplicarAtaquesEspeciales(this, f, c);
            }
        }

        // Fase 4: Enfrentamiento de Ataque Bruto
        for (int f = 0; f < 5; f++) {
            int colAtacante = turnoAzul ? 3 : 4;
            int colDefensor = turnoAzul ? 4 : 3;

            DefaultCard atacante = tablero[f][colAtacante];
            DefaultCard defensor = tablero[f][colDefensor];

            if (atacante != null && defensor != null) {
                
                int ataqueTotal = calcularAtaqueEfectivo(f, colAtacante, atacante);

                if (intentarAnulacionPorBufonDefensor(!turnoAzul, atacante)) {
                    continue; // El Bufón anuló el ataque
                }

                int[] posReceptor = buscarCaballeroInterceptor(!turnoAzul, f, atacante);
                int filaReceptor = f;
                int colReceptor = colDefensor;
                DefaultCard receptor = defensor;

                if (posReceptor != null) {
                    filaReceptor = posReceptor[0];
                    colReceptor = posReceptor[1];
                    receptor = tablero[filaReceptor][colReceptor];
                }

                if (receptor != null) {
                    int vidaAntes = receptor.getVida();
                    receptor.recibirDanio(ataqueTotal); // El escudo temporal se rompe primero

                    if (!receptor.estaViva()) {
                        tablero[filaReceptor][colReceptor] = null;
                        compactarCarril(filaReceptor, !turnoAzul);
                    } else {
                        int vidaPerdida = vidaAntes - receptor.getVida();
                        if (vidaPerdida > 0) {
                            int recuperacion = vidaPerdida / 2;
                            receptor.setVida(receptor.getVida() + recuperacion);
                        }
                    }
                }
            }
        }

        // Fase 5: Desactivación de bonus temporales en AMBOS bandos
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 8; c++) {
                if (tablero[f][c] != null) {
                    tablero[f][c].limpiarBufosTurno();
                }
            }
        }
    }

    private int calcularAtaqueEfectivo(int fila, int colAtacante, DefaultCard atacante) {
        int total = atacante.getAtaque();
        int colInicio = turnoAzul ? 0 : 4;
        int colFin = turnoAzul ? 3 : 7;
        int bonoRey = 0;

        for (int c = colInicio; c <= colFin; c++) {
            if (c != colAtacante && tablero[fila][c] != null) {
                bonoRey += tablero[fila][c].getBonoAtaqueCarril(this, fila, c);
            }
        }

        if (bonoRey > 0) {
            total += bonoRey;
            registrarEvento("Rey (" + atacante.getEquipo() + ") aumentó en +" + bonoRey + " el ataque de " + atacante.getNombre());
        }
        return total;
    }

    private boolean intentarAnulacionPorBufonDefensor(boolean defensorEsAzul, DefaultCard atacante) {
        int colFrenteDefensor = defensorEsAzul ? 3 : 4;
        List<Integer> filasBufon = new ArrayList<>();

        for (int f = 0; f < 5; f++) {
            if (tablero[f][colFrenteDefensor] instanceof JesterCard) {
                filasBufon.add(f);
            }
        }

        if (!filasBufon.isEmpty()) {
            Collections.shuffle(filasBufon);
            int filaElegida = filasBufon.get(0);
            DefaultCard bufon = tablero[filaElegida][colFrenteDefensor];
            return bufon.intentarAnularAtaque(this, filaElegida, colFrenteDefensor, atacante);
        }
        return false;
    }

    private int[] buscarCaballeroInterceptor(boolean defensorEsAzul, int filaObjetivo, DefaultCard atacante) {
        int colInicio = defensorEsAzul ? 0 : 4;
        int colFin = defensorEsAzul ? 3 : 7;
        List<int[]> caballerosCandidatos = new ArrayList<>();

        for (int f = 0; f < 5; f++) {
            if (Math.abs(f - filaObjetivo) == 1) {
                for (int c = colInicio; c <= colFin; c++) {
                    if (tablero[f][c] instanceof KnightCard) {
                        caballerosCandidatos.add(new int[]{f, c});
                    }
                }
            }
        }

        Collections.shuffle(caballerosCandidatos);
        for (int[] pos : caballerosCandidatos) {
            DefaultCard caballero = tablero[pos[0]][pos[1]];
            if (caballero.intentarAbsorberAtaque(this, pos[0], pos[1], filaObjetivo, atacante)) {
                return pos;
            }
        }
        return null;
    }

    public DefaultCard getFrenteAliado(int fila, boolean esAzul) {
        return esAzul ? tablero[fila][3] : tablero[fila][4];
    }

    public DefaultCard getFrenteEnemigo(int fila, boolean atacanteEsAzul) {
        return atacanteEsAzul ? tablero[fila][4] : tablero[fila][3];
    }

    public void verificarMuerteEnPosicion(int fila, int col) {
        DefaultCard carta = tablero[fila][col];
        if (carta != null && !carta.estaViva()) {
            boolean esAzul = "AZUL".equals(carta.getEquipo());
            tablero[fila][col] = null;
            compactarCarril(fila, esAzul);
        }
    }

    private void compactarCarril(int fila, boolean esAzul) {
        List<DefaultCard> cartasRestantes = new ArrayList<>();
        if (esAzul) {
            for (int c = 3; c >= 0; c--) {
                if (tablero[fila][c] != null) cartasRestantes.add(tablero[fila][c]);
                tablero[fila][c] = null;
            }
            for (int i = 0; i < cartasRestantes.size(); i++) {
                tablero[fila][3 - i] = cartasRestantes.get(i);
            }
        } else {
            for (int c = 4; c <= 7; c++) {
                if (tablero[fila][c] != null) cartasRestantes.add(tablero[fila][c]);
                tablero[fila][c] = null;
            }
            for (int i = 0; i < cartasRestantes.size(); i++) {
                tablero[fila][4 + i] = cartasRestantes.get(i);
            }
        }
    }

    private int contarCartasCarril(int fila, boolean esAzul) {
        int count = 0;
        if (esAzul) {
            for (int c = 0; c <= 3; c++) {
                if (tablero[fila][c] != null) count++;
            }
        } else {
            for (int c = 4; c <= 7; c++) {
                if (tablero[fila][c] != null) count++;
            }
        }
        return count;
    }

    private void verificarVictoria() {
        boolean azulSinCartas = jugador1.getCartasMano().isEmpty()
                && jugador1.getObjetoMazo().getCartasRestantes() == 0
                && contarTablero(true) == 0;
        boolean rojoSinCartas = jugador2.getCartasMano().isEmpty()
                && jugador2.getObjetoMazo().getCartasRestantes() == 0
                && contarTablero(false) == 0;

        if (azulSinCartas && rojoSinCartas) {
            juegoTerminado = true;
            mensajeEstado = "¡EMPATE TÉCNICO! Ambos quedaron sin cartas.";
        } else if (rojoSinCartas) {
            juegoTerminado = true;
            mensajeEstado = "¡VICTORIA DEL JUGADOR AZUL!";
        } else if (azulSinCartas) {
            juegoTerminado = true;
            mensajeEstado = "¡VICTORIA DEL JUGADOR ROJO!";
        }
    }

    private int contarTablero(boolean esAzul) {
        int total = 0;
        for (int f = 0; f < 5; f++) {
            total += contarCartasCarril(f, esAzul);
        }
        return total;
    }

    public DefaultCard[][] getTablero() { return tablero; }
    public boolean isTurnoAzul() { return turnoAzul; }
    public int getEnergiaActual() { return energiaActual; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public String getMensajeEstado() { return mensajeEstado; }
    public List<String> getHistorialEventos() { return historialEventos; }
}