package com.fengxudong.frpc.filter.wrapper;

import com.fengxudong.frpc.filter.AbstractRpcFilter;
import com.fengxudong.frpc.filter.FRpcFilter;
import com.fengxudong.frpc.util.ClassLoadUtil;
import com.fengxudong.frpc.wrapper.FRpcWrapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author feng xud
 */
public class FRpcFilterWrapper implements FRpcWrapper<List<FRpcFilter>> {

    private static final List<FRpcFilter> F_RPC_FILTERS = new ArrayList<>();

    public FRpcFilterWrapper() {

    }

    @Override
    public List<FRpcFilter> getObjects() {
        if(CollectionUtils.isEmpty(F_RPC_FILTERS)){
            synchronized (FRpcFilterWrapper.class){
                if(CollectionUtils.isEmpty(F_RPC_FILTERS)){
                    List<AbstractRpcFilter> fRpcFilters = ClassLoadUtil.loadFromSuper(AbstractRpcFilter.class);
                    fRpcFilters.sort(Comparator.comparingInt(FRpcFilter::order));
                    F_RPC_FILTERS.addAll(fRpcFilters);
                }
            }
        }
        return F_RPC_FILTERS;
    }

}
