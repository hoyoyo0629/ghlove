package saleson.common.sweettracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class TrackResponse {
    private boolean found;
    private String status; // IN_TRANSIT, DELIVERED ...
    private List<Trace> traces;

    @Data
    public static class Trace {
        private String time;       // "2025-08-25 13:20:00"
        private String location;   // "서울중앙집배점"
        private String desc;       // "간선상차"
    }
}
