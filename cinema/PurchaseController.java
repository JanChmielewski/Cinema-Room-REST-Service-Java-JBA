package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping
public class PurchaseController {

    private final Room room = new Room(9, 9);
    private final ConcurrentHashMap<String, Boolean> seatsMap = new ConcurrentHashMap<>();

    @GetMapping("/seats")
    public Object getRoomInfo() {
        List<Seat> availableSeats = new ArrayList<>();

        for (int row = 1; row <= room.getRows(); row++) {
            for (int column = 1; column <= room.getColumns(); column++) {
                String seatKey = row + "-" + column;

                if (!seatsMap.containsKey(seatKey)) {
                    availableSeats.add(new Seat(row, column));
                }
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("rows", room.getRows());
        response.put("columns", room.getColumns());
        response.put("seats", availableSeats);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> postPurchase(@RequestBody Seat seat) {
        String seatKey = seat.getRow() + "-" + seat.getColumn();
        if (seat.getRow() > room.getRows() || seat.getRow() <= 0 || seat.getColumn() > room.getColumns() || seat.getColumn() <= 0) {
            return errorResponse("The number of a row or a column is out of bounds!", HttpStatus.BAD_REQUEST);
        } else {
            if (!seatsMap.containsKey(seatKey)) {
                seatsMap.put(seatKey, true);
                Seat purchasedSeat = room.getSeat(seat.getRow(), seat.getColumn());
                return ResponseEntity.ok(purchasedSeat);
            } else {
                return errorResponse("The ticket has been already purchased!", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/purchase/{row}/{column}")
    public ResponseEntity<?> getPurchasedSeat(@PathVariable int row, @PathVariable int column) {
        String seatKey = row + "-" + column;
        if (seatsMap.containsKey(seatKey)) {
            Seat purchasedSeat = room.getSeat(row, column);
            return ResponseEntity.ok(purchasedSeat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> errorResponse(String errorMessage, HttpStatus status) {
        CustomError error = new CustomError(errorMessage);
        return ResponseEntity.status(status).body(error);

    }
}
