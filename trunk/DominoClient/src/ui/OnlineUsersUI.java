package ui;

import game.Manager;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;

import system.Parameters;

import model.Invite;

import java.awt.Color;

public class OnlineUsersUI extends JFrame {

	private static final long serialVersionUID = -8749793144865213548L;

	private Manager manager;
	private RefreshForm refresh;

	private JPanel contentPane;
	private JTextField txtMsgChat;
	private JList<String> lstOnlineUsers;
	private JTextArea txtChat;
	private JTextArea txtMessages;
	private JList<String> lstInvites;
	private JButton btnIniciar;
	private JButton btnConvitesPendentes;
	private JButton btnConvidar;

	/**
	 * Create the frame.
	 */
	public OnlineUsersUI(Manager manager) {

		this.manager = manager;
		this.refresh = new RefreshForm();

		setTitle("On-line Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 678, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(503, 23, 153, 190);
		contentPane.add(scrollPane);

		lstOnlineUsers = new JList<String>();
		scrollPane.setViewportView(lstOnlineUsers);

		JLabel lblUsuriosOnline = new JLabel("Usu\u00E1rios Online");
		lblUsuriosOnline.setBounds(503, 10, 153, 14);
		contentPane.add(lblUsuriosOnline);

		btnConvidar = new JButton("Convidar");
		btnConvidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convidar();
			}
		});
		btnConvidar.setBounds(503, 214, 153, 23);
		contentPane.add(btnConvidar);

		btnConvitesPendentes = new JButton("Convites (0)");
		btnConvitesPendentes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPedingInvites();
			}
		});
		btnConvitesPendentes.setBounds(503, 359, 153, 23);
		contentPane.add(btnConvitesPendentes);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(5, 5, 490, 344);
		contentPane.add(scrollPane_1);

		txtChat = new JTextArea();
		scrollPane_1.setViewportView(txtChat);

		txtMsgChat = new JTextField();
		txtMsgChat.setBounds(5, 360, 403, 20);
		contentPane.add(txtMsgChat);
		txtMsgChat.setColumns(10);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendChatMsg();
			}
		});
		btnEnviar.setBounds(410, 360, 83, 20);
		contentPane.add(btnEnviar);

		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciarJogo();
			}
		});
		btnIniciar.setBounds(503, 328, 153, 23);
		btnIniciar.setEnabled(false);
		contentPane.add(btnIniciar);

		JLabel lblUsuriosConvidados = new JLabel("Usu\u00E1rios Convidados");
		lblUsuriosConvidados.setBounds(503, 248, 153, 14);
		contentPane.add(lblUsuriosConvidados);

		txtMessages = new JTextArea();
		txtMessages.setBackground(new Color(255, 248, 220));
		txtMessages.setBounds(0, 386, 662, 45);
		contentPane.add(txtMessages);

		lstInvites = new JList<String>();
		lstInvites.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		lstInvites.setBounds(503, 261, 153, 67);
		contentPane.add(lstInvites);

		refresh.start();
	}

	private void sendChatMsg() {
		String msg = txtMsgChat.getText();
		if (msg != null && !msg.trim().equals("")) {
			manager.sendChatMsg(msg);
		}
		txtMsgChat.setText("");
	}

	private void convidar() {
		String userSelected = lstOnlineUsers.getSelectedValue();
		manager.invite(userSelected);
	}

	private void showPedingInvites() {
		PendingInvitesUI o = new PendingInvitesUI(manager);
		o.setVisible(true);
	}
	
	private void iniciarJogo() {
		manager.startGame();
	}
	
	class RefreshForm extends Thread {

		private boolean finished;

		@Override
		public void run() {

			finished = false;

			while (!finished) {

				refreshLstOnlineUsers();
				refreshTxtChat();
				refreshTxtMessages();
				refreshTblInvites();
				rereshBtnConvitesPendentes();
				
				if(manager.getRoom().isGameStarted() && !manager.getRoom().isFinishedGame()) {
					finished = true;
					dispose();
					RoomUI room = new RoomUI(manager);
					room.setVisible(true);
				}
				
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void refreshTxtChat() {
			String currentText = txtChat.getText();
			List<String> chatMsgs = manager.getChatMsg();

			for (int i = 0; i < chatMsgs.size(); i++) {
				currentText += "\n" + chatMsgs.get(i);
			}

			txtChat.setText(currentText);

		}

		private void refreshLstOnlineUsers() {

			List<String> online = manager.getOnlineUsers();

			if (online.size() > 1) {
				String currentUserSelected = lstOnlineUsers.getSelectedValue();
				String[] dataList = new String[online.size() - 1];
				int j = 0;
				int index = 0;

				for (int i = 0; i < online.size(); i++) {

					if (!manager.getMyUsername().equals(online.get(i))) {
						if (currentUserSelected != null
								&& currentUserSelected.equals(online.get(i))) {
							index = j;
						}
						dataList[j] = online.get(i);
						j++;
					}
				}

				lstOnlineUsers.setListData(dataList);
				lstOnlineUsers.setSelectedIndex(index);
			}
		}

		private void refreshTxtMessages() {
			List<String> messages = manager.getMessages();
			String currentMessages = txtMessages.getText();

			for (String msg : messages) {
				currentMessages = msg + "\n" + currentMessages;
			}

			txtMessages.setText(currentMessages);

		}

		private void refreshTblInvites() {
			List<Invite> invites = manager.getInvites();
			boolean ableBtnInicar = false;
			int amountInvite = 0;

			if (invites.size() > 0) {
				String[] dataInvite = new String[invites.size()];
				
				for (int i = 0; i < invites.size(); i++) {
					dataInvite[i] = invites.get(i).getReceptor() + "  -  "
							+ invites.get(i).getStatus();
					if (invites.get(i).getStatus().equals(Invite.ACCPETED)) {
						ableBtnInicar = true;
					}
					
					if (invites.get(i).getStatus().equals(Invite.ACCPETED)
							|| invites.get(i).getStatus().equals(Invite.PENDENT)) {
						amountInvite++;
					}
				}
				
				lstInvites.setListData(dataInvite);
			}
			
			btnIniciar.setEnabled(ableBtnInicar);

			if(amountInvite == (Parameters.MAX_NUMBER_OF_PLAYERS - 1) || manager.isAcceptedInvite()) {
				btnConvidar.setEnabled(false);
			} else {
				btnConvidar.setEnabled(true);
			}

		}

		private void rereshBtnConvitesPendentes() {
			List<Invite> pending = manager.getReceivedInvites();
			int pendingAmount = 0;

			for (Invite invite : pending) {
				if (invite.getStatus().equals(Invite.PENDENT)) {
					pendingAmount++;
				}
			}

			btnConvitesPendentes.setText("Convites(" + pendingAmount + ")");
		}

		public void stopMe() {
			finished = true;
		}
	}
}
