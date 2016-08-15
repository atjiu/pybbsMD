package cn.tomoya.android.md.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tomoya.android.md.R;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.ui.adapter.TopicSimpleListAdapter;

public class UserDetailItemFragment extends Fragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    private TopicSimpleListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopicSimpleListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged(List<Topic> topicSimpleList) {
        adapter.getTopicSimpleList().clear();
        adapter.getTopicSimpleList().addAll(topicSimpleList);
        adapter.notifyDataSetChanged();
    }

}
