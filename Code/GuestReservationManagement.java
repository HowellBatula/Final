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
import java.awt.Toolkit;

public class GuestReservationManagement extends JFrame {
    JFrame frmGuestReservationManagement;
    private String userName;
    private String contactNumber;
    private Date date;
    private List<String> reservations;
    private String startTime;
    private String endTime;
    private JTextField purpose;
    private JTextField name;
    private JTextField number;
	protected Object frame;

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
    public GuestReservationManagement(Date selectedDate, String startTime,String endTime) {

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
        frmGuestReservationManagement = new JFrame();
        frmGuestReservationManagement.setTitle("Guest Reservation Management");
        frmGuestReservationManagement.setIconImage(Toolkit.getDefaultToolkit().getImage(GuestReservationManagement.class.getResource("/Code/IMAGES/LASTHOME.png")));
        frmGuestReservationManagement.setResizable(false);
        frmGuestReservationManagement.setBounds(100, 100, 1000, 600);
        frmGuestReservationManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmGuestReservationManagement.setLocationRelativeTo(null);
        frmGuestReservationManagement.getContentPane().setLayout(null);

        JLabel reserveBtn = new JLabel("");

        reserveBtn.setBounds(463, 491, 80, 36);
        reserveBtn.setIcon(new ImageIcon(RESERVATIONMANAGEMENT.class.getResource("/IMAGES/reservebtn.png")));
        frmGuestReservationManagement.getContentPane().add(reserveBtn);

        JLabel startTimeShow = new JLabel(startTime);
        startTimeShow.setHorizontalAlignment(SwingConstants.CENTER);
        startTimeShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        startTimeShow.setBounds(142, 317, 221, 26);
        frmGuestReservationManagement.getContentPane().add(startTimeShow);

        JLabel endTimeShow = new JLabel(endTime);
        endTimeShow.setHorizontalAlignment(SwingConstants.CENTER);
        endTimeShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        endTimeShow.setBounds(638, 322, 221, 26);
        frmGuestReservationManagement.getContentPane().add(endTimeShow);

        JLabel dateShow = new JLabel();
        dateShow.setHorizontalAlignment(SwingConstants.CENTER);
        dateShow.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        dateShow.setBounds(389, 279, 221, 26);
        frmGuestReservationManagement.getContentPane().add(dateShow);

        // Convert the selectedDate to a string representation
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        dateShow.setText(dateString);

        purpose = new JTextField();
        purpose.setBounds(426, 362, 156, 36);
        frmGuestReservationManagement.getContentPane().add(purpose);
        purpose.setColumns(10);
        
        name = new JTextField();
        name.setColumns(10);
        name.setBounds(426, 125, 156, 36);
        frmGuestReservationManagement.getContentPane().add(name);
        
        number = new JTextField();
        number.setColumns(10);
        number.setBounds(426, 190, 156, 36);
        frmGuestReservationManagement.getContentPane().add(number);
        
                JLabel managementImg = new JLabel("");
                managementImg.setBounds(0, 0, 984, 561);
                managementImg.setIcon(new ImageIcon(RESERVATIONMANAGEMENT.class.getResource("/IMAGES/RESERVATIONMANAGEMENT.png")));
                frmGuestReservationManagement.getContentPane().add(managementImg);

        //Event Listeners
        // Inside the initialize() method
        reserveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Create the receipt message
            	String enteredName = name.getText();
                String enteredNumber = number.getText();
                String enteredPurpose = purpose.getText();

                StringBuilder receiptBuilder = new StringBuilder();
                receiptBuilder.append("Reservation Details:\n");
                receiptBuilder.append("Name: ").append(enteredName).append("\n");
                receiptBuilder.append("Contact Number: ").append(enteredNumber).append("\n");
                receiptBuilder.append("Date: ").append(dateString).append("\n");
                receiptBuilder.append("Start Time: ").append(startTime).append("\n");
                receiptBuilder.append("End Time: ").append(endTime).append("\n");
                receiptBuilder.append("Purpose: ").append(enteredPurpose).append("\n");
                receiptBuilder.append("THIS WILL SERVE AS YOUR RECEIPT SCREENSHOT THIS!").append("\n");

                // Show the receipt message in a JOptionPane
                JOptionPane.showMessageDialog(frmGuestReservationManagement, receiptBuilder.toString(), "Reservation Receipt",
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

    // Method to save the reservation details to the file
   

}
