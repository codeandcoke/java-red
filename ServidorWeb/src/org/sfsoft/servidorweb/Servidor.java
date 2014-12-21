package org.sfsoft.servidorweb;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Ejemplo Java de Servidor Web
 * 
 * En esta clase se reciben las conexiones y se instancia un objeto para atender
 * cada una de esas conexiones
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 *
 */
public class Servidor {

	public static final int PUERTO = 80;
	
	public static void main(String args[]) {
		
		boolean conectado = true;
		ServerSocket servidor = null;
		try {
			servidor = new ServerSocket(80);
			System.out.println("Servidor iniciado");
			while (conectado) {
				
				Cliente cliente = new Cliente(servidor.accept());
				cliente.start();
				System.out.println("Iniciando comunicación con un cliente . . .");
			}
			
			if (servidor != null)
				servidor.close();
 		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (servidor != null)
					servidor.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
