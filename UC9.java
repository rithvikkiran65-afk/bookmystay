import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Room Inventory (with validation)
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Deluxe", 2);
        inventory.put("Standard", 1);
    }

    // Validate and allocate room
    public void bookRoom(String roomType) throws InvalidBookingException {

        // Validate room type
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Check availability
        int available = inventory.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        // Update inventory safely
        inventory.put(roomType, available - 1);

        System.out.println("Room booked successfully: " + roomType);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Validator class (optional separation)
class BookingValidator {

    public static void validateInput(String guestName, String roomType) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }
    }
}

// MAIN CLASS
public class UC9 {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Test cases (valid + invalid)
        String[][] bookings = {
                {"Rithvik", "Deluxe"},
                {"", "Standard"},          // invalid name
                {"Arjun", "Premium"},      // invalid room
                {"Meena", "Standard"},
                {"John", "Standard"}       // no availability
        };

        for (String[] booking : bookings) {

            String name = booking[0];
            String room = booking[1];

            try {
                System.out.println("\nProcessing booking for: " + name + " | " + room);

                // Step 1: Validate input
                BookingValidator.validateInput(name, room);

                // Step 2: Book room
                inventory.bookRoom(room);

            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        // Final inventory state
        System.out.println("\n=== Final Inventory ===");
        inventory.displayInventory();
    }
}