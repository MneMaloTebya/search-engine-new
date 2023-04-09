package searchengine.dto.indexing;

public class ErrorIndexingResponse extends IndexingResponse {

    private boolean result;
    private String error;

    public ErrorIndexingResponse() {
    }

    public ErrorIndexingResponse(String error) {
        this.result = false;
        this.error = error;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
