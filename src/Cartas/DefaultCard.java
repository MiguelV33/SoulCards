package Cartas;

import java.awt.Image;
import javax.swing.ImageIcon;
import Board.Board;

public abstract class DefaultCard {

    private String nombre;
    private int vida;
    private int vidaMaxima;
    private int ataque;
    private int defensa;
    private String equipo;
    private Image imagenPersonaje;
    private int bonoDefensaTurno = 0;
    private String descripcionHabilidad = "Unidad básica de combate.\nSin habilidad especial.";

    public String getDescripcionHabilidad() {
        return descripcionHabilidad;
    }

    public void setDescripcionHabilidad(String descripcionHabilidad) {
        this.descripcionHabilidad = descripcionHabilidad;
    }
    
    public DefaultCard(String nombre, int vida, int ataque, int defensa, String rutaPersonaje) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defensa = defensa;

        try {
            if (rutaPersonaje != null) {
                this.imagenPersonaje = new ImageIcon(getClass().getResource(rutaPersonaje)).getImage();
            }
        } catch (Exception e) {
            System.out.println("Error cargando la imagen " + nombre + ": " + e.getMessage());
        }
    }

    public void ejecutarHabilidadCombate(Board board, int fila, int col) {
        
    }

    public int getBonoAtaqueCarril(Board board, int fila, int col) {
        return 0;
    }

    public boolean intentarAnularAtaque(Board board, int filaBufon, int colBufon, DefaultCard atacante) {
        return false;
    }

    public boolean intentarAbsorberAtaque(Board board, int filaCaballero, int colCaballero, int filaObjetivo, DefaultCard atacante) {
        return false;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = Math.max(0, Math.min(vida, vidaMaxima)); }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = Math.max(0, ataque); }
    public void setDefensa(int defensa) { this.defensa = Math.max(0, defensa); }
    public void modificarDefensa(int cantidad) { this.defensa = Math.max(0, this.defensa + cantidad); }

   
    public void recibirDanioDirecto(int danio) {
        if (danio > 0) {
            this.vida = Math.max(0, this.vida - danio);
        }
    }

    public boolean estaViva() { return this.vida > 0; }
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public Image getImagenPersonaje() { return imagenPersonaje; }
    
    
    public void aplicarBufos(Board board, int fila, int col) {}
    public void aplicarAtaquesEspeciales(Board board, int fila, int col) {}

    public void agregarBonoDefensa(int cantidad) {
        this.bonoDefensaTurno += cantidad;
    }

    public void limpiarBufosTurno() {
        this.bonoDefensaTurno = 0;
    }

 
    public int getDefensa() { 
        return this.defensa + this.bonoDefensaTurno; 
    }

    
    public void recibirDanio(int danio) {
        if (danio <= 0) return;
        
    
        if (this.bonoDefensaTurno >= danio) {
            this.bonoDefensaTurno -= danio;
            return;
        } else {
            danio -= this.bonoDefensaTurno;
            this.bonoDefensaTurno = 0;
        }


        if (this.defensa >= danio) {
            this.defensa -= danio;
        } else {
            int danioRestante = danio - this.defensa;
            this.defensa = 0;
            this.vida = Math.max(0, this.vida - danioRestante);
        }
    }
}