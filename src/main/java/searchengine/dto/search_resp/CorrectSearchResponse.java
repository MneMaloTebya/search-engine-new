package searchengine.dto.search_resp;

import searchengine.dto.DataDto;

public class CorrectSearchResponse extends SearchResponse{

    private boolean result;

    private int count;

    private DataDto data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }
}
