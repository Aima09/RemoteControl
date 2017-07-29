package com.yf.remotecontrolclient.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.domain.Writer;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;

public class WriteActivity extends BaseActivity {
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initspinner();
        mEditText = (EditText) (findViewById(R.id.et_write));
//		mEditText.setFocusable(true);
//		mEditText.setFocusableInTouchMode(true);
//		mEditText.requestFocus();
        mEditText.addTextChangedListener(mTextWatcher);
        businessServiceImpl = new MouseBusinessServiceImpl();
//		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private String data = "";
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // mTextView.setText(s+"");//将输入的内容实时显示
        }

        String data;
        boolean isfirst = true;

        @Override
        public void afterTextChanged(Editable s) {
            editStart = mEditText.getSelectionStart();
            editEnd = mEditText.getSelectionEnd();
            Writer writer = new Writer();
            writer.setCmd("write");
            writer.setData(s.toString());
            businessServiceImpl.sendWriter(writer);
        }
    };
    private MouseBusinessServiceImpl businessServiceImpl;

    protected void onDestroy() {
        data = "";
        super.onDestroy();
    }

    ;

}
