package ProccesCards;

import Cartas.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<DefaultCard> cartas;

    public Deck() {
        this.cartas = new ArrayList<>();
        generarMazo();
    }

    private void generarMazo() {
        // Aquí defines las cantidades máximas de tu tabla:
        
        // 5 Soldados
        for (int i = 0; i < 5; i++) { cartas.add(new SoldierCard()); }
        
        // 1 Rey
        for (int i = 0; i < 1; i++) { cartas.add(new KingCard()); }
        
        // 3 Magos
        for (int i = 0; i < 3; i++) { cartas.add(new WizardCard()); }
        
        // 3 Príncipes
        for (int i = 0; i < 3; i++) { cartas.add(new PrinceCard()); }
        
        // 2 Bufones
        for (int i = 0; i < 2; i++) { cartas.add(new JesterCard()); }
        
        // 4 Arqueros
        for (int i = 0; i < 4; i++) { cartas.add(new ArcherCard()); }
        
        // 3 Caballeros
        for (int i = 0; i < 3; i++) { cartas.add(new KnightCard()); }

        // Mezclar las 21 cartas en orden aleatorio
        Collections.shuffle(this.cartas);
    }

    public List<DefaultCard> getCartas() {
        return cartas;
        
    }
    public DefaultCard removeCard() {
        if (!this.cartas.isEmpty()) {
            // Remueve y devuelve la última carta de la lista (la parte superior del mazo)
            return this.cartas.remove(this.cartas.size() - 1);
        }
        return null; // Si el mazo se queda sin cartas
    }
    public int getCartasRestantes() {
	    return cartas.size();
	}
}