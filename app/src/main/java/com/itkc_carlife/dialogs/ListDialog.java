package com.itkc_carlife.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.itkc_carlife.R;
import com.itkc_carlife.handlers.ColorHandle;
import com.itkc_carlife.tool.WinTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDialog {

    private Context context;
    private AlertDialog ad;
    private Window window;
    private TextView title;
    private ListView list;
    private LinearLayout LL;

    private Map<Integer, Integer> coloaMap = null;

    public ListDialog(Context context) {
        this.context = context;
        ad = new AlertDialog.Builder(context).create();
        ad.show();
        window = ad.getWindow();
        window.setContentView(R.layout.layout_list_dailog);

        title = (TextView) window.findViewById(R.id.dialog_title);
        list = (ListView) window.findViewById(R.id.dialog_list);
        LL = (LinearLayout) window.findViewById(R.id.dialog_LL);

        coloaMap = new HashMap<Integer, Integer>();

        setLayout();
    }

    public void setTitle(String name) {
        title.setText(name);
    }

    public void setTitleColor(int color) {
        LL.setBackgroundColor(color);
    }

    public void setTitleGone() {
        LL.setVisibility(View.GONE);
    }

    public void setList(String[] items) {
        List<String> list = new ArrayList<String>();
        for (String str : items) {
            list.add(str);
        }
        setList(list);
    }

    public void setList(List<String> items) {
        if (list != null) {
            ListBaseAdapter lba = new ListBaseAdapter(items);
            list.setAdapter(lba);
        }
    }

    public void setItemListener(OnItemClickListener listener) {
        list.setOnItemClickListener(listener);
    }

    public void setLayout() {
        setLayout(0.8, 0.7);
    }

    public void setLayout(double Xnum, double Ynum) {
        if (list.getCount() > 7) {
            window.setLayout((int) (WinTool.getWinWidth(context) * Xnum),
                    (int) (WinTool.getWinHeight(context) * Ynum));
        }
    }

    public void dismiss() {
        ad.dismiss();
    }

    public void setItemColor(int position, int color) {
        coloaMap.put(position, color);
    }

    public void setItemColor(int color) {
        for (int i = 0; i < list.getCount(); i++) {
            coloaMap.put(i, color);
        }
    }

    class ListBaseAdapter extends BaseAdapter {

        private List<String> list;
        private LayoutInflater infater;

        public ListBaseAdapter(List<String> list) {
            this.list = list;
            infater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = infater.inflate(R.layout.layout_list_dailog_item, null);
            }

            TextView item = (TextView) convertView
                    .findViewById(R.id.dialog_item_text);
            item.setText(list.get(position));

            if (coloaMap.containsKey(position)) {
                item.setTextColor(ColorHandle.getColorForID(context,
                        coloaMap.get(position)));
            } else {
                item.setTextColor(ColorHandle
                        .getColorForID(context, R.color.text_black));
            }

            return convertView;
        }
    }
}
