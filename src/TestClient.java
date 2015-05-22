import java.awt.image.BufferedImage;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.SMove;
import lenz.htw.kimpl.Move;
import lenz.htw.kimpl.Server;
import lenz.htw.kimpl.net.NetworkClient;


public class TestClient {
	public static void main(String[] args) {
//		Runnable serverRunner = new Runnable() {
//			
//			@Override
//			public void run() {
//				String[] args = {"800", "600", "4"};
//				Server.main(args);
//			}
//		};
//		
//		Thread serverThread = new Thread(serverRunner, "server");
//		serverThread.start();
		
		BufferedImage image = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		int round = 0;
		long [] field = GameFieldUtils.createInital();

		NClient n0 = new NClient("127.0.0.1", "jakob0", image);
		NClient n1 = new NClient("127.0.0.1", "none", image);
		NClient n2 = new NClient("127.0.0.1", "none", image);
		NClient n3 = new NClient("127.0.0.1", "none", image);
		
		NClient[] clients = {n0,n1,n2,n3};
		
		for (NClient nClient : clients) {
			Thread clientThread = new Thread(nClient, nClient.getName());
			clientThread.start();
		}
	}
	

}
