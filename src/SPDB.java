//package com.nitin.csci585;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import oracle.spatial.geometry.JGeometry;

public class HW2 extends JFrame implements ActionListener{

	//HW2 hw2 = new HW2();
	Connection mainConnection = null;
	private JPanel contentPane;
	JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton rdbtnWholeRegion;
	JRadioButton rdbtnPointQuery;
	JRadioButton rdbtnRangeQuery;
	JRadioButton rdbtnSurroundingStudent;
	JRadioButton rdbtnEmergencyQuery;
	JCheckBox chkboxAs;
	JCheckBox chkboxBuildings;
	JCheckBox chkboxStudents;
	JTextArea displayQuery;
	JScrollPane scrollPane;
	JTextField mouseLocation;
	ArrayList<Point> userPointInput = new ArrayList<Point>() ;
	String[] ASid = new String[10];
	static int queryCount = 0;
	boolean rightClick = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					HW2 frame = new HW2();
					frame.ConnectToDB();
					frame.setTitle("Nitin Dua - 4563947761");
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HW2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0,screen.width - 300,screen.height - 70);
		contentPane = new JPanel();
		//contentPane = new JFrame();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//contentPane.set
		setContentPane(contentPane);
		
		//panel = new JPanel();
		panel = new paintPanel();
		javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 820, Short.MAX_VALUE));
        imagePanelLayout.setVerticalGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 580, Short.MAX_VALUE));
        
		panel.setBounds(0, 0, 820, 580);
        contentPane.setLayout(null);
        contentPane.setLayout(null);
		contentPane.add(panel);
		
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            	if(rdbtnPointQuery.isSelected())
            	{
            		if(userPointInput.size() != 0)
            		{
            			userPointInput.clear();		//only the most recent point is stored
            			paintPanel.clearImage(panel.getGraphics());
            		}
            			
	            	if(evt.getButton() == MouseEvent.BUTTON1);
	            	{
	            		paintPanel.drawUserPoint(panel.getGraphics(), evt.getPoint());
	            		userPointInput.add(evt.getPoint()) ;
	            	}
            	}
            	if(rdbtnRangeQuery.isSelected())
            	{
            		if(evt.getButton() == MouseEvent.BUTTON1)			//left click
            		{
            			if(rightClick == true)
            			{
            				paintPanel.clearImage(panel.getGraphics());
            				userPointInput.clear();
            				rightClick = false;
            			}
            			if(userPointInput.size() == 0)
            			{
            				userPointInput.add(evt.getPoint());
            			}
            			else
            			{
            				userPointInput.add(evt.getPoint());
            				paintPanel.drawLine(panel.getGraphics(), evt.getPoint(), userPointInput.get(userPointInput.size() - 2));	//line between current point and the one just before
            				
            			}
            		}
            		else if(evt.getButton() == MouseEvent.BUTTON3)		//right click
            		{
            			//userPointInput.add(evt.getPoint());
            			rightClick = true;
            			paintPanel.drawLine(panel.getGraphics(), userPointInput.get(0), userPointInput.get(userPointInput.size() - 1));	//line between last point and first point
            		}
            	}
            	if(rdbtnSurroundingStudent.isSelected())
            	{		
            		if(evt.getButton() == MouseEvent.BUTTON1)
            		{
            			if(userPointInput.size() != 0)
                		{
            				paintPanel.clearImage(panel.getGraphics());;
                			userPointInput.clear();
                		}
            			userPointInput.add(evt.getPoint());
            			displayNearestAStoPoint(evt.getPoint());
            		}
            	}
            	if(rdbtnEmergencyQuery.isSelected())
            	{
            		if(evt.getButton() == MouseEvent.BUTTON1)
            		{
            			if(userPointInput.size() != 0)
                		{
                			paintPanel.clearImage(panel.getGraphics());;
                			userPointInput.clear();
                		}
            			userPointInput.add(evt.getPoint());
            			displayNearestAStoPoint(evt.getPoint());
            		}
            	}
            	
            }
   
        });
		
		displayQuery = new JTextArea();
		//displayQuery.setBounds(5, 606, 1035, 28);
		displayQuery.setColumns(20);
		displayQuery.setRows(10);
		displayQuery.setWrapStyleWord(true);
		contentPane.add(displayQuery);
		//scrollPane = new JScrollPane(displayQuery);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 590, 1035, 60);
		displayQuery.setRows(5);
		displayQuery.setEditable(false);
		scrollPane.getViewport().setView(displayQuery);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		JLabel label1 = new JLabel("Active Feature Type");
		label1.setFont(new Font("Arial", Font.BOLD, 18));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setBounds(847, 31, 193, 35);
		contentPane.add(label1);
		
		chkboxAs = new JCheckBox("AS");
		chkboxAs.setFont(new Font("Arial", Font.PLAIN, 15));
		chkboxAs.setHorizontalAlignment(SwingConstants.LEFT);
		chkboxAs.setBounds(864, 85, 48, 23);
		contentPane.add(chkboxAs);
		
		chkboxBuildings = new JCheckBox("Buildings");
		chkboxBuildings.setFont(new Font("Arial", Font.PLAIN, 15));
		chkboxBuildings.setHorizontalAlignment(SwingConstants.LEFT);
		chkboxBuildings.setBounds(945, 85, 85, 23);
		contentPane.add(chkboxBuildings);
		
		chkboxStudents = new JCheckBox("Students\r\n");
		chkboxStudents.setHorizontalAlignment(SwingConstants.LEFT);
		chkboxStudents.setFont(new Font("Arial", Font.PLAIN, 15));
		chkboxStudents.setBounds(899, 120, 85, 23);
		contentPane.add(chkboxStudents);
		
		JLabel label2 = new JLabel("Query");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setFont(new Font("Arial", Font.BOLD, 18));
		label2.setBounds(847, 176, 193, 35);
		contentPane.add(label2);
		
		rdbtnWholeRegion = new JRadioButton("Whole Region");
		rdbtnWholeRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userPointInput.clear();
				panel.repaint();
			}
		});
		buttonGroup.add(rdbtnWholeRegion);
		rdbtnWholeRegion.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtnWholeRegion.setBounds(857, 217, 187, 23);
		contentPane.add(rdbtnWholeRegion);
		
		rdbtnPointQuery = new JRadioButton("Point Query\r\n");
		rdbtnPointQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userPointInput.clear();
				panel.repaint();
				//clear panel (image)
			}
		});
		buttonGroup.add(rdbtnPointQuery);
		rdbtnPointQuery.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtnPointQuery.setBounds(857, 259, 187, 23);
		contentPane.add(rdbtnPointQuery);
		
		rdbtnRangeQuery = new JRadioButton("Range Query");
		rdbtnRangeQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userPointInput.clear();
				panel.repaint();
			}
		});
		buttonGroup.add(rdbtnRangeQuery);
		rdbtnRangeQuery.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtnRangeQuery.setBounds(857, 303, 187, 23);
		contentPane.add(rdbtnRangeQuery);
		
		rdbtnSurroundingStudent = new JRadioButton("Surrounding Student");
		rdbtnSurroundingStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userPointInput.clear();
				panel.repaint();
			}
		});
		buttonGroup.add(rdbtnSurroundingStudent);
		rdbtnSurroundingStudent.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtnSurroundingStudent.setBounds(857, 346, 187, 23);
		contentPane.add(rdbtnSurroundingStudent);
		
		rdbtnEmergencyQuery = new JRadioButton("Emergency Query");
		rdbtnEmergencyQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userPointInput.clear();
				panel.repaint();
			}
		});
		buttonGroup.add(rdbtnEmergencyQuery);
		rdbtnEmergencyQuery.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtnEmergencyQuery.setBounds(857, 389, 187, 23);
		contentPane.add(rdbtnEmergencyQuery);
		
		mouseLocation = new JTextField();
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseLocation.setText("( " + e.getX() + " , " + e.getY() + " )");
			}
		});
		mouseLocation.setBounds(882, 434, 100, 35);
		contentPane.add(mouseLocation);
		
		JButton SubmitQueryButton = new JButton("Submit Query");
		SubmitQueryButton.addActionListener(this);
		SubmitQueryButton.setFont(new Font("Arial", Font.PLAIN, 15));
		SubmitQueryButton.setBounds(852, 494, 167, 35);
		contentPane.add(SubmitQueryButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(rdbtnWholeRegion.isSelected())
		{
			paintPanel.clearImage(panel.getGraphics());
			if(chkboxStudents.isSelected() && !chkboxBuildings.isSelected() && !chkboxAs.isSelected())
			{
				displayStudents();
			}
			if(!chkboxStudents.isSelected() && chkboxBuildings.isSelected() && !chkboxAs.isSelected())
			{
				displayBuildings();
			}
			if(!chkboxStudents.isSelected() && !chkboxBuildings.isSelected() && chkboxAs.isSelected())
			{
				displayAS();
			}
			if(chkboxStudents.isSelected() && chkboxBuildings.isSelected() && !chkboxAs.isSelected())
			{
				displayStudents();
				displayBuildings();
			}
			if(chkboxStudents.isSelected() && !chkboxBuildings.isSelected() && chkboxAs.isSelected())
			{
				displayStudents();
				displayAS();
			}
			if(!chkboxStudents.isSelected() && chkboxBuildings.isSelected() && chkboxAs.isSelected())
			{
				displayBuildings();
				displayAS();
			}
			if(chkboxStudents.isSelected() && chkboxBuildings.isSelected() && chkboxAs.isSelected())
			{
				displayStudents();
				displayBuildings();
				displayAS();
			}
		}
		else if(rdbtnPointQuery.isSelected())
		{
			//paintPanel.clearImage(panel.getGraphics());
			displayPointQuery(userPointInput.get(0), 50);
		}
		else if(rdbtnRangeQuery.isSelected())
		{
			//paintPanel.clearImage(panel.getGraphics());
			displayRangeQuery(userPointInput);
		}
		else if(rdbtnSurroundingStudent.isSelected())
		{
			displaySurroundingStudentQuery(userPointInput.get(0));
		}
		else if(rdbtnEmergencyQuery.isSelected())
		{
			paintPanel.clearImage(panel.getGraphics());
			displayEmergencyQuery(userPointInput.get(0));
		}

	}
	
	
	public void displayStudents()
	{
		Statement stmt = null;
		int x;
		int y;
		String sql = "SELECT s.shape from STUDENTS s";
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		try
		{
			stmt = mainConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("shape");
				JGeometry jgeom = JGeometry.load(struct);
				Point2D point = jgeom.getJavaPoint();
				x = (int) point.getX();
				y = (int) point.getY();
				//draw the point
				paintPanel.drawStudents(panel.getGraphics(), x, y, Color.GREEN);
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public void displayAS()
	{
		Statement stmt = null;
		int x;
		int y;
		int radius;
		String sql = "SELECT a.radius, a.center from ANNOUNCEMENTSYSTEMS a";
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		try
		{
			stmt = mainConnection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("center");
				JGeometry jgeom = JGeometry.load(struct);
				Point2D point = jgeom.getJavaPoint();
				x = (int) point.getX();
				y = (int) point.getY();
				radius = rs.getInt("radius");
				//draw the point
				paintPanel.drawAS(panel.getGraphics(), x, y, Color.RED);
				//draw the circle
				paintPanel.drawASRange(panel.getGraphics(), x, y, radius, Color.RED);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void displayBuildings()
	{
		Statement stmt = null;
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		double[] points;
		String sql = "SELECT b.SHAPE from BUILDINGS b order by b.BUILDINGID";
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		try
		{
			stmt = mainConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				x.clear();
				y.clear();
				oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("shape");
				JGeometry jgeom = JGeometry.load(struct);
				points = jgeom.getOrdinatesArray();
				for(int i = 0; i < points.length; i++)
				{
					if((i % 2) == 0)
					{
						x.add((int)points[i]);
					}
					else
					{
						y.add((int)points[i]);
					}
				}
				//draw the building
				paintPanel.drawBoundaries(panel.getGraphics(), x, y, x.size(), Color.YELLOW);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void displayPointQuery(Point userPointInput, int radius)
	{
		Statement stmt = null;
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		double[] points;
		
		int pointX = (int) userPointInput.getX();
		int pointY = (int) userPointInput.getY();
		
		//create the circle polygon depending on user point and check for interaction with it in the tables
		if(chkboxBuildings.isSelected())
		{	
			Boolean minResult = false;
			String sql = "SELECT b.shape from BUILDINGS b where SDO_ANYINTERACT(b.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
					+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + "))) = 'TRUE'";
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					x.clear();
					y.clear();
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					points = jgeom.getOrdinatesArray();
					for(int i = 0; i < points.length; i++)
					{
						if((i % 2) == 0)
						{
							x.add((int)points[i]);
						}
						else
						{
							y.add((int)points[i]);
						}
					}
					//draw the building
					minResult = true;
					paintPanel.drawBoundaries(panel.getGraphics(), x, y, x.size(), Color.GREEN);
				}
				
				//sql = "SELECT b.shape from BUILDINGS b where SDO_NN(b.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
						//+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + ")), 'SDO_NUM_RES = 1') = 'TRUE'";
				if(minResult == true)
				{
					sql = "SELECT b.shape from BUILDINGS b where SDO_NN(b.shape, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("+ pointX + ", " + pointY + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'";
					queryCount ++;
					if(queryCount != 1)
						displayQuery.append("\n");
					displayQuery.append("Query " + queryCount + ": " + sql);
					
					stmt = mainConnection.createStatement();
					rs = stmt.executeQuery(sql);
					rs.next();
					x.clear();
					y.clear();
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					points = jgeom.getOrdinatesArray();
					for(int i = 0; i < points.length; i++)
					{
						if((i % 2) == 0)
						{
							x.add((int)points[i]);
						}
						else
						{
							y.add((int)points[i]);
						}
					}
					//draw the building
					paintPanel.drawBoundaries(panel.getGraphics(), x, y, x.size(), Color.YELLOW);
				}	
			
			} catch (SQLException e) {
				
				System.out.println(e.getMessage());
			}
		}	
		if(chkboxStudents.isSelected())
		{
			Boolean minResult = false;
			String sql = "SELECT s.shape from STUDENTS s where SDO_ANYINTERACT(s.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
					+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + "))) = 'TRUE'";
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					//draw the point
					paintPanel.drawStudents(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.GREEN);
					minResult = true;
				}
				if(minResult == true)
				{
					sql = "SELECT s.shape from STUDENTS s where SDO_NN(s.shape, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("+ pointX + ", " + pointY + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'";
					queryCount ++;
					if(queryCount != 1)
						displayQuery.append("\n");
					displayQuery.append("Query " + queryCount + ": " + sql);
					stmt = mainConnection.createStatement();
					rs = stmt.executeQuery(sql);
					rs.next();
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					//draw the point
					paintPanel.drawStudents(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.YELLOW);
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if(chkboxAs.isSelected())
		{
			boolean minResult = false;
			String sql = "";
			stmt = null;
			sql = "SELECT a.shape, a.radius, a.center from ANNOUNCEMENTSYSTEMS a where SDO_ANYINTERACT(a.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
					+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + "))) = 'TRUE'";
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("center");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					minResult = true;
					//draw the point
					paintPanel.drawAS(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.GREEN);
					paintPanel.drawASRange(panel.getGraphics(), (int) point.getX(),(int) point.getY(), (int) rs.getInt("radius"), Color.GREEN);
				}
				if(minResult == true)
				{
					sql = "SELECT a.shape, a.radius, a.center from ANNOUNCEMENTSYSTEMS a where SDO_NN(a.shape, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("+ pointX + ", " + pointY + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'";
					queryCount ++;
					if(queryCount != 1)
						displayQuery.append("\n");
					displayQuery.append("Query " + queryCount + ": " + sql);
					stmt = mainConnection.createStatement();
					rs = stmt.executeQuery(sql);
					rs.next();
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("center");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					//draw the point
					paintPanel.drawAS(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.YELLOW);
					paintPanel.drawASRange(panel.getGraphics(), (int) point.getX(),(int) point.getY(), (int) rs.getInt("radius"), Color.YELLOW);
				}	
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void displayRangeQuery(ArrayList<Point> userInputPoint)
	{
		Statement stmt = null;
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		double[] points;
		
		if(chkboxBuildings.isSelected())
		{
			String sql = "SELECT b.shape from BUILDINGS b where SDO_ANYINTERACT(b.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), "
					+ "SDO_ORDINATE_ARRAY(" ;
			
			for(int i = 0; i < userInputPoint.size(); i++)
			{
				sql += (int) userInputPoint.get(i).getX() + ", " + (int) userInputPoint.get(i).getY() + ", ";
			}
			//sql += (int) userInputPoint.get(userInputPoint.size() - 1).getX() + ", " + (int) userInputPoint.get(userInputPoint.size() - 1).getY() + "))) = 'TRUE'";
			sql += (int) userInputPoint.get(0).getX() + ", " + (int) userInputPoint.get(0).getY() + "))) = 'TRUE'";
			
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					x.clear();
					y.clear();
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					points = jgeom.getOrdinatesArray();
					for(int i = 0; i < points.length; i++)
					{
						if((i % 2) == 0)
						{
							x.add((int)points[i]);
						}
						else
						{
							y.add((int)points[i]);
						}
					}
					//draw the building
					paintPanel.drawBoundaries(panel.getGraphics(), x, y, x.size(), Color.YELLOW);
					
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		if(chkboxStudents.isSelected())
		{
			String sql = "";
			stmt = null;
			
			sql = "SELECT s.shape from STUDENTS s where SDO_ANYINTERACT(s.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), "
					+ "SDO_ORDINATE_ARRAY(" ;
			for(int i = 0; i < userInputPoint.size(); i++)
			{
				sql += (int) userInputPoint.get(i).getX() + ", " + (int) userInputPoint.get(i).getY() + ", ";
			}
			//sql += (int) userInputPoint.get(userInputPoint.size() - 1).getX() + ", " + (int) userInputPoint.get(userInputPoint.size() - 1).getY() + "))) = 'TRUE'";
			sql += (int) userInputPoint.get(0).getX() + ", " + (int) userInputPoint.get(0).getY() + "))) = 'TRUE'";
			
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("shape");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					//draw the point
					paintPanel.drawStudents(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.GREEN);
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}	
		if(chkboxAs.isSelected())
		{
			String sql = "";
			stmt = null;
			sql = "SELECT a.shape, a.center, a.radius from ANNOUNCEMENTSYSTEMS a where SDO_ANYINTERACT(a.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), "
					+ "SDO_ORDINATE_ARRAY(" ;
			for(int i = 0; i < userInputPoint.size(); i++)
			{
				sql += (int) userInputPoint.get(i).getX() + ", " + (int) userInputPoint.get(i).getY() + ", ";
			}
			//sql += (int) userInputPoint.get(userInputPoint.size() - 1).getX() + ", " + (int) userInputPoint.get(userInputPoint.size() - 1).getY() + "))) = 'TRUE'";
			sql += (int) userInputPoint.get(0).getX() + ", " + (int) userInputPoint.get(0).getY() + "))) = 'TRUE'";
			
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			try {
				stmt = mainConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					oracle.sql.STRUCT struct = (oracle.sql.STRUCT) rs.getObject("center");
					JGeometry jgeom = JGeometry.load(struct);
					Point2D point = jgeom.getJavaPoint();
					//draw the point
					paintPanel.drawAS(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.RED);
					paintPanel.drawASRange(panel.getGraphics(), (int) point.getX(),(int) point.getY(), (int) rs.getInt("radius"), Color.RED);
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());	
			}
		}	
		
	}
	
	public void displayNearestAStoPoint(Point userInputPoint)
	{
		Statement stmt = null;
		String sql = "SELECT a.shape, a.center, a.radius from ANNOUNCEMENTSYSTEMS a where SDO_NN(a.center, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("
				+ (int) userInputPoint.getX() + ", " + (int) userInputPoint.getY() + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'"; 
		
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		
		try {
			stmt = mainConnection.createStatement();
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("center");
			JGeometry jgeom = JGeometry.load(struct);
			Point2D point = jgeom.getJavaPoint();
			//draw the point
			paintPanel.drawAS(panel.getGraphics(), (int) point.getX(),(int) point.getY(), Color.RED);
			paintPanel.drawASRange(panel.getGraphics(), (int) point.getX(),(int) point.getY(), (int) rs.getInt("radius"), Color.RED);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void displaySurroundingStudentQuery(Point userInputPoint)
	{
		//Point ASPoint = getNearestAStoPoint(userInputPoint);
		Statement stmt = null;
		String sql = "SELECT a.shape, a.center, a.radius from ANNOUNCEMENTSYSTEMS a where SDO_NN(a.center, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("
				+ (int) userInputPoint.getX() + ", " + (int) userInputPoint.getY() + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'"; 
		
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		
		try {
			stmt = mainConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("center");
			JGeometry jgeom = JGeometry.load(struct);
			Point2D point = jgeom.getJavaPoint();
			int radius = (int) rs.getInt("radius");
			//draw the point
			int pointX = (int) point.getX();
			int pointY = (int) point.getY();
			
			stmt = null;
			sql = "";
			rs = null;
			
			sql = "SELECT s.shape from STUDENTS s where SDO_ANYINTERACT(s.shape,  SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
					+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + "))) = 'TRUE'";
			
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			stmt = mainConnection.createStatement();
			rs = stmt.executeQuery(sql);
			int x, y;
			while(rs.next())
			{
				struct = (oracle.sql.STRUCT) rs.getObject("shape");
				jgeom = JGeometry.load(struct);
				point = jgeom.getJavaPoint();
				x = (int) point.getX();
				y = (int) point.getY();
				//draw the point
				paintPanel.drawStudents(panel.getGraphics(), x, y, Color.GREEN);	
			}
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void displayEmergencyQuery(Point userInputPoint)
	{
		Color[] colorAS = { Color.CYAN, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.LIGHT_GRAY, Color.BLUE, Color.MAGENTA, Color.PINK, Color.WHITE, Color.GRAY};
		setASArray(ASid);
		Statement stmt = null;
		String sql = "SELECT a.asid,a.shape, a.center, a.radius from ANNOUNCEMENTSYSTEMS a where SDO_NN(a.center, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("
				+ (int) userInputPoint.getX() + ", " + (int) userInputPoint.getY() + ", NULL), NULL, NULL), 'SDO_NUM_RES = 1') = 'TRUE'"; 
		
		queryCount ++;
		if(queryCount != 1)
			displayQuery.append("\n");
		displayQuery.append("Query " + queryCount + ": " + sql);
		
		try {
			stmt = mainConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			oracle.sql.STRUCT struct = (oracle.sql.STRUCT)rs.getObject("center");
			JGeometry jgeom = JGeometry.load(struct);
			Point2D point = jgeom.getJavaPoint();
			int radius = (int) rs.getInt("radius");
			//draw the point
			int pointX = (int) point.getX();
			int pointY = (int) point.getY();
			int i;
			String brokenAS = rs.getString("asid");
			stmt = null;
			sql = "";
			rs = null;
			
			sql = "SELECT s.shape from STUDENTS s where SDO_ANYINTERACT(s.shape,  SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 4), "
					+ "SDO_ORDINATE_ARRAY(" + pointX + "," + (pointY + radius) + "," + pointX + "," + (pointY - radius) + "," + (pointX + radius) + "," + pointY + "))) = 'TRUE'";
			
			queryCount ++;
			if(queryCount != 1)
				displayQuery.append("\n");
			displayQuery.append("Query " + queryCount + ": " + sql);
			
			stmt = mainConnection.createStatement();
			rs = stmt.executeQuery(sql);
			int x, y;
			ResultSet rs1 = null;
			while(rs.next())
			{
				struct = (oracle.sql.STRUCT) rs.getObject("shape");
				jgeom = JGeometry.load(struct);
				point = jgeom.getJavaPoint();
				x = (int) point.getX();
				y = (int) point.getY();
				//draw the point
				//paintPanel.drawStudents(panel.getGraphics(), x, y, Color.GREEN);
				try{
				sql = "SELECT a.asid, a.shape, a.center, a.radius from ANNOUNCEMENTSYSTEMS a where a.asid <> '" + brokenAS + "' AND SDO_NN(a.shape, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE("
						+ x + ", " + y + ", NULL), NULL, NULL), 'SDO_NUM_RES = 2') = 'TRUE'";
				
				queryCount ++;
				if(queryCount != 1)
					displayQuery.append("\n");
				displayQuery.append("Query " + queryCount + ": " + sql);
				
				//System.out.println(sql);
				stmt = mainConnection.createStatement();
				rs1 = stmt.executeQuery(sql);
				rs1.next();
				for(i = 0; i < ASid.length; i++)
				{
					if(ASid[i].equalsIgnoreCase(rs1.getString("asid")))
						break;
				}
				
				paintPanel.drawStudents(panel.getGraphics(), x, y, colorAS[i]); 
				oracle.sql.STRUCT struct1 = (oracle.sql.STRUCT) rs1.getObject("center");
				JGeometry jgeom1 = JGeometry.load(struct1);
				Point2D point1 = jgeom1.getJavaPoint();

				paintPanel.drawAS(panel.getGraphics(), (int) point1.getX(),(int) point1.getY(), colorAS[i]);
				paintPanel.drawASRange(panel.getGraphics(), (int) point1.getX(),(int) point1.getY(), rs1.getInt("radius"), colorAS[i]);
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void setASArray(String[] ASid)
	{
		try {
			String sql = "SELECT a.asid from ANNOUNCEMENTSYSTEMS a";
			Statement stmt = mainConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while(rs.next())
			{
				ASid[i] = rs.getString("asid");
				i++;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void ConnectToDB()
 	{
		try
		{
			// loading Oracle Driver
	    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    	//System.out.println(", Loaded.");

	    	String URL = "jdbc:oracle:thin:@localhost:1521:hw2";
	    	String userName = "system";
	    	String password = "hw2";

	    	//System.out.print("Connecting to DB...");
	    	mainConnection = DriverManager.getConnection(URL, userName, password);
	    	//System.out.println(", Connected!");

    		//mainStatement = mainConnection.createStatement();

   		}
   		catch (Exception e)
   		{
     		System.out.println( "Error while connecting to DB: "+ e.toString() );
     		System.out.println(e.getMessage());
     		System.exit(-1);
   		}
 	}

}

class paintPanel extends JPanel
{
	private static BufferedImage map;
	
	public static void drawBoundaries(Graphics g, ArrayList<Integer> x, ArrayList<Integer> y, int verticesCount, Color c)
	{
		g.setColor(c);
		int[] coordinateX = new int[verticesCount];
		int[] coordinateY = new int[verticesCount];
		for(int i = 0; i < verticesCount; i++)
		{
			coordinateX[i] = x.get(i).intValue();
			coordinateY[i] = y.get(i).intValue();
		}
		g.drawPolygon(coordinateX, coordinateY, verticesCount);
	}
	
	
	public static void drawStudents(Graphics g, int x, int y, Color c)
	{
		g.setColor(c);
		g.fillRect(x - 5, y - 5, 10, 10);
	}
	
	
	public static void drawAS(Graphics g, int x, int y, Color c)
	{
		g.setColor(c);
		g.fillRect(x, y, 15, 15);
	}
	
	public static void drawASRange(Graphics g, int x, int y, int radius, Color c)
	{
		g.setColor(c);
		g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}
	
	public static void drawUserPoint(Graphics g, Point p)
	{
		g.setColor(Color.RED);
		g.fillRect((int) p.getX(), (int) p.getY(), 5, 5);
		g.drawOval((int) p.getX() - 50, (int) p.getY() - 50, 2*50, 2*50);
	}
	
	public static void drawLine(Graphics g, Point p1, Point p2)
	{
		g.setColor(Color.RED);
		g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
	}
	
	public paintPanel() 
	{
        try 
        {
        	//map = ImageIO.read(getClass().getResource("map.jpg"));
        	map = ImageIO.read(new File("map.jpg"));
        } catch (IOException e) {
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(map, 0, 0, null);

    }
   
	public static void clearImage(Graphics g)
	{
		g.drawImage(map, 0, 0, null);
	}
}
