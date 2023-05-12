package com.fengxudong.frpc.filter;


import com.fengxudong.frpc.context.FRpcContext;
import com.fengxudong.frpc.context.FRpcContextHolder;
import com.fengxudong.frpc.filter.wrapper.FRpcFilterWrapper;
import com.fengxudong.frpc.remote.domain.FRpcRequest;

import java.util.List;

/**
 * @author feng xud
 */
public abstract class AbstractRpcFilter implements FRpcFilter{
    @Override
    public void filter(FRpcRequest request) {
        List<FRpcFilter> fRpcFilters = new FRpcFilterWrapper().getObjects();
        FRpcContextHolder.setRequest(request);
        FRpcContextHolder.confirmProvider();
        for (FRpcFilter fRpcFilter : fRpcFilters) {
            fRpcFilter.doFilter(request);
        }
    }

    @Override
    public void doFilter(FRpcRequest request) {

    }

    @Override
    public int order() {
        return -1;
    }
}
