package com.nock.nfc_android;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by secret on 12/5/16.
 */

public class MainPresenter {
    private MainContract.View view;
    private MainModel mMainModel;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.mMainModel = new MainModel();
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
                Toast.makeText((MainActivity) view, "쓰기 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText((MainActivity) view, "포맷되지 않은 태그이므로 먼저 포맷하고 데이터를 씁니다.",
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

    public NdefMessage getTextAsNdef() {
//        byte[] textBytes = mTextView.getText().toString().getBytes();
        byte[] textBytes = "Hello, World!".getBytes();

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
}
