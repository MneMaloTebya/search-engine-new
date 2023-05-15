package searchengine.dto;

import lombok.Data;

@Data
public class DataDto {
    private String site;
    private String siteName;
    private String url;
    private String title;
    private String snippet;
    private int relevance;
}
