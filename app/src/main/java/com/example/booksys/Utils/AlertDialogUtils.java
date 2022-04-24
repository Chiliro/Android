package com.example.booksys.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.booksys.R;

public class AlertDialogUtils {


    /**
     * @param context        上下文
     * @param title          标题
     * @param content        内容
     * @param btnCancelText  取消按钮文本
     * @param btnSureText    确定按钮文本
     * @param cancelListener 取消监听
     * @param sureListener   确定监听
     * @return AlertDialog
     */
    public synchronized static AlertDialog showDialog(Context context, String title, String content,
                                                      String btnCancelText, String btnSureText, DialogInterface.OnClickListener cancelListener,
                                                      DialogInterface.OnClickListener sureListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();

        // dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.setCanceledOnTouchOutside(false);

        //dialog弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCancelable(false);

        View view = View.inflate(context, R.layout.alert_dialog, null);

        //标题
        TextView tvTitle = view.findViewById(R.id.tv_alert_title);

        //内容
        TextView tvContent = view.findViewById(R.id.tv_alert_content);

        //取消按钮
        TextView buttonCancel = view.findViewById(R.id.tv_dialog_cancel);

        //确定按钮
        TextView buttonOk = view.findViewById(R.id.tv_dialog_ok);

        //view
        View viewLine = view.findViewById(R.id.view_line);

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        tvContent.setText(TextUtils.isEmpty(content) ? "" : content);

        if (TextUtils.isEmpty(btnCancelText)) {
            buttonCancel.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {
            buttonCancel.setText(btnCancelText);
        }
        if (TextUtils.isEmpty(btnSureText)) {
            buttonOk.setVisibility(View.GONE);
        } else {
            buttonOk.setText(btnSureText);
        }

        final AlertDialog dialogFinal = dialog;
        final DialogInterface.OnClickListener finalCancleListener = cancelListener;
        final DialogInterface.OnClickListener finalSureListener = sureListener;
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCancleListener.onClick(dialogFinal, DialogInterface.BUTTON_NEGATIVE);
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSureListener.onClick(dialogFinal, DialogInterface.BUTTON_POSITIVE);
            }
        });

        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }
}


