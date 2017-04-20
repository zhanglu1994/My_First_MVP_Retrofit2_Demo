package zhangl.my_first_mvp_retrofit2_demo.main;


public interface MainView extends BaseView {

    void getDataSuccess(MainModel model);

    void getDataFail(String msg);

}
