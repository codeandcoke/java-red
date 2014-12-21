package org.sfsoft.servidorFTP;

import java.io.File;

import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

/**
 * Ejemplo de servidor FTP en Java
 * utilizando la librería ftpserver de Apache
 * http://mina.apache.org/ftpserver-project/
 * 
 * Proporciona autenticación de usuarios basada en un fichero (usuarios.properties)
 * con las contraseñas cifradas mediante MD5
 * 
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class ServidorFTP {

	public static final int PUERTO = 21;
	
	public static void main(String args[]) {
		
		FtpServerFactory serverFactory = new FtpServerFactory();
	
		// Ajusta el puerto en el que escuchará el servidor
		ListenerFactory miListenerFactory = new ListenerFactory();
		miListenerFactory.setPort(PUERTO);
		serverFactory.addListener("default", miListenerFactory.createListener());
		
		try {
			/*
			 *  Fija la configuración del servidor
			 *  En este caso se habilita el uso de usuarios anónimos
			 */
			ConnectionConfigFactory miConnectionConfigFactory = new ConnectionConfigFactory();
			miConnectionConfigFactory.setAnonymousLoginEnabled(true);
			ConnectionConfig connectionConfig = miConnectionConfigFactory.createConnectionConfig();
			serverFactory.setConnectionConfig(connectionConfig);
		
			// Fija la configuración de las cuentas de usuario
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setFile(new File("usuarios.properties"));         
			serverFactory.setUserManager(userManagerFactory.createUserManager());
			
			// Crea el servidor FTP
			FtpServer servidorFtp = serverFactory.createServer();
	
			// Inicia el servidor FTP
			servidorFtp.start();
		} catch (FtpException fe) {
			fe.printStackTrace();
		}
	}
}
