package org.sfsoft.clientechat.gui;

import org.sfsoft.clientechat.utils.Constantes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Ventana principal del Cliente de Chat
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class JChat {
    private JPanel panel;
    private JList jlUsuarios;
    private JTextField tfMensaje;
    private JLabel lbEstado;
    private JTextArea taChat;

    private DefaultListModel mListaUsuarios;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private boolean conectado;
    private static final int PUERTO = 5000;


    public JChat() {
        tfMensaje.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    enviarMensaje();
            }
        });

        inicializar();
    }

    public JMenuBar getMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("JChat");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Conectar");
        menuItem.setAccelerator(KeyStroke.getKeyStroke("control C"));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                conectar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Desconectar");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                desconectar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Salir");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                salir();
            }
        });
        menu.add(menuItem);

        return menuBar;
    }

    private void inicializar() {
        mListaUsuarios = new DefaultListModel<>();
        jlUsuarios.setModel(mListaUsuarios);

        tfMensaje.requestFocus();
    }

    private void conectar() {

        final JConecta jConecta = new JConecta();

        if (jConecta.mostrarDialogo() == Constantes.Accion.CANCELAR)
            return;

        // Ejecuta el método en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String host = jConecta.getHost();
                conectarServidor(host);
            }
        });
    }

    private void conectarServidor(String servidor) {

        try {
            socket = new Socket(servidor, PUERTO);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            conectado = true;
            lbEstado.setText("Conectado");

                Thread hiloRecibir = new Thread(new Runnable() {
                    public void run() {
                        while (conectado) {
                            try {
                                if (socket.isClosed()) {
                                    conectado = false;
                                    break;
                                }
                                String mensaje = entrada.readLine();
                                if (mensaje == null)
                                    continue;

                                int indice = 0;
                                if (mensaje.startsWith("/server")) {
                                    indice = mensaje.indexOf(" ");
                                    taChat.append("** " + mensaje.substring(indice + 1) + " **\n");
                                } else if (mensaje.startsWith("/users")) {
                                    indice = mensaje.indexOf(" ", 7);
                                    String nick = mensaje.substring(7, indice);
                                    taChat.append("#" + nick + "# ");
                                    taChat.append(mensaje.substring(indice + 1) + "\n");
                                } else if (mensaje.startsWith("/nicks")) {
                                    String[] nicks = mensaje.split(",");
                                    mListaUsuarios.clear();
                                    for (int i = 1; i < nicks.length; i++) {
                                        mListaUsuarios.addElement(nicks[i]);
                                    }
                                }
                            } catch (SocketException se) {
                                desconectar();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    }
                });
                hiloRecibir.start();

        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void enviarMensaje() {

        String mensaje = tfMensaje.getText();
        salida.println(mensaje);

        tfMensaje.setText("");
    }

    private void desconectar() {
        try {
            salida.println("/quit");
            conectado = false;
            lbEstado.setText("Desconectado");
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void salir() {
        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JChat");
        JChat jChat = new JChat();
        frame.setJMenuBar(jChat.getMenuBar());
        frame.setContentPane(jChat.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
