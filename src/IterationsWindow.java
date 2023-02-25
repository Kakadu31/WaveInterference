import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class IterationsWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField iterationsText;
	private JTextField min_tresholdText;
	private JTextField max_tresholdText;
	private JTextField maxSourcesText;
	private PaintSurface ps;
	private JTextField txtSpotsize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			IterationsWindow dialog = new IterationsWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public IterationsWindow() {
		setBounds(100, 100, 268, 268);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblIterations = new JLabel("Iterations:");
		lblIterations.setBounds(10, 11, 85, 14);
		contentPanel.add(lblIterations);
		
		JLabel lblMintreshold = new JLabel("Min_treshold:");
		lblMintreshold.setBounds(10, 36, 85, 14);
		contentPanel.add(lblMintreshold);
		
		JLabel lblMaxtreshold = new JLabel("Max_treshold:");
		lblMaxtreshold.setBounds(10, 61, 85, 14);
		contentPanel.add(lblMaxtreshold);
		
		JLabel lblMaxSources = new JLabel("Max. Sources:");
		lblMaxSources.setBounds(10, 86, 85, 14);
		contentPanel.add(lblMaxSources);
		
		iterationsText = new JTextField();
		iterationsText.setHorizontalAlignment(SwingConstants.CENTER);
		iterationsText.setText("200");
		iterationsText.setBounds(105, 8, 86, 20);
		contentPanel.add(iterationsText);
		iterationsText.setColumns(10);
		
		min_tresholdText = new JTextField();
		min_tresholdText.setHorizontalAlignment(SwingConstants.CENTER);
		min_tresholdText.setText("0.7");
		min_tresholdText.setBounds(105, 33, 86, 20);
		contentPanel.add(min_tresholdText);
		min_tresholdText.setColumns(10);
		
		max_tresholdText = new JTextField();
		max_tresholdText.setHorizontalAlignment(SwingConstants.CENTER);
		max_tresholdText.setText("1.0");
		max_tresholdText.setBounds(105, 58, 86, 20);
		contentPanel.add(max_tresholdText);
		max_tresholdText.setColumns(10);
		
		maxSourcesText = new JTextField();
		maxSourcesText.setHorizontalAlignment(SwingConstants.CENTER);
		maxSourcesText.setText("1000");
		maxSourcesText.setBounds(105, 83, 86, 20);
		contentPanel.add(maxSourcesText);
		maxSourcesText.setColumns(10);
		
		final JRadioButton iterationWithLines = new JRadioButton("");
		iterationWithLines.setSelected(true);
		iterationWithLines.setBounds(142, 110, 72, 23);
		contentPanel.add(iterationWithLines);
		
		JLabel lblWithlines = new JLabel("WithLines:");
		lblWithlines.setBounds(10, 111, 89, 22);
		contentPanel.add(lblWithlines);
		
		JLabel lblSpotsize = new JLabel("Spotsize: (d)");
		lblSpotsize.setBounds(10, 143, 72, 14);
		contentPanel.add(lblSpotsize);
		
		txtSpotsize = new JTextField();
		txtSpotsize.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpotsize.setText("100000");
		txtSpotsize.setBounds(105, 140, 86, 20);
		contentPanel.add(txtSpotsize);
		txtSpotsize.setColumns(10);
		
		JLabel lblNm = new JLabel("nm");
		lblNm.setBounds(196, 143, 46, 14);
		contentPanel.add(lblNm);
		
		JLabel lblRunning = new JLabel("Moving:");
		lblRunning.setBounds(10, 172, 72, 14);
		contentPanel.add(lblRunning);
		
		final JRadioButton isMoving = new JRadioButton("");
		isMoving.setBounds(142, 167, 109, 23);
		contentPanel.add(isMoving);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ps.setIterations(iterationsText.getText(),min_tresholdText.getText(),max_tresholdText.getText(),maxSourcesText.getText(), Boolean.toString(iterationWithLines.isSelected()), txtSpotsize.getText(), Boolean.toString(isMoving.isSelected()));
						dispose();
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
