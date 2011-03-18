package de.htwdd.robotics.wienert;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.htwdd.robotics.Processor;

public class ThinningControl extends JPanel {
	private JButton button1 = new JButton("Step");
	private ThinningProcessor tp;
	public Action stepAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tp.getThinningAlgorithm().thinn();
		}
	};	
	ThinningControl(ThinningProcessor thinningProc) {
		button1 = new JButton(stepAction );
		button1.setText("Step");
		this.add(button1);
	}
}
