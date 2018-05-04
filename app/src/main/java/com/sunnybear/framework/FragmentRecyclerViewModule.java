package com.sunnybear.framework;

import com.sunnybear.framework.adapter.RecyclerAdapter;
import com.sunnybear.framework.databinding.FragmentRecyclerBinding;
import com.sunnybear.framework.library.base.BaseViewModule;

import java.util.ArrayList;
import java.util.List;

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
        List<String> datas = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            datas.add("内容" + i);
        }
        return datas;
    }
}
