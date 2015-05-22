import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import lenz.htw.kimpl.Move;
import lenz.htw.kimpl.net.NetworkClient;
import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.GameFieldUtils.EvalStrategy;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.SMove;
import de.htw.jschmolling.ai.Search;
import de.htw.jschmolling.ai.Search.OrderStrategy;


public class NClient implements Runnable {

	private Players currentPlayer;
	private Players localPlayer;
	private long[] field;
	private NetworkClient nc;
	private String name;
	private long msTimeLimit = 0;
	
	private final String ip;
	private final String basicName;
	private final BufferedImage image;
	
	private final Search searcher;

	public NClient(String ip, String name, BufferedImage image) {
		this.ip = ip;
		this.basicName = name;
		this.image = image;
		this.name = name;
		
		this.searcher = new Search(EvalStrategy.HIT_FIRST, OrderStrategy.RANDOM);
	}

	private void init() {
		System.out.println(name + ": waiting for start");
		nc = new NetworkClient(ip, name, image);
		msTimeLimit = TimeUnit.SECONDS.toMillis(nc.getTimeLimitInSeconds());
		System.out.println("connected to " + nc);
		
		int serverPlayerNumber = nc.getMyPlayerNumber();
		localPlayer = Players.fromServerNumber(serverPlayerNumber);
		this.name = name + ":" + localPlayer.toString();

		System.out.println(this.name + ".playerNumber= " + nc.getMyPlayerNumber());
		currentPlayer = Players.fromServerNumber(0);
		field = GameFieldUtils.createInital();
		
		this.searcher.setTimeLimit(msTimeLimit);
	}

	@Override
	public void run() {
		init();
		
		Move nextMove = null;
		int move = GameFieldUtils.INVALID_POSITION;
		int round = 0;
		
		do {
//			
			
			nextMove = nc.receiveMove();
			if (nextMove != null){
				move = SMove.fromObjectMove(field, nextMove);
				currentPlayer = Players.values()[GameFieldUtils.getPlayerNumber(field, SMove.from(move))];
				System.out.println(name + ":recieved move: " + SMove.toString(move));
			} else {
				currentPlayer = localPlayer;
				searcher.setTimeLimit(msTimeLimit);
				
				move = searcher.getBestMove(field, currentPlayer);
				if(basicName == "none"){
					move = getFirstMove(field, currentPlayer);
				}
				
				System.out.println(name + "-----new move-----");
//				System.out.println(GameFieldUtils.toString(field));
				System.out.println(localPlayer + " move: " + SMove.toString(move));
				
				nextMove = SMove.getMoveObject(move);
				System.out.println(name + ": sending move " + SMove.toString(move));
				nc.sendMove(nextMove);
				Move confirmation = nc.receiveMove();	
				if (isEmptyServerMove(confirmation)){
					System.out.println(name + ": confirmation empty " + confirmation);
					break;
				}
				System.out.println("----------->");
			}
			
			GameFieldUtils.performMove(field, move, currentPlayer);
			++round;
			nextMove = null;
		} while (nextMove != null || round < Players.MAX_ROUNDS);
		System.out.println(name + ": ending ");
		
	}
	

	private boolean isEmptyServerMove(Move nextMove) {
		return nextMove.fromX == 0 && nextMove.fromY == 0
				&& nextMove.toX == 0 && nextMove.toY == 0;
	}

	public static int getFirstMove(long[] field, Players p) {
		int [] moves = SMove.getPossibleMoves(field, p);
		System.out.println(GameFieldUtils.toString(field));
		return moves[0];
	}

	public String getName() {
		return basicName;
	}

	public static BufferedImage getEmptyImage() {
		// TODO Auto-generated method stub
		return new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
	}

	public void start() {
		Thread tconn = new Thread(this, basicName);
		tconn.start();
		
	}
}
