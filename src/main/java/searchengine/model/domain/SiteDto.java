package searchengine.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.dto.StatusType;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDto {
    private int id;
    private StatusType statusType;
    private LocalDateTime statusTime;
    private String lastError;
    private String url;
    private String name;
}
