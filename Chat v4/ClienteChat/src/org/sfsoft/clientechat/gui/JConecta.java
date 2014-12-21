package org.sfsoft.clientechat.gui;

import org.sfsoft.clientechat.utils.Constantes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Diálogo que muestra una caja de texto para introducir el host donde está
 * el servidor de Chat
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class JConecta extends JDialog {
    private JPanel panel1;
    private JTextField tfHost;
    private JButton btCancelar;
    private JButton btConectar;
    private JTextField tfUsuario;
    private JPasswordField tfContrasena;
    private JLabel lbEstado;

    private String host;
    private String usuario;
    private String contrasena;
    private Constantes.Accion accion;

    public JConecta() {

        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        btConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });

        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void aceptar() {
        accion = Constantes.Accion.ACEPTAR;

        host = tfHost.getText();
        usuario = tfUsuario.getText();
        contrasena = tfContrasena.getPassword().toString();

        if (host.equals("")) {
            lbEstado.setText("Introduce el host");
            return;
        }
        if (usuario.equals("") || contrasena.equals("")) {
            lbEstado.setText("Introduce el nombre de usuario y la contraseña");
            return;
        }

        setVisible(false);
    }

    private void cancelar() {
        accion = Constantes.Accion.CANCELAR;
        setVisible(false);
    }

    public String getHost() {
        return host;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Constantes.Accion mostrarDialogo() {

        setVisible(true);

        return accion;
    }
}
