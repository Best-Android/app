package app.hjtao.best.com.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import app.hjtao.best.com.myapp.bean.Outer;
import app.hjtao.best.com.myapp.bean.Results;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public class WeixinFragment extends Fragment {
    String url = "http://gank.io/api/data/Android/10/1";
    @BindView(R.id.recycler_wx)
    RecyclerView recyclerView;
    ArrayList<Results> mResultList;
    RecyclerViewAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    Unbinder unbinder;

    public WeixinFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.image_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }
    private void initView(View view) {
        mResultList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(view.getContext(),mResultList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Outer outer = Outer.parseJson(response);
                List<Results> list = outer.getResults();
                mAdapter.initData(list);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //ViewHolder类
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Img;
        TextView tv_Desc;
        TextView tv_Who;
        TextView tv_Createdat;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_Img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_Desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_Who = (TextView) itemView.findViewById(R.id.tv_who);
            tv_Createdat = (TextView) itemView.findViewById(R.id.tv_createdat);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int TYPE_ITEM = 0;//代表图片item类型的布局
        final static int TYPE_FOOTER = 1;//代表页脚item类型的布局

        Context context;
        ArrayList<Results> GankList;

        public RecyclerViewAdapter(Context context, ArrayList<Results> GankList) {
            this.context = context;
            this.GankList = GankList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = null;
            switch (i) {
                case TYPE_FOOTER:
                    layout = inflater.inflate(R.layout.text_item, viewGroup, false);
                    holder = new FooterViewHolder(layout);
                    break;
                case TYPE_ITEM:
                    layout = inflater.inflate(R.layout.weixin, viewGroup, false);
                    holder = new MyViewHolder(layout);
                    break;
            }
            return holder;
        }

        //绑定数据
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (i == getItemCount() - 1) {
                return;
            }
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Results results = GankList.get(i);
            myViewHolder.tv_Desc.setText(results.getDesc());
            myViewHolder.tv_Who.setText(results.getWho());
            myViewHolder.tv_Createdat.setText(results.getCreatedat());
            if (results.getimages().size() > 0) {
                Glide.with(context).load(results.getimages().get(0))
                        .placeholder(R.mipmap.ic_launcher).into(((MyViewHolder) holder).iv_Img);
            }
        }

        @Override
        public int getItemCount() {
            return GankList == null ? 0 :GankList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        public void initData(List<Results> list) {
            if (GankList != null) {
                GankList.clear();
            }
            GankList.addAll(list);
            notifyDataSetChanged();
        }
    }

}

