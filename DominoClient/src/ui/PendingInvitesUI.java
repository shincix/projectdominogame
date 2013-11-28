package ui;

import game.Manager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JButton;

import model.Invite;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class PendingInvitesUI extends JFrame {

	private Manager manager;
	private RefreshForm refresh;

	private static final long serialVersionUID = 7479101660315387337L;
	private JPanel contentPane;
	private JList<String> list;

	/**
	 * Create the frame.
	 */
	public PendingInvitesUI(Manager manager) {

		this.manager = manager;
		this.refresh = new RefreshForm();

		setTitle("Convites Pendentes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		list = new JList<String>();
		list.setBounds(10, 11, 260, 146);
		contentPane.add(list);

		JButton btnAceitar = new JButton("Aceitar");
		btnAceitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceitar();
			}
		});
		btnAceitar.setBounds(10, 171, 72, 23);
		contentPane.add(btnAceitar);

		JButton btnRecusar = new JButton("Recusar");
		btnRecusar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recusar();
			}
		});
		btnRecusar.setBounds(103, 171, 72, 23);
		contentPane.add(btnRecusar);

		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();
			}
		});
		btnFechar.setBounds(198, 171, 72, 23);
		contentPane.add(btnFechar);

		refresh.start();
	}

	private void fechar() {
		dispose();
	}

	private void recusar() {
		String user = list.getSelectedValue();
		if (user != null && !user.trim().equals("")) {
			manager.refuseInvite(user);
		}
	}

	private void aceitar() {
		String user = list.getSelectedValue();
		if (user != null && !user.trim().equals("")) {
			manager.acceptInvite(user);
			for(int i = 0; i < list.getModel().getSize(); i++) {
				String userList = list.getModel().getElementAt(i);
				if(!userList.equals(user)) {
					manager.refuseInvite(userList);
				}
			}
			dispose();
		}
	}

	class RefreshForm extends Thread {

		private boolean finished;

		@Override
		public void run() {

			finished = false;

			while (!finished) {

				refreshList();

				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void refreshList() {

			List<Invite> pending = manager.getReceivedInvites();
			if (pending.size() > 0) {
				int i = 0;
				String currentUserSelected = list.getSelectedValue();
				int actualIndex = 0;

				String[] dataList = new String[pending.size()];
				for (Invite invite : pending) {
					if (invite.getStatus().equals(Invite.PENDENT)) {
						dataList[i] = invite.getIssuing();
						if (invite.getIssuing().equals(currentUserSelected)) {
							actualIndex = i;
						}
						i++;

					}
				}

				list.setListData(dataList);
				list.setSelectedIndex(actualIndex);
			}

		}

		public void stopMe() {
			finished = true;
		}
	}

}
