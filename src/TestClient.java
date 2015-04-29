import java.awt.image.BufferedImage;

import lenz.htw.kimpl.net.NetworkClient;


public class TestClient {
	public static void main(String[] args) {
		NetworkClient nc = new NetworkClient("127.0.0.1", "test", new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB));
	}

}
