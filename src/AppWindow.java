import java.awt.EventQueue;
import java.awt.Shape;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.io.*;
import javax.swing.JTextField;
import java.awt.Rectangle;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencv.core.Core;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import java.awt.Button;



public class AppWindow {

	//Voreinstellungen
	String filePath = "C:\\Users\\basti\\git\\WaveInterference\\Results\\";
	//Depricated String pythonScriptPath = ("F:\\Desktop\\RippleTankPython\\3DWave.py");
	int areaSize = 15000;
	int resolution = 15;
	String defaultAmplitude = "1";
	String defaultFrequenz = "0.11814";
	String defaultPhase = "0";
	String defaultZerfallsK = "0";
	String defaultZerfallsType = "exponential";
	String imageType = "jpg";
	public String [] iterationsData = new String[5];
	double waveSpeed = 0.0628505;				//Hilft beim normieren der Frequenz/Wellenlänge
	private boolean isRunningSim = false;
	private boolean colored = true;
	private boolean normiert = false;
	ArrayList<Shape> shapesSaved = new ArrayList<Shape>();
	ArrayList<MyEllipse2D> pointsSaved = new ArrayList<MyEllipse2D>();
	ArrayList<MyLine2D> linesSaved = new ArrayList<MyLine2D>();
	ArrayList<MyRectangle2D> rectanglesSaved = new ArrayList<MyRectangle2D>();
	
	//Variablen Initialisierung
	private String filename;
	private String runtype;
	public PaintSurface ps = new PaintSurface();
	public JFrame simulationPanelFrame;
	private JFrame frmDrawinterface;
	public SimulationPanel simulationPanel;
	public WaveGenerator WaveGenerator;
	public IterationsWindow IterationsWindow;
	private JTextField originTextX;
	private JTextField originTextY;
	private JTextField moveTextX;
	private JTextField moveTextY;
	private JTextField amplitudeText;
	private JTextField waveLText;
	private JTextField phaseText;
	private JTextField zerfallsKText;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txtInsertFileName_1;
	private JComboBox<String> comboBox_1 = new JComboBox<String>();  //ZerfallsType
	private final ButtonGroup buttonGroup = new ButtonGroup();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//nu.pattern.OpenCV.loadShared();
					//OpenCV.loadShared();
					System.loadLibrary(Core.NATIVE_LIBRARY_NAME);;
					AppWindow window = new AppWindow();
					window.frmDrawinterface.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDrawinterface = new JFrame();
		frmDrawinterface.setResizable(false);
		frmDrawinterface.setTitle("DrawInterface by Kakadu31");
		frmDrawinterface.setBounds(100, 100, 1257, 1060);
		frmDrawinterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		frmDrawinterface.getContentPane().add(panel, BorderLayout.NORTH);
		ps.setBounds(new Rectangle(100, 50, 0, 0));
		ps.setName("file name");
		ps.setToolTipText("Insert file name");
		
		ps.setSize(new Dimension(1000, 1000));
		ps.setSize(1000,1000);
		frmDrawinterface.getContentPane().add(ps, BorderLayout.CENTER);
	
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//Deprecated: saveWaveFile();
					saveJSON();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println(shapesSaved);
			}
		});
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		btnNewButton.setVerticalTextPosition(SwingConstants.TOP);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(btnNewButton);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					reset();
					loadJSON();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println(shapesSaved);
			}
		});
		panel.add(btnLoad);
		panel.add(btnReset);
		
		final JRadioButton rdbtnSelect = new JRadioButton("Select");
		buttonGroup.add(rdbtnSelect);
		panel.add(rdbtnSelect);
		rdbtnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnSelect.isSelected()) {
					setMode("select");
				}
			}
		});
		
		final JRadioButton rdbtnNewRadioRectangle = new JRadioButton("Rectangle");
		rdbtnNewRadioRectangle.setSelected(true);
		buttonGroup.add(rdbtnNewRadioRectangle);
		panel.add(rdbtnNewRadioRectangle);
		rdbtnNewRadioRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnNewRadioRectangle.isSelected()) {
					setMode("rectangle");
				}
			}
		});
		
		final JRadioButton rdbtnLine = new JRadioButton("Line");
		buttonGroup.add(rdbtnLine);
		panel.add(rdbtnLine);
		rdbtnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnLine.isSelected()) {
					setMode("line");
				}
			}
		});
		
		final JRadioButton rdbtnNewRadioPoint = new JRadioButton("Point");
		buttonGroup.add(rdbtnNewRadioPoint);
		panel.add(rdbtnNewRadioPoint);
		
		
		txtInsertFileName_1 = new JTextField();
		txtInsertFileName_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 filename = txtInsertFileName_1.getText();
			}
		});
		txtInsertFileName_1.setText("Insert file name");
		panel.add(txtInsertFileName_1);
		txtInsertFileName_1.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//Deprecated saveWaveFile();
					isRunningSim = true;
					//runPython()
					runJava(createPointSources(),createLineSources(), 10, areaSize, resolution);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button filebutton = new Button("File");
		filebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        final JFileChooser fc = new JFileChooser(filePath);
		        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		        fc.addPropertyChangeListener(new PropertyChangeListener() {
		            public void propertyChange(PropertyChangeEvent evt) {
		                if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(
		                        evt.getPropertyName())) {
		                    //System.out.println("DIRECTORY CHANGED");
		                    //fc.approveSelection();
		                }
		            }
		        });

		        int result = fc.showOpenDialog(frmDrawinterface);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            filePath = fc.getCurrentDirectory().getPath() + "\\";
		            filename = removeExtension(fc.getSelectedFile().getName());
		            System.out.println("Filepath: " + filePath);
		            System.out.println("Filename: " + filename);
		            txtInsertFileName_1.setText(filename);
		        }
		    }

			public String removeExtension(String s) {

			    String separator = System.getProperty("file.separator");
			    String filename;

			    // Remove the path upto the filename.
			    int lastSeparatorIndex = s.lastIndexOf(separator);
			    if (lastSeparatorIndex == -1) {
			        filename = s;
			    } else {
			        filename = s.substring(lastSeparatorIndex + 1);
			    }

			    // Remove the extension.
			    int extensionIndex = filename.lastIndexOf(".");
			    if (extensionIndex == -1)
			        return filename;

			    return filename.substring(0, extensionIndex);
			}
		});
		
		filebutton.setForeground(Color.BLACK);
		panel.add(filebutton);
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(Color.GREEN);
		panel.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isRunningSim = false;
				stopJava();
			}
		});
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pauseJava(isRunningSim);
			}
		});
		panel.add(btnPause);
		btnStop.setForeground(Color.BLACK);
		btnStop.setBackground(Color.RED);
		panel.add(btnStop);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				areaSize = Integer.parseInt(textField.getText());
			}
		});
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("15000");
		panel.add(textField);
		textField.setColumns(5);
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resolution = Integer.parseInt(textField_1.getText());
			}
		});
		
		JLabel lblNm = new JLabel("nm");
		panel.add(lblNm);
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setText("15");
		panel.add(textField_1);
		textField_1.setColumns(5);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runtype = (String) comboBox.getSelectedItem();
		        if (runtype == "Iterations"){
		        	runIterationsWindow();
		        }
			}
		});
		
		JLabel lblNm_1 = new JLabel("nm");
		panel.add(lblNm_1);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Realtime", "Image", "Frames", "Iterations"}));
		panel.add(comboBox);
		
		JButton btnWavegenerator = new JButton("WaveG.");
		btnWavegenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runWaveGenerator();
			}
		});
		
		final JCheckBox chckbxColor = new JCheckBox("Color");
		chckbxColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxColor.isSelected()) {
					colored = true;
					normiert = false;
				}
				else{
					colored = false;
					normiert = true;
				}
			}
		});
		chckbxColor.setSelected(true);
		panel.add(chckbxColor);
		panel.add(btnWavegenerator);
		rdbtnNewRadioPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnNewRadioPoint.isSelected()) {
					setMode("point");
				}
			}
		});
		
		
		//Popupmenu on rightclick
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(ps, popupMenu);
		addSidePanelUpdater(ps);
		
		JButton popupDelete = new JButton("Delete");
		popupMenu.add(popupDelete);
		popupDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ps.deleteSelection();
			}
		});
		
		//Create SidePanel
		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(250, 1000));
		sidePanel.setMinimumSize(new Dimension(250, 1000));
		sidePanel.setMaximumSize(new Dimension(250, 1000));
		sidePanel.setAlignmentY(Component.TOP_ALIGNMENT);
		sidePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		frmDrawinterface.getContentPane().add(sidePanel, BorderLayout.EAST);
		
		
		//OriginSidePanel
		JLabel lblOrigin = new JLabel("Origin:");
		lblOrigin.setFont(new Font("Tahoma", Font.BOLD, 11));		
		JLabel lblX = new JLabel("x:");		
		JLabel lblY = new JLabel("y:");		
		originTextX = new JTextField();
		originTextX.setHorizontalAlignment(SwingConstants.CENTER);
		originTextX.setText("0");
		originTextX.setColumns(10);	
		originTextY = new JTextField();
		originTextY.setHorizontalAlignment(SwingConstants.CENTER);
		originTextY.setText("0");
		originTextY.setColumns(10);		
		JButton originSetX = new JButton("Set");
		originSetX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float x = Float.parseFloat(originTextX.getText())/areaSize*1000;
				float y = Float.parseFloat(originTextY.getText())/areaSize*1000;
				if (selectedShapes.size() == 1) {
					Shape s = selectedShapes.get(0);
    				if ((s instanceof MyRectangle2D)) {
	        			((MyRectangle2D) s).moveTo(x,y);
					}
					else if((s instanceof MyLine2D)) {
	        			((MyLine2D) s).moveTo((int)x,(int)y);
					}
					else if((s instanceof MyEllipse2D)) {
	        			((MyEllipse2D) s).moveTo(x,y);
					}
				}
				else{
					for (Shape s : selectedShapes) {
						float xCalc = (float) (x-ps.selectingBox.getX());
						float yCalc = (float) (y-ps.selectingBox.getY());
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).move(xCalc,yCalc);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).move((int)xCalc,(int)yCalc);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).move(xCalc,yCalc);
						}
				}					
				}
				ps.selectingBox.moveTo(x, y);
				ps.repaint();
			}
		});
		
		//MoveSidePanel
		JLabel lblMove = new JLabel("Move:");
		lblMove.setFont(new Font("Tahoma", Font.BOLD, 11));		
		JLabel lblX_1 = new JLabel("x:");		
		JLabel lblY_1 = new JLabel("y:");		
		moveTextX = new JTextField();
		moveTextX.setHorizontalAlignment(SwingConstants.CENTER);
		moveTextX.setText("0");
		moveTextX.setColumns(10);		
		moveTextY = new JTextField();
		moveTextY.setText("0");
		moveTextY.setHorizontalAlignment(SwingConstants.CENTER);
		moveTextY.setColumns(10);		
		JButton btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float x = Float.parseFloat(moveTextX.getText());
				float y = Float.parseFloat(moveTextY.getText());
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).move(x,y);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).move((int)x,(int)y);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).move(x,y);
						}
				}
				ps.selectingBox.move(x, y);
				ps.repaint();
			}
		});
		
		
		//SourceSidePanel
		JLabel lblSourcedata = new JLabel("SourceData:");
		lblSourcedata.setFont(new Font("Tahoma", Font.BOLD, 11));		
		JLabel lblAmplitude = new JLabel("Amp.:");		
		JLabel lblFrequeny = new JLabel("WaveL.:");		
		JLabel lblPhase = new JLabel("Phase:");	
		JLabel lblZerf = new JLabel("Zerf.:");
		amplitudeText = new JTextField();
		amplitudeText.setHorizontalAlignment(SwingConstants.CENTER);
		amplitudeText.setText("0");
		amplitudeText.setColumns(10);		
		waveLText = new JTextField();
		waveLText.setHorizontalAlignment(SwingConstants.CENTER);
		waveLText.setText("0");
		waveLText.setColumns(10);		
		phaseText = new JTextField();
		phaseText.setHorizontalAlignment(SwingConstants.CENTER);
		phaseText.setText("0");
		phaseText.setColumns(10);		
		zerfallsKText = new JTextField();
		zerfallsKText.setHorizontalAlignment(SwingConstants.CENTER);
		zerfallsKText.setText("0");
		zerfallsKText.setColumns(10);
		
		JButton amplitudeSet = new JButton("Set");
		amplitudeSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float amp = Float.parseFloat(amplitudeText.getText());
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).setAmplitude(amp);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).setAmplitude(amp);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).setAmplitude(amp);
						}
				}
				ps.repaint();
			}
		});
		
		JButton frequencySet = new JButton("Set");
		frequencySet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float freq = (float) (waveSpeed/Float.parseFloat(waveLText.getText()));
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).setFrequency(freq);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).setFrequency(freq);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).setFrequency(freq);
						}
				}
				ps.repaint();
			}
		});
		
		JButton phaseSet = new JButton("Set");
		phaseSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float phase = Float.parseFloat(phaseText.getText());
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).setPhase(phase);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).setPhase(phase);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).setPhase(phase);
						}
				}
				ps.repaint();
			}
		});
		
		JButton zerfallsKSet = new JButton("Set");
		zerfallsKSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				float zerfallsK = Float.parseFloat(zerfallsKText.getText());
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			((MyRectangle2D) s).setZerfallsK(zerfallsK);
						}
						else if((s instanceof MyLine2D)) {
		        			((MyLine2D) s).setZerfallsK(zerfallsK);
						}
						else if((s instanceof MyEllipse2D)) {
		        			((MyEllipse2D) s).setZerfallsK(zerfallsK);
						}
				}
				ps.repaint();
			}
		});
		
		comboBox_1 = new JComboBox<String>();
		comboBox_1.setMaximumRowCount(7);
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				String zerfallsType = (String) comboBox_1.getSelectedItem();
					for (Shape s : selectedShapes) {
						if ((s instanceof MyRectangle2D)) {
		        			if (zerfallsType != "") {
		        				((MyRectangle2D) s).setZerfallsType(zerfallsType);
		        			}
						}
						else if((s instanceof MyLine2D)) {
							if (zerfallsType != "") {
								((MyLine2D) s).setZerfallsType(zerfallsType);
							}
						}
						else if((s instanceof MyEllipse2D)) {
							if (zerfallsType != "") {
								((MyEllipse2D) s).setZerfallsType(zerfallsType);
							}
						}
				}
				ps.repaint();
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"linear", "exponential",""}));
		
		
		
		JLabel lblType = new JLabel("type:");
		
		JLabel lblNm_2 = new JLabel("um");
		GroupLayout gl_sidePanel = new GroupLayout(sidePanel);
		gl_sidePanel.setHorizontalGroup(
			gl_sidePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sidePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMove)
						.addComponent(lblSourcedata)
						.addGroup(gl_sidePanel.createSequentialGroup()
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFrequeny)
								.addComponent(lblPhase)
								.addComponent(lblZerf))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_sidePanel.createSequentialGroup()
									.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(amplitudeText, 0, 0, Short.MAX_VALUE)
										.addComponent(zerfallsKText, 0, 0, Short.MAX_VALUE)
										.addComponent(phaseText, 0, 0, Short.MAX_VALUE)
										.addComponent(waveLText, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
									.addGap(1)
									.addComponent(lblNm_2)
									.addGap(18)
									.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
										.addComponent(zerfallsKSet, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
										.addComponent(phaseSet)
										.addComponent(frequencySet)
										.addComponent(amplitudeSet)))
								.addComponent(lblType)
								.addGroup(gl_sidePanel.createSequentialGroup()
									.addGap(30)
									.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblOrigin)
							.addGroup(gl_sidePanel.createSequentialGroup()
								.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
									.addComponent(lblAmplitude)
									.addGroup(gl_sidePanel.createSequentialGroup()
										.addComponent(lblX)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(originTextX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_sidePanel.createSequentialGroup()
										.addComponent(lblY)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(originTextY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_sidePanel.createSequentialGroup()
										.addComponent(lblX_1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(moveTextX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_sidePanel.createSequentialGroup()
										.addComponent(lblY_1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(moveTextY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
									.addComponent(btnMove)
									.addComponent(originSetX)))))
					.addGap(52))
		);
		gl_sidePanel.setVerticalGroup(
			gl_sidePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sidePanel.createSequentialGroup()
					.addContainerGap(22, Short.MAX_VALUE)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_sidePanel.createSequentialGroup()
							.addComponent(lblOrigin)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblX)
								.addComponent(originTextX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblY)
								.addComponent(originTextY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMove)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_sidePanel.createSequentialGroup()
							.addComponent(originSetX)
							.addGap(39)))
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_sidePanel.createSequentialGroup()
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblX_1)
								.addComponent(moveTextX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblY_1)
								.addComponent(moveTextY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblSourcedata))
						.addGroup(gl_sidePanel.createSequentialGroup()
							.addGap(13)
							.addComponent(btnMove)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAmplitude)
						.addComponent(amplitudeSet)
						.addComponent(amplitudeText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrequeny)
						.addComponent(waveLText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(frequencySet)
						.addComponent(lblNm_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPhase)
						.addComponent(phaseText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(phaseSet))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(zerfallsKText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblZerf)
						.addComponent(zerfallsKSet))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_sidePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblType))
					.addGap(683))
		);
		sidePanel.setLayout(gl_sidePanel);
		
		
		
		
		
	}
	
	/*//Depricated (for Python)
	private void saveWaveFile() {
        toTxtExport(createPointSources());
	}*/
	
	//Erstellt PointSources
	private ArrayList<PointSource> createPointSources() {
		shapesSaved = ps.shapes;
		pointsSaved = ps.getPoints();

		ArrayList<PointSource> allSources = new ArrayList<PointSource>();
		ArrayList<PointSource> pointsFromPoints = pointToPoint(pointsSaved);
        allSources.addAll(pointsFromPoints);
        return allSources;
	}
	
	//Erstellt LineSources
	private ArrayList<LineSource> createLineSources() {
		shapesSaved = ps.shapes;
		linesSaved = ps.getLines();
		rectanglesSaved = ps.getRectangles();
		ArrayList<LineSource> allSources = new ArrayList<LineSource>();
        ArrayList<LineSource> pointsFromLines = lineToLine(linesSaved);
        ArrayList<LineSource> pointsFromRectangles = rectToLine(rectanglesSaved);
        allSources.addAll(pointsFromLines);
        allSources.addAll(pointsFromRectangles);
        return allSources;
	}
	
	//Speichere Alle Daten in einer JSON Datei
	@SuppressWarnings({ "unchecked" })
	private void saveJSON() {
		shapesSaved = ps.shapes;
		JSONArray savedShapes = new JSONArray();
		for (Shape s : shapesSaved) {
			JSONObject currentShapeDetails = new JSONObject();
			JSONObject currentShapeObject = new JSONObject();
			if (s instanceof MyRectangle2D) {
				MyRectangle2D r = (MyRectangle2D) s;
				currentShapeDetails.put("X", r.getX());
				currentShapeDetails.put("Y", r.getY());
				currentShapeDetails.put("Width", r.getWidth());
				currentShapeDetails.put("Height", r.getHeight());
				currentShapeDetails.put("isSelected", r.isSelected());
				currentShapeDetails.put("isFilled", r.isFilled());
				currentShapeDetails.put("Color", r.getColor());
				currentShapeDetails.put("amplitude", r.getAmplitude());
				currentShapeDetails.put("frequency", r.getFrequency());
				currentShapeDetails.put("phase", r.getPhase());
				currentShapeDetails.put("zerfallsK", r.getZerfallsK());
				currentShapeDetails.put("zerfallsType", r.getZerfallsType());
				
				currentShapeObject.put("MyRectangle2D", currentShapeDetails);
				savedShapes.add(currentShapeObject);
			}
			else if (s instanceof MyLine2D) {
				MyLine2D l = (MyLine2D) s;
				currentShapeDetails.put("X1", l.getX1());
				currentShapeDetails.put("Y1", l.getY1());
				currentShapeDetails.put("X2", l.getX2());
				currentShapeDetails.put("Y2", l.getY2());
				currentShapeDetails.put("Speed", l.getSpeed());
				currentShapeDetails.put("Color", l.getColor());
				currentShapeDetails.put("isSelected", l.isSelected());
				currentShapeDetails.put("amplitude", l.getAmplitude());
				currentShapeDetails.put("frequency", l.getFrequency());
				currentShapeDetails.put("phase", l.getPhase());
				currentShapeDetails.put("zerfallsK", l.getZerfallsK());
				currentShapeDetails.put("zerfallsType", l.getZerfallsType());
				
				currentShapeObject.put("MyLine2D", currentShapeDetails);
				savedShapes.add(currentShapeObject);
			}
			else if (s instanceof MyEllipse2D) {
				MyEllipse2D e = (MyEllipse2D) s;
				currentShapeDetails.put("X", e.getX());
				currentShapeDetails.put("Y", e.getY());
				currentShapeDetails.put("Width", e.getWidth());
				currentShapeDetails.put("Height", e.getHeight());
				currentShapeDetails.put("Speed", e.getSpeed());
				currentShapeDetails.put("isFilled", e.isFilled());
				currentShapeDetails.put("Color", e.getColor());
				currentShapeDetails.put("isSelected", e.isSelected());
				currentShapeDetails.put("amplitude", e.getAmplitude());
				currentShapeDetails.put("frequency", e.getFrequency());
				currentShapeDetails.put("phase", e.getPhase());
				currentShapeDetails.put("zerfallsK", e.getZerfallsK());
				currentShapeDetails.put("zerfallsType", e.getZerfallsType());
				
				currentShapeObject.put("MyEllipse2D", currentShapeDetails);
				savedShapes.add(currentShapeObject);
			}
			
		}
         
        //Write JSON file
	    File directory = new File(filePath+"saves\\");
	    if (! directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
	    
        try (FileWriter file = new FileWriter(filePath+"saves\\"+filename+".json")) {
 
            file.write(savedShapes.toJSONString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void setMode(String mode) {
		ps.mode(mode);
	}

	//Hilfsfunktion die aus InterfacePoints Quellen erzeugt
	private ArrayList<PointSource> pointToPoint(ArrayList<MyEllipse2D> inp) {
		ArrayList<PointSource> sources = new ArrayList<PointSource>();
        Iterator<MyEllipse2D> iter  = inp.iterator(); 
        while (iter.hasNext()) {
        	MyEllipse2D currentPoint = iter.next();
        	PointSource pointSource = new PointSource(currentPoint.getAmplitude(),(float) currentPoint.getFrequency(),currentPoint.getPhase(),((float) currentPoint.getX()), ((float) currentPoint.getY()));
        	pointSource.setZerfallsK(currentPoint.getZerfallsK());
        	pointSource.setZerfallsType(currentPoint.getZerfallsType());
        	sources.add(pointSource);
         }
        return sources;
	}
	
	//Hilfsfunktion die aus InterfaceLinien LinienQuellen erzeugt
	private ArrayList<LineSource> lineToLine(ArrayList<MyLine2D> inp) {
		ArrayList<LineSource> sources = new ArrayList<LineSource>();
        Iterator<MyLine2D> iter  = inp.iterator(); 
        while (iter.hasNext()) {
        	MyLine2D currentLine = iter.next();
        	LineSource pointSource = new LineSource(currentLine.getAmplitude(),(float) currentLine.getFrequency(),currentLine.getPhase(),((float) currentLine.getX1()), ((float) currentLine.getY1()),((float) currentLine.getX2()),((float) currentLine.getY2()));
        	sources.add(pointSource);
         }
        return sources;
	}
	
	//Checkt ob die Pointsource schon vorhanden ist
	public boolean checkNotDouble(PointSource pointSource, ArrayList<PointSource> sources) {
		Iterator<PointSource> iter  = sources.iterator();
		PointSource currentSource;
		float sX = pointSource.getX();
		float sY = pointSource.getY();
		while (iter.hasNext()) {
			currentSource = iter.next();
			if ((currentSource.getX() == sX) && (currentSource.getY() == sY)) {
				return false;
			}
		}
		return true;
	}
	
	//Zerteilt die InterfaceRechtecke in 4 Linien
	private ArrayList<LineSource> rectToLine(ArrayList<MyRectangle2D> inp) {
		ArrayList<LineSource> lines = new ArrayList<LineSource>();
        Iterator<MyRectangle2D> iter  = inp.iterator();
		MyRectangle2D currentRectangle;
		ArrayList<MyLine2D> linesFromRect;
        while (iter.hasNext()) {
        	currentRectangle = iter.next();
        	linesFromRect = currentRectangle.toLines();
        	for (MyLine2D currentLine : linesFromRect) {
            	LineSource lineSource = new LineSource(Float.parseFloat(defaultAmplitude),Float.parseFloat(defaultFrequenz),Float.parseFloat(defaultPhase),((float) currentLine.getX1()), ((float) currentLine.getY1()),((float) currentLine.getX2()), ((float) currentLine.getY2()));
            	lines.add(lineSource);
        	}
        	}
        return lines;
      	}

	//Löscht alle aktuellen Elemente
	private void reset() {
		ps.shapes.clear();
		//ps.points.clear();
		//ps.lines.clear();
		//ps.rectangles.clear();
		ps.selectingBox.clear();
		ps.repaint();
	}
	
	//Liest eine gespeicherte JSON Datei aus
    private  void loadJSON()
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader(filePath+"saves\\"+filename+".json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray shapesLoaded = (JSONArray) obj;
             
            //Iterate over employee array
            for (Object s : shapesLoaded) {
            	parseLoadedShape((JSONObject) s);
            }
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
 
    //Wird gebraucht zum Laden
    private void parseLoadedShape(JSONObject shapePassed)
    {
        //Load Rectangles
    	try{
	        JSONObject shape = (JSONObject) shapePassed.get("MyRectangle2D");
	        Shape r = makeRectangle(((Double) shape.get("X")).intValue(), ((Double) shape.get("Y")).intValue(), ((Double) shape.get("X")).intValue() +  ((Double) shape.get("Width")).intValue(), ((Double) shape.get("Y")).intValue() + ((Double) shape.get("Height")).intValue(), ((Double) shape.get("amplitude")).floatValue(), ((Double) shape.get("frequency")).doubleValue(), ((Double) shape.get("phase")).floatValue(), ((Double) shape.get("zerfallsK")).doubleValue(), ((String) shape.get("zerfallsType")));
	        if ((boolean) shape.get("isSelected") == true){
	        	((MyRectangle2D) r).select();
	        }
	        else {
	        	((MyRectangle2D) r).unSelect();
	        }
	        if ((boolean) shape.get("isFilled") == true){
	        	((MyRectangle2D) r).fill();
	        }
	        else {
	        	((MyRectangle2D) r).unFill();
	        }
	        ((MyRectangle2D) r).setColor((Color) shape.get("Color"));
	        ps.shapes.add(r);
	        //ps.rectangles.add((MyRectangle2D) r);
    	}
    	catch(Exception e) {
    		 //e.printStackTrace();
    	}
    	
    	//LoadEllipses
    	try{
	        JSONObject shape = (JSONObject) shapePassed.get("MyEllipse2D");
	        Shape p = makePoint(((Double) shape.get("X")).intValue(), ((Double) shape.get("Y")).intValue(), 10, 10, ((Double) shape.get("amplitude")).floatValue(), ((Double) shape.get("frequency")).doubleValue(), ((Double) shape.get("phase")).floatValue(),((Double) shape.get("zerfallsK")).doubleValue(), ((String) shape.get("zerfallsType")));
	        if ((boolean) shape.get("isSelected") == true){
	        	((MyEllipse2D) p).select();
	        }
	        else {
	        	((MyEllipse2D) p).unSelect();
	        }
	        if ((boolean) shape.get("isFilled") == true){
	        	((MyEllipse2D) p).fill();
	        }
	        else {
	        	((MyEllipse2D) p).unFill();
	        }
	        ((MyEllipse2D) p).setColor((Color) shape.get("Color"));
	        ((MyEllipse2D) p).setSpeed((double) shape.get("Speed"));
	        ps.shapes.add(p);
	        //ps.points.add((MyEllipse2D) p);
    	}
    	catch(Exception e) {
    		//
    	}
    	
    	//LoadLines
    	try{
	        JSONObject shape = (JSONObject) shapePassed.get("MyLine2D");
	        Shape l = makeLine(((Double) shape.get("X1")).intValue(), ((Double) shape.get("Y1")).intValue(), ((Double) shape.get("X2")).intValue(), ((Double) shape.get("Y2")).intValue(), ((Double) shape.get("amplitude")).floatValue(), ((Double) shape.get("frequency")).doubleValue(), ((Double) shape.get("phase")).floatValue(),((Double) shape.get("zerfallsK")).doubleValue(), ((String) shape.get("zerfallsType")));
	        if ((boolean) shape.get("isSelected") == true){
	        	((MyLine2D) l).select();
	        }
	        else {
	        	((MyLine2D) l).unSelect();
	        }
	        ((MyLine2D) l).setColor((Color) shape.get("Color"));
	        ((MyLine2D) l).setSpeed((double) shape.get("Speed"));
	        ps.shapes.add(l);
	        //ps.lines.add((MyLine2D) l);
    	}
    	catch(Exception e) {
    		//
    	}
    }
	
    
    /*Depricated
	private void toTxtExport(ArrayList<PointSource> sources) {
		try{
			File r = new File(filePath+filename+".txt");
			if (r.exists() && r.isFile()) { r.delete(); }
			r.createNewFile();
			FileWriter pw = new FileWriter(r, false);
			PrintWriter pr = new PrintWriter(pw, false);
	        Iterator<PointSource> iter  = sources.iterator();
	        pr.println(areaSize);
	        pr.println(angle);
	        while (iter.hasNext()) { 
	          pr.println(iter.next());
	        }
	        pr.close();
	        pw.close();
			}catch(IOException e){}
	}

	public void runPython() throws IOException {
		// set up the command and parameter
		String[] cmd = new String[3];
		cmd[0] = "python"; // check version of installed python: python -V
		cmd[1] = pythonScriptPath;
		cmd[2] = filename;
		 
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		System.out.println("Starting...");
		 
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while((isRunningSim)&&(line = bfr.readLine()) != null) {
		// display each output line form python script
		System.out.println(line);
		}
	}*/
	
	public void runJava(ArrayList<PointSource> pointsPassed, ArrayList<LineSource> linesPassed, int fps, int areaSize, int resolution) throws InterruptedException {
			simulationPanelFrame = new JFrame("Simulation");
	        simulationPanel = new SimulationPanel();
	        simulationPanel.setPointSourceLocations(pointsPassed);
	        simulationPanel.setLineSourceLocations(linesPassed);
	        simulationPanel.setFps(fps);
	        simulationPanel.setResolution(resolution);
	        simulationPanel.setAreaSize(areaSize);
	        simulationPanel.setColored(colored);
	        simulationPanel.setNormiert(normiert);
	        simulationPanelFrame.getContentPane().add(simulationPanel);
	        simulationPanelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        simulationPanelFrame.pack();
	        simulationPanelFrame.setVisible(true);
	        if (runtype == "Realtime") {
	        	simulationPanel.myTimer.start();
	        }
	        else if (runtype == "Image"){
	        	simulationPanel.myTimer.stop();
	        	simulationPanel.saveImage = true;
	        	simulationPanel.saveFramesPassInfo(filePath+"images\\",filename,imageType);
	        }
	        else if (runtype == "Frames"){
	        	simulationPanel.saveFrames = true;
	        	simulationPanel.saveFramesPassInfo(filePath+"images\\",filename,imageType);
	        }
	        else if (runtype == "Iterations"){
	        	simulationPanel.myTimer.stop();
	        	simulationPanel.iterate = true;
	        	simulationPanel.setColored(false);
	        	simulationPanel.setNormiert(true);
	        	simulationPanel.iterationsData = ps.iterationsData;
	        	simulationPanel.saveFramesPassInfo(filePath+"images\\",filename,imageType);
	        	simulationPanel.iterationTimer.start();
	        }
    }
	
	
	//Schmeisst den RandomWaveGenerator an
	public void runWaveGenerator() {
		WaveGenerator = new WaveGenerator();
		WaveGenerator.setPS(ps);
		WaveGenerator.setTitle("WaveGenerator");
		WaveGenerator.setModal(true);
		WaveGenerator.setVisible(true);
    }
	
	public void runIterationsWindow() {
		IterationsWindow = new IterationsWindow();
		IterationsWindow.setPS(ps);
		IterationsWindow.setTitle("IterationsWindow");
		IterationsWindow.setModal(true);
		IterationsWindow.setVisible(true);
	}
	
	public void iterate(ArrayList<PointSource> pointsPassed, ArrayList<LineSource> linesPassed, int fps, int areaSize, int resolution) {
		System.out.println(iterationsData);
	}
	
	//Schliesst das Simulationsfenster
	public void stopJava() {
		simulationPanelFrame.dispose();
	}
	
	//Stoppt/Restartet den Timer für die Simulationsfensterframes
	public void pauseJava(boolean paused) {
		if (this.isRunningSim){
			isRunningSim = false;
			simulationPanel.myTimer.stop();
		}
		else {
			isRunningSim = true;
			simulationPanel.myTimer.start();
		}
	}
	
//Öffnet das Popupmenu
	private void addPopup(final PaintSurface ps, final JPopupMenu popup) {
		ps.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
					if (ps.selectingBox.menuPopped) {
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
			}
		});
	}
	
	//Updated das sidePanel wenn Neues ausgewählt
	private void addSidePanelUpdater(final PaintSurface ps) {
		ps.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				updateTextFieldsOnSidePanel();
			}
			public void mouseReleased(MouseEvent e) {
				updateTextFieldsOnSidePanel();
			}
			public void updateTextFieldsOnSidePanel() {
				ArrayList<Shape> selectedShapes = ps.checkOverlap(ps.selectingBox, ps.shapes);
				if (selectedShapes.size() == 1) {
//					System.out.println("update");
					Shape s = selectedShapes.get(0);
					if ((s instanceof MyRectangle2D)) {
						originTextX.setText((Double.toString(((MyRectangle2D) s).getX()/1000*areaSize)));
						originTextY.setText((Double.toString(((MyRectangle2D) s).getY()/1000*areaSize)));
						amplitudeText.setText((Double.toString(((MyRectangle2D) s).getAmplitude())));
						waveLText.setText((Double.toString(waveSpeed/((MyRectangle2D) s).getFrequency())));
						phaseText.setText((Double.toString(((MyRectangle2D) s).getPhase())));
						zerfallsKText.setText((Double.toString(((MyRectangle2D) s).getZerfallsK())));
						comboBox_1.setSelectedItem((((MyRectangle2D) s).getZerfallsType()));
					}
					else if((s instanceof MyLine2D)) {
						originTextX.setText((Double.toString(((MyLine2D) s).getX1()/1000*areaSize)));
						originTextY.setText((Double.toString(((MyLine2D) s).getY1()/1000*areaSize)));
						amplitudeText.setText((Double.toString(((MyLine2D) s).getAmplitude())));
						waveLText.setText((Double.toString(waveSpeed/((MyLine2D) s).getFrequency())));
						phaseText.setText((Double.toString(((MyLine2D) s).getPhase())));
						zerfallsKText.setText((Double.toString(((MyLine2D) s).getZerfallsK())));
						comboBox_1.setSelectedItem((((MyLine2D) s).getZerfallsType()));
					}
					else if((s instanceof MyEllipse2D)) {
						originTextX.setText((Double.toString(((MyEllipse2D) s).getX()/1000*areaSize)));
						originTextY.setText((Double.toString(((MyEllipse2D) s).getY()/1000*areaSize)));
						amplitudeText.setText((Double.toString(((MyEllipse2D) s).getAmplitude())));
						waveLText.setText((Double.toString(waveSpeed/((MyEllipse2D) s).getFrequency())));
						phaseText.setText((Double.toString(((MyEllipse2D) s).getPhase())));
						zerfallsKText.setText((Double.toString(((MyEllipse2D) s).getZerfallsK())));
						comboBox_1.setSelectedItem((((MyEllipse2D) s).getZerfallsType()));
					}
				}
				else{
					originTextX.setText((Double.toString((ps.selectingBox.getX()/1000*areaSize))));
					originTextY.setText((Double.toString((ps.selectingBox.getY()/1000*areaSize))));
					amplitudeText.setText("");
					waveLText.setText("");
					phaseText.setText("");
					zerfallsKText.setText("");
					comboBox_1.setSelectedItem("");
				}
				ps.repaint();
			}
		});
	}	

	
	//Erstelle die nötigen Shapes
    
	@SuppressWarnings("unused")
	private MyRectangle2D makeRectangle(int x1, int y1, int x2, int y2, float amp, float freq, float phase) {
		return new MyRectangle2D(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), amp, freq, phase);
    }
	
	private MyRectangle2D makeRectangle(int x1, int y1, int x2, int y2, float amp, double freq, float phase, double zerfallsK, String zerfallsType) {
		return new MyRectangle2D(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), amp, freq, phase, zerfallsK, zerfallsType);
    }
	
	
	@SuppressWarnings("unused")
	private MyLine2D makeLine(int x1, int y1, int x2, int y2, float amp, float freq, float phase) {
		return new MyLine2D(x1, y1, x2, y2, amp, freq, phase);
    }
	
	private MyLine2D makeLine(int x1, int y1, int x2, int y2, float amp, double freq, float phase, double zerfallsK, String zerfallsType) {
		return new MyLine2D(x1, y1, x2, y2, amp, freq, phase, zerfallsK, zerfallsType);
    }
	
	@SuppressWarnings("unused")
	private MyEllipse2D makePoint(int x1, int y1, int x2, int y2, float amp, float freq, float phase) {
		return new MyEllipse2D(x1, y1, x2, y2, amp, freq, phase);
    }
	
	private MyEllipse2D makePoint(int x1, int y1, int x2, int y2, float amp, double freq, float phase, double zerfallsK, String zerfallsType) {
		return new MyEllipse2D(x1, y1, x2, y2, amp, freq, phase, zerfallsK, zerfallsType);
    }
}

