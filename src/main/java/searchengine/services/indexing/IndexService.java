package searchengine.services.indexing;

import searchengine.dto.indexing.IndexingResponse;

public interface IndexService {
    IndexingResponse startIndex();
    IndexingResponse stopIndex();
}
