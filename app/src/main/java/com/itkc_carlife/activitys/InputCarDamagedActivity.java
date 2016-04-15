package com.itkc_carlife.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/13.
 */
public class InputCarDamagedActivity extends BaseActivity {

    public final static String WHERE_KEY = "where_form";
    public final static String INSURANCE_KEY = "insurance";
    public final static String REPAIR_KEY = "repair";

    public final static String A = "A";
    public final static String B = "B";
    public final static String C = "C";
    public final static String D = "D";
    public final static String E = "E";
    public final static String F = "F";
    public final static String G = "G";
    public final static String H = "H";
    public final static String I = "I";
    public final static String J = "J";
    public final static String K = "K";
    public final static String L = "L";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.carDamaged_damaged11)
    private RelativeLayout damaged11;
    @ViewInject(R.id.carDamaged_damaged12)
    private RelativeLayout damaged12;
    @ViewInject(R.id.carDamaged_damaged13)
    private RelativeLayout damaged13;
    @ViewInject(R.id.carDamaged_damaged21)
    private RelativeLayout damaged21;
    @ViewInject(R.id.carDamaged_damaged22)
    private RelativeLayout damaged22;
    @ViewInject(R.id.carDamaged_damaged23)
    private RelativeLayout damaged23;
    @ViewInject(R.id.carDamaged_damaged31)
    private RelativeLayout damaged31;
    @ViewInject(R.id.carDamaged_damaged32)
    private RelativeLayout damaged32;
    @ViewInject(R.id.carDamaged_damaged33)
    private RelativeLayout damaged33;
    @ViewInject(R.id.carDamaged_damaged41)
    private RelativeLayout damaged41;
    @ViewInject(R.id.carDamaged_damaged42)
    private RelativeLayout damaged42;
    @ViewInject(R.id.carDamaged_damaged43)
    private RelativeLayout damaged43;
    @ViewInject(R.id.carDamaged_damagedImageForA)
    private ImageView imageForA;
    @ViewInject(R.id.carDamaged_damagedTextForA)
    private TextView textForA;
    @ViewInject(R.id.carDamaged_damagedSizeForA)
    private TextView sizeForA;
    @ViewInject(R.id.carDamaged_damagedImageForB)
    private ImageView imageForB;
    @ViewInject(R.id.carDamaged_damagedTextForB)
    private TextView textForB;
    @ViewInject(R.id.carDamaged_damagedSizeForB)
    private TextView sizeForB;
    @ViewInject(R.id.carDamaged_damagedImageForC)
    private ImageView imageForC;
    @ViewInject(R.id.carDamaged_damagedTextForC)
    private TextView textForC;
    @ViewInject(R.id.carDamaged_damagedSizeForC)
    private TextView sizeForC;
    @ViewInject(R.id.carDamaged_damagedImageForD)
    private ImageView imageForD;
    @ViewInject(R.id.carDamaged_damagedTextForD)
    private TextView textForD;
    @ViewInject(R.id.carDamaged_damagedSizeForD)
    private TextView sizeForD;
    @ViewInject(R.id.carDamaged_damagedImageForE)
    private ImageView imageForE;
    @ViewInject(R.id.carDamaged_damagedTextForE)
    private TextView textForE;
    @ViewInject(R.id.carDamaged_damagedSizeForE)
    private TextView sizeForE;
    @ViewInject(R.id.carDamaged_damagedImageForF)
    private ImageView imageForF;
    @ViewInject(R.id.carDamaged_damagedTextForF)
    private TextView textForF;
    @ViewInject(R.id.carDamaged_damagedSizeForF)
    private TextView sizeForF;
    @ViewInject(R.id.carDamaged_damagedImageForG)
    private ImageView imageForG;
    @ViewInject(R.id.carDamaged_damagedTextForG)
    private TextView textForG;
    @ViewInject(R.id.carDamaged_damagedSizeForG)
    private TextView sizeForG;
    @ViewInject(R.id.carDamaged_damagedImageForH)
    private ImageView imageForH;
    @ViewInject(R.id.carDamaged_damagedTextForH)
    private TextView textForH;
    @ViewInject(R.id.carDamaged_damagedSizeForH)
    private TextView sizeForH;
    @ViewInject(R.id.carDamaged_damagedImageForI)
    private ImageView imageForI;
    @ViewInject(R.id.carDamaged_damagedTextForI)
    private TextView textForI;
    @ViewInject(R.id.carDamaged_damagedSizeForI)
    private TextView sizeForI;
    @ViewInject(R.id.carDamaged_damagedImageForJ)
    private ImageView imageForJ;
    @ViewInject(R.id.carDamaged_damagedTextForJ)
    private TextView textForJ;
    @ViewInject(R.id.carDamaged_damagedSizeForJ)
    private TextView sizeForJ;
    @ViewInject(R.id.carDamaged_damagedImageForK)
    private ImageView imageForK;
    @ViewInject(R.id.carDamaged_damagedTextForK)
    private TextView textForK;
    @ViewInject(R.id.carDamaged_damagedSizeForK)
    private TextView sizeForK;
    @ViewInject(R.id.carDamaged_damagedImageForL)
    private ImageView imageForL;
    @ViewInject(R.id.carDamaged_damagedTextForL)
    private TextView textForL;
    @ViewInject(R.id.carDamaged_damagedSizeForL)
    private TextView sizeForL;
    @ViewInject(R.id.title_sainningIcon)
    private ImageView sainningIcon;

    private Bundle mBundle;
    private String where;
    private String settlingId, repairId;

    private List<String> pathListForA;
    private List<String> pathListForB;
    private List<String> pathListForC;
    private List<String> pathListForD;
    private List<String> pathListForE;
    private List<String> pathListForF;
    private List<String> pathListForG;
    private List<String> pathListForH;
    private List<String> pathListForI;
    private List<String> pathListForJ;
    private List<String> pathListForK;
    private List<String> pathListForL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_damaged);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle b = data.getExtras();
            switch (requestCode) {
                case AddDamagedImageActivity.REQUEST_CODE:
                    String w = b.getString(AddDamagedImageActivity.WHERE_KEY);
                    List<String> l = b.getStringArrayList(AddDamagedImageActivity.PATH_LIST_KEY);
                    List<String> oblPathList = getPathList(w);
                    oblPathList.removeAll(oblPathList);
                    oblPathList.addAll(l);
                    setDamagedMessage(w);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.carDamaged_nextBtn, R.id.title_sainningIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.carDamaged_nextBtn:
                if (isThrough()) {
                    Passageway.jumpActivity(context, InputVideoActivity.class, mBundle);
                } else {
                    MessageHandler.showToast(context, "最少添加一张图片");
                }
                break;
            case R.id.title_sainningIcon:
                jumpQrCodeActivity();
                break;
        }
    }

    @OnClick({R.id.carDamaged_damagedTextForA, R.id.carDamaged_damagedTextForB, R.id.carDamaged_damagedTextForC, R.id.carDamaged_damagedTextForD, R.id.carDamaged_damagedTextForE,
            R.id.carDamaged_damagedTextForF, R.id.carDamaged_damagedTextForG, R.id.carDamaged_damagedTextForH, R.id.carDamaged_damagedTextForI, R.id.carDamaged_damagedTextForJ,
            R.id.carDamaged_damagedTextForK, R.id.carDamaged_damagedTextForL})
    public void addImage(View v) {
        switch (v.getId()) {
            case R.id.carDamaged_damagedTextForA:
                jumpAddImage(A);
                break;
            case R.id.carDamaged_damagedTextForB:
                jumpAddImage(B);
                break;
            case R.id.carDamaged_damagedTextForC:
                jumpAddImage(C);
                break;
            case R.id.carDamaged_damagedTextForD:
                jumpAddImage(D);
                break;
            case R.id.carDamaged_damagedTextForE:
                jumpAddImage(E);
                break;
            case R.id.carDamaged_damagedTextForF:
                jumpAddImage(F);
                break;
            case R.id.carDamaged_damagedTextForG:
                jumpAddImage(G);
                break;
            case R.id.carDamaged_damagedTextForH:
                jumpAddImage(H);
                break;
            case R.id.carDamaged_damagedTextForI:
                jumpAddImage(I);
                break;
            case R.id.carDamaged_damagedTextForJ:
                jumpAddImage(J);
                break;
            case R.id.carDamaged_damagedTextForK:
                jumpAddImage(K);
                break;
            case R.id.carDamaged_damagedTextForL:
                jumpAddImage(L);
                break;
        }
    }

    private void jumpQrCodeActivity() {
        Bundle b = new Bundle();
        b.putString(QRCodeActivity.QR_CODE, settlingId);
        Passageway.jumpActivity(context, QRCodeActivity.class, b);
    }

    private void jumpAddImage(String index) {
        Bundle b = new Bundle();
        b.putString(AddDamagedImageActivity.WHERE_KEY, index);
        b.putString(WHERE_KEY, where);
        if (where.equals(INSURANCE_KEY)) {
            b.putString(SettingInputMessageActivity.SRTTLING_ID_KEY, settlingId);
        } else if (where.equals(REPAIR_KEY)) {
            b.putString(RepairInputMessageActivity.REPAIR_ID_KEY, repairId);
        }
        b.putStringArrayList(AddDamagedImageActivity.PATH_LIST_KEY, (ArrayList) getPathList(index));
        b.putBoolean(SettingInputMessageActivity.IS_THR_MAIN, mBundle.getBoolean(SettingInputMessageActivity.IS_THR_MAIN));
        Passageway.jumpActivity(context, AddDamagedImageActivity.class, AddDamagedImageActivity.REQUEST_CODE, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("车辆定损");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            where = mBundle.getString(WHERE_KEY);
            if (where.equals(INSURANCE_KEY)) {
                settlingId = mBundle.getString(SettingInputMessageActivity.SRTTLING_ID_KEY);
            } else if (where.equals(REPAIR_KEY)) {
                repairId = mBundle.getString(RepairInputMessageActivity.REPAIR_ID_KEY);
            }
            if (mBundle.getBoolean(SettingInputMessageActivity.IS_THR_MAIN)) {
                sainningIcon.setVisibility(View.VISIBLE);
            }
            initDamagedImage();
            initDamagedMessage();
        } else {
            finish();
        }

    }

    private void initDamagedMessage() {
        pathListForA = new ArrayList<>(6);
        pathListForB = new ArrayList<>(6);
        pathListForC = new ArrayList<>(6);
        pathListForD = new ArrayList<>(6);
        pathListForE = new ArrayList<>(6);
        pathListForF = new ArrayList<>(6);
        pathListForG = new ArrayList<>(6);
        pathListForH = new ArrayList<>(6);
        pathListForI = new ArrayList<>(6);
        pathListForJ = new ArrayList<>(6);
        pathListForK = new ArrayList<>(6);
        pathListForL = new ArrayList<>(6);
        setDamagedMessage(A);
        setDamagedMessage(B);
        setDamagedMessage(C);
        setDamagedMessage(D);
        setDamagedMessage(E);
        setDamagedMessage(F);
        setDamagedMessage(G);
        setDamagedMessage(H);
        setDamagedMessage(I);
        setDamagedMessage(J);
        setDamagedMessage(K);
        setDamagedMessage(L);
    }

    private void initDamagedImage() {
        double w = WinTool.getWinWidth(context) / 3;
        double h1 = w / 215d * 206d;
        double h2 = w / 215d * 154d;
        double h3 = w / 215d * 172d;
        double h4 = w / 215d * 238d;
        setDamagedParams(damaged11, w, h1);
        setDamagedParams(damaged12, w, h1);
        setDamagedParams(damaged13, w, h1);

        setDamagedParams(damaged21, w, h2);
        setDamagedParams(damaged22, w, h2);
        setDamagedParams(damaged23, w, h2);

        setDamagedParams(damaged31, w, h3);
        setDamagedParams(damaged32, w, h3);
        setDamagedParams(damaged33, w, h3);

        setDamagedParams(damaged41, w, h4);
        setDamagedParams(damaged42, w, h4);
        setDamagedParams(damaged43, w, h4);
    }

    private void setDamagedParams(View view, double w, double h) {
        view.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));
    }

    public void setDamagedMessage(String index) {
        List<String> l = getPathList(index);
        setSize(index, l.size());
        setText(index, l.size());
        setImage(index, l);
    }

    private void setImage(String i, List<String> l) {
        ImageView v = getImageView(i);
        if (!l.isEmpty()) {
            v.setVisibility(View.VISIBLE);
            DownloadImageLoader.loadImageForFile(v, l.get(0));
        } else {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private ImageView getImageView(String i) {
        switch (i) {
            case A:
                return imageForA;
            case B:
                return imageForB;
            case C:
                return imageForC;
            case D:
                return imageForD;
            case E:
                return imageForE;
            case F:
                return imageForF;
            case G:
                return imageForG;
            case H:
                return imageForH;
            case I:
                return imageForI;
            case J:
                return imageForJ;
            case K:
                return imageForK;
            case L:
                return imageForL;
        }
        return null;
    }

    private void setText(String index, int s) {
        TextView v = getTextView(index);
        if (s == 0) {
            v.setTextColor(ColorHandle.getColorForID(context, R.color.text_gary_02));
        } else {
            v.setTextColor(ColorHandle.getColorForID(context, R.color.text_red));
        }
    }

    private TextView getTextView(String i) {
        switch (i) {
            case A:
                return textForA;
            case B:
                return textForB;
            case C:
                return textForC;
            case D:
                return textForD;
            case E:
                return textForE;
            case F:
                return textForF;
            case G:
                return textForG;
            case H:
                return textForH;
            case I:
                return textForI;
            case J:
                return textForJ;
            case K:
                return textForK;
            case L:
                return textForL;
        }
        return null;
    }

    private void setSize(String index, int s) {
        TextView v = getSizeView(index);
        if (s == 0) {
            v.setVisibility(View.GONE);
        } else {
            v.setText(String.valueOf(s));
            v.setVisibility(View.VISIBLE);
        }
    }

    private TextView getSizeView(String i) {
        switch (i) {
            case A:
                return sizeForA;
            case B:
                return sizeForB;
            case C:
                return sizeForC;
            case D:
                return sizeForD;
            case E:
                return sizeForE;
            case F:
                return sizeForF;
            case G:
                return sizeForG;
            case H:
                return sizeForH;
            case I:
                return sizeForI;
            case J:
                return sizeForJ;
            case K:
                return sizeForK;
            case L:
                return sizeForL;
        }
        return null;
    }

    private List<String> getPathList(String i) {
        switch (i) {
            case A:
                return pathListForA;
            case B:
                return pathListForB;
            case C:
                return pathListForC;
            case D:
                return pathListForD;
            case E:
                return pathListForE;
            case F:
                return pathListForF;
            case G:
                return pathListForG;
            case H:
                return pathListForH;
            case I:
                return pathListForI;
            case J:
                return pathListForJ;
            case K:
                return pathListForK;
            case L:
                return pathListForL;
        }
        return null;
    }

    public boolean isThrough() {
        for (char c = 'A'; c <= 'L'; c += 1) {
            if (!getPathList(String.valueOf(c)).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
