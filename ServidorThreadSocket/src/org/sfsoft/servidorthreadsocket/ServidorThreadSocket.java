
package org.sfsoft.servidorthreadsocket;

import java.io.IOException;

/**
 * Ejemplo de Servidor con soporte para múltiples cliente
 * utilizando hilos
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class ServidorThreadSocket {

	public static void main(String args[]) {
		
		Servidor servidor = new Servidor(7);
		ConexionCliente cliente = null;
		
		try {
			// Inicia el servidor
			servidor.conectar();
			// Mientras el servidor está conectado aceptas nuevas
			// conexiones de clientes, que serán atendidas a través
			// de hilos
			while (servidor.estaConectado()) {
				cliente = new ConexionCliente(servidor.escuchar());
				System.out.println("Nuevo cliente conectado");
				cliente.start();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
