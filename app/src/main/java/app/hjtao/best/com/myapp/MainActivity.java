package app.hjtao.best.com.myapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.hjtao.best.com.myapp.bean.Images;
import app.hjtao.best.com.myapp.bean.Outer;
import app.hjtao.best.com.myapp.bean.Results;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.string.ok;


public class MainActivity extends FragmentActivity {
    private ViewPager viewPager;
    private TextView mtv;
    private RadioGroup radioGroup;
    private RadioButton rbChat, rbContacts, rbDiscovery, rbMe;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        String url = "http://gank.io/api/data/Android/10/1";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("name", "胡金涛")
                .addParams("password", "123456")
                .build()
                .execute(new StringCallback() {
                    public static final String TAG = "MainActivity";

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        mtv.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Outer outer = null;
                        //Log.d(TAG,response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean error = jsonObject.getBoolean("error");
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                List<Results> list = new ArrayList<Results>();
                                List<String> imagesList = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonResults = (JSONObject) jsonArray.get(i);
                                    String _id = jsonResults.getString("_id");
                                    String createdAt = jsonResults.getString("createdAt");
                                    String desc = jsonResults.getString("desc");

                                    if (jsonResults.has("images")) {
                                        JSONArray imagesArray = jsonResults.getJSONArray("images");
                                        for (int j = 0; j < imagesArray.length(); j++) {
                                            String images = (String) imagesArray.get(j);
                                            Log.d(TAG, "onResponse: " + images);
                                            imagesList.add(images);
                                        }
                                    }


                                    String publishedAt = jsonResults.getString("publishedAt");
                                    String source = jsonResults.getString("source");
                                    String type = jsonResults.getString("type");
                                    String url = jsonResults.getString("url");
                                    Boolean used = jsonResults.getBoolean("used");
                                    String who = jsonResults.getString("who");
                                    Results results = new Results(_id, createdAt, desc, imagesList, publishedAt, source, type, url, used, who);
                                    list.add(results);
                                }
                                Log.d(TAG, list.toString());
                                outer = new Outer();
                                outer.setError(error);
                                outer.setResults(list);
                                Log.d(TAG, "onResponse: " + outer.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbChat = (RadioButton) findViewById(R.id.rb_chat);
        rbContacts = (RadioButton) findViewById(R.id.rb_contacts);
        rbDiscovery = (RadioButton) findViewById(R.id.rb_discovery);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chat:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_contacts:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_discovery:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_me:
                        viewPager.setCurrentItem(3, false);
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        WeixinFragment weChatFragment = new WeixinFragment();
        ContactsFragment contactsFragment = new ContactsFragment();
        FindFragment discoveryFragment = new FindFragment();
        MeFragment meFragment = new MeFragment();
        List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(weChatFragment);
        alFragment.add(contactsFragment);
        alFragment.add(discoveryFragment);
        alFragment.add(meFragment);
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_chat);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_contacts);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_discovery);
                        break;
                    case 3:
                        radioGroup.check(R.id.rb_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}



