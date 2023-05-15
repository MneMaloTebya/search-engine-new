package searchengine.dto.indx_resp;

public class CorrectIndexingResponse extends IndexingResponse {

    private boolean result;

    public CorrectIndexingResponse() {
        result = true;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
