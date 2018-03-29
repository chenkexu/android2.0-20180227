package vr.md.com.mdlibrary.okhttp.requestMap;

/**
 * Created by seven on 16/4/11.
 */
public class MDListRequestMap extends MDBasicRequestMap {
    private int pageNo = 1;
    private int size = 20;

    public MDListRequestMap() {
        super();
    }


    private void updateMap() {
        this.put("page", String.valueOf(this.pageNo));
        this.put("size", String.valueOf(this.size));
    }

    /**
     * 获取下一个页码
     */
    public void nextPage() {
        this.pageNo++;
        this.updateMap();
    }

    /**
     * 重置页码
     *
     * @return
     */
    public void resetToFirstPage() {
        this.pageNo = 1;
        this.updateMap();
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页码
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        this.updateMap();
    }

    /**
     * 获取每一次翻页的最大记录数
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置每一次翻页的最大记录数
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
        this.updateMap();
    }

}
