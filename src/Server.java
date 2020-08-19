

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;





public class Server {
	ServerSocketChannel serversocket;
	Selector selector ;
	ByteBuffer buffer = ByteBuffer.allocate(256);

	public Server(int Port) throws Throwable 
	{
		/*InetSocketAddress address= new InetSocketAddress("localhost",Port);
		 serversocket.bind(address);*/
		serversocket=ServerSocketChannel.open();
		serversocket.configureBlocking(false);
		//serversocket.socket().bind(new InetSocketAddress(Port));
		serversocket.bind(new InetSocketAddress(Port));
		selector=Selector.open();
		serversocket.register(selector, SelectionKey.OP_ACCEPT);
		
	}
	
	/*void accept() throws IOException 
	{
		SocketChannel clientsocket=serversocket.accept();
		clientsocket.configureBlocking(false);
		clientsocket.register(selector, SelectionKey.OP_READ);
		System.out.println("New client ip=" + clientsocket.getRemoteAddress());
	}
	
	void repeat(SelectionKey key) 
	{
		
	}*/
    
	
	void serverwork() throws IOException
	{
		System.out.println("server is running on port : 1337");
		SelectionKey key;
		Iterator<SelectionKey>iter;
    	while (serversocket.isOpen()) 
    	{
    		selector.select();
    		iter=selector.selectedKeys().iterator();
    		while (iter.hasNext()) 
    		{
    			key=iter.next();
    			iter.remove();
    			if(key.isAcceptable()) 
    				handleAccept(key); // accept la connextion
				if(key.isReadable())
					handleRead(key);   // faire lectuere des messages
    		}
    	}
	}
	
	private final ByteBuffer welcomeBuf = ByteBuffer.wrap("Welcome to NioChat!\n".getBytes());
	private void handleAccept(SelectionKey key) throws IOException {
		
		SocketChannel ClientSocket = ((ServerSocketChannel) key.channel()).accept();
		// registre the ip @ and port number of the client
		String address = (new StringBuilder( ClientSocket.socket().getInetAddress().toString() )).append(":").append( ClientSocket.socket().getPort() ).toString();
		ClientSocket.configureBlocking(false);
		ClientSocket.register(selector, SelectionKey.OP_READ, address); // registre the client socketc in selector 
		ClientSocket.write(welcomeBuf); // stocke the message of the client in the biffer "welcomebuffer"
		//Invoke rewind method before a sequence of channel-write or get
		welcomeBuf.rewind();
		System.out.println("accepted connection from: "+address);
	}
	
	private void handleRead(SelectionKey key) throws IOException {
		SocketChannel ch = (SocketChannel) key.channel();
		StringBuilder sb = new StringBuilder();

		buffer.clear();
		int read = 0;
		    // in this boucle we read data from
			try {
				while( (read = ch.read(buffer)) > 0 ) {  // read from channel and store in the buffer
				buffer.flip();// prepare the buffer to be availble to reading
				byte[] bytes = new byte[buffer.limit()]; // create a tab of byte
				buffer.get(bytes);// read from buffer and store in the table
				sb.append(new String(bytes));
				buffer.clear();// prepare the buffre to be availbe to writhing
   }
			} catch (Exception e) { key.cancel(); read = -1; } 
   
		String msg;
		if(read<0) {
			msg = key.attachment()+" left the chat.\n";
			ch.close();
		}
		else {
			msg = key.attachment()+": "+sb.toString(); // mesg = what we has readed from the channel
		}

		System.out.println(msg);// affiche the message 
		broadcast(msg); // diffuse the message
	}

	private void broadcast(String msg) throws IOException {
		ByteBuffer msgBuf=ByteBuffer.wrap(msg.getBytes());
		for(SelectionKey key : selector.keys()) {
			if(key.isValid() && key.channel() instanceof SocketChannel) {
				SocketChannel sch=(SocketChannel) key.channel();
				sch.write(msgBuf);
				msgBuf.rewind();
			}
		}
	}
	}
