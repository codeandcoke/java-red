package org.sfsoft.servidorsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Ejemplo de aplicación que funciona como servidor
 * Funciona como un servidor echo: 
 * 	Recibe mensajes y los reenvía al cliente
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class ServidorSocket {

	public static void main(String args[]) {
		
		int puerto = 7;
		
		try {
			// Se inicia el servidor en el equipo local en el puerto indicado
			ServerSocket socketServidor = new ServerSocket(puerto);
			// Espera la conexión con un cliente
			Socket socketCliente = socketServidor.accept();
			
			// Establece los flujos de salida y entrada (desde y hacia el cliente, respectivamente)
			PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			
			// Envía algunos mensajes al cliente en cuanto éste se conecta
			salida.println("Hola " + socketCliente.getInetAddress().getHostAddress());
			salida.println("Sólo sé repetir lo que me escribas");
			salida.println("Cuando escribas '.', se terminará la conexión");
			
			String linea = null;
			/*
			 * Espera la entrada por parte del cliente y actúa según
			 * su protocolo: Repetir los mensajes y si el cliente
			 * envía el caracter . salir
			 */
			while ((linea = entrada.readLine()) != null) {
				
				if (linea.equals(".")) {
					salida.println("Saliendo . . .");
					// Cierra la conexión con el cliente
					socketCliente.close();
					// Para el servidor
					socketServidor.close();
					break;
				}
				
				salida.println("Has escrito: " + linea);
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
