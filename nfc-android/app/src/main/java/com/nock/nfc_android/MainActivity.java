package com.nock.nfc_android;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private AQuery aq;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
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
        writeTag(getTextAsNdef(), tag);
//        if (tag != null) {
//            byte[] tagId = tag.getId();
//            mTextView.setText("TagID: " + toHexString(tagId));
//        }
    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }

                if (ndef.getMaxSize() < size) {
                    return false;
                }

                ndef.writeNdefMessage(message);
                Toast.makeText(getApplicationContext(), "쓰기 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "포맷되지 않은 태그이므로 먼저 포맷하고 데이터를 씁니다.",
                        Toast.LENGTH_SHORT).show();

                NdefFormatable formatable = NdefFormatable.get(tag);
                if (formatable != null) {
                    try {
                        formatable.connect();
                        formatable.format(message);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }

        return true;
    }

    private NdefMessage getTextAsNdef() {
        byte[] textBytes = mTextView.getText().toString().getBytes();

        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[]{},
                textBytes);

        return new NdefMessage(new NdefRecord[]{textRecord});
    }

    public static String toHexString(byte[] data) {
        final String CHARS = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }

    @Override
    public void initView() {
        aq = new AQuery(this);

        mTextView = (TextView) findViewById(R.id.main_text_view);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }
}
