package Controlador;

import Modelo.modelo;
import Vista.vista;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Controlador para el juego de damas.
 */
public class controlador implements ActionListener {
    
    private vista view;
    private modelo model;
    private boolean seleccionadoOrigen = true;
    private int origenI = -1;
    private int origenJ = -1;
    private int turnPlayer=0;
    private int fichaNegraComida=0;
    private int fichaBlancaComida=0;

    
    
    public controlador(vista view, modelo model) {
        this.view = view;
        this.model = model;
        this.view.setVisible(true);
        this.view.jbIniciar.addActionListener(this);
        this.view.jbReiniciar.addActionListener(this);
        
        view.iniciarJuego();
        iniciarJuego();
        // Agregar ActionListener a todos los botones del tablero para que respondan a los clics
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                view.tablero[i][j].addActionListener(this);
            }
        }
        view.setVisible(true);
    }
    
    
    
    //Crea botones JButton para cada celda del tablero, establece su posición y los agrega al panel jpTablero
    public void iniciarJuego(){
        int x=0;
        int y=0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                view.tablero[i][j] = new JButton();
                view.tablero[i][j].setBounds(y, x, 100, 100);
                view.jpTablero.add(view.tablero[i][j] );
                // identificar el botón por sus coordenadas
                view.tablero[i][j].setActionCommand(i + "," + j);
                x+=100;
            }
            x=0;
            y+=100;
        }
        Color beige = new Color(253, 242, 233);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i + j) % 2 == 0) {
                    view.tablero[i][j].setBackground(beige);
                } else {
                    view.tablero[i][j].setBackground(Color.red);
                }
            }
        }
             
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // Rutas de las imágenes de las fichas
        String rutaFichaBlanca = "C:\\Users\\santi\\OneDrive\\Desktop\\Facultad\\A Primero\\Segundo Semestre\\Laboratorio 2\\Damas9\\src\\main\\java\\ImagenesDamas\\FichaBlanca.png";
        String rutaFichaNegra = "C:\\Users\\santi\\OneDrive\\Desktop\\Facultad\\A Primero\\Segundo Semestre\\Laboratorio 2\\Damas9\\src\\main\\java\\ImagenesDamas\\FichaNegra.png";

        // Cargar imágenes y escalarlas
        ImageIcon iconoFichaBlanca = escalarImagen(rutaFichaBlanca);
        ImageIcon iconoFichaNegra = escalarImagen(rutaFichaNegra);
        
        // Manejar eventos según el origen del evento
        if (e.getSource() == view.jbIniciar || e.getSource() == view.jbReiniciar) {
            view.jpTurn.setBackground(Color.BLACK); 
            view.jpWinner.setBackground(new Color(0,51,102));
            fichaNegraComida=0;
            fichaBlancaComida=0;
            //Primero se debe resetear en caso de que el usuario haya jugado una partida anteriormente 
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    view.tablero[i][j].setIcon(null);
                }
            }
            ordenarFichas(iconoFichaBlanca, iconoFichaNegra);
        } else {
            procesarSeleccionTablero(e, iconoFichaBlanca, iconoFichaNegra);
        }
    }
    
    // Método para escalar una imagen y devolver un ImageIcon
    private ImageIcon escalarImagen(String rutaImagen) {
        ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }
    
    
    
    // Método para posicionar las fichas iniciales en el tablero
    private void ordenarFichas(ImageIcon iconoFichaBlanca, ImageIcon iconoFichaNegra) {        
        for (int i = 0; i < 6; i+=2) {
            view.tablero[i][0].setIcon(iconoFichaNegra);
        }
        for (int i = 1; i < 6; i+=2) {
            view.tablero[i][1].setIcon(iconoFichaNegra);
        }
        for (int i = 0; i < 6; i+=2) {
            view.tablero[i][4].setIcon(iconoFichaBlanca);
        }
        for (int i = 1; i < 6; i+=2) {
            view.tablero[i][5].setIcon(iconoFichaBlanca);
        }
    }
    
    
    
    // Método para procesar la selección de un botón en el tablero
    private void procesarSeleccionTablero(ActionEvent e, ImageIcon iconoFichaBlanca, ImageIcon iconoFichaNegra) {
        
        // Extraer la posición del botón presionado desde su comando de acción "i,j"
        String actionCommand = e.getActionCommand();
        if (actionCommand != null && !actionCommand.isEmpty()) {
            // Divide el comando de acción en partes usando la coma como delimitador
            String[] parts = actionCommand.split(",");
            if (parts.length == 2) {
                try {
                    int i = Integer.parseInt(parts[0]);
                    int j = Integer.parseInt(parts[1]);
                    // Si seleccionadoOrigen es true, significa que es el primer clic y el usuario está seleccionando una ficha.
                    if (seleccionadoOrigen) {
                        // Guarda las coordenadas de la ficha seleccionada en origenI y origenJ
                        origenI = i;
                        origenJ = j;
                        seleccionadoOrigen = false;
                    } else {
                        // Segundo clic selecciona el destino de la ficha
                        // Establece seleccionadoOrigen a true para volver al estado inicial para la próxima selección
                        seleccionadoOrigen = true;
                        verificarMovimiento(origenI, origenJ, i, j, iconoFichaBlanca, iconoFichaNegra);
                    }

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    
    
    // Metodo para verificar si el movimiento es valido 
    private void verificarMovimiento(int origenI, int origenJ, int destinoI, int destinoJ, ImageIcon iconoFichaBlanca, ImageIcon iconoFichaNegra) {
        JButton botonPresionado = view.tablero[origenI][origenJ]; //Obtiene el boton de la posicion de origen 
        Icon iconoActual = botonPresionado.getIcon(); //Obtiene el icono del boton de origen (el icono es la ficha)
        JButton botonDestino = view.tablero[destinoI][destinoJ];//Obtiene el boton de la posicion de destino
        Icon iconoDestino = botonDestino.getIcon();//Obtiene el icono del boton de destino (el icono es la ficha)
        
        //Verifico que haya una ficha en la posición de origen y que la posición de destino esté vacía
        if (iconoActual != null && iconoDestino == null) { 
            //determina el tipo de ficha de la posicion de origen. Recibe 1 si es blanca. Recibe 2 si es negra
            int n = model.imagenesIguales(((ImageIcon) iconoActual).getImage(), iconoFichaBlanca.getImage(), iconoFichaNegra.getImage());
            //Verifica de que jugador es turno 
            if (turnPlayer==0) {
                if (n==2) {
                    movimientoDestinoNegra(origenI, origenJ, destinoI, destinoJ, iconoFichaNegra, iconoFichaBlanca);
                } 
            }else if(turnPlayer==1){
                if (n==1) {
                    movimientoDestinoBlanca(origenI, origenJ, destinoI, destinoJ, iconoFichaBlanca, iconoFichaNegra);
                }
            }    
        }else if (iconoActual == null){
            System.out.println("No hay ficha en la posición de origen");
        } else if (iconoDestino != null){
            System.out.println("Ya hay una ficha en la posicion de destino");
        }
            
    }

    
    
    private void movimientoDestinoNegra(int origenI, int origenJ, int destinoI, int destinoJ, ImageIcon iconoFichaNegra, ImageIcon iconoFichaBlanca ) {        
        
        // Se verifica si el movimiendo es un movimiento valido
        int e = model.verificarDestino(origenI, origenJ, destinoI, destinoJ);

        // Si e es 1, entonces el movimiento es válido
        if (e == 1) {
            turnPlayer=1; //Actualizo el turno
            //Actualizo el tablero moviendo la ficha
            view.tablero[destinoI][destinoJ].setIcon(iconoFichaNegra);
            view.tablero[origenI][origenJ].setIcon(null);
            view.jpTurn.setBackground(Color.WHITE);
            // Verificar y realiza la captura de una ficha blanca si es posible
            comerFichaBlanca(origenI, origenJ, destinoI, destinoJ, iconoFichaNegra, iconoFichaBlanca);
        } else {
            System.out.println("Movimiento invalido");
        }
        view.setVisible(true);
    }

    private void movimientoDestinoBlanca(int origenI, int origenJ, int destinoI, int destinoJ, ImageIcon iconoFichaBlanca, ImageIcon iconoFichaNegra) {
        
        // Se verifica si el movimiendo es un movimiento valido
        int e = model.verificarDestino(origenI, origenJ, destinoI, destinoJ);

        // Si e es 1, entonces el movimiento es válido
        if (e == 1) {
            turnPlayer=0;//Actualizo el turno
            // Actualizar el tablero moviendo la ficha negra
            view.tablero[destinoI][destinoJ].setIcon(iconoFichaBlanca);
            view.tablero[origenI][origenJ].setIcon(null);
            view.jpTurn.setBackground(Color.BLACK); 
            // Verificar y realiza la captura de una ficha blanca si es posible
            comerFichaNegra(origenI, origenJ, destinoI, destinoJ, iconoFichaNegra, iconoFichaBlanca);
        }else {
            System.out.println("Movimiento invalido");
        }
        view.setVisible(true);
    }
    
    
    
    private void comerFichaBlanca(int origenI, int origenJ, int destinoI, int destinoJ, ImageIcon imgFichaNegra, ImageIcon imgFichaBlanca) {
        
        // Verifica la posibilidad de comer una ficha
        int comer = model.comerFicha(origenI, origenJ, destinoI, destinoJ);

        int fin; // Va a representar el número de celdas que la ficha ha recorrido desde su posición de origen hasta la posición de destino
        
        //Dependiendo del valor de comer, verifica y captura fichas blancas en la trayectoria del movimiento:
        switch (comer) {
            case 1: //Movimiento diagonal izquierda arriba
                fin = origenJ - destinoJ;
                //Iterar sobre todas las celdas en la trayectoria dle movimiento, menos la celda de origen
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI - i][origenJ - i]; 
                    Icon iconoActual = botonPresionado.getIcon(); // Obtengo el icono del boton 
                    //Compruebo si hay una ficha en la celda actual
                    if (iconoActual != null) { 
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal izquierda arriba
                        if (model.imagenesIguales(imgIconoActual, imgFichaBlanca.getImage(), imgFichaNegra.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI - i][origenJ - i].setIcon(null);
                            // Sumo una ficha comida. Su funcion es al momento de verificar un ganador 
                            fichaBlancaComida++;
                        }
                    }
                }
                break;
            case 2: //Movimiento diagonal derecha abajo
                fin = destinoJ - origenJ;
                //Iterar sobre todas las celdas en la trayectoria dle movimiento, menos la celda de origen
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI + i][origenJ + i];
                    Icon iconoActual = botonPresionado.getIcon();// Obtengo el icono del boton 
                    //Compruebo si hay una ficha en la celda actual
                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal derecha abajo
                        if (model.imagenesIguales(imgIconoActual, imgFichaBlanca.getImage(), imgFichaNegra.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI + i][origenJ + i].setIcon(null);
                            // Sumo una ficha comida. Su funcion es al momento de verificar un ganador 
                            fichaBlancaComida++;
                        }
                    }
                }
                break;
            case 3: //Movimiento izquierda derecha abajo
                fin = destinoJ - origenJ;
                //Iterar sobre todas las celdas en la trayectoria dle movimiento, menos la celda de origen
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI - i][origenJ + i];
                    Icon iconoActual = botonPresionado.getIcon();// Obtengo el icono del boton 
                    //Compruebo si hay una ficha en la celda actual
                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();
                        // Verificar si hay una ficha blanca en la diagonal izquierda abajo
                        if (model.imagenesIguales(imgIconoActual, imgFichaBlanca.getImage(), imgFichaNegra.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI - i][origenJ + i].setIcon(null);
                            // Sumo una ficha comida. Su funcion es al momento de verificar un ganador 
                            fichaBlancaComida++;
                        }
                    }

                }
                break;
            case 4: //Movimiento diagonal derecha arriba
                fin = origenJ - destinoJ;
                //Iterar sobre todas las celdas en la trayectoria dle movimiento, menos la celda de origen
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI + i][origenJ - i];
                    Icon iconoActual = botonPresionado.getIcon();// Obtengo el icono del boton 
                    //Compruebo si hay una ficha en la celda actual
                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();
                        // Verificar si hay una ficha blanca en la diagonal derecha arriba
                        if (model.imagenesIguales(imgIconoActual, imgFichaBlanca.getImage(), imgFichaNegra.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI + i][origenJ - i].setIcon(null);
                            // Sumo una ficha comida. Su funcion es al momento de verificar un ganador 
                            fichaBlancaComida++;
                        }
                    }
                }
                break;
        }
        if (model.verificarGanador(fichaBlancaComida, fichaNegraComida)==1) { // 2 indica que se eliminó una ficha blanca
            System.out.println("Ganador fichas Negras");
            view.jpWinner.setBackground(Color.BLACK);
        }
    }

    private void comerFichaNegra(int origenI, int origenJ, int destinoI, int destinoJ, ImageIcon imgFichaNegra, ImageIcon imgFichaBlanca) {
        
        int comer = model.comerFicha(origenI, origenJ, destinoI, destinoJ);

        int fin;
        switch (comer) {
            case 1:
                fin = origenJ - destinoJ;
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI - i][origenJ - i];
                    Icon iconoActual = botonPresionado.getIcon();
                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal izquierda arriba
                        if (model.imagenesIguales(imgIconoActual, imgFichaNegra.getImage(), imgFichaBlanca.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI - i][origenJ - i].setIcon(null);
                            fichaNegraComida++;
                        }
                    }
                }
                break;
            case 2:
                fin = destinoJ - origenJ;
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI + i][origenJ + i];
                    Icon iconoActual = botonPresionado.getIcon();
                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal derecha abajo
                        if (model.imagenesIguales(imgIconoActual, imgFichaNegra.getImage(), imgFichaBlanca.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI + i][origenJ + i].setIcon(null);
                            fichaNegraComida++;
                        }
                    }
                }
                break;
            case 3:
                fin = destinoJ - origenJ;
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI - i][origenJ + i];
                    Icon iconoActual = botonPresionado.getIcon();

                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal izquierda abajo
                        if (model.imagenesIguales(imgIconoActual, imgFichaNegra.getImage(), imgFichaBlanca.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI - i][origenJ + i].setIcon(null);
                            fichaNegraComida++;
                        }
                    }
                }
                break;
            case 4:
                fin = origenJ - destinoJ;
                for (int i = 1; i < fin; i++) {
                    JButton botonPresionado = view.tablero[origenI + i][origenJ - i];
                    Icon iconoActual = botonPresionado.getIcon();

                    if (iconoActual != null) {
                        // Convertir Icon a Image para la comparación
                        Image imgIconoActual = ((ImageIcon) iconoActual).getImage();

                        // Verificar si hay una ficha blanca en la diagonal derecha arriba
                        if (model.imagenesIguales(imgIconoActual, imgFichaNegra.getImage(), imgFichaBlanca.getImage()) == 1) {
                            // Comer la ficha blanca
                            view.tablero[origenI + i][origenJ - i].setIcon(null);
                            fichaNegraComida++;
                        }
                    }
                }
                break;
        }
        
        if (model.verificarGanador(fichaBlancaComida, fichaNegraComida)==2) { // 1 indica que se eliminó una ficha negra
            System.out.println("Ganador fichas Blancas");
            view.jpWinner.setBackground(Color.WHITE);
        }

    }
    
}



