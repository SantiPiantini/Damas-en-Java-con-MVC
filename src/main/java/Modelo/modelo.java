package Modelo;

import Controlador.controlador;
import Vista.vista;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Clase modelo para el juego de damas.
 */
public class modelo {
    private vista view;
    private controlador ctrl;

    public int verificarDestino(int origenI, int origenJ, int destinoI, int destinoJ) {
        int e = 0;
        
        // Verificar todos los posibles movimientos (solo movimientos diagonales)
        for (int t = 1; t <= 5; t++) {
            if (destinoI == origenI + t && destinoJ == origenJ + t) e = 1;
            if (destinoI == origenI - t && destinoJ == origenJ - t) e = 1;
            if (destinoI == origenI + t && destinoJ == origenJ - t) e = 1;
            if (destinoI == origenI - t && destinoJ == origenJ + t) e = 1;
        }
        
        // Movimiento vertical simple invalido
        if ((destinoI == origenI + 1 && destinoJ == origenJ) || (destinoI == origenI - 1 && destinoJ == origenJ)) {
            e = 0;
        }
        
        // Movimiento horizontal simple invalido
        for (int t = 1; t <= 6; t++) {
            if (destinoI == origenI && (destinoJ == origenJ + t || destinoJ == origenJ - t)) {
                e = 0;
            }
        }
        
        //Devuelvo 1 si es un movimiento valido
        return e;
    }
    
    public int comerFicha(int origenI, int origenJ, int destinoI, int destinoJ){
        int comer=0;
        // Diagonal izquierda arriba
        if ((destinoJ < origenJ) && (destinoI < origenI)) {
            comer = 1;
        }
        // Diagonal derecha abajo
        if (destinoJ > origenJ && destinoI > origenI) {
            comer = 2;
        }
        // Diagonal izquierda abajo
        if (destinoJ > origenJ && destinoI < origenI) {
            comer = 3;
        }
        // Diagonal derecha arriba
        if (destinoJ < origenJ && destinoI > origenI) {
            comer = 4;
        }
        
        //Dependiendo en que direccion fue el movimiento la logica para comer ficha va a ser diferente
        return comer;
    }
    
    //Metodo para comparar las imagenes pixel por 
    public int imagenesIguales(Image imgActual, Image imgBlanca, Image imgNegra) {
        // Convertir las imágenes a BufferedImage para poder comparar píxeles
        BufferedImage bufferedImg1 = aBufferedImage(imgActual);
        BufferedImage bufferedImg2 = aBufferedImage(imgBlanca);
        BufferedImage bufferedImg3 = aBufferedImage(imgNegra);

        // Comparar píxel por píxel 
        if (bufferedImagesSonIguales(bufferedImg1, bufferedImg2)) { //Compara píxel por píxel imgActual con imgBlanca 
            return 1; // Ficha blanca
            
        } else if (bufferedImagesSonIguales(bufferedImg1, bufferedImg3)) { //Si no son iguales, compara imgActual con imgNegra
            return 2; // Ficha negra
        } else {
            return 0; // No se reconoce la ficha
        }
    }

    // Método para convertir Image a BufferedImage (Tipo de imagen que se puede manipular pixel por pixel)
    private BufferedImage aBufferedImage(Image img) {
        
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Si no es un BufferedImage, crea una nueva BufferedImage del mismo tamaño que la imagen original.
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    // Método para comparar dos BufferedImage píxel por píxel
    private boolean bufferedImagesSonIguales(BufferedImage img1, BufferedImage img2) {
        //Verifica si las dos imágenes tienen el mismo ancho y alto. Si no es así, retorna false.
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        //Compara cada picel de las dos imagenes
        //Si todos los píxeles son iguales, retorna true. Si encuentra algún píxel diferente, retorna false
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public int verificarGanador(int fichaBlancaComida, int fichaNegraComida) {
        //Recibo los dos contadores, los cuales contienen las cantidades de fichas blancas y negras comidas.
        //Si alguno de los dos equipos comio 6 fichas, significa que gano la partida
        if (fichaNegraComida==6) {
            return 2;
        }else if(fichaBlancaComida==6){
            return 1;
        }else{
            return 0;
        }
    }

}
