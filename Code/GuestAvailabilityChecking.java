package Code;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import Code.AVAILABILITYCHECKING.ButtonMouseAdapter;

import java.awt.Color;
import java.awt.Font;
import java.io.FileWriter;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class GuestAvailabilityChecking extends JFrame  {
    private static final long serialVersionUID = 1L;
    private List<String> reservations;
    private Date selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private JTextArea textArea;
	protected Object frame;
	

    public GuestAvailabilityChecking() {
        initialize();
        reservations = loadReservations();
    }
    
    private void initialize () {
        setTitle("Calendar");
        setBounds(100, 100, 1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // Create and configure components
        JDateChooser daychecker = new JDateChooser();
        daychecker.setBounds(186, 150, 243, 20);
        daychecker.setForeground(new Color(0, 0, 0));
        daychecker.setDateFormatString("MMMM d, y");

        List<String> startOptions = generateTimeOptionsStart();
        DefaultComboBoxModel<String> comboBoxModelStart = new DefaultComboBoxModel<>(startOptions.toArray(new String[0]));

        List<String> endOptions = generateTimeOptionsEnd();
        DefaultComboBoxModel<String> comboBoxModelEnd = new DefaultComboBoxModel<>(endOptions.toArray(new String[0]));

        JComboBox<String> startBox = new JComboBox<>(comboBoxModelStart);
        startBox.setBounds(186, 305, 195, 22);

        JComboBox<String> endBox = new JComboBox<>(comboBoxModelEnd);
        endBox.setBounds(186, 355, 195, 22);

        // Add components to the content pane
        getContentPane().add(daychecker);
        getContentPane().add(startBox);
        getContentPane().add(endBox);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(500, 150, 410, 250);
        getContentPane().add(scrollPane_1);
        
        
        textArea = new JTextArea();
        scrollPane_1.setViewportView(textArea);
        textArea.setEditable(false);

        // Register event listeners
        daychecker.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDate = daychecker.getDate();
                String selectedDateString = String.format("Selected Date: %tF", selectedDate);
                System.out.println(selectedDateString);         
            }
        });

        endBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedEndTime = (String) endBox.getSelectedItem();
                System.out.println("Selected end time: " + selectedEndTime);
            }
        });

        startBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedStartTime = (String) startBox.getSelectedItem();
                System.out.println("Selected start time: " + selectedStartTime);
            }
        });
                
        JPanel checkPanel = new JPanel();
		checkPanel.setOpaque(false);
		checkPanel.setBounds(157, 414, 140, 40);
		getContentPane().add(checkPanel);
		checkPanel.setLayout(null);
		
		JLabel checkBtn = new JLabel("");
		checkBtn.setBounds(5, 5, 125, 30);
		checkPanel.add(checkBtn);
		checkBtn.setIcon(new ImageIcon(GuestAvailabilityChecking.class.getResource("/IMAGES/availbtn.png")));
		
		JLabel checkBig = new JLabel("");
		checkPanel.addMouseListener(new ButtonMouseAdapter(checkBtn, checkBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		checkBig.setIcon(new ImageIcon(GuestAvailabilityChecking.class.getResource("/IMAGES/availBig.png")));
		checkBig.setBounds(0, 0, 140, 40);
		checkBig.setVisible(false);
		checkPanel.add(checkBig);
	
		JPanel seePanel = new JPanel();
		seePanel.setOpaque(false);
		seePanel.setBounds(500, 100, 140, 45);
		getContentPane().add(seePanel);
		seePanel.setLayout(null);
		
		JLabel seeBtn = new JLabel("");
		seeBtn.setBounds(5, 5, 128, 36);
		seePanel.add(seeBtn);
		seeBtn.setIcon(new ImageIcon(GuestAvailabilityChecking.class.getResource("/IMAGES/reservationsbtn.png")));
		
		JLabel seeBig = new JLabel("");
		seePanel.addMouseListener(new ButtonMouseAdapter(seeBtn, seeBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				reservations = loadReservations();
		        StringBuilder sb = new StringBuilder();
		        for (String reservation : reservations) {
		            sb.append(reservation).append("\n");
		        }
		        textArea.setText(sb.toString());
			}
		});
		seeBig.setIcon(new ImageIcon(GuestAvailabilityChecking.class.getResource("/IMAGES/reservationsBig.png")));
		seeBig.setHorizontalAlignment(SwingConstants.CENTER);
		seeBig.setBounds(0, 0, 140, 45);
		seeBig.setVisible(false);
		seePanel.add(seeBig);
		
		JPanel reservePanel = new JPanel();
		reservePanel.setOpaque(false);
		reservePanel.setBounds(460, 480, 100, 57);
		getContentPane().add(reservePanel);
		reservePanel.setLayout(null);
		reservePanel.setVisible(false);
		
		JLabel reserveBtn = new JLabel("");
		reserveBtn.setHorizontalAlignment(SwingConstants.CENTER);
		reserveBtn.setBounds(0, 11, 100, 36);
		reservePanel.add(reserveBtn);
		reserveBtn.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reservebtn.png")));
		
		JLabel reserveBig = new JLabel("");
		
		reserveBig.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/reserveBig.png")));
		reserveBig.setBounds(0, 11, 100, 35);
		reserveBig.setVisible(false);
		reservePanel.add(reserveBig);
		
		
		reservePanel.addMouseListener(new ButtonMouseAdapter(reserveBtn, reserveBig) {
			@Override
			public void mouseClicked(MouseEvent e) {
				GuestReservationManagement r = new GuestReservationManagement(selectedDate, selectedStartTime, selectedEndTime);
				r.frmGuestReservationManagement.setVisible(true);
				dispose();
				
			}
		});
		
		checkPanel.addMouseListener(new ButtonMouseAdapter(checkBtn, checkBig) {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // Check if the selected date is null
		    	// Check availability logic here
		    	 if (selectedDate == null) {
			            JOptionPane.showMessageDialog(null, "Please select a date.");
			            return; // Exit the method early
			        }
		    	 if (selectedStartTime.equals(selectedEndTime)) {
			            JOptionPane.showMessageDialog(null, "Cannot make reservation. Start time and end time are the same.");
		    	 } else {
		    	 if (selectedDate != null && selectedStartTime != null && selectedEndTime != null) {
		            String selectedStartDateTime = getFormattedDateTime(selectedDate, selectedStartTime);
		            String selectedEndDateTime = getFormattedDateTime(selectedDate, selectedEndTime);
		            
		            if (isTimeRangeAvailable(selectedStartDateTime, selectedEndDateTime)) {
		                JOptionPane.showMessageDialog(null, "Date and time are available.", "Availability", JOptionPane.INFORMATION_MESSAGE);
		                reservePanel.setVisible(true);
		            } else {
		                JOptionPane.showMessageDialog(null, "Date and time are not available.", "Availability", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Please select a date and time range.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    	 }
		    }
		});
		
		JLabel backBtn = new JLabel("");
		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WELCOME.main(null);
				dispose();
			}
		});
		backBtn.setIcon(new ImageIcon(GuestAvailabilityChecking.class.getResource("/Code/IMAGES/backButton.png")));
		backBtn.setBounds(922, 89, 32, 39);
		getContentPane().add(backBtn);
		
		JLabel availImg = new JLabel("");
		availImg.setIcon(new ImageIcon(AVAILABILITYCHECKING.class.getResource("/IMAGES/AVAILABILITYCHECKING.png")));
		availImg.setBounds(0, 0, 984, 561);
		getContentPane().add(availImg);

    }
    private List<String> generateTimeOptionsStart() {
        List<String> options = new ArrayList<>();
        for (int hour = 7; hour <= 22; hour++) {
            options.add(String.format("%02d:00", hour));
        }
        return options;
    }

    private List<String> generateTimeOptionsEnd() {
        List<String> options = new ArrayList<>();
        for (int hour = 7; hour <= 22; hour++) {
            options.add(String.format("%02d:00", hour));
        }
        return options;
    }

    private String getFormattedDateTime(Date date, String time) {
        return String.format("%tF %s", date, time);
    }

    private boolean isTimeRangeAvailable(String startDateTime, String endDateTime) {
        for (String reservation : reservations) {
            String[] parts = reservation.split(" - ");
            String existingStartDateTime = parts[0];
            String existingEndDateTime = parts[1];
            if (isDateTimeOverlap(startDateTime, endDateTime, existingStartDateTime, existingEndDateTime)) {
                return false; // Reservation time range overlaps with existing reservation
            }
        }
        return true; // Reservation time range is available
    }

    private boolean isDateTimeOverlap(String startDateTime1, String endDateTime1, String startDateTime2, String endDateTime2) {
        // Convert date-time strings to Date objects for comparison
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date start1 = format.parse(startDateTime1);
            Date end1 = format.parse(endDateTime1);
            Date start2 = format.parse(startDateTime2);
            Date end2 = format.parse(endDateTime2);

            // Check for overlap
            return start1.before(end2) && start2.before(end1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> loadReservations() {
        List<String> reservations = new ArrayList<>();
        try {
            Path path = Paths.get("reservation.txt");
            if (Files.exists(path)) {
                reservations = Files.readAllLines(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AVAILABILITYCHECKING window = new AVAILABILITYCHECKING();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    private class ButtonMouseAdapter extends MouseAdapter{
		JLabel label;
		JLabel bigLabel;
		public ButtonMouseAdapter(JLabel label, JLabel bigLabel) {
			this.label = label;
			this.bigLabel = bigLabel;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			label.setVisible(false);
			bigLabel.setVisible(true);
		}
		public void mouseExited(MouseEvent e) {
			label.setVisible(true);
			bigLabel.setVisible(false);
		}
		public void mousePressed(MouseEvent e) {
			label.setVisible(true);
			bigLabel.setVisible(false);
		}
		public void mouseReleased(MouseEvent e) {
			label.setVisible(false);
			bigLabel.setVisible(true);
		}	
    }
}