package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.MsgCenterAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.bean.Message;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Jiangwz on 2016/11/21.
 */

public class MsgCenterActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.gpb)
    GoogleProgressBar gpb;

    private List<Message> data;
    private MsgCenterAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_center;
    }

    @Override
    protected void initView() {
        mTvTitle.setText("通知中心");

        data = new ArrayList<Message>();
        mAdapter = new MsgCenterAdapter(mActivity, data);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        getData();
        mAdapter.setmOnRVItemClickListener(new MsgCenterAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String id = data.get(position).getTargetId();
                BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
                query.include("author,author.character");
                query.getObject(id, new QueryListener<Koukekouke>() {
                    @Override
                    public void done(Koukekouke koukekouke, BmobException e) {
                        if (e == null) {
                            //将消息状态设为已读
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", koukekouke);
                            Redirct.openActivity(mActivity, InfoActivity.class, bundle);
                        }
                    }
                });
                Message newMsg = new Message();
                newMsg.setRead(true);
                newMsg.update(data.get(position).getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            //
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void initData() {
    }

    public void getData(){
        BmobQuery<Message> query = new BmobQuery<Message>();
        User user = BmobUser.getCurrentUser(User.class);
        query.addWhereRelatedTo("msg", new BmobPointer(user));
        query.setLimit(50);
        query.order("-createdAt");
        query.include("fromWho,fromWho.character");
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if(e == null){
                    if (list.size() != 0 && list.get(list.size() - 1) != null){
                        data.clear();
                        data.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Redirct.makeToast(mActivity, "暂无消息");
                    }
                }else{
                    Redirct.makeToast(mActivity, e.toString());
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_iv_left)
    public void onClick() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
