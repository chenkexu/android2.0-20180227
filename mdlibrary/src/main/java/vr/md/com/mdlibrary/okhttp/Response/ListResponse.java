package vr.md.com.mdlibrary.okhttp.Response;

/**
 * Created by seven on 16/4/11.
 */
public class ListResponse extends BaseResponse {

    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
