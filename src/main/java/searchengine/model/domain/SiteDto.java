package searchengine.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.dto.StatusType;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDto {
    private int id;
    private StatusType statusType;
    private Date statusTime;
    private String lastError;
    private String url;
    private String name;
}
