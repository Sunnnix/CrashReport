package de.snx.crashReport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CrashWindow extends JDialog {

	private static final long serialVersionUID = 3530946542843279815L;

	public CrashWindow(JFrame window, String title, String crashText, ModalityType modalityType) {
		super(window, title, modalityType);
		initPanel(crashText);
		initFrame(window);
	}

	private void initPanel(String crashText) {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel freeSpace[] = new JPanel[4];
		for (int i = 0; i < freeSpace.length; i++) {
			freeSpace[i] = new JPanel();
			if (i == 0)
				freeSpace[i].setPreferredSize(new Dimension(5, 15));
			else
				freeSpace[i].setPreferredSize(new Dimension(5, 5));
		}
		JTextArea text = new JTextArea(crashText);
		text.setEditable(false);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension(600, 400));
		panel.add(scroll, BorderLayout.CENTER);
		panel.add(freeSpace[0], BorderLayout.NORTH);
		panel.add(freeSpace[1], BorderLayout.WEST);
		panel.add(freeSpace[2], BorderLayout.SOUTH);
		panel.add(freeSpace[3], BorderLayout.EAST);
		this.add(panel);
	}

	private void initFrame(JFrame window) {
		this.addWindowListener(new WindowListener());
		this.pack();
		this.setLocationRelativeTo(window);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	class WindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			System.exit(-1);
		}
	}
}
