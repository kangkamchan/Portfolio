package net.haebup.utils;

public class Pagination {
    private int pageNo;       // 현재 페이지 번호
    private int totalItems;   // 전체 게시글 수
    private int pageSize;     // 한 페이지에 출력할 게시글 수
    private int blockSize;    // 한 블록에 출력할 페이지 번호 수

    public Pagination(int pageNo, int totalItems, int pageSize, int blockSize) {
        this.pageNo = pageNo;
        this.totalItems = totalItems; // 전체 게시글 수
        this.pageSize = pageSize;
        this.blockSize = blockSize;
    }


    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }


    public int getTotalPage() {
        return (int) Math.ceil((double) totalItems / pageSize);
    }


    public int getStartBlockPage() {
        return ((pageNo - 1) / blockSize) * blockSize + 1;
    }


    public int getEndBlockPage() {
        int endBlockPage = getStartBlockPage() + blockSize - 1;
        return Math.min(endBlockPage, getTotalPage()); 
    }

    // 전체 블록 수 계산
    public int getTotalBlock() {
        return (int) Math.ceil((double) getTotalPage() / blockSize);
    }
}
