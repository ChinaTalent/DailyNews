package com.jw.dailyNews.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.ThemeUtils;
import com.jw.dailyNews.wiget.ElasticTouchListener;
import com.jw.dailyNews.wiget.LoadingPage;

import java.util.HashMap;

import Lib.NewsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class FragmentMe extends BaseFragment implements View.OnClickListener,
        NewsManager.AuthListener, NewsManager.ShowUserListener {

    @BindView(R.id.tv_mName)
    TextView tvMName;
    @BindView(R.id.tv_mDesc)
    TextView tvMDesc;
    public static RelativeLayout rlMe;
    @BindView(R.id.tv_mWeibo)
    TextView tvMWeibo;
    @BindView(R.id.tv_mFans)
    TextView tvMFans;
    @BindView(R.id.tv_mConcerns)
    TextView tvMConcerns;

    private CircleImageView ivMe;
    private AlertDialog dialog;
    private HashMap<String, Object> userInfos;
    private Platform Sina= ShareSDK.getPlatform(SinaWeibo.NAME);

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvMName.setText((String) userInfos.get("name"));
            tvMDesc.setText((String) userInfos.get("description"));
            tvMWeibo.setText(userInfos.get("credit_score") + "");
            tvMFans.setText(userInfos.get("followers_count") + "");
            tvMConcerns.setText(userInfos.get("friends_count") + "");
            Glide.with(getContext()).load(userInfos.get("profile_image_url")).into(ivMe);
            return false;
        }
    }) ;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ci_me:
                if (NewsManager.getInstance().isValid(Sina))
                    showExitDialog();
                else {
                    NewsManager.getInstance().auth(Sina,this);
                }
                break;
            case R.id.cancel_exit:
                dialog.dismiss();
                break;
            case R.id.ok_exit:
                dialog.dismiss();
                NewsManager.getInstance().exitAuth(Sina);
                ivMe.setImageResource(R.drawable.icon_default_user);
                tvMName.setText("");
                tvMDesc.setText("");
                tvMWeibo.setText("");
                tvMFans.setText("");
                tvMConcerns.setText("");
                break;
        }
    }


    @Override
    protected LoadingPage.LoadResult load() {
        return LoadingPage.LoadResult.success;
    }

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        unbinder = ButterKnife.bind(this, view);
        ivMe = (CircleImageView) view.findViewById(R.id.ci_me);
        rlMe = (RelativeLayout) view.findViewById(R.id.rl_me);
        rlMe.setOnTouchListener(new ElasticTouchListener() {});
        ivMe.setOnClickListener(this);
        ThemeUtils.changeViewColor(rlMe, CacheUtils.getCacheInt("indicatorColor", Color.RED));
        updateUserInfos();
        return view;
    }

    /**
     * 更新用户资料
     */
    private void updateUserInfos() {
        if (NewsManager.getInstance().isValid(Sina)) {
            //ThemeUtils.show(getActivity(),"正在获取用户信息");
            NewsManager.getInstance().showUser(Sina, this);
        }
    }


    /**
     * 退出登录dialog
     */
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //自定义对话框
        dialog = builder.create();
        View view = View.inflate(getContext(), R.layout.dialog_login, null);
        dialog.setView(view);
        TextView cancelExit = (TextView) view.findViewById(R.id.cancel_exit);
        TextView okExit = (TextView) view.findViewById(R.id.ok_exit);
        cancelExit.setOnClickListener(this);
        okExit.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onAuthSuccess(Platform platform) {
        //ThemeUtils.show(getActivity(),"授权成功");
        updateUserInfos();
    }

    @Override
    public void onShowUserSuccess(HashMap<String, Object> userInfos) {
        this.userInfos = userInfos;
        //ThemeUtils.show(getActivity(),"获取用户信息成功");
        mHandler.sendEmptyMessage(0);
    }
}
