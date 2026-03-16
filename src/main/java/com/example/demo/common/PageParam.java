package com.example.demo.common;

import lombok.Data;

@Data
public class PageParam {
    private Integer pageNum = 1;
    private Integer pageSize = 0;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
