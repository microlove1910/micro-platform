package com.micro.thing.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Collections;
import java.util.List;

public class PageVo<T> {

    private Integer page;

    private Integer size;

    private Long total;

    private List<T> rows;

    public PageVo(PageInfo<T> info) {
        this.page = info.getPageNum();
        this.size = info.getPageSize();
        this.total = info.getTotal();
        this.rows = info.getList();
    }

    public PageVo(List<T> rows) {
        if (rows instanceof Page) {
            Page<T> page = (Page<T>) rows;
            this.page = page.getPageNum();
            this.size = page.getPageSize();
            this.rows = page.getResult();
            this.total = page.getTotal();
        } else if (rows instanceof Collections) {
            this.page = 1;
            this.size = rows.size();
            this.rows = rows;
            this.total = Long.valueOf(rows.size() + "");
        }
    }

    public static <E> PageVo<E> of(List<E> rows) {
        return new PageVo<E>(rows);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
