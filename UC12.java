import java.io.*;
import java.util.*;

// Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String id;
    String roomType;

    public Reservation(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
    }

    public String toString() {
        return id + " | " + roomType;
    }
}

// System State
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE = "data.dat";

    // Save data
    public void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE))) {

            out.writeObject(state);
            System.out.println("Data saved successfully.");

        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    // Load data
    public SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("Data loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Corrupted file. Using default state.");
        }

        // Default state
        Map<String, Integer> inv = new HashMap<>();
        inv.put("Deluxe", 2);
        inv.put("Standard", 1);

        return new SystemState(inv, new ArrayList<>());
    }
}

// MAIN CLASS
public class UC12 {

    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        // Load previous data
        SystemState state = service.load();

        Map<String, Integer> inventory = state.inventory;
        List<Reservation> bookings = state.bookings;

        System.out.println("\n=== Restored State ===");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);

        // Add booking
        System.out.println("\nAdding booking...");

        if (inventory.get("Deluxe") > 0) {
            inventory.put("Deluxe", inventory.get("Deluxe") - 1);
            bookings.add(new Reservation("RES" + (100 + bookings.size()), "Deluxe"));
            System.out.println("Booking successful.");
        } else {
            System.out.println("No Deluxe rooms available.");
        }

        // Save updated state
        service.save(new SystemState(inventory, bookings));

        System.out.println("\n=== Current State ===");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);
    }
}
