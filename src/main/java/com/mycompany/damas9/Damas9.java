/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
//Juego de Damas 6x6 en MVC
//Elaborado por Santino Piantini
//Ultima actualizacion 21/07/2024

package com.mycompany.damas9;

import Controlador.controlador;
import Modelo.modelo;
import Vista.vista;

/**
 *
 * @author santino
 */
public class Damas9 {

    public static void main(String[] args) {
        modelo modelo = new modelo();        
        vista vista = new vista();
        controlador ctr1 = new controlador(vista, modelo);  
        
        vista.setVisible(true);
    }
}
