package zhangl.my_first_mvp_retrofit2_demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import zhangl.my_first_mvp_retrofit2_demo.R;
import zhangl.my_first_mvp_retrofit2_demo.main.MainModel;
import zhangl.my_first_mvp_retrofit2_demo.main.MainPresenter;
import zhangl.my_first_mvp_retrofit2_demo.main.MainView;
import zhangl.my_first_mvp_retrofit2_demo.other.MvpActivity;
import zhangl.my_first_mvp_retrofit2_demo.retrofit2.ApiCallback;
import zhangl.my_first_mvp_retrofit2_demo.retrofit2.RetrofitCallback;


/**
 * 由Activity/Fragment实现View里方法，包含一个Presenter的引用
 * Created by WuXiaolong on 2015/9/23.
 * github:https://github.com/WuXiaolong/
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @Bind(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBarAsHome("MVP+Retrofit+Rxjava");

    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    public void getDataSuccess(MainModel model) {
        //接口成功回调
        dataSuccess(model);
    }

    @Override
    public void getDataFail(String msg) {
        toastShow("网络不给力");

    }


    @OnClick({R.id.button0, R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button0:
                loadDataByRetrofit();
                break;
            case R.id.button1:
                loadDataByRetrofitRxjava();
                break;
            case R.id.button2:
                //请求接口
                mvpPresenter.loadDataByRetrofitRxjava("101310201");
                break;
        }
    }

    private void loadDataByRetrofit() {
        showProgressDialog();
        Call<MainModel> call = apiStores.loadDataByRetrofit("101280101");
        call.enqueue(new RetrofitCallback<MainModel>() {
            @Override
            public void onSuccess(MainModel model) {
                dataSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(msg);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getMessage());
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
        addCalls(call);
    }

    //全国+国外主要城市代码http://mobile.weather.com.cn/js/citylist.xml
    private void loadDataByRetrofitRxjava() {
        showProgressDialog();
        addSubscription(apiStores.loadDataByRetrofitRxjava("101200801"),
                new ApiCallback<MainModel>() {
                    @Override
                    public void onSuccess(MainModel model) {
                        dataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        toastShow(msg);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void dataSuccess(MainModel model) {
        MainModel.WeatherinfoBean weatherinfo = model.getWeatherinfo();
        String showData = getResources().getString(R.string.city) + weatherinfo.getCity()
                + getResources().getString(R.string.wd) + weatherinfo.getWD()
                + getResources().getString(R.string.ws) + weatherinfo.getWS()
                + getResources().getString(R.string.time) + weatherinfo.getTime();
        text.setText(showData);
    }
}
