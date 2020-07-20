package com.example.socketdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class TcpActivity extends AppCompatActivity {
    private Button sendBtn;
    private EditText editText;
    private TextView contentTv;
    private TcpClientBiz clientBiz;
    private EditText edIp;
    private EditText edPort;
    private Button connection;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        title = findViewById(R.id.id_title);
        title.setText("TCP测试");
    }

    private void initView() {
        sendBtn = findViewById(R.id.send_btn);
        editText = findViewById(R.id.ed_msg);
        contentTv = findViewById(R.id.text_content);
        edIp = findViewById(R.id.ed_ip);
        edPort = findViewById(R.id.ed_port);
        connection = findViewById(R.id.connection_btn);

        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = edIp.getText().toString();
                String port = edPort.getText().toString();
                if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(port)) {
                    Integer dk = Integer.valueOf(port);
                    try {
                        clientBiz  = new TcpClientBiz(ip,dk);
                        if (clientBiz != null) {
                            Log.d("测试", "onCreate: listenner");
                            clientBiz.setOnMsgReturnedListenner(new TcpClientBiz.OnMsgReturnedListenner() {
                                @Override
                                public void OnMsgReturned(String msg) {
                                    Log.d("测试", "onCreate:msg");
                                    appendMsgToContent(msg + "");
                                }

                                @Override
                                public void OnFailure(Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (Exception e) {
                        appendMsgToContent("Error:"+e);
                    }
                } else {
                    Toast.makeText(TcpActivity.this,"IP和端口不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str = editText.getText().toString();
                editText.setText("");
                if (TextUtils.isEmpty(str)){
                    return;
                }
                if (clientBiz != null) {
                    clientBiz.SendMsg(str);
                }else {
                    Toast.makeText(TcpActivity.this,"未连接server",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void appendMsgToContent(String msg){
        contentTv.append(msg+"\n");
    }

    @Override
    protected void onDestroy() {
        if (clientBiz != null) {
            try {
                clientBiz.onTcpDestroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
