package com.sunnybear.framework.ui.sheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sunnybear.framework.R;

import java.util.ArrayList;
import java.util.List;

public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_action_sheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayout_content = view.findViewById(R.id.sLayout_content);
        lLayout_content = view.findViewById(R.id.lLayout_content);
        txt_title = view.findViewById(R.id.txt_title);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelTextColor(int color) {
        txt_cancel.setTextColor(color);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    public ActionSheetDialog addSheetItem(String strItem, int color, int size, OnSheetItemClickListener listener) {
        if (sheetItemList == null)
            sheetItemList = new ArrayList<>();
        sheetItemList.add(new SheetItem(strItem, color, size, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) return;

        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LayoutParams) sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            final SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            int color = sheetItem.color;
            int textSize = sheetItem.size;
            final OnSheetItemClickListener listener = sheetItem.itemClickListener;

            TextView textView = new TextView(context);
            textView.setText(strItem);
            if (textSize == 0)
                textView.setTextSize(18);
            else
                textView.setTextSize(textSize);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle)
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                else
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size)
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    else
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    if (i == 1)
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    else if (i < size)
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    else
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                }
            }

            // 字体颜色
            if (color == 0)
                textView.setTextColor(Color.parseColor("#037BFF"));
            else
                textView.setTextColor(color);

            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, height));

            // 点击事件
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSheetItemClick(sheetItem, (index - 1));
                    dialog.dismiss();
                }
            });
            lLayout_content.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {

        void onSheetItemClick(SheetItem item, int position);
    }

    public class SheetItem {
        public String name;
        public OnSheetItemClickListener itemClickListener;
        public int color;
        public int size;

        public SheetItem(String name, int color, int size, OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.size = size;
            this.itemClickListener = itemClickListener;
        }
    }
}
