import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Room Inventory (THREAD SAFE)
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Deluxe", 1);
        inventory.put("Standard", 1);
    }

    // Critical Section (SYNCHRONIZED)
    public synchronized boolean allocateRoom(String roomType) {

        if (!inventory.containsKey(roomType)) {
            System.out.println("Invalid room type: " + roomType);
            return false;
        }

        int available = inventory.get(roomType);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " booking " + roomType);

            inventory.put(roomType, available - 1);

            System.out.println("SUCCESS: " + roomType +
                    " allocated to " + Thread.currentThread().getName());

            return true;
        } else {
            System.out.println("FAILED: No " + roomType +
                    " available for " + Thread.currentThread().getName());
            return false;
        }
    }

    public void display() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Shared Booking Queue
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            BookingRequest request;

            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            inventory.allocateRoom(request.roomType);

            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// MAIN CLASS
public class UC11 {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulating multiple requests
        queue.addRequest(new BookingRequest("Rithvik", "Deluxe"));
        queue.addRequest(new BookingRequest("Arjun", "Deluxe"));   // conflict
        queue.addRequest(new BookingRequest("Meena", "Standard"));
        queue.addRequest(new BookingRequest("John", "Standard"));  // conflict

        // Multiple threads (guests)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.display();
    }
}