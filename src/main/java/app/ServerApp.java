package app;

import java.awt.Container;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import dao.ProductDao;
import dao.impl.ProductImpl;
import model.Product;

public class ServerApp extends JFrame {
	private static final long serialVersionUID = -6354598938656721068L;
	private JTextArea ta;
	private ProductDao productImpl;

	public ServerApp() {
		setTitle("Server");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		Container cp = getContentPane();
		cp.add(new JScrollPane(ta = new JTextArea()));

		productImpl = new ProductImpl();

		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(9080);

				SwingUtilities.invokeLater(() -> {
					ta.append("Server is started at: " + LocalTime.now());
				});

				while (true) {
					Socket socket = serverSocket.accept();
					SwingUtilities.invokeLater(() -> {
						ta.append("\nClient IP: " + socket.getInetAddress().getHostAddress());
						ta.append("\nClient Host name: " + socket.getInetAddress().getHostName());
					});

					ProductHandler productHandler = new ProductHandler(socket);
					Thread thread = new Thread(productHandler);
					thread.start();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ServerApp().setVisible(true);
		});
	}

	class ProductHandler implements Runnable {
		private Socket socket;
		private DataInputStream inputStream;
		private ObjectOutputStream objOutputStream;
		private List<Product> products;

		public ProductHandler(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				inputStream = new DataInputStream(socket.getInputStream());
				objOutputStream = new ObjectOutputStream(socket.getOutputStream());

				while (true) {
					//// Nhận từ client
					String title = inputStream.readUTF();
					SwingUtilities.invokeLater(() -> {
						ta.append("\n(ID Address : " + socket.getInetAddress().getHostAddress() + ") Title: " + title);
					});

					// Nhận dạng dữ liệu đầu vào
					String[] inputStrings = title.split(">");
					System.out.println(inputStrings[0]);
					System.out.println(inputStrings[1]);

					// Xử lý yêu cầu
					if (inputStrings[0].equals("1")) {
						List<Product> products = productImpl.getProducts(inputStrings[1]);

						if (products.size() == 0) {
							products = null;
							objOutputStream.writeObject(products);

						} else {
							objOutputStream.writeObject(products);
						}
					} else if (inputStrings[0].equals("2")) {

						List<Product> products = productImpl.getProductNotSold();
						System.out.println("tim sp chua ban");
						if (products.size() == 0) {
							products = null;
							objOutputStream.writeObject(products);

						} else {
							objOutputStream.writeObject(products);
						}
					} else if (inputStrings[0].equals("3")) {
						int total = 0;
						total = productImpl.getTotalOfProducts();
						
						objOutputStream.writeObject(total);

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
