package app.hjtao.best.com.myapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //声明四个Tab的布局文件
    private LinearLayout TabWeixin;
    private LinearLayout TabContacts;
    private LinearLayout TabFind;
    private LinearLayout TabMe;

    //声明四个Tab的ImageButton
    private ImageButton BtnWeixinImg;
    private ImageButton BtnContactsImg;
    private ImageButton BtnFindImg;
    private ImageButton BtnMeImg;

    //声明四个Tab分别对应的Fragment
    private Fragment FragWeixin;
    private Fragment FragContacts;
    private Fragment FragFind;
    private Fragment FragMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        selectTab(0);//默认选中第一个Tab
    }

    private void initEvents() {
        TabWeixin.setOnClickListener(this);
        TabContacts.setOnClickListener(this);
        TabFind.setOnClickListener(this);
        TabMe.setOnClickListener(this);
    }

    private void initViews() {
        TabWeixin=(LinearLayout) findViewById(R.id.id_tab_weixin);
        TabFind=(LinearLayout) findViewById(R.id.id_tab_find);
        TabContacts=(LinearLayout) findViewById(R.id.id_tab_contacts);
        TabMe=(LinearLayout) findViewById(R.id.id_tab_me);

        //初始化四个ImageButton
        BtnWeixinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
        BtnContactsImg = (ImageButton) findViewById(R.id.id_tab_ontacts_img);
        BtnFindImg = (ImageButton) findViewById(R.id.id_tab_find_img);
        BtnMeImg = (ImageButton) findViewById(R.id.id_tab_me_img);
    }
    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_weixin:
                selectTab(0);//当点击的是微信的Tab就选中微信的Tab
                break;
            case R.id.id_tab_contacts:
                selectTab(1);
                break;
            case R.id.id_tab_find:
                selectTab(2);
                break;
            case R.id.id_tab_me:
                selectTab(3);
                break;

        }
    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            //当选中点击的是微信的Tab时
            case 0:
                //设置微信的ImageButton为绿色
                BtnWeixinImg.setImageResource(R.drawable.tabweixinpressed);
                //如果微信对应的Fragment没有实例化，则进行实例化，并显示出来
                if (FragWeixin == null) {
                    FragWeixin = new WeixinFragment();
                    transaction.add(R.id.id_content, FragWeixin);
                } else {
                    //如果微信对应的Fragment已经实例化，则直接显示出来
                    transaction.show(FragWeixin);
                }
                break;
            case 1:
                BtnContactsImg.setImageResource(R.drawable.tabcontactspressed);
                if (FragContacts == null) {
                    FragContacts = new ContactsFragment();
                    transaction.add(R.id.id_content, FragContacts);
                } else {
                    transaction.show(FragContacts);
                }
                break;
            case 2:
                BtnFindImg.setImageResource(R.drawable.tabfindpressed);
                if (FragFind == null) {
                    FragFind = new FindFragment();
                    transaction.add(R.id.id_content, FragFind);
                } else {
                    transaction.show(FragFind);
                }
                break;
            case 3:
                BtnMeImg.setImageResource(R.drawable.tabmepressed);
                if (FragMe == null) {
                    FragMe = new MeFragment();
                    transaction.add(R.id.id_content, FragMe);
                } else {
                    transaction.show(FragMe);
                }
                break;
        }
        //不要忘记提交事务
        transaction.commit();
    }

    //将四个的Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        if (FragWeixin != null) {
            transaction.hide(FragWeixin);
        }
        if (FragContacts != null) {
            transaction.hide(FragContacts);
        }
        if (FragFind != null) {
            transaction.hide(FragFind);
        }
        if (FragMe != null) {
            transaction.hide(FragMe);
        }
    }



    //将四个ImageButton置为灰色
    private void resetImgs() {
        BtnWeixinImg.setImageResource(R.drawable.tabweixinnormal);
        BtnContactsImg.setImageResource(R.drawable.tabcontactsnormal);
        BtnFindImg.setImageResource(R.drawable.tabfindnormal);
        BtnMeImg.setImageResource(R.drawable.tabmenormal);
    }

    }



