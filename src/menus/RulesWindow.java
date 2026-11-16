package menus;

import javax.swing.*;
import java.awt.*;

public class RulesWindow extends JPanel { // Extiende de JPanel

    public RulesWindow(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(new Color(35, 35, 45)); // Fondo oscuro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Título
        JLabel titleLabel = new JLabel("REGLAS DEL JUEGO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Texto de reglas
        JTextArea rulesText = new JTextArea(
            "1. Cada jugador dispone de 4 Jugadas por turno.\n" +
            "2. Hay 2 carriles de combate con espacio para máx. 4 cartas.\n" +
            "3. Límite de mano: Máximo 5 cartas.\n" +
            "4. Robar o colocar una carta consume 1 Jugada. \n"+
            "5. Gana el ultimo en quedarse sin cartas"
            
        );
        rulesText.setFont(new Font("Arial", Font.PLAIN, 16));
        rulesText.setEditable(false);
        rulesText.setBackground(new Color(35, 35, 45));
        rulesText.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(rulesText, gbc);

        // Botón Volver
        JButton btnVolver = new JButton("VOLVER AL MENÚ");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        add(btnVolver, gbc);

        // Evento para regresar con el CardLayout
        btnVolver.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));
    }
}