package searchengine.services.indexing;

import searchengine.dto.indx_resp.IndexingResponse;

public interface IndexingService {
    IndexingResponse startIndex();
    IndexingResponse stopIndex();
    IndexingResponse indexPage(String url);
}
