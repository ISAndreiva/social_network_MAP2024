package internal.andreiva.socialnetwork.utils;

public class Pageable {
    private final int pageNumber;
    private final int pageSize;

    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
