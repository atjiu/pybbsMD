package cn.tomoya.android.md.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.User;
import cn.tomoya.android.md.ui.fragment.UserDetailItemFragment;

public class UserDetailPagerAdapter extends FragmentPagerAdapter {

    private final List<UserDetailItemFragment> fmList = new ArrayList<>();
    private final String[] titles = {
            "最近回复",
            "最新发布",
            "话题收藏"
    };

    public UserDetailPagerAdapter(@NonNull FragmentManager manager) {
        super(manager);
        fmList.add(new UserDetailItemFragment());
        fmList.add(new UserDetailItemFragment());
        fmList.add(new UserDetailItemFragment());
    }

    public void update(@NonNull User user) {
        fmList.get(0).notifyDataSetChanged(user.getReplies());
        fmList.get(1).notifyDataSetChanged(user.getTopics());
    }

    public void update(@NonNull List<Topic> topicList) {
//        List<TopicSimple> topicSimpleList = new ArrayList<>();
//        for (Topic topic : topicList) {
//            topicSimpleList.add(topic);
//        }
        fmList.get(2).notifyDataSetChanged(topicList);
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
