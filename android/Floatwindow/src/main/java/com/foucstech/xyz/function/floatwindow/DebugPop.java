package com.foucstech.xyz.function.floatwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * debug快捷操作悬浮球
 *
 * @author 李昂 <liang@xyz.cn>
 * @date 2018-09-17 10:12
 * @since 4.17.10
 */
public class DebugPop {
    private static final String TAG = "DebugPop";
    private static String selectedHost = "";

    /**
     * 显示单选操作
     *
     * @param activity
     * @param id       字符串数组id
     * @param title    dialog标题
     * @param action   获取到被保存的选项的动作
     * @param listener 当选项被选中的监听事件
     */
    public static void showDialog(Activity activity, int id, int title, GetSelectedValueAction action, OnConfigItemSelectedListener listener) {
        AlertDialog alertDialog;
        Map<String, String> ips = new LinkedHashMap<>();
        ScrollView dialogView;
        String configValue = "";
        String dialogTitle = activity.getResources().getString(title);
        if (action != null) {
            configValue = action.getSelectedValue();
        }
        dialogView = (ScrollView) LayoutInflater.from(activity).inflate(R.layout.dialog, null);
        setupLayoutParams(dialogView);
        setupKeyBoard(activity, dialogView);
        String[] ipTablets = activity.getResources().getStringArray(id);
        XRadioGroup radioGroup = dialogView.findViewById(R.id.rg_main);
        for (String s : ipTablets) {
            String ipTitle = s.split("\\|")[0].trim();
            String ipDetail = s.split("\\|")[1].trim();
            if (ipTitle == null) {
                continue;
            }
            ips.put(ipTitle, ipDetail);
            LinearLayout radioItem = createRadioItem(activity, ipTitle, ipDetail, configValue.equals(ipDetail));
            radioGroup.addView(radioItem, 0);
        }
        TextView tip = new TextView(activity);
        tip.setText("每次启动App会自动应用此处设置");
        radioGroup.addView(tip, 0);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton.getId() != R.id.radio_btn) {
                selectedHost = ips.get(radioButton.getText());
            } else {
                selectedHost = ((EditText) group.findViewById(R.id.custom_ip)).getText().toString().trim();
            }
        });
        if (radioGroup.getCheckedRadioButtonId() == -1 && !TextUtils.isEmpty(configValue)) {
            ((EditText) radioGroup.findViewById(R.id.custom_ip)).setText(configValue);
            selectedHost = configValue;
            ((RadioButton) radioGroup.findViewById(R.id.rb_custom_ip)).setChecked(true);
        }
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.rb_custom_ip) {
                        selectedHost = ((EditText) radioGroup.findViewById(R.id.custom_ip)).getText().toString().trim();
                        //Node需要输入http://
                        if (!selectedHost.startsWith("http") &&
                                dialogTitle.equals(activity.getResources().getString(R.string.select_node_host))) {
                            selectedHost = "http://" + selectedHost;
                        }
                        //RN不需要输http://
                        if (selectedHost.startsWith("http") &&
                                dialogTitle.equals(activity.getResources().getString(R.string.select_rn_host))) {
                            selectedHost = selectedHost.split("://")[1];
                        }
                    }
                    if (TextUtils.isEmpty(selectedHost)) {
                        Toast.makeText(activity, "输入不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Patterns.WEB_URL.matcher(selectedHost).matches() && !selectedHost.contains("localhost")) {
                        Toast.makeText(activity, "请输入正确的URL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (listener != null) {
                        listener.onSelected(selectedHost);
                    }
                })
                .create();
        alertDialog.show();
    }

    /**
     * 设定输入相关
     *
     * @param activity
     * @param dialogView
     */
    private static void setupKeyBoard(Activity activity, ScrollView dialogView) {
        EditText editText = dialogView.findViewById(R.id.custom_ip);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ((RadioButton) dialogView.findViewById(R.id.rb_custom_ip)).setChecked(true);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (((RadioButton) dialogView.findViewById(R.id.rb_custom_ip)).isChecked()) {
                    selectedHost = s.toString().trim();
                }
            }
        });
    }

    /**
     * 设定视图相关参数
     *
     * @param dialogView
     */
    private static void setupLayoutParams(ScrollView dialogView) {
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        dialogView.setLayoutParams(layoutParams);
    }

    /**
     * 创建radiobutton 参数
     *
     * @param activity
     * @param ipTitle       标题
     * @param ipDetail      详情
     * @param isNeedChecked 是否需要选中
     * @return
     */
    private static LinearLayout createRadioItem(Activity activity, String ipTitle, String ipDetail, boolean isNeedChecked) {
        Log.d(TAG, "createRadioItem: " + "activity = [" + activity + "], ipTitle = [" + ipTitle + "], ipDetail = [" + ipDetail + "], isNeedChecked = [" + isNeedChecked + "]");
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_dialog, null);
        itemView.setLayoutParams(new XRadioGroup.LayoutParams(XRadioGroup.LayoutParams.MATCH_PARENT, XRadioGroup.LayoutParams.WRAP_CONTENT));
        ((RadioButton) itemView.findViewById(R.id.radio_btn)).setText(ipTitle);
        if (isNeedChecked) {
            ((RadioButton) itemView.findViewById(R.id.radio_btn)).setChecked(true);
        }
        ((TextView) itemView.findViewById(R.id.ip_detail)).setText(ipDetail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            itemView.findViewById(R.id.radio_btn).setId(View.generateViewId());
        }
        return itemView;
    }
}
