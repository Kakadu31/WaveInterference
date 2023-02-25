import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class WaveGenerator extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField rwgAmp;
	private JTextField rwgWavelength;
	private JTextField rwgPhase;
	private JTextField rwgX;
	private JTextField rwgY;
	private JTextField rwgAmpDev;
	private JTextField rwgWavelengthDev;
	private JTextField rwgPhaseDev;
	private JTextField rwgXDev;
	private JTextField rwgYDev;
	private PaintSurface ps;
	private JTextField rwgNum;
	private JLabel lblWaves;
	double waveSpeed = 0.0628505;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WaveGenerator dialog = new WaveGenerator();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WaveGenerator() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblAmplitude = new JLabel("Amplitude:");
		
		JLabel lblWavelength = new JLabel("Wavelength:");
		
		JLabel lblPhase = new JLabel("Phase");
		
		JLabel lblOrigin = new JLabel("Origin:");
		
		JLabel lblX = new JLabel("x");
		
		JLabel lblY = new JLabel("y");
		
		rwgAmp = new JTextField();
		rwgAmp.setHorizontalAlignment(SwingConstants.CENTER);
		rwgAmp.setText("1");
		rwgAmp.setColumns(10);
		
		rwgWavelength = new JTextField();
		rwgWavelength.setHorizontalAlignment(SwingConstants.CENTER);
		rwgWavelength.setText("0.510");
		rwgWavelength.setColumns(10);
		
		rwgPhase = new JTextField();
		rwgPhase.setHorizontalAlignment(SwingConstants.CENTER);
		rwgPhase.setText("0");
		rwgPhase.setColumns(10);
		
		rwgX = new JTextField();
		rwgX.setHorizontalAlignment(SwingConstants.CENTER);
		rwgX.setText("500");
		rwgX.setColumns(10);
		
		rwgY = new JTextField();
		rwgY.setHorizontalAlignment(SwingConstants.CENTER);
		rwgY.setText("500");
		rwgY.setColumns(10);
		
		rwgAmpDev = new JTextField();
		rwgAmpDev.setHorizontalAlignment(SwingConstants.CENTER);
		rwgAmpDev.setText("0.5");
		rwgAmpDev.setColumns(10);
		
		rwgWavelengthDev = new JTextField();
		rwgWavelengthDev.setHorizontalAlignment(SwingConstants.CENTER);
		rwgWavelengthDev.setText("0.01");
		rwgWavelengthDev.setColumns(10);
		
		rwgPhaseDev = new JTextField();
		rwgPhaseDev.setHorizontalAlignment(SwingConstants.CENTER);
		rwgPhaseDev.setText("360");
		rwgPhaseDev.setColumns(10);
		
		rwgXDev = new JTextField();
		rwgXDev.setHorizontalAlignment(SwingConstants.CENTER);
		rwgXDev.setText("1000");
		rwgXDev.setColumns(10);
		
		rwgYDev = new JTextField();
		rwgYDev.setHorizontalAlignment(SwingConstants.CENTER);
		rwgYDev.setText("1000");
		rwgYDev.setColumns(10);
		
		rwgNum = new JTextField();
		rwgNum.setHorizontalAlignment(SwingConstants.CENTER);
		rwgNum.setText("1");
		rwgNum.setColumns(10);
		
		lblWaves = new JLabel("Waves:");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblOrigin)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAmplitude)
								.addComponent(lblWavelength)
								.addComponent(lblPhase)
								.addComponent(lblX)
								.addComponent(lblY)
								.addComponent(lblWaves))
							.addGap(31)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(rwgY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(rwgYDev, 0, 0, Short.MAX_VALUE))
								.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
									.addComponent(rwgX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(rwgXDev, 0, 0, Short.MAX_VALUE))
								.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
									.addComponent(rwgPhase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(rwgPhaseDev, 0, 0, Short.MAX_VALUE))
								.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
									.addComponent(rwgWavelength, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(rwgWavelengthDev, 0, 0, Short.MAX_VALUE))
								.addComponent(rwgNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(rwgAmp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(rwgAmpDev, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(362, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rwgNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblWaves))
					.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAmplitude)
						.addComponent(rwgAmp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rwgAmpDev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWavelength)
						.addComponent(rwgWavelength, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rwgWavelengthDev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPhase)
						.addComponent(rwgPhase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rwgPhaseDev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblOrigin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblX)
						.addComponent(rwgX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(rwgXDev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblY)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(rwgY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(rwgYDev, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Generate");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ps.createRandomPoints(Integer.parseInt(rwgNum.getText()), Float.parseFloat(rwgAmp.getText()), Float.parseFloat(rwgAmpDev.getText()),(float) waveSpeed/Float.parseFloat(rwgWavelength.getText()), (float) (waveSpeed*Float.parseFloat(rwgWavelengthDev.getText()))/(Float.parseFloat(rwgWavelength.getText())*Float.parseFloat(rwgWavelength.getText())), Float.parseFloat(rwgPhase.getText()), Float.parseFloat(rwgPhaseDev.getText()), Integer.parseInt(rwgX.getText()), Integer.parseInt(rwgY.getText()), Integer.parseInt(rwgXDev.getText()), Integer.parseInt(rwgYDev.getText()));
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void setPS(PaintSurface ps) {
		this.ps = ps;
	}
	
	public PaintSurface setPS() {
		return ps;
	}
}
