package de.htwdd.robotics.wienert;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwdd.robotics.gui.BatteryPanel;
import de.htwdd.robotics.gui.BumperPanel;
import de.htwdd.robotics.gui.ColorImagePanel;
import de.htwdd.robotics.gui.HeadLightPanel;
import de.htwdd.robotics.gui.HeadPanel;
import de.htwdd.robotics.gui.LaserComponent;
import de.htwdd.robotics.gui.ManualSteeringComponent;
import de.htwdd.robotics.gui.OpenMaskedOccupancyMapMenuItem;
import de.htwdd.robotics.gui.PosePanel;
import de.htwdd.robotics.gui.ProcessorPanel;
import de.htwdd.robotics.gui.PtuPanel;
import de.htwdd.robotics.gui.RobotFrame;
import de.htwdd.robotics.gui.SaveOccupancyMapMenuItem;
import de.htwdd.robotics.gui.environment.CurrentPosePlugin;
import de.htwdd.robotics.gui.environment.EnvironmentPanel;
import de.htwdd.robotics.gui.environment.LaserScanPlugin;
import de.htwdd.robotics.gui.environment.MapPlugin;
import de.htwdd.robotics.gui.environment.SonarScanPlugin;
import de.htwdd.robotics.wienert.MyRobot;

/**
 * The GUI of the custom SCITOS robot.
 * 
 * @author Peter Poschmann
 */
public class MyRobotFrame extends RobotFrame {

	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static final Logger log = LoggerFactory
			.getLogger(MyRobotFrame.class);

	private final MyRobot robot;

	/**
	 * Constructs a new custom robot frame.
	 * 
	 * @param robot
	 *            The custom SCITOS robot.
	 */
	public MyRobotFrame(final MyRobot robot) {
		super(robot);
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1]
					.getClassName());
		} catch (Exception e) {
		}

		this.robot = robot;

		
		
		ProcessorPanel processorPanel = new ProcessorPanel(robot);
		addComponent("Processors", new JScrollPane(processorPanel), new Point(
				50, 50), new Dimension(250, 200), false);
		LaserComponent laserComponent = new LaserComponent(
				robot.getLaserScanContainer());
		addComponent("Laser Scan", laserComponent, new Point(0, 0),
				new Dimension(250, 250), true);

		ManualSteeringComponent steeringComponent = new ManualSteeringComponent(
				robot.getVelocityCommandContainer(),
				robot.getVelocityContainer(), robot.getTimeProvider(), 1, 2);
		addComponent("Manual Steering", steeringComponent, new Point(0, 250),
				new Dimension(250, 200), false);

		EnvironmentPanel environmentPanel = new EnvironmentPanel(100);
		environmentPanel
				.addPlugIn(new MapPlugin(robot.getMapContainer(), true));
		environmentPanel.addPlugIn(new LaserScanPlugin(
				robot.getPoseContainer(), robot.getLaserScanContainer(),
				new Color(127, 165, 255, 127), Color.BLACK));
		environmentPanel.addPlugIn(new SonarScanPlugin(
				robot.getPoseContainer(), robot.getSonarScanContainer(),
				new Color(127, 255, 165, 127), Color.BLACK));
		environmentPanel.addPlugIn(new CurrentPosePlugin(robot
				.getPoseContainer(), Color.RED));
		addComponent("Environment", environmentPanel, new Point(250, 0),
				new Dimension(500, 400), false);

		BatteryPanel batteryPanel = new BatteryPanel(
				robot.getBatteryStateContainer());
		addComponent("Battery", batteryPanel, new Point(750, 0), new Dimension(
				200, 130), false);

		BumperPanel bumperPanel = new BumperPanel(
				robot.getBumperStateContainer(),
				robot.getBumperResetContainer(), robot.getTimeProvider());
		addComponent("Bumper", bumperPanel, new Point(750, 320), new Dimension(
				150, 80), false);

		addUtilityMenuItem(new OpenMaskedOccupancyMapMenuItem(this,
				robot.getMapContainer()));
		addUtilityMenuItem(new SaveOccupancyMapMenuItem(this,
				robot.getMapContainer()));
		
		ThinningProcessor tp = new ThinningProcessor("thinner", 100);
		tp.setGridMap(robot.getMapContainer().get());
		tp.testme();
	}

	public MyRobot getRobot() {
		return robot;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MyRobot robot = new MyRobot("localhost");
					MyRobotFrame frame = new MyRobotFrame(robot);
					frame.setSize(1024, 740);

					// frame.setLocationRelativeTo(null); // center frame
					frame.setVisible(true);
					frame.setExtendedState(frame.getExtendedState()
							| JFrame.MAXIMIZED_BOTH); // maximize frame
				} catch (IOException exc) {
					log.error("Robot creation failed", exc);
					JOptionPane.showMessageDialog(
							null,
							"The robot could not be created: "
									+ exc.getMessage(),
							"Robot creation failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
