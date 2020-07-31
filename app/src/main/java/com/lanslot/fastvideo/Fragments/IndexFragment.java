package com.lanslot.fastvideo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lanslot.fastvideo.Adapter.AdBannerAdapter;
import com.lanslot.fastvideo.Adapter.MsgBannerAdapter;
import com.lanslot.fastvideo.Adapter.PlatformAdapter;
import com.lanslot.fastvideo.Bean.ImageRotation;
import com.lanslot.fastvideo.Bean.JSON.IndexInfoJSON;
import com.lanslot.fastvideo.Bean.MovieChannel;
import com.lanslot.fastvideo.Bean.Notice;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.R;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.fragment_index)
public class IndexFragment extends Fragment {
    @ViewInject(R.id.adBanner)
    private Banner adBanner;
    @ViewInject(R.id.msgBanner)
    private Banner msgBanner;
    @ViewInject(R.id.grid_platform)
    private GridView platformView;

    ArrayList<Notice> notices;
    ArrayList<ImageRotation> imageRotations;
    ArrayList<MovieChannel> movieChannels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();
        return view;
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpCommon.INDEX_INFO);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                IndexInfoJSON jo = JSON.parseObject(result, IndexInfoJSON.class);
                if (jo.getCode() == 0) {
                    notices = jo.getDatas().getNoticeList();
                    imageRotations = jo.getDatas().getImageRotationList();
                    movieChannels = jo.getDatas().getMovieChannelList();

                    platformView.setAdapter(new PlatformAdapter(getContext(), movieChannels));
                    msgBanner.setAdapter(new MsgBannerAdapter(notices,getActivity()));
                    adBanner.setAdapter(new AdBannerAdapter(getContext(), imageRotations))
                            .setIndicator(new CircleIndicator(getActivity()));
                } else {
                    Toast.makeText(getActivity(), jo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
