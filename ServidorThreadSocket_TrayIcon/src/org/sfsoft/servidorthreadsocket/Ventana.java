package org.sfsoft.servidorthreadsocket;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.awt.FlowLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class Ventana {

	private JFrame frmServidorChat;
	private JButton btParar;
	private JButton btIniciar;
	
	private Servidor servidor = null;
	private TrayIcon trayIcon;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frmServidorChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ventana() {
		initialize();
		inicializar();
	}
	
	private void inicializar() {
		
		servidor = new Servidor(7);
	
		// Crea el icono en la barra de tareas
		trayIcon = new TrayIcon(getImage("icono.jpg", "icono"));
		
		// Crea un popupmenu con dos acciones
		PopupMenu popup = new PopupMenu();
		MenuItem iniciarItem = new MenuItem("Iniciar");
		iniciarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iniciar();
			}
		});
		MenuItem pararItem = new MenuItem("Parar");
		pararItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parar();
			}
		});
		popup.add(iniciarItem);
		popup.add(pararItem);
		
		// Añade el popupMenu al icono de notificación
		trayIcon.setPopupMenu(popup);
		
		// Añade el icono de notificación en la barra de notificación
        try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException awte) {
			awte.printStackTrace();
		}
	}
	
	private Image getImage(String ruta, String descripcion) {
		
        URL url = Ventana.class.getResource(ruta);
         
        if (url == null) {
            System.err.println("Imagen no encontrada: " + ruta);
            return null;
        } else {
            return (new ImageIcon(url, descripcion)).getImage();
        }
	}
	
	private void iniciar() {
		
		Thread hiloServidor = new Thread(new Runnable() {
			public void run() {
				
				btIniciar.setEnabled(false);
				btParar.setEnabled(true);
				
				ConexionCliente cliente = null;
				
				try {
					// Inicia el servidor
					servidor.conectar();
					System.out.println("Servidor iniciado");
					// Mientras el servidor esté conectado aceptas nuevas
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
		});
		hiloServidor.start();
	}
	
	private void parar() {
		
		try {
			servidor.desconectar();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		btIniciar.setEnabled(true);
		btParar.setEnabled(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServidorChat = new JFrame();
		frmServidorChat.setTitle("Servidor Chat");
		frmServidorChat.setBounds(100, 100, 423, 89);
		frmServidorChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServidorChat.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		frmServidorChat.getContentPane().add(getBtIniciar());
		frmServidorChat.getContentPane().add(getBtParar());
	}

	public JButton getBtParar() {
		if (btParar == null) {
			btParar = new JButton("Parar");
			btParar.setEnabled(false);
			btParar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parar();
				}
			});
		}
		return btParar;
	}
	public JButton getBtIniciar() {
		if (btIniciar == null) {
			btIniciar = new JButton("Iniciar");
			btIniciar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					iniciar();
				}
			});
		}
		return btIniciar;
	}
}
