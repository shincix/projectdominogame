package ui;

import game.Manager;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.util.List;

import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;

import model.Domino;
import model.DominoOnBoard;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import system.Parameters;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoomUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3028719584988240249L;

	private JPanel contentPane;

	private JList list;
	private JList list_left;
	private JList list_right;

	private JButton btnPutOnRight;
	private JButton btnPutOnLeft;
	private JButton btnPushDominoBoard;
	private JButton btnPass;
	
	private JLabel lblDominosAvaliables;
	private JLabel QtdDominoPlayer1;
	private JLabel QtdDominoPlayer2;
	private JLabel QtdDominoPlayer3;
	
	private String me;
	private String player1;
	private String player2;
	private String player3;

	private Manager manager;
	private final JTextArea textArea = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoomUI frame = new RoomUI(new Manager());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RoomUI(Manager manager) {

		this.manager = manager;
		
		/*
		 * Get the sequence players
		 */
		me = manager.getMyPlayer().getUsername();
		player1 = manager.getRoom().getNextPlayerById(manager.getMyPlayer().getId()).getUsername();
		player2 = manager.getRoom().getNextPlayerById(manager.getRoom().getPlayer(player1).getId()).getUsername();
		player3 = manager.getRoom().getNextPlayerById(manager.getRoom().getPlayer(player2).getId()).getUsername();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 645);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Color color = new Color(255, 255, 255);

		JPanel panelWest = new JPanel();
		panelWest.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelWest.setBackground(color);
		panelWest.setBounds(0, 0, 100, 537);
		contentPane.add(panelWest);
		panelWest.setLayout(null);

		JLabel lblPlayer_1 = new JLabel(player1);
		lblPlayer_1.setBounds(10, 11, 80, 14);
		panelWest.add(lblPlayer_1);

		String s = "Dominos: " + manager.getRoom().getPlayer(player1).getDominos().size();
		QtdDominoPlayer1 = new JLabel(s);
		QtdDominoPlayer1.setBounds(10, 39, 80, 14);
		panelWest.add(QtdDominoPlayer1);

		JPanel panelNorth = new JPanel();
		panelNorth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelNorth.setBackground(color);
		panelNorth.setBounds(100, 0, 785, 100);
		contentPane.add(panelNorth);
		panelNorth.setLayout(null);

		JLabel lblPlayer_2 = new JLabel(manager.getRoom().getPlayer(player2).getUsername());
		lblPlayer_2.setBounds(10, 11, 134, 14);
		panelNorth.add(lblPlayer_2);

		s = "Dominos: " + manager.getRoom().getPlayer(player2).getDominos().size();
		QtdDominoPlayer2 = new JLabel(s);
		QtdDominoPlayer2.setBounds(10, 36, 134, 14);
		panelNorth.add(QtdDominoPlayer2);

		JPanel panelEast = new JPanel();
		panelEast.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEast.setBackground(color);
		panelEast.setBounds(884, 0, 100, 537);
		contentPane.add(panelEast);
		panelEast.setLayout(null);

		JLabel lblPlayer_3 = new JLabel(manager.getRoom().getPlayer(player3).getUsername());
		lblPlayer_3.setBounds(10, 11, 80, 14);
		panelEast.add(lblPlayer_3);

		s = "Dominos: " + manager.getRoom().getPlayer(player3).getDominos().size();
		QtdDominoPlayer3 = new JLabel(s);
		QtdDominoPlayer3.setBounds(10, 35, 80, 14);
		panelEast.add(QtdDominoPlayer3);

		JPanel panelSouth = new JPanel();
		panelSouth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelSouth.setBackground(color);
		panelSouth.setBounds(100, 437, 785, 100);
		contentPane.add(panelSouth);
		panelSouth.setLayout(null);

		JLabel lblPlayer = new JLabel(manager.getRoom().getPlayer(me).getUsername());
		lblPlayer.setBounds(10, 11, 146, 14);
		panelSouth.add(lblPlayer);

		s = "Dominnos: " + DominoForLabel(manager.getRoom().getPlayer(me).getDominos());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(212, 11, 193, 78);
		panelSouth.add(scrollPane);

		list = new JList(DominoForListModel(manager.getRoom().getPlayer(me).getDominos()));
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list.setAutoscrolls(true);

		btnPutOnLeft = new JButton("Put On Left");
		btnPutOnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				putOnLeft();
			}
		});
		btnPutOnLeft.setBounds(420, 11, 103, 23);
		panelSouth.add(btnPutOnLeft);

		btnPutOnRight = new JButton("Put On Right");
		btnPutOnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				putOnRight();
			}
		});
		btnPutOnRight.setBounds(420, 45, 103, 23);
		panelSouth.add(btnPutOnRight);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		btnRefresh.setBounds(544, 11, 125, 23);
		panelSouth.add(btnRefresh);
		
		btnPushDominoBoard = new JButton("Push Domino Board");
		btnPushDominoBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushDominoBoard();
			}
		});
		btnPushDominoBoard.setBounds(544, 45, 125, 23);
		panelSouth.add(btnPushDominoBoard);
		
		btnPass = new JButton("Pass!!");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pass();
			}
		});
		btnPass.setBounds(675, 11, 89, 57);
		panelSouth.add(btnPass);

		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelCenter.setBackground(new Color(80, 200, 120));
		panelCenter.setBounds(100, 100, 782, 339);
		contentPane.add(panelCenter);
		panelCenter.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(90, 69, 170, 183);
		panelCenter.add(scrollPane_1);

		list_left = new JList(BoardLeftList());
		scrollPane_1.setViewportView(list_left);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(324, 67, 154, 189);
		panelCenter.add(scrollPane_2);

		list_right = new JList(BoardRightList());
		scrollPane_2.setViewportView(list_right);

		JLabel lblLeft = new JLabel("Left");
		lblLeft.setBounds(92, 50, 46, 14);
		panelCenter.add(lblLeft);

		JLabel lblRight = new JLabel("Right");
		lblRight.setBounds(324, 50, 46, 14);
		panelCenter.add(lblRight);
		
		lblDominosAvaliables = new JLabel("Dominos Avaliables:");
		lblDominosAvaliables.setBounds(606, 11, 154, 14);
		panelCenter.add(lblDominosAvaliables);

		textArea.setBounds(0, 537, 984, 69);
		textArea.setEnabled(false);
		textArea.setText("Mensagens...");
		contentPane.add(textArea);

		refresh();

	}

	private DefaultListModel<Domino> BoardLeftList() {

		boolean segue = false;
		DefaultListModel<Domino> model = new DefaultListModel<Domino>();

		DominoOnBoard dob = manager.getRoom().getDominoCenter();

		if (dob != null) {
			model.add(0, dob.getDomino());
			int i = 1;

			while (!segue) {
				dob = dob.getLeft();
				if (dob != null) {
					model.add(i, dob.getDomino());
					i++;
				} else {
					segue = true;
				}
			}
		}

		return model;
	}

	private DefaultListModel<Domino> BoardRightList() {

		boolean segue = false;
		DefaultListModel<Domino> model = new DefaultListModel<Domino>();

		DominoOnBoard dob = manager.getRoom().getDominoCenter();
		if (dob != null) {
			model.add(0, dob.getDomino());
			int i = 1;

			while (!segue) {
				dob = dob.getRight();
				if (dob != null) {
					model.add(i, dob.getDomino());
					i++;
				} else {
					segue = true;
				}
			}
		}

		return model;
	}

	private void putOnLeft() {
		Domino x = (Domino) list.getSelectedValue();
		manager.putOnBoard(x, Parameters.LEFT, me);
		refresh();
	}

	private void putOnRight() {
		Domino x = (Domino) list.getSelectedValue();
		manager.putOnBoard(x, Parameters.RIGHT, me);
		refresh();
	}

	private DefaultListModel<Domino> DominoForListModel(List<Domino> dominos) {
		DefaultListModel<Domino> model = new DefaultListModel<Domino>();
		int i = 0;

		for (Domino d : dominos) {
			model.add(i, d);
			i++;
		}

		return model;
	}

	private String DominoForLabel(List<Domino> dominos) {
		String result = "";

		for (Domino d : dominos) {
			result += d.getSide1() + "x" + d.getSide2() + "   |   ";
		}

		return result;
	}

	private void enabled() {
		list.setEnabled(true);
		btnPutOnLeft.setEnabled(true);
		btnPutOnRight.setEnabled(true);
		btnPass.setEnabled(true);
	}

	private void disabled() {
		list.setEnabled(false);
		btnPutOnLeft.setEnabled(false);
		btnPutOnRight.setEnabled(false);
		btnPass.setEnabled(false);
	}
	
	/*
	 * Será necessário colocar esse refresh em uma thread. Refresh constante...
	 * Porém quando o usuário estiver jogando, parar a thread até que ele conclua a jogada
	 * 
	 */
	private void refresh() {
		
		if(manager.finishedGame()) {
			disabled();
		} else if (manager.isMyTurn()) {
			enabled();
		} else {
			disabled();
		}

		String msg = "";
		if (manager.getRoom().isGameStarted()) {
			msg += manager.getRoom().whoIsPlaying().getUsername() + " is playing!"+ "\n";
		}

		for (String s : manager.getMessages()) {
			msg += s + "\n";
		}
		
		textArea.setText(msg);
		
		list.setModel(DominoForListModel(manager.getRoom().getPlayer(me).getDominos()));
		list_left.setModel(BoardLeftList());
		list_right.setModel(BoardRightList());
		btnPushDominoBoard.setEnabled(manager.getRoom().getDominosAvailable().size() > 0);
		QtdDominoPlayer1.setText("Dominos: " + manager.getRoom().getPlayer(manager.getRoom().getPlayer(player1).getUsername()).getDominos().size());
		QtdDominoPlayer2.setText("Dominos: " + manager.getRoom().getPlayer(manager.getRoom().getPlayer(player2).getUsername()).getDominos().size());
		QtdDominoPlayer3.setText("Dominos: " + manager.getRoom().getPlayer(manager.getRoom().getPlayer(player3).getUsername()).getDominos().size());
		lblDominosAvaliables.setText("Dominos Avaliables: " + manager.getRoom().getDominosAvailable().size());
	}
	
	private void pass() {
		manager.next();
		refresh();
	}
	
	private void pushDominoBoard() {
		manager.pushDominoBoard();
	}
}
