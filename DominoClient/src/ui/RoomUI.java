package ui;

import game.Manager;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
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
	private JLabel lblpieceN1,lblpieceN2,lblpieceN3,lblpieceN4,lblpieceN5,lblpieceN6,lblpieceN7;
	private JLabel lblpieceE1,lblpieceE2,lblpieceE3,lblpieceE4,lblpieceE5,lblpieceE6,lblpieceE7;
	private JLabel lblpieceW1,lblpieceW2,lblpieceW3,lblpieceW4,lblpieceW5,lblpieceW6,lblpieceW7;
	private JLabel lblpieceS1,lblpieceS2,lblpieceS3,lblpieceS4,lblpieceS5,lblpieceS6,lblpieceS7;
	
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

		//Labels com as imagens das peças Panel oeste
		lblpieceW1 = new JLabel();
		lblpieceW2 = new JLabel();
		lblpieceW3 = new JLabel();
		lblpieceW4 = new JLabel();
		lblpieceW5 = new JLabel();
		lblpieceW6 = new JLabel();
		lblpieceW7 = new JLabel();
		
		lblpieceW1.setBounds(15, 97, 62, 27);
		lblpieceW2.setBounds(15, 127, 62, 27);
		lblpieceW3.setBounds(15, 157, 62, 27);
		lblpieceW4.setBounds(15, 187, 62, 27);
		lblpieceW5.setBounds(15, 217, 62, 27);
		lblpieceW6.setBounds(15, 247, 62, 27);
		lblpieceW7.setBounds(15, 277, 62, 27);
		
		lblpieceW1.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW2.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW3.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW4.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW5.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW6.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceW7.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));	
		
		//Panel oeste
		JPanel panelWest = new JPanel();
		panelWest.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		//panelWest.setBackground(color);
		panelWest.setBounds(0, 0, 100, 537);
		
		//Adicionando os labels com as imagens das peças ao Panel oeste
		panelWest.add(lblpieceW1);
		panelWest.add(lblpieceW2);
		panelWest.add(lblpieceW3);
		panelWest.add(lblpieceW4);
		panelWest.add(lblpieceW5);
		panelWest.add(lblpieceW6);
		panelWest.add(lblpieceW7);
		
		contentPane.add(panelWest);
		panelWest.setLayout(null);

		JLabel lblPlayer_1 = new JLabel(player1);
		lblPlayer_1.setBounds(10, 11, 80, 14);
		panelWest.add(lblPlayer_1);

		String s = "Dominos: " + manager.getRoom().getPlayer(player1).getDominos().size();
		QtdDominoPlayer1 = new JLabel(s);
		QtdDominoPlayer1.setBounds(10, 39, 80, 14);
		panelWest.add(QtdDominoPlayer1);	
		
		//Labels com as imagens das pecas Panel norte
		lblpieceN1 = new JLabel();
		lblpieceN2 = new JLabel();
		lblpieceN3 = new JLabel();
		lblpieceN4 = new JLabel();
		lblpieceN5 = new JLabel();
		lblpieceN6 = new JLabel();
		lblpieceN7 = new JLabel();
		
		lblpieceN1.setBounds(100, 15, 27, 62);
		lblpieceN2.setBounds(130, 15, 27, 62);
		lblpieceN3.setBounds(160, 15, 27, 62);
		lblpieceN4.setBounds(190, 15, 27, 62);
		lblpieceN5.setBounds(220, 15, 27, 62);
		lblpieceN6.setBounds(250, 15, 27, 62);
		lblpieceN7.setBounds(280, 15, 27, 62);
		
		lblpieceN1.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));		
		lblpieceN2.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));
		lblpieceN3.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));
		lblpieceN4.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));
		lblpieceN5.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));		
		lblpieceN6.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));		
		lblpieceN7.setIcon(new ImageIcon(getClass().getResource("/images/piece.png")));
		
		//Panel norte
		JPanel panelNorth = new JPanel();
		
		panelNorth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelNorth.setBounds(100, 0, 785, 100);
		
		//Adicionando os labels com as imagens das pecas ao panel norte
		panelNorth.add(lblpieceN1);
		panelNorth.add(lblpieceN2);
		panelNorth.add(lblpieceN3);
		panelNorth.add(lblpieceN4);
		panelNorth.add(lblpieceN5);
		panelNorth.add(lblpieceN6);
		panelNorth.add(lblpieceN7);
		
		contentPane.add(panelNorth);
		panelNorth.setLayout(null);

		JLabel lblPlayer_2 = new JLabel(manager.getRoom().getPlayer(player2).getUsername());
		lblPlayer_2.setBounds(10, 11, 134, 14);
		panelNorth.add(lblPlayer_2);

		s = "Dominos: " + manager.getRoom().getPlayer(player2).getDominos().size();
		QtdDominoPlayer2 = new JLabel(s);
		QtdDominoPlayer2.setBounds(10, 36, 134, 14);
		panelNorth.add(QtdDominoPlayer2);		
		
		//Labels com as imagens das peças Panel leste
		lblpieceE1 = new JLabel();
		lblpieceE2 = new JLabel();
		lblpieceE3 = new JLabel();
		lblpieceE4 = new JLabel();
		lblpieceE5 = new JLabel();
		lblpieceE6 = new JLabel();
		lblpieceE7 = new JLabel();
				
		lblpieceE1.setBounds(15, 97, 62, 27);
		lblpieceE2.setBounds(15, 127, 62, 27);
		lblpieceE3.setBounds(15, 157, 62, 27);
		lblpieceE4.setBounds(15, 187, 62, 27);
		lblpieceE5.setBounds(15, 217, 62, 27);
		lblpieceE6.setBounds(15, 247, 62, 27);
		lblpieceE7.setBounds(15, 277, 62, 27);
				
		lblpieceE1.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE2.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE3.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE4.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE5.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE6.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
		lblpieceE7.setIcon(new ImageIcon(getClass().getResource("/images/pieceh.png")));
				
		//Panel leste
		JPanel panelEast = new JPanel();
		//panelEast.setBackground(color);
		panelEast.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));		
		panelEast.setBounds(884, 0, 100, 537);
					
		//Adicionando os labels com as imagens das peças ao Panel leste
		panelEast.add(lblpieceE1);
		panelEast.add(lblpieceE2);
		panelEast.add(lblpieceE3);
		panelEast.add(lblpieceE4);
		panelEast.add(lblpieceE5);
		panelEast.add(lblpieceE6);
		panelEast.add(lblpieceE7);
		
		contentPane.add(panelEast);
		panelEast.setLayout(null);

		JLabel lblPlayer_3 = new JLabel(manager.getRoom().getPlayer(player3).getUsername());
		lblPlayer_3.setBounds(10, 11, 134, 14);
		panelEast.add(lblPlayer_3);

		s = "Domi " + manager.getRoom().getPlayer(player3).getDominos().size();
		QtdDominoPlayer3 = new JLabel(s);
		QtdDominoPlayer3.setBounds(10, 35, 134, 14);
		panelEast.add(QtdDominoPlayer3);

		//Labels com as imagens das peças Panel sul
		lblpieceS1 = new JLabel();
		lblpieceS2 = new JLabel();
		lblpieceS3 = new JLabel();
		lblpieceS4 = new JLabel();
		lblpieceS5 = new JLabel();
		lblpieceS6 = new JLabel();
		lblpieceS7 = new JLabel();
						
		lblpieceS1.setBounds(2, 28, 27, 62);
		lblpieceS2.setBounds(32, 28, 27, 62);
		lblpieceS3.setBounds(62, 28, 27, 62);
		lblpieceS4.setBounds(92, 28, 27, 62);
		lblpieceS5.setBounds(122, 28, 27, 62);
		lblpieceS6.setBounds(152, 28, 27, 62);
		lblpieceS7.setBounds(182, 28, 27, 62);
	 				
		lblpieceS1.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(0).toString() + ".png")));
		lblpieceS2.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(1).toString() + ".png")));
		lblpieceS3.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(2).toString() + ".png")));
		lblpieceS4.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(3).toString() + ".png")));
		lblpieceS5.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(4).toString() + ".png")));
		lblpieceS6.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(5).toString() + ".png")));
		lblpieceS7.setIcon(new ImageIcon(getClass().getResource("/images/"+ manager.getRoom().getPlayer(me).getDominos().get(6).toString() + ".png")));
		
		//Panel sul
		JPanel panelSouth = new JPanel();
		panelSouth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		//panelSouth.setBackground(color);
		panelSouth.setBounds(100, 437, 785, 100);
		
		panelSouth.add(lblpieceS1);
		panelSouth.add(lblpieceS2);
		panelSouth.add(lblpieceS3);
		panelSouth.add(lblpieceS4);
		panelSouth.add(lblpieceS5);
		panelSouth.add(lblpieceS6);
		panelSouth.add(lblpieceS7);
		
		contentPane.add(panelSouth);
		panelSouth.setLayout(null);

		JLabel lblPlayer = new JLabel(manager.getRoom().getPlayer(me).getUsername());
		lblPlayer.setBounds(10, 11, 146, 14);
		panelSouth.add(lblPlayer);

		s = "Dominos: " + DominoForLabel(manager.getRoom().getPlayer(me).getDominos());

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

		
		
		//Label com img da mesa
		JLabel lblmesa = new JLabel();
		lblmesa.setBounds(1, 1, 779, 336);
		lblmesa.setIcon(new ImageIcon(getClass().getResource("/images/border.png")));
		
		//Painel central com imagem da mesa
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelCenter.setBounds(100, 100, 782, 339);
		
		//panelCenter.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		//panelCenter.setBackground(new Color(100, 200, 220));

		panelCenter.add(lblmesa);
		
		
		contentPane.add(panelCenter);
		panelCenter.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(90, 69, 170, 183);
		lblmesa.add(scrollPane_1);

		list_left = new JList(BoardLeftList());
		scrollPane_1.setViewportView(list_left);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(324, 67, 154, 189);
		lblmesa.add(scrollPane_2);

		list_right = new JList(BoardRightList());
		scrollPane_2.setViewportView(list_right);

		JLabel lblLeft = new JLabel("Left");
		lblLeft.setBounds(92, 50, 46, 14);
		lblmesa.add(lblLeft);

		JLabel lblRight = new JLabel("Right");
		lblRight.setBounds(324, 50, 46, 14);
		lblmesa.add(lblRight);
		
		lblDominosAvaliables = new JLabel("Dominos Avaliables:");
		lblDominosAvaliables.setBounds(606, 11, 154, 14);
		lblmesa.add(lblDominosAvaliables);

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
	 * Ser� necess�rio colocar esse refresh em uma thread. Refresh constante...
	 * Por�m quando o usu�rio estiver jogando, parar a thread at� que ele conclua a jogada
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
