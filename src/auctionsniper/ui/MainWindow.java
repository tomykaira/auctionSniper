package auctionsniper.ui;

import static auctionsniper.Main.STATUS_JOINING;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	private final JLabel sniperStatus = createLabel(STATUS_JOINING);
	
	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		add(sniperStatus);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JLabel createLabel(String initialText) {
		JLabel result = new JLabel(initialText);
		result.setName(SNIPER_STATUS_NAME);
		result.setBorder(new LineBorder(Color.BLACK));
		return result;
	}

	public void showStatus(String status) {
		sniperStatus.setText(status);
	}
}
