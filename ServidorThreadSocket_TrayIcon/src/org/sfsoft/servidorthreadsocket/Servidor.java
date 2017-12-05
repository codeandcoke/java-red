package org.sfsoft.servidorthreadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Representa al servidor echo
 * @author Santiago Faci
 *
 */
public class Servidor {

	private int puerto;
	private ServerSocket socket;
	
	public Servidor(int puerto) {
		this.puerto = puerto;
	}
	
	/**
	 * Indica si el servidor está conectado
	 * @return
	 */
	public boolean estaConectado() {
		return !socket.isClosed();
	}
	
	/**
	 * Inicia el servidor
	 */
	public void conectar() throws IOException {
		socket = new ServerSocket(puerto);
	}
	
	/**
	 * Desconecta el servidor
	 * @throws IOException
	 */
	public void desconectar() throws IOException {
		socket.close();
	}
	
	/**
	 * Escucha la conexión de un cliente
	 * @return El socket de conexión con el cliente
	 * @throws IOException
	 */
	public Socket escuchar() throws IOException {
		return socket.accept();
	}
}
