import java.util.*;

// Reservation class
class Reservation {
    String reservationId;
    String roomType;
    boolean isActive;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.isActive = true;
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Deluxe", 2);
        inventory.put("Standard", 1);
    }

    public boolean allocateRoom(String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            return false;
        }
        inventory.put(roomType, inventory.get(roomType) - 1);
        return true;
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void add(Reservation r) {
        history.put(r.reservationId, r);
    }

    public Reservation get(String id) {
        return history.get(id);
    }

    public boolean exists(String id) {
        return history.containsKey(id);
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history.values()) {
            System.out.println(r.reservationId + " | " + r.roomType + " | Active: " + r.isActive);
        }
    }
}

// Cancellation Service
class CancellationService {

    private BookingHistory history;
    private RoomInventory inventory;
    private Stack<String> rollbackStack;

    public CancellationService(BookingHistory history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    // Confirm booking
    public void confirmBooking(String id, String roomType) {
        if (inventory.allocateRoom(roomType)) {
            Reservation r = new Reservation(id, roomType);
            history.add(r);
            rollbackStack.push(id); // track for rollback
            System.out.println("Booking confirmed: " + id);
        } else {
            System.out.println("Booking failed (No availability): " + roomType);
        }
    }

    // Cancel booking
    public void cancelBooking(String id) {

        // Validation
        if (!history.exists(id)) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        Reservation r = history.get(id);

        if (!r.isActive) {
            System.out.println("Cancellation Failed: Already cancelled");
            return;
        }

        // LIFO rollback
        if (!rollbackStack.isEmpty() && rollbackStack.peek().equals(id)) {
            rollbackStack.pop();
        } else {
            rollbackStack.remove(id); // safety fallback
        }

        // Restore inventory
        inventory.releaseRoom(r.roomType);

        // Update booking state
        r.isActive = false;

        System.out.println("Cancellation successful: " + id);
    }
}

// MAIN CLASS
public class UC10 {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService service = new CancellationService(history, inventory);

        // Confirm bookings
        service.confirmBooking("RES101", "Deluxe");
        service.confirmBooking("RES102", "Standard");

        // Perform cancellations
        service.cancelBooking("RES102"); // valid
        service.cancelBooking("RES200"); // not found
        service.cancelBooking("RES102"); // already cancelled

        // Final state
        history.display();
        System.out.println();
        inventory.display();
    }
}
