package ProccesCards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Cartas.ArcherCard;
import Cartas.DefaultCard;
import Cartas.JesterCard;
import Cartas.KingCard;
import Cartas.KnightCard;
import Cartas.PrinceCard;
import Cartas.SoldierCard;
import Cartas.WizardCard;
public class DeckTest {
    private List<DefaultCard> cartas;

    public DeckTest() {
        this.cartas = new ArrayList<>();
        generarMazo();
    }

    private void generarMazo() {
        //Instanciar la cantidad de cada carta, define el orden del maso desde el inicio(21 cartas)
        for (int i = 0; i < 5; i++) { cartas.add(new SoldierCard()); }
        for (int i = 0; i < 1; i++) { cartas.add(new KingCard()); }
        for (int i = 0; i < 3; i++) { cartas.add(new WizardCard()); }
        for (int i = 0; i < 3; i++) { cartas.add(new PrinceCard()); }
        for (int i = 0; i < 2; i++) { cartas.add(new JesterCard()); }
        for (int i = 0; i < 4; i++) { cartas.add(new ArcherCard()); }
        for (int i = 0; i < 3; i++) { cartas.add(new KnightCard()); }

        //sirve para ezclar el mazo de forma aleatoria(NO BORRAR)
        Collections.shuffle(this.cartas);

        //Sirve para imprimir el resultado en la consola
        mostrarMazoEnConsola();
    }

    private void mostrarMazoEnConsola() {
        System.out.println("=== ORDEN DEL MAZO MEZCLADO ===");
        for (int i = 0; i < cartas.size(); i++) {
            DefaultCard carta = cartas.get(i);
            // Muestra la posición (1 al 21) y el nombre de la carta
            System.out.println("Carta " + (i + 1) + ": " + carta.getNombre());
        }
        System.out.println("================================");
    }

    public List<DefaultCard> getCartas() {
        return cartas;
        //sirve para llamar la lista de cartas 
        //DefaultCard cartaRobada = listaDeCartas.remove(0); --linea importante , en algun momento la usare(sirve para remover la carta 0 , y retorna la carta 0 a eliminar)
    }
    /*QUITAR COMENTARIOS EN CASO DE TEST(HAY QUE QUITAR CUALQUIER OTRO MAIN PARA PROBAR)
    public static void main(String[] args) {
        // Instancia la clase para que se ejecute el constructor y se imprima el mazo
        DeckTest mazoPrueba = new DeckTest(); 
    }
    */
}