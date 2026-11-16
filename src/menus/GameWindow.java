package menus;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Board.Board;
import Board.player;
import Cartas.DefaultCard;

public class GameWindow extends JPanel {

    private Board board;
    private player jugador1;
    private player jugador2;

    private Image marcoAzul;
    private Image marcoRojo;

    private int selectedHandIndex = -1;
    private Point selectedBoardCell = null;
    private DefaultCard cartaSeleccionadaRender = null;
    private Rectangle rectCartaSeleccionada = null;

    private JButton btnTerminar;
    private JButton btnVolver;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private boolean nombresSolicitados = false;
    
    private JTextPane chatArea;
    private JScrollPane scrollChat;
    private int ultimoIndexMensaje = 0;
    
    

    
    public GameWindow(CardLayout cardLayout, JPanel mainContainer) {
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;

        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));

        
        
        jugador1 = new player("Jugador 1", "AZUL");
        jugador2 = new player("Jugador 2", "ROJO");
        board = new Board(jugador1, jugador2);

        try {
            if (getClass().getResource("/resources/Marco_Azul.png") != null) {
                marcoAzul = new ImageIcon(getClass().getResource("/resources/Marco_Azul.png")).getImage();
            }
            if (getClass().getResource("/resources/Marco_Rojo.png") != null) {
                marcoRojo = new ImageIcon(getClass().getResource("/resources/Marco_Rojo.png")).getImage();
            }
        } catch (Exception e) {
            System.out.println("Error cargando marcos: " + e.getMessage());
        }

        btnTerminar = new JButton("Terminar Turno");
        btnTerminar.setFont(new Font("SansSerif", Font.BOLD, 17));
        btnTerminar.setBackground(new Color(30, 130, 60));
        btnTerminar.setForeground(Color.WHITE);
        btnTerminar.setFocusPainted(false);
        btnTerminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVolver.setBackground(new Color(150, 40, 40));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(btnTerminar);
        add(btnVolver);
        

        configurarEventosMouse();

        btnTerminar.addActionListener(e -> {
            if (board.isJuegoTerminado()) {
                cardLayout.show(mainContainer, "MENU");
                return;
            }
            board.finalizarTurno();
            selectedHandIndex = -1;
            selectedBoardCell = null;
            if (board.isJuegoTerminado()) {
                btnTerminar.setText("Terminar Partida");
                btnTerminar.setBackground(new Color(180, 130, 20));
            }
            repaint();
        });

        btnVolver.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (!nombresSolicitados) {
                    nombresSolicitados = true;
                    SwingUtilities.invokeLater(() -> solicitarNombres());
                }
            }
        });
        
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setOpaque(false);
        chatArea.setFocusable(false);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        
        JPanel chatPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 200)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        chatPanel.setOpaque(false);
        chatPanel.add(chatArea, BorderLayout.CENTER);
        
        scrollChat = new JScrollPane(chatPanel);
        scrollChat.setOpaque(false);
        scrollChat.getViewport().setOpaque(false);
        scrollChat.setBorder(null);
        scrollChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); 
        add(scrollChat);
    }

    private void solicitarNombres() {
        String n1 = pedirNombre("Jugador 1", "AZUL");
        String n2 = pedirNombre("Jugador 2", "ROJO");
        jugador1 = new player(n1, "AZUL");
        jugador2 = new player(n2, "ROJO");
        board = new Board(jugador1, jugador2);
        selectedHandIndex = -1;
        selectedBoardCell = null;
        btnTerminar.setText("Terminar Turno");
        btnTerminar.setBackground(new Color(30, 130, 60));
        repaint();
    }

    private String pedirNombre(String titulo, String bando) {
        String input = JOptionPane.showInputDialog(
            SwingUtilities.getWindowAncestor(this),
            "Ingrese el nombre del " + titulo + " (" + bando + "):",
            "Configuración",
            JOptionPane.QUESTION_MESSAGE
        );
        return (input == null || input.trim().isEmpty()) ? titulo : input.trim();
    }

    private int anchoEfectivo() {
        return getWidth() > 0 ? getWidth() : 1280;
    }

    private int altoEfectivo() {
        return getHeight() > 0 ? getHeight() : 720;
    }

    
    @Override
    public void doLayout() {
        super.doLayout();
        int w = anchoEfectivo();
        int h = altoEfectivo();
        btnTerminar.setBounds(w / 2 - 115, (int) (h * 0.89), 230, 42);
        btnVolver.setBounds(20, (int) (h * 0.91), 145, 34);
        
        // Posicionar el chat en la esquina inferior derecha
        int chatWidth = 450;
        int chatHeight = 150;
        scrollChat.setBounds(w - chatWidth - 20, h - chatHeight - 20, chatWidth, chatHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = anchoEfectivo();
        int h = altoEfectivo();

        if (board.isTurnoAzul()) {
            g2.setColor(new Color(14, 28, 58));
        } else {
            g2.setColor(new Color(58, 16, 22));
        }
        g2.fillRect(0, 0, w, h);

        cartaSeleccionadaRender = null;
        rectCartaSeleccionada = null;

        dibujarTablero(g2, w, h);
        dibujarEsquinasDecks(g2, w, h);
        dibujarManos(g2, w, h);
        dibujarPanelInferior(g2, w, h);

        if (cartaSeleccionadaRender != null && rectCartaSeleccionada != null) {
            //Dibujar la carta aumentada por encima de todo
            dibujarCarta(
                g2,
                cartaSeleccionadaRender,
                rectCartaSeleccionada.x,
                rectCartaSeleccionada.y,
                rectCartaSeleccionada.width,
                rectCartaSeleccionada.height,
                true,
                selectedHandIndex != -1
            );

            // DIBUJAR CUADRO DE INFORMACIÓN DE HABILIDAD 
            int infoW = 380; // Ancho del cuadro
            int infoH = 65;  // Alto del cuadro negro
            int infoX = w / 2 - infoW / 2;
            int infoY = (int) (h * 0.02); // Separación desde arriba

            // Cuadro principal negro 
            g2.setColor(new Color(0, 0, 0, 210));
            g2.fillRoundRect(infoX, infoY + 20, infoW, infoH, 15, 15);

            //Cuadro superior Nombre de la carta en Rojo o Azu
            boolean esAzul = "AZUL".equals(cartaSeleccionadaRender.getEquipo());
            g2.setColor(esAzul ? new Color(20, 60, 180) : new Color(180, 20, 30));
            g2.fillRoundRect(infoX + 40, infoY, infoW - 80, 30, 10, 10);

            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fmNom = g2.getFontMetrics();
            String nom = cartaSeleccionadaRender.getNombre();
            g2.drawString(nom, infoX + 40 + (infoW - 80 - fmNom.stringWidth(nom)) / 2, infoY + 20);

            
            g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
            FontMetrics fmDesc = g2.getFontMetrics();
            String desc = cartaSeleccionadaRender.getDescripcionHabilidad();
            
            if (desc != null) {
                String[] lineas = desc.split("\n"); // Separa el texto si hay un salto de línea
                int textY = infoY + 45;
                for (String linea : lineas) {
                    g2.drawString(linea, infoX + (infoW - fmDesc.stringWidth(linea)) / 2, textY);
                    textY += fmDesc.getHeight() + 2; // Baja para la siguiente línea
                }
            }
        }

        if (board.isJuegoTerminado()) {
            dibujarBannerVictoria(g2, w, h);
        }
        actualizarChatLog();
    }

    private Dimension calcularDimensionCarta(int w, int h) {
        int cellW = (int) (w * 0.56) / 8;
        int cellH = (int) (h * 0.66) / 5;
        return new Dimension(cellW - 8, cellH - 8);
    }

    private void dibujarTablero(Graphics2D g2, int w, int h) {
        int cellW = (int) (w * 0.56) / 8;
        int cellH = (int) (h * 0.66) / 5;
        int boardStartX = (w - (8 * cellW)) / 2;
        int boardStartY = (int) (h * 0.14);

        DefaultCard[][] matriz = board.getTablero();
        int gap = 4;

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 8; c++) {
                int cx = boardStartX + c * cellW;
                int cy = boardStartY + r * cellH;

                if (c <= 3) {
                    g2.setColor(new Color(22, 48, 92));
                } else {
                    g2.setColor(new Color(92, 24, 32));
                }
                g2.fillRoundRect(cx + gap, cy + gap, cellW - gap * 2, cellH - gap * 2, 15, 15);

                g2.setColor(new Color(255, 255, 255, 35));
                g2.drawRoundRect(cx + gap, cy + gap, cellW - gap * 2, cellH - gap * 2, 15, 15);

                DefaultCard card = matriz[r][c];
                if (card != null) {
                    boolean isSelected = (selectedBoardCell != null && selectedBoardCell.x == c && selectedBoardCell.y == r);
                    int cardW = cellW - gap * 2;
                    int cardH = cellH - gap * 2;

                    if (isSelected) {
                        cartaSeleccionadaRender = card;
                        rectCartaSeleccionada = new Rectangle(cx + gap, cy + gap, cardW, cardH);
                    } else {
                        dibujarCarta(g2, card, cx + gap, cy + gap, cardW, cardH, false, false);
                    }
                }
            }
        }
    }

    private void dibujarManos(Graphics2D g2, int w, int h) {
        Dimension dim = calcularDimensionCarta(w, h);
        int cardW = dim.width;
        int cardH = dim.height;
        int gapV = 8;

        int startY = (int) (h * 0.14) + 4;
        int xP1 = (int) (w * 0.13);
        int xP2 = (int) (w * 0.87) - cardW;

        dibujarColumnaMano(g2, jugador1, xP1, startY, cardW, cardH, gapV, board.isTurnoAzul());
        dibujarColumnaMano(g2, jugador2, xP2, startY, cardW, cardH, gapV, !board.isTurnoAzul());
    }

    private void dibujarColumnaMano(Graphics2D g2, player p, int x, int startY, int cardW, int cardH, int gapV, boolean esSuTurno) {
        java.util.List<DefaultCard> mano = p.getCartasMano();

        for (int i = 0; i < 5; i++) {
            int cy = startY + i * (cardH + gapV);

            g2.setColor(new Color(10, 15, 20, 110));
            g2.fillRoundRect(x, cy, cardW, cardH, 15, 15);
            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(x, cy, cardW, cardH, 15, 15);

            if (i < mano.size()) {
                DefaultCard card = mano.get(i);
                boolean isSelected = (esSuTurno && selectedHandIndex == i);
                if (isSelected) {
                    cartaSeleccionadaRender = card;
                    rectCartaSeleccionada = new Rectangle(x, cy, cardW, cardH);
                } else {
                    dibujarCarta(g2, card, x, cy, cardW, cardH, false, true);
                }
            }
        }
    }

    private void dibujarEsquinasDecks(Graphics2D g2, int w, int h) {
        Dimension dim = calcularDimensionCarta(w, h);
        int cardW = dim.width;
        int cardH = dim.height;

        int marginY = (int) (h * 0.03);
        int xP1 = (int) (w * 0.03);
        int xP2 = (int) (w * 0.97) - cardW;

        g2.setFont(new Font("SansSerif", Font.BOLD, 17));
        g2.setColor(board.isTurnoAzul() ? Color.GREEN : Color.LIGHT_GRAY);
        g2.drawString(jugador1.getNombre(), xP1, marginY + 15);
        dibujarMazo(g2, xP1, marginY + 25, cardW, cardH, marcoAzul, jugador1.getObjetoMazo().getCartasRestantes());

        g2.setColor(!board.isTurnoAzul() ? Color.GREEN : Color.LIGHT_GRAY);
        String nameP2 = jugador2.getNombre();
        int nameW = g2.getFontMetrics().stringWidth(nameP2);
        g2.drawString(nameP2, xP2 + cardW - nameW, marginY + 15);
        dibujarMazo(g2, xP2, marginY + 25, cardW, cardH, marcoRojo, jugador2.getObjetoMazo().getCartasRestantes());
    }

    private void dibujarMazo(Graphics2D g2, int x, int y, int w, int h, Image marco, int restantes) {
        g2.setColor(new Color(15, 15, 20));
        g2.fillRoundRect(x, y, w, h, 10, 10);
        if (marco != null) {
            g2.drawImage(marco, x, y, w, h, this);
        }
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        String txt = String.valueOf(restantes);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, x + (w - fm.stringWidth(txt)) / 2, y + (h + fm.getAscent()) / 2 - 8);
    }

    private void dibujarPanelInferior(Graphics2D g2, int w, int h) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        String txtEnergia = "Energía: " + board.getEnergiaActual() + " / 5";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txtEnergia, w / 2 - fm.stringWidth(txtEnergia) / 2, (int) (h * 0.87));

        g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
        g2.setColor(new Color(255, 215, 0));
        String msg = board.getMensajeEstado() != null ? board.getMensajeEstado() : "";
        g2.drawString(msg, w / 2 - g2.getFontMetrics().stringWidth(msg) / 2, (int) (h * 0.98));
    }

    private void dibujarCarta(Graphics2D g2, DefaultCard card, int x, int y, int cW, int cH, boolean selected, boolean isFullStats) {
        int dX = x, dY = y, dW = cW, dH = cH;

        if (selected) {
            dW = (int) (cW * 1.1);
            dH = (int) (cH * 1.1);
            dX = x - (dW - cW) / 2;
            dY = y - (dH - cH) / 2;
        }

        int pad = 6;
        Image img = card.getImagenPersonaje();
        if (img != null) {
            g2.drawImage(img, dX + pad, dY + pad, dW - pad * 2, dH - pad * 2, this);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(dX + pad, dY + pad, dW - pad * 2, dH - pad * 2);
        }

        boolean esAzul = "AZUL".equals(card.getEquipo());
        Image marco = esAzul ? marcoAzul : marcoRojo;
        if (marco != null) {
            g2.drawImage(marco, dX, dY, dW, dH, this);
        }

        g2.setColor(esAzul ? new Color(20, 60, 180) : new Color(180, 20, 30));
        g2.fillRoundRect(dX + 4, dY - 6, dW - 8, 18, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        String nombre = card.getNombre() != null ? card.getNombre() : "";
        g2.drawString(nombre, dX + (dW - fm.stringWidth(nombre)) / 2, dY + 7);

        if (selected || isFullStats) {
            dibujarEstadisticasNumericas(g2, card, dX, dY, dW, dH);
        } else {
            dibujarBarrasEstadisticas(g2, card, dX, dY, dW, dH);
        }

        if (selected) {
            g2.setColor(new Color(255, 215, 0, 240));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(dX, dY, dW, dH, 8, 8);
            g2.setStroke(new BasicStroke(1));
        }
    }

    private void dibujarEstadisticasNumericas(Graphics2D g2, DefaultCard card, int x, int y, int w, int h) {
        int sq = Math.max(14, (int) (w * 0.27));
        int gap = (w - (3 * sq)) / 4;
        int sX = x + gap;
        int sY = y + h - sq - 6;

        dibujarCuadro(g2, String.valueOf(card.getVida()), new Color(40, 180, 40), sX, sY, sq);
        dibujarCuadro(g2, String.valueOf(card.getAtaque()), new Color(200, 40, 40), sX + sq + gap, sY, sq);
        dibujarCuadro(g2, String.valueOf(card.getDefensa()), new Color(40, 80, 200), sX + 2 * (sq + gap), sY, sq);
    }

    private void dibujarCuadro(Graphics2D g2, String text, Color c, int x, int y, int size) {
        g2.setColor(c);
        g2.fillRoundRect(x, y, size, size, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, size / 2 + 1)));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x + (size - fm.stringWidth(text)) / 2, y + (size - fm.getHeight()) / 2 + fm.getAscent());
    }

    private void dibujarBarrasEstadisticas(Graphics2D g2, DefaultCard card, int x, int y, int w, int h) {
        int bH = Math.max(3, h / 22);
        int bW = w - 16;
        int sX = x + 8;
        int sY = y + h - (3 * bH + 10);

        int v = Math.max(0, Math.min(bW, (int) ((double) card.getVida() / Math.max(1, card.getVidaMaxima()) * bW)));
        g2.setColor(new Color(40, 180, 40));
        g2.fillRect(sX, sY, v, bH);

        int a = Math.max(0, Math.min(bW, (int) ((double) card.getAtaque() / 15.0 * bW)));
        g2.setColor(new Color(200, 40, 40));
        g2.fillRect(sX, sY + bH + 2, a, bH);

        int d = Math.max(0, Math.min(bW, (int) ((double) card.getDefensa() / 15.0 * bW)));
        g2.setColor(new Color(40, 80, 200));
        g2.fillRect(sX, sY + 2 * (bH + 2), d, bH);
    }

    private void dibujarBannerVictoria(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 44));
        String msg = board.getMensajeEstado() != null ? board.getMensajeEstado() : "¡PARTIDA TERMINADA!";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2 - 20);
    }

    private void configurarEventosMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board.isJuegoTerminado()) return;

                int mx = e.getX(), my = e.getY();
                int w = anchoEfectivo(), h = altoEfectivo();

                Dimension dim = calcularDimensionCarta(w, h);
                int cardW = dim.width;
                int cardH = dim.height;

                Rectangle rDeck1 = new Rectangle((int) (w * 0.03), (int) (h * 0.03) + 25, cardW, cardH);
                Rectangle rDeck2 = new Rectangle((int) (w * 0.97) - cardW, (int) (h * 0.03) + 25, cardW, cardH);

                if (board.isTurnoAzul() && rDeck1.contains(mx, my)) {
                    board.accionRobarCarta();
                    repaint();
                    return;
                } else if (!board.isTurnoAzul() && rDeck2.contains(mx, my)) {
                    board.accionRobarCarta();
                    repaint();
                    return;
                }

                int startY = (int) (h * 0.14) + 4;
                int gapV = 8;
                int xMano;
                player activo;
                if (board.isTurnoAzul()) {
                    xMano = (int) (w * 0.13);
                    activo = jugador1;
                } else {
                    xMano = (int) (w * 0.87) - cardW;
                    activo = jugador2;
                }

                for (int i = 0; i < 5; i++) {
                    Rectangle rHand = new Rectangle(xMano, startY + i * (cardH + gapV), cardW, cardH);
                    if (rHand.contains(mx, my)) {
                        if (i < activo.getCartasMano().size()) {
                            selectedHandIndex = (selectedHandIndex == i) ? -1 : i;
                            selectedBoardCell = null;
                        }
                        repaint();
                        return;
                    }
                }

                int cellW = (int) (w * 0.56) / 8;
                int cellH = (int) (h * 0.66) / 5;
                int boardStartX = (w - (8 * cellW)) / 2;
                int boardStartY = (int) (h * 0.14);

                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 8; c++) {
                        Rectangle rCell = new Rectangle(boardStartX + c * cellW, boardStartY + r * cellH, cellW, cellH);
                        if (rCell.contains(mx, my)) {
                            procesarClicTablero(r, c);
                            repaint();
                            return;
                        }
                    }
                }

                selectedHandIndex = -1;
                selectedBoardCell = null;
                repaint();
            }
        });
    }

    private void procesarClicTablero(int r, int c) {
        boolean zonaValida = (board.isTurnoAzul() && c <= 3) || (!board.isTurnoAzul() && c >= 4);

        if (selectedHandIndex != -1) {
            if (zonaValida) {
                board.accionColocarCarta(r, selectedHandIndex);
                selectedHandIndex = -1;
            }
        } else if (selectedBoardCell != null) {
            if (selectedBoardCell.y == r && selectedBoardCell.x == c) {
                selectedBoardCell = null;
                return;
            }
            DefaultCard cartaSel = board.getTablero()[selectedBoardCell.y][selectedBoardCell.x];
            String eqTurno = board.isTurnoAzul() ? "AZUL" : "ROJO";

            if (cartaSel != null && eqTurno.equals(cartaSel.getEquipo()) && zonaValida) {
                boolean exito = board.accionReposicionarCarta(selectedBoardCell.y, selectedBoardCell.x, r);
                if (exito) {
                    selectedBoardCell = null;
                }
            } else {
                seleccionarCartaTablero(r, c);
            }
        } else {
            seleccionarCartaTablero(r, c);
        }
    }

    private void seleccionarCartaTablero(int r, int c) {
        if (board.getTablero()[r][c] != null) {
            selectedBoardCell = new Point(c, r);
        } else {
            selectedBoardCell = null;
        }
    }
    private void actualizarChatLog() {
        java.util.List<String> historial = board.getHistorialEventos();
        
        if (historial.size() > ultimoIndexMensaje) {
            StyledDocument doc = chatArea.getStyledDocument();
            
            Style estiloBlanco = chatArea.addStyle("Blanco", null);
            StyleConstants.setForeground(estiloBlanco, Color.WHITE);
            StyleConstants.setFontFamily(estiloBlanco, "SansSerif");
            StyleConstants.setFontSize(estiloBlanco, 12);
            
            Style estiloAzul = chatArea.addStyle("Azul", null);
            StyleConstants.setForeground(estiloAzul, new Color(100, 150, 255));
            StyleConstants.setBold(estiloAzul, true);
            
            Style estiloRojo = chatArea.addStyle("Rojo", null);
            StyleConstants.setForeground(estiloRojo, new Color(255, 100, 100));
            StyleConstants.setBold(estiloRojo, true);

            try {
                for (int i = ultimoIndexMensaje; i < historial.size(); i++) {
                    String linea = historial.get(i) + "\n";
                    
                    // Procesamiento simple de colores: busca palabras clave para pintar
                    String[] palabras = linea.split(" ");
                    for (String p : palabras) {
                        Style estiloActivo = estiloBlanco;
                        
                        if (p.contains("(AZUL)") || p.equals("Azul") || p.equals("AZUL")) {
                            estiloActivo = estiloAzul;
                        } else if (p.contains("(ROJO)") || p.equals("Rojo") || p.equals("ROJO")) {
                            estiloActivo = estiloRojo;
                        }
                        
                        doc.insertString(doc.getLength(), p + " ", estiloActivo);
                    }
                    // Salto de línea limpio al final de cada frase
                    doc.insertString(doc.getLength(), "\n", estiloBlanco);
                }
                ultimoIndexMensaje = historial.size();
                
                // Auto-scroll hacia abajo
                chatArea.setCaretPosition(doc.getLength());
            } catch (Exception e) {
                System.out.println("Error actualizando chat: " + e.getMessage());
            }
        }
    }
}