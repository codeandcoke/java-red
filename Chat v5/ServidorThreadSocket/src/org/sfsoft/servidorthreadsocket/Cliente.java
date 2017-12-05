package org.sfsoft.servidorthreadsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Gestiona la comunicación con cada uno de los clientes
 * conectados al servidor
 * @author Santiago Faci
 *
 */
public class Cliente extends Thread {

	private Socket socket;
	private PrintWriter salida;
	private BufferedReader entrada;
	private Servidor servidor;
	private String nick;
	private boolean contestaPing;
	private boolean conectado;
	
	public Cliente(Socket socket, Servidor servidor) throws IOException {
		this.socket = socket;
		this.servidor = servidor;
		
		salida = new PrintWriter(socket.getOutputStream(), true);
		entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public PrintWriter getSalida() {
		return salida;
	}
	
	public void cambiarNick(String nick) {
		this.nick = nick;
	}
	
	public String getNick() {
		return nick;
	}
	
	private void desconectar() throws IOException {
		conectado = false;
		socket.close();
		servidor.eliminarCliente(this);
		servidor.enviarNicks();
	}
	
	@Override
	public void run() {
		System.out.println("Iniciando comunicación con el cliente");
		
		// Envía algunos mensajes al cliente en cuanto éste se conecta
		salida.println("/server Hola " + socket.getInetAddress().getHostName());
		salida.println("/server Escribe tu nick y pulsa enter");
		try {
			String nick = entrada.readLine();
			cambiarNick(nick);
			salida.println("/server Bienvenido " + nick);
			salida.println("/server Hay " + servidor.getNumeroClientes() + " usuarios conectados");
			salida.println("/server Cuando escribas '/quit', se terminará la conexión");
			
			servidor.anadirCliente(this);
			servidor.enviarNicks();
			conectado = true;
			
			Thread hiloPing = new Thread(new Runnable() {
				@Override
				public void run() {
					while (conectado) {
						
						contestaPing = false;
						salida.println("/ping");
						System.out.println("ping");
						
						try {
							Thread.sleep(Servidor.TIMEOUT);
						} catch (InterruptedException ie) {}
						
						if (!contestaPing)
							try {
								desconectar();
							} catch (IOException ioe) {
								ioe.printStackTrace();
							}
					}
				}
			});
			hiloPing.start();
		
			String linea = null;
			/*
			 * Espera la entrada por parte del cliente y actúa según
			 * su protocolo: Repetir los mensajes y si el cliente
			 * envía el caracter . salir
			 */
			while (conectado) {
				
				linea = entrada.readLine();
				
				if (linea.equals("/quit")) {
					salida.println("/server Saliendo . . .");
					// Cierra la conexión con el cliente
					desconectar();
					break;
				}
				else if (linea.equals("/pong")) {
					System.out.println("pong");
					contestaPing = true;
					continue;
				}
				
				servidor.enviarATodos("/users " + nick + " " + linea);
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
