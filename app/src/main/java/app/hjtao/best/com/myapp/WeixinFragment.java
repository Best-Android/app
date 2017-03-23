package app.hjtao.best.com.myapp;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import app.hjtao.best.com.myapp.bean.Images;
import app.hjtao.best.com.myapp.bean.Outer;
import app.hjtao.best.com.myapp.bean.Results;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;




/**
 * Created by Administrator on 2017/3/10.
 */

public class WeixinFragment extends Fragment {
    private final static String TAG = "WeixinFragment";
    //new_line5
    final static int ACTION_DOWNLOAD=0;
    final static int ACTION_PULL_DOWN=1;
    final static int ACTION_PULL_UP=2;
    private final static String ROOT_URL="http://gank.io/api/data/Android/10/";
    //String url = "http://gank.io/api/data/Android/10/1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_refresh)
    TextView tvrefresh;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Results> mResultList;
    RecyclerViewAdapter mAdapter;
    LinearLayoutManager mlinearLayoutManager;
    //new_line2
    int mPageId=1;
    //int mNewState;
    Unbinder unbinder;

    public WeixinFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.image_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        setListener();
        return view;
    }
//new_setListener
    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullDownListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
                tvrefresh.setVisibility(View.INVISIBLE);
               // Log.d(TAG, "setPullDownListener: 000000000000000");
                mPageId = 1;
                downloadData(ACTION_PULL_DOWN,mPageId);
            }
        });
    }
    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               // Log.d(TAG, "setPullUpListener: 000000000000000");
                super.onScrollStateChanged(recyclerView, newState);
                //mNewState = newState;
                lastPosition = mlinearLayoutManager.findLastVisibleItemPosition();
               // Log.d(TAG, "setPullUpListener: 111111111111111");
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE) {
                   // Log.d(TAG, "onScrollStateChanged: 2222222222222222222222");
                    mPageId++;
                    downloadData(ACTION_PULL_UP, mPageId);
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition=mlinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    private void initView(View view) {
        mResultList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(view.getContext(),mResultList);
        recyclerView.setAdapter(mAdapter);
        mlinearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mlinearLayoutManager);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //new
        downloadData(ACTION_DOWNLOAD,1);

    }
        private void downloadData(final int actionDown,int pageId){
            Log.d(TAG, "downloadData: "+actionDown+","+pageId);
            String url=ROOT_URL+pageId;
            Log.d(TAG,"downloadData:"+url);
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG,"onError:"+e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    Outer outer = Outer.parseJson(response);
                    List<Results> list = outer.getResults();

                    mAdapter.setMore(list != null && list.size() > 0);
                    mAdapter.initData(list);
                    if (!mAdapter.isMore()) {
                        if (actionDown == ACTION_PULL_UP) {
                            mAdapter.setFooter("没有更多数据");
                        }
                        return;
                    }

                    switch (actionDown) {
                        case ACTION_DOWNLOAD:
                            mAdapter.initData(list);
                            mAdapter.setFooter("加载更多数据");
                            break;
                        case ACTION_PULL_DOWN:
                            mAdapter.initData(list);
                            mAdapter.setFooter("加载更多数据");
                            swipeRefreshLayout.setRefreshing(false);
                            tvrefresh.setVisibility(View.GONE);
                            break;
                        case ACTION_PULL_UP:
                            mAdapter.addData(list);
                            break;
                    }
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
        String footerText;
        boolean isMore;

        public RecyclerViewAdapter(Context context, ArrayList<Results> GankList) {
            this.context = context;
            this.GankList = GankList;
        }
        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
        }

        public void setFooter(String footer) {
            this.footerText = footer;
            notifyDataSetChanged();
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




        public void addData(List<Results> list) {
            this.GankList.addAll(list);
            notifyDataSetChanged();
        }
    }

}

