package server.yf.com.remotecontrolserver_as.LanMina;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import server.yf.com.remotecontrolserver_as.R;

public class MainActivity extends Activity implements TCPServer.CallBack,View.OnClickListener{
    TextView tv_message;

    private TCPServer tcpServer;
    private Button btnOpen;
    private Button btnClose;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CharSequence content = tv_message.getText();
            tv_message.setText(String.valueOf(content) + "\n" + msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanmina_main);
        tv_message=(TextView)findViewById(R.id.tv_message);
        btnOpen=(Button) findViewById(R.id.btn_open);
        btnOpen.setOnClickListener(this);
        btnClose=(Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open:
                new Thread(new Runnable() {
                    @Override public void run() {
                        startService(new Intent(getApplicationContext(),TCPServer.class));

                        System.out.println("开启Service");
                    }
                }).start();
                break;
            case R.id.btn_close:
                new Thread(new Runnable() {
                    @Override public void run() {
                        tcpServer.close();
                    }
                }).start();
        break;
        }
    }

    @Override
    public void callBack(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        handler.sendMessage(message);
    }
}
