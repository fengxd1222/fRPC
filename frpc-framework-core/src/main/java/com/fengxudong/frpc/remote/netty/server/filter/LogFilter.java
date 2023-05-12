package com.fengxudong.frpc.remote.netty.server.filter;

import com.fengxudong.frpc.filter.AbstractRpcFilter;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feng xud
 */
@Slf4j
public class LogFilter extends AbstractRpcFilter {

    @Override
    public void doFilter(FRpcRequest request) {
        log.info("LogFilter log [{}]",request);
    }

    @Override
    public int order() {
        return 0;
    }
}
