import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class ClientGui extends JFrame implements ActionListener{

	public static void main(String[] args) {
		new ClientGui();
	}

	private JTextField ip;
	private JButton connect;
	private JTextField name;
	
	public ClientGui() {
		
		this.setMinimumSize(new Dimension(800, 400));
		this.setVisible(true);
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.add(new JLabel("IP:"));
		this.ip = new JTextField("127.0.0.1");
		this.add(ip);
		this.connect = new JButton("Connect!");
		this.connect.addActionListener(this);
		this.add(connect);
		this.add(new JLabel("Name:"));
		this.name = new JTextField("jakob");
		this.add(name);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		
//		if (e.getID() == e.)
		System.out.println(this.name.getText());
		
		NClient conn = new NClient(this.ip.getText(), this.name.getText(), NClient.getEmptyImage());
		conn.start();
		
	}
}
