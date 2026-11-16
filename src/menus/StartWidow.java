package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWidow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;

    public StartWidow() {
        //Configuración de la Ventana Principal
        setTitle("SOULCARDS"); 
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en pantalla

        //Contenedor principal con CardLayout para alternar pantallas
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        //Crear y agregar el panel del Menú Principal
        JPanel menuPanel = createMenuPanel();
        
        // Panel de respaldo para el tablero (futuro tablero\\en oproceso)
        
        
        //panel de las reglas
        JPanel rulesPanel = new JPanel(); 
        rulesPanel.setBackground(new Color(128, 128, 128)); // verde 

        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(new RulesWindow(cardLayout, mainContainer), "REGLAS");
        mainContainer.add(new GameWindow(cardLayout, mainContainer), "JUEGO");
        
        add(mainContainer);
    }
    

    // Método para construir visualmente el Menú
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(25, 25, 35)); // Fondo oscuro 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        // Título del Juego
        JLabel titleLabel = new JLabel("SOULCARDS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Botón de INICIO
        JButton btnInicio = new JButton("INICIO");
        btnInicio.setFont(new Font("Arial", Font.BOLD, 18));
        btnInicio.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = 1;
        panel.add(btnInicio, gbc);
        
        //menu de reglas
        JButton btnReglas = new JButton("REGLAS");
        btnReglas.setFont(new Font("Arial", Font.BOLD, 18));
        btnReglas.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = 2;
        panel.add(btnReglas, gbc);

        // Botón de SALIDA
        JButton btnSalir = new JButton("SALIDA");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalir.setPreferredSize(new Dimension(200, 50));
        gbc.gridy = 3;
        panel.add(btnSalir, gbc);

        //Eventos de los Botones 
        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambia la vista hacia la pantalla del Tablero
                cardLayout.show(mainContainer, "JUEGO");
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cierra la aplicación de forma limpia
            }
        });
        
        btnReglas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	cardLayout.show(mainContainer, "REGLAS");
            	
            }
        });

        return panel;
    }
   
    
    
    

    public static void main(String[] args) {
        // Iniciar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            StartWidow menuGame = new StartWidow();
            menuGame.setVisible(true);
        });
    }
}