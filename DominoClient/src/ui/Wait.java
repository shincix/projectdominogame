package ui;

import game.Manager;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Wait extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private Manager manager;

	private JTextArea textArea;
	
	private JButton btnStart;

	/**
	 * Create the frame.
	 */
	public Wait(Manager manager) {

		this.manager = manager;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 402, 239);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea = new JTextArea();
		textArea.setText("Aguardando servidor remoto...");
		JScrollPane scroll = new JScrollPane(textArea);
		contentPane.add(scroll, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnStart = new JButton("Iniciar");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				play();
			}
		});
		btnStart.setEnabled(false);
		panel.add(btnStart);

		JButton btnExit = new JButton("Sair");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		panel.add(btnExit);

		RemoteExecute threadMessages = new RemoteExecute();
		threadMessages.start();

	}

	public void exit() {
		manager.exit();
		dispose();
		System.exit(0);
	}
	
	public void play() {
		dispose();
		RoomUI room = new RoomUI(manager);
		room.setVisible(true);
	}

	class RemoteExecute extends Thread {

		@Override
		public void run() {

			while (true) {

				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				List<String> messages = manager.getMessages();
				String message = textArea.getText();

				for (String s : messages) {
					message += "\n" + s;
				}
				
				textArea.setText(message);
				
				if(!btnStart.isEnabled() && manager.getRoom().isGameStarted()) {
					btnStart.setEnabled(true);
				}
			}
		}
	}

}
