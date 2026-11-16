package Board;

import java.util.ArrayList;
import java.util.List;
import ProccesCards.Deck;
import Cartas.DefaultCard;

public class player {
    private Deck ObjetoMazo;
    private List<DefaultCard> mazo;
    private List<DefaultCard> cartasMano;
    private String nombre;
    private String equipo;

    public player(String nombre, String equipo) {
        this.nombre = nombre;
        this.equipo = equipo;
        this.ObjetoMazo = new Deck();
        this.mazo = ObjetoMazo.getCartas();
        this.cartasMano = new ArrayList<>();
    }

    public List<DefaultCard> getCartas() {
        return mazo;
    }

    public List<DefaultCard> getCartasMano() {
        return cartasMano;
    }

    public void tomarCartaDelMazo() {
        if (cartasMano.size() >= 5) {
            return;
        }
        DefaultCard ultimaCarta = this.ObjetoMazo.removeCard();
        if (ultimaCarta != null) {
            ultimaCarta.setEquipo(this.equipo);
            this.cartasMano.add(ultimaCarta);
        }
    }

    public void robarManoInicial() {
        while (cartasMano.size() < 5 && ObjetoMazo.getCartasRestantes() > 0) {
            tomarCartaDelMazo();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getEquipo() {
        return equipo;
    }

    public Deck getObjetoMazo() {
        return this.ObjetoMazo;
    }
}