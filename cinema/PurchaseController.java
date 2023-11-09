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
    private final ConcurrentHashMap<String, Seat> ticketMap = new ConcurrentHashMap<>();

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
        String ticketKey = seat.getRow() + "-" + seat.getColumn();
        if (seat.getRow() > room.getRows() || seat.getRow() <= 0 || seat.getColumn() > room.getColumns() || seat.getColumn() <= 0) {
            return errorResponse("The number of a row or a column is out of bounds!", HttpStatus.BAD_REQUEST);
        } else {
            if (!seatsMap.containsKey(ticketKey)) {
                seatsMap.put(ticketKey, true);
                Seat purchasedSeat = room.getSeat(seat.getRow(), seat.getColumn());

                String token = UUID.randomUUID().toString();

                Map<String, Object> purchaseInfo = new LinkedHashMap<>();
                purchaseInfo.put("token", token);
                purchaseInfo.put("ticket", purchasedSeat);
                ticketMap.put(token, purchasedSeat);

                return ResponseEntity.ok(purchaseInfo);
            } else {
                return errorResponse("The ticket has been already purchased!", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> postReturn(@RequestBody ReturnRequest returnRequest) {
        String token = returnRequest.getToken();

        if (ticketMap.containsKey(token)) {
            Seat purchasedSeat = ticketMap.get(token);

            String ticketKey = purchasedSeat.getRow() + "-" + purchasedSeat.getColumn();
            seatsMap.remove(ticketKey);

            ticketMap.remove(token);

            Map<String, Object> ticketInfo = new LinkedHashMap<>();
            ticketInfo.put("row", purchasedSeat.getRow());
            ticketInfo.put("column", purchasedSeat.getColumn());
            ticketInfo.put("price", purchasedSeat.getPrice());

            return ResponseEntity.ok(Collections.singletonMap("ticket", ticketInfo));
        } else {
            return errorResponse("Wrong token!", HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity<?> errorResponse(String errorMessage, HttpStatus status) {
        CustomError error = new CustomError(errorMessage);
        return ResponseEntity.status(status).body(error);

    }
}
