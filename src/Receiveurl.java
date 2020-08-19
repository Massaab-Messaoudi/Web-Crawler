

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class Receiveurl
 */
@WebServlet("/Receiveurl")
public class Receiveurl extends HttpServlet {

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// fetch the data comming frmo the server
		String url=request.getParameter("URL_param");
		System.out.println(url);
		
		
		  Server server = null;
		try {
			server = new Server(1337);
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      server.serverwork();
	      
	      response.sendRedirect("wait_finish.jsp");
	}
 
	

}
