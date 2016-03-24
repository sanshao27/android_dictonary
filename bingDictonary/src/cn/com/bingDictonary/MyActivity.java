package cn.com.bingDictonary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     *
     */

    private TextView listView;
    private String url = "http://xtk.azurewebsites.net/BingService.aspx";
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (TextView) findViewById(R.id.textID);

        //新建线程Thread，开始网络操作
        new Thread(){
            @Override
            public void run() {
                try {
                    //封装url
                    URL httpUrl = new URL(url);
                    //和服务器建立联系
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    //设置超时
                    conn.setReadTimeout(5000);
                    //设置请求方式为post
                    conn.setRequestMethod("POST");
                    //拼接要发送的信息,内容为作者示例的数据
                    String message = "Action=search&Word=try";
                    //写入到输出流
                    OutputStream is = conn.getOutputStream();
                    is.write(message.getBytes());

                    //把获取的数据不断存放到stringBuffer中
                    final StringBuffer bf = new StringBuffer();
                    //使用read向输入流中读取数据，不断存放到缓冲流中
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

                    //定义
                    String line;
                    //只要还没有读取完，一只读取
                    while ((line = reader.readLine()) != null){
                         //在stringbuffer中添加
                        bf.append(line+"\n");
                    }
                     //使用handler更新UI，
                    // 这里也可以使用sendMessage（），handMessage（）来操作
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //进行json解析
//                            try {
                                String s1 = null;
                                String json = bf.toString();
                                s1 = json.substring(1,json.length()-2);
                                System.out.println("-----"+s1+"sfdsdfsf");
                                String [] str = s1.split(",");
                                for (int i = 0;i<str.length;i++) {
                                    System.out.println("==========" + str[i]+ "----------");
                                }
//                                JSONObject jsonObject = new JSONObject(json);
//                                jsonObject.optString("ismy","");
//                                System.out.println(jsonObject);
                                System.out.println(json);
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }

                        }
                    });
                }catch (MalformedURLException e){
                   e.printStackTrace();
                }catch (IOException e){
                   e.printStackTrace();
                }
            }
        }.start();
    }
}

