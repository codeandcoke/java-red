package org.sfsoft.navegadorweb;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Navegador Web en Java
 * Ejemplo que utiliza JEditorPane para renderizar páginas web
 * Hay que modificar el ContentType del componente para que soporte html (text/html)
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 *
 */
public class Navegador {

	private JFrame frmNavegadorWebV;
	private JTextField tfDireccion;
	private JLabel lbEstado;
	private JEditorPane epPagina;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Navegador window = new Navegador();
					window.frmNavegadorWebV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Navegador() {
		initialize();
	}
	
	/*
	 * Prepara la dirección escrita por el usuario
	 * para cargarla en el JEditorPane
	 */
	private void cargarPagina() {
		
		// Añade http:// en la dirección si es necesario
		String direccion = tfDireccion.getText();
		if (!direccion.startsWith("http://"))
			direccion = "http://".concat(direccion);
		tfDireccion.setText(direccion);
		
		try {
			mostrarPagina(new URL(direccion));
		} catch (MalformedURLException murle) {
			lbEstado.setText("La dirección no es válida. Compruébala");
		}
	}
	
	/*
	 * Carga la página que indica la url en el JEditorPane
	 */
	private void mostrarPagina(final URL url) {
		
		Thread hiloCarga = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lbEstado.setText("Cargando . . .");
					
					// Carga la página en el JEditorPane
					epPagina.setPage(url);
					
					lbEstado.setText("Listo");
				} catch (IOException ioe) {
					lbEstado.setText("Se ha producido un error con la conexión. Inténtalo de nuevo");
				}	
			}
		});
		hiloCarga.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNavegadorWebV = new JFrame();
		frmNavegadorWebV.setTitle("Navegador Web v1");
		frmNavegadorWebV.setBounds(100, 100, 450, 300);
		frmNavegadorWebV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNavegadorWebV.getContentPane().add(getTfDireccion(), BorderLayout.NORTH);
		frmNavegadorWebV.getContentPane().add(getLbEstado(), BorderLayout.SOUTH);
		frmNavegadorWebV.getContentPane().add(getEpPagina(), BorderLayout.CENTER);
	}

	public JTextField getTfDireccion() {
		if (tfDireccion == null) {
			tfDireccion = new JTextField();
			tfDireccion.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						cargarPagina();
				}
			});
			tfDireccion.setColumns(10);
		}
		return tfDireccion;
	}
	public JLabel getLbEstado() {
		if (lbEstado == null) {
			lbEstado = new JLabel("Navegador Web v1");
		}
		return lbEstado;
	}
	public JEditorPane getEpPagina() {
		if (epPagina == null) {
			epPagina = new JEditorPane();
			epPagina.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent he) {
					// Si el usuario ha hecho click se muestra la p�gina de la url
					if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
						mostrarPagina(he.getURL());
				}
			});
			epPagina.setEditable(false);
			epPagina.setContentType("text/html");
		}
		return epPagina;
	}
}
