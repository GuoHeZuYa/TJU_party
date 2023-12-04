package edu.twt.party.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageHelper {
    private int page;
    private int limit;
    private int total;
    private Object data;

    public int calOffset() {
        return this.limit * (this.page - 1);
    }

    public PageHelper(int total, int page, int limit) {
        this.total = total;
        if(limit * (page - 1) >= this.total) {
            page = this.total / limit + ((this.total % limit > 0)? 1: 0);
        }
        if(page < 1) {
            page = 1;
        }
        this.page = page;
        this.limit = limit;
    }
}
