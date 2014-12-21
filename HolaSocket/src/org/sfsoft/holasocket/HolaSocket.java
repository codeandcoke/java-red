package org.sfsoft.holasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Proyecto de prueba que utiliza un socket para conectar
 * con un servidor de echo en un equipo remoto
 * Un servidor de echo es un servicio que emite el mismo mensaje que se le envía
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class HolaSocket {

	public static void main(String args[]) {
		
		// Nombre del host remoto, donde haya disponible un servidor echo
		String hostname = "videosdeinformatica.com";
		// Puerto donde conectará el socket
		int puerto = 7;
		
		try {
			// Establece la conexión con el servicio remoto
			Socket socket = new Socket(hostname, puerto);
			// Establece el flujo de salida hacia el servicio remoto
			PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
			// Establece el flujo de entrada, desde el servicio remoto
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			
			String cadena = null;
			/*
			 * Captura por teclado una línea, la envía al servicio y lee la respuesta
			 * de éste
			 */
			while ((cadena = teclado.readLine()) != null) {
				// Envía el mensaje al servicio a través del socket
				salida.println(cadena);
				// Lee la respuesta del servicio a través del socket
				System.out.println(entrada.readLine());
			}
			
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
}
