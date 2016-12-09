package com.nock.nfc_android;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidquery.AQuery;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private AQuery aq;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private MainPresenter mMainPresenter;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }


    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mMainPresenter.writeTag(mMainPresenter.getTextAsNdef(), tag);
//        if (tag != null) {
//            byte[] tagId = tag.getId();
//            mTextView.setText("TagID: " + toHexString(tagId));
//        }
    }

    @Override
    public void initView() {
        aq = new AQuery(this);
        mMainPresenter = new MainPresenter(this);

        mTextView = (TextView) findViewById(R.id.main_text_view);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }
}
