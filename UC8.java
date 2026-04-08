import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double baseCost;

    public Reservation(String reservationId, String guestName, String roomType, double baseCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.baseCost = baseCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBaseCost() {
        return baseCost;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | ₹" + baseCost;
    }
}

// Booking History class
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\n=== Booking History ===");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Reporting Service class
class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n=== Booking Summary Report ===");

        int totalBookings = reservations.size();
        double totalRevenue = 0;

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getBaseCost();

            roomCount.put(
                    r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("\nRoom Type Distribution:");
        for (String type : roomCount.keySet()) {
            System.out.println(type + " : " + roomCount.get(type));
        }
    }
}

// MAIN CLASS
public class UC8 {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService report = new BookingReportService();

        // Simulated confirmed bookings
        history.addReservation(new Reservation("RES101", "Rithvik", "Deluxe", 2000));
        history.addReservation(new Reservation("RES102", "Arjun", "Standard", 1500));
        history.addReservation(new Reservation("RES103", "Meena", "Deluxe", 2000));

        // Admin views history
        history.displayHistory();

        // Admin generates report
        report.generateReport(history.getAllReservations());
    }
}