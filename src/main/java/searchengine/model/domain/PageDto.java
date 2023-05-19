package searchengine.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private int id;
    private int siteId;
    private String path;
    private int code;
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto pageDto = (PageDto) o;
        return id == pageDto.id && siteId == pageDto.siteId && code == pageDto.code && Objects.equals(path, pageDto.path) && Objects.equals(content, pageDto.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteId, path, code, content);
    }
}
