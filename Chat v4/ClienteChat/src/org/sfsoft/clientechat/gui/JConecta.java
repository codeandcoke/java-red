package org.sfsoft.clientechat.gui;

import org.sfsoft.clientechat.utils.Constantes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
    private JLabel lbEstado;

    private String host;
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
        tfHost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    btConectar.doClick();
            }
        });
    }


    private void aceptar() {
        accion = Constantes.Accion.ACEPTAR;

        host = tfHost.getText();
        if (host.equals("")) {
            lbEstado.setText("Introduce el host");
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

    public Constantes.Accion mostrarDialogo() {

        setVisible(true);

        return accion;
    }
}
