package cinema;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
public class RoomController {

    @GetMapping
    private Room returnRoomInfo() {
        return new Room(9, 9);
    }
}
