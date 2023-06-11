package Code;

import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RESERVATIONMANAGEMENT extends JFrame {
    JFrame frame;
    private String userName;
    private String contactNumber;
    private Date date;
    private List<String> reservations;
    private String startTime;
    private String endTime;
    private JTextField purpose;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    /**
     * Create the application.
     */
    public RESERVATIONMANAGEMENT(String userName, String contactNumber, Date selectedDate, String startTime,
            String endTime) {
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.date = selectedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        reservations = loadReservations();

        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(null);

        JLabel reserveBtn = new JLabel("");

        reserveBtn.setBounds(463, 491, 80, 36);
        reserveBtn.setIcon(new ImageIcon(RESERVATIONMANAGEMENT.class.getResource("/IMAGES/reservebtn.png")));
        frame.getContentPane().add(reserveBtn);

        JLabel lblNewLabel = new JLabel(userName);
        lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lblNewLabel.setBounds(389, 133, 221, 26);
        frame.getContentPane().add(lblNewLabel);

        JLabel contactShow = new JLabel(contactNumber);
        contactShow.setHorizontalAlignment(SwingConstants.CENTER);
        contactShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        contactShow.setBounds(389, 200, 221, 26);
        frame.getContentPane().add(contactShow);

        JLabel startTimeShow = new JLabel(startTime);
        startTimeShow.setHorizontalAlignment(SwingConstants.CENTER);
        startTimeShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        startTimeShow.setBounds(142, 317, 221, 26);
        frame.getContentPane().add(startTimeShow);

        JLabel endTimeShow = new JLabel(endTime);
        endTimeShow.setHorizontalAlignment(SwingConstants.CENTER);
        endTimeShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        endTimeShow.setBounds(638, 322, 221, 26);
        frame.getContentPane().add(endTimeShow);

        JLabel dateShow = new JLabel();
        dateShow.setHorizontalAlignment(SwingConstants.CENTER);
        dateShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        dateShow.setBounds(389, 279, 221, 26);
        frame.getContentPane().add(dateShow);

        // Convert the selectedDate to a string representation
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        dateShow.setText(dateString);

        purpose = new JTextField();
        purpose.setBounds(426, 362, 156, 36);
        frame.getContentPane().add(purpose);
        purpose.setColumns(10);
        
        JLabel backBtn = new JLabel("");
        backBtn.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
				AVAILABILITYCHECKING a = new AVAILABILITYCHECKING(userName,contactNumber);
				a.setVisible(true);
				frame.dispose();
        	}
        });
        backBtn.setIcon(new ImageIcon(RESERVATIONMANAGEMENT.class.getResource("/Code/IMAGES/backButton.png")));
        backBtn.setBounds(29, 101, 32, 36);
        frame.getContentPane().add(backBtn);
        
                JLabel managementImg = new JLabel("");
                managementImg.setBounds(0, 0, 984, 561);
                managementImg.setIcon(new ImageIcon(RESERVATIONMANAGEMENT.class.getResource("/IMAGES/RESERVATIONMANAGEMENT.png")));
                frame.getContentPane().add(managementImg);

        //Event Listeners
        // Inside the initialize() method
        reserveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	
                String enteredPurpose = purpose.getText();

            	StringBuilder receiptBuilder = new StringBuilder();
                receiptBuilder.append("Reservation Details:\n");
                receiptBuilder.append("Name: ").append(userName).append("\n");
                receiptBuilder.append("Contact Number: ").append(contactNumber).append("\n");
                receiptBuilder.append("Date: ").append(dateString).append("\n");
                receiptBuilder.append("Start Time: ").append(startTime).append("\n");
                receiptBuilder.append("End Time: ").append(endTime).append("\n");
                receiptBuilder.append("Purpose: ").append(enteredPurpose).append("\n");
                receiptBuilder.append("THIS WILL SERVE AS YOUR RECEIPT SCREENSHOT THIS!").append("\n");
                

                // Show the receipt message in a JOptionPane
                JOptionPane.showMessageDialog(frame, receiptBuilder.toString(), "Reservation Receipt",
                        JOptionPane.INFORMATION_MESSAGE);
                
            	reserve(date,startTime,endTime);
            }
        });
    }
    private void reserve(Date date, String startTime, String endTime) {
    	if (date != null && startTime != null && endTime != null) {
            String selectedStartDateTime = getFormattedDateTime(date, startTime);
            String selectedEndDateTime = getFormattedDateTime(date, endTime);
            if (isTimeRangeAvailable(selectedStartDateTime, selectedEndDateTime)) {
                String selectedDateTime = selectedStartDateTime + " - " + selectedEndDateTime;
                reservations.add(selectedDateTime);
                saveReservations(reservations);
                JOptionPane.showMessageDialog(null, "Reservation made.", "Success", JOptionPane.INFORMATION_MESSAGE);
                try {
                    FileWriter writer = new FileWriter("reservation.txt", true);
                    writer.write(selectedDateTime + "\n");
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
             }}
    }
    private void saveReservations(List<String> reservations) {
        try {
            FileWriter writer = new FileWriter("reservations.txt");
            for (String reservation : reservations) {
                writer.write(reservation + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private String getFormattedDateTime(Date date, String time) {
        return String.format("%tF %s", date, time);
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
}
