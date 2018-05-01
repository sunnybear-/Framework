package com.sunnybear.framework;

import com.sunnybear.framework.adapter.RecyclerAdapter;
import com.sunnybear.framework.databinding.FragmentRecyclerBinding;
import com.sunnybear.framework.library.base.BaseViewModule;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class FragmentRecyclerViewModule extends BaseViewModule<RecyclerFragment, FragmentRecyclerBinding> {

    public FragmentRecyclerViewModule(RecyclerFragment recyclerFragment, FragmentRecyclerBinding viewDataBinding) {
        super(recyclerFragment, viewDataBinding);
    }

    @Override
    public void init() {
//        mViewDataBinding.rvData.setLayoutManager(new LinearLayoutManager(mContext));
        mViewDataBinding.setAdapter(new RecyclerAdapter(getDatas()));
    }

    private List<String> getDatas() {
        return Flowable.range(1, 100)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "内容" + integer;
                    }
                })
                .toList().blockingGet();
    }
}
