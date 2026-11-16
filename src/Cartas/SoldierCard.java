package Cartas;

public class SoldierCard extends DefaultCard {

    public SoldierCard() {
        
    	super("Soldado", 9, 9, 7, "/resources/Soldier_Card.jpg");
    	setDescripcionHabilidad("No cuenta con ninguna habilidad especial.");
        //no tiene ninguna habilidad especial, por eso es 0
    }
}
