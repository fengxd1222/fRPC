package com.fengxudong.frpc.filter;

import com.fengxudong.frpc.context.FRpcContext;
import com.fengxudong.frpc.remote.domain.FRpcRequest;

/**
 * @author feng xud
 */
public interface FRpcFilter {
    public void filter(FRpcRequest request);

    public void doFilter(FRpcRequest request);

    public int order();
}
