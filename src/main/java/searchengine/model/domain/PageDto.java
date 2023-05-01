package searchengine.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private int id;
    private int siteId;
    private String path;
    private int code;
    private String content;
}
