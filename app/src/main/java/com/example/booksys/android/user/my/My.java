package com.example.booksys.android.user.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.TypeReference;
import com.example.booksys.Login;
import com.example.booksys.R;
import com.example.booksys.Utils.AlertDialogUtils;
import com.example.booksys.Utils.FastJsonUtils;
import com.example.booksys.Utils.NetEnum;
import com.example.booksys.Utils.NetUtil;
import com.example.booksys.pojo.Account;

import java.util.Map;


public class My extends Fragment {

    /**
     * 修改信息状态标志位，false的时候为隐藏
     */
    boolean modifyLayoutFlag = false;

    /**
     * false的时候确认Button为更换QQ使用的
     * true的时候确认Button为修改密码使用的
     */
    boolean Confirm_changesFlag;

    String response;

    SharedPreferences person;
    SharedPreferences.Editor editor;

    TextView identification,                    //个人昵称文本框
            studentName,                        //学生姓名文本框
            studentID,                          //学生id文本框
            readerType,                         //读者类别文本框
            totalCanLendNum,                    //总可借书数量文本框
            CanLendDay;                         //可借天数文本框

    EditText QQ_edit,                           //更换绑定QQ编辑框
            password_edit,                      //密码编辑框
            password_confirm_edit,              //再次确认密码编辑框
            password_check;                     //更改信息的密码校验编辑框


    Button switchAccount,                       //切换账号Button
            modifyInformation,                  //修改信息的Button
            chang_QQ,                           //修改QQ的Button
            chang_password,                     //修改密码的Button
            Confirm_changes,                    //确认修改信息的Button
            github,                             //跳转GitHub的Button
            logoutAccount;                      //注销账号的Button

    LinearLayout modifyInformation_layout,      //修改信息的总菜单
            modifyLayout,                       //修改信息的次级菜单（修改QQ或密码）
            chang_QQ_edit,                      //更改QQ的layout
            chang_password_edit,                //更改密码的layout
            check_password_edit,                //更改信息的密码校验
            Confirm,                            //确认更改layout
            github_layout;                      //github的layout


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("************************************");

        setInformation();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_my, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /**
         *  个人信息文本框编辑
         *
         */

        identification = getView().findViewById(R.id.my_identification);    //个人昵称文本框
        studentName = getView().findViewById(R.id.my_studentName);          //学生姓名文本框
        studentID = getView().findViewById(R.id.my_studentID);              //学生id文本框
        readerType = getView().findViewById(R.id.my_readerType);            //读者类别文本框
        totalCanLendNum = getView().findViewById(R.id.my_totalCanLendNum);  //总可借书数量文本框
        CanLendDay = getView().findViewById(R.id.my_CanLendDay);            //可借天数文本框


        //设置个人信息
        person = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = person.edit();
        setInformation();


        /**
         * 切换账号Button
         */
        switchAccount = getView().findViewById(R.id.switchAccount);
        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        //会清除该进程空间的所有Activity
                startActivity(intent);
            }
        });


        /**
         * 修改信息Button
         */
        modifyInformation = getView().findViewById(R.id.modifyInformation);

        modifyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changInformation();
            }
        });

        /**
         * 修改信息总菜单layout
         *
         */
        modifyInformation_layout = getView().findViewById(R.id.modifyInformation_layout);
        modifyInformation_layout.setClickable(true);
        modifyInformation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changInformation();
            }
        });


        /**
         * 修改次级菜单的layout
         *
         */
        modifyLayout = getView().findViewById(R.id.chang_one_layout);
        modifyLayout.setVisibility(View.GONE);


        /**
         * 修改QQ的Button
         */
        chang_QQ = getView().findViewById(R.id.chang_QQ);
        chang_QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chang_QQ_edit.setVisibility(View.VISIBLE);
                chang_password_edit.setVisibility(View.GONE);
                check_password_edit.setVisibility(View.VISIBLE);
                if(Confirm_changesFlag){
                    //切换button时清空编辑框
                    password_edit.setText("");
                    password_confirm_edit.setText("");
                    password_check.setText("");
                }
                Confirm.setVisibility(View.VISIBLE);
                Confirm_changesFlag = false;
            }
        });

        /**
         * 修改password的Button
         */
        chang_password = getView().findViewById(R.id.chang_password);
        chang_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chang_QQ_edit.setVisibility(View.GONE);
                chang_password_edit.setVisibility(View.VISIBLE);
                Confirm.setVisibility(View.VISIBLE);
                if(!Confirm_changesFlag){
                    //切换button时清空编辑框

                    QQ_edit.setText("");
                    password_check.setText("");
                }
                check_password_edit.setVisibility(View.VISIBLE);
                Confirm_changesFlag = true;
            }
        });


        /**
         * 更改QQ的layout
         */
        chang_QQ_edit = getView().findViewById(R.id.chang_QQ_edit);
        chang_QQ_edit.setVisibility(View.GONE);

        /**
         * 更改password的layout
         */
        chang_password_edit = getView().findViewById(R.id.chang_password_edit);
        chang_password_edit.setVisibility(View.GONE);

        /**
         * 修改信息的密码校验layout
         */
        check_password_edit = getView().findViewById(R.id.My_edit_check_password);
        check_password_edit.setVisibility(View.GONE);

        /**
         * 修改信息编辑框的绑定
         */
        QQ_edit = getView().findViewById(R.id.QQ_edit);
        password_edit = getView().findViewById(R.id.password_edit);
        password_confirm_edit = getView().findViewById(R.id.password_confirm_edit);
        password_check = getView().findViewById(R.id.password_check);


        /**
         * 修改的确认Button
         */
        Confirm_changes = getView().findViewById(R.id.Confirm_changes);
        Confirm_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = person.getString("logPassword", "");            //旧密码


                Account account = new Account();
                account.setIdentification(person.getString("identification", ""));
                account.setPassword(oldPassword);
                account.setRoot((byte) 0);
                account.setRdId(person.getInt("rdId", 0));


                String oldPassword_check = password_check.getText().toString();         //输入旧密码校验

                /**
                 * 更换绑定QQ
                 */
                if (!Confirm_changesFlag) {

                    String QQ = QQ_edit.getText().toString();
                    if (TextUtils.isEmpty(QQ)) {
                        Toast.makeText(getContext(), "请输入换绑的QQ号", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(oldPassword_check)) {
                        Toast.makeText(getContext(), "请输入密码校验", Toast.LENGTH_SHORT).show();
                    } else {

                        AlertDialogUtils.showDialog(getContext(), "修改绑定QQ", "确认修改绑定QQ号吗？", "取消",
                                "确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (!oldPassword_check.equals(oldPassword)) {
                                            password_check.setText("");
                                            Toast.makeText(getContext(), "密码校验错误", Toast.LENGTH_SHORT).show();
                                        } else {

                                            account.setQQ(QQ);
                                            /**
                                             * 向后台发送更换绑定QQ的请求
                                             */
                                            String json = FastJsonUtils.objectToJsonString(account);

                                            String data;
                                            try {
                                                data = "account=" + json
                                                        + "&status=" + 0;

                                                NetUtil netUtil = new NetUtil(data, NetEnum.ChangeAccountServletUrl, "POST");
                                                netUtil.start();
                                                netUtil.join();//线程同步
                                                response = netUtil.getResponse();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                                                    new TypeReference<Map<String, Object>>() {
                                                    });

                                            int status = (int) map.get("status");
                                            if (status == 11) {
                                                Toast.makeText(getContext(), "QQ换绑成功", Toast.LENGTH_SHORT).show();
                                            } else if (status == 12) {
                                                Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                                            } else if (status == 13) {
                                                Toast.makeText(getContext(), "json格式出差", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "异常", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        dialogInterface.dismiss();
                                    }

                                });
                    }

                } else {
                    /**
                     * 修改密码
                     */
                    String newPassword = password_edit.getText().toString();
                    String password_confirm = password_confirm_edit.getText().toString();

                    if (TextUtils.isEmpty(newPassword)) {
                        Toast.makeText(getContext(), "请输入修改的密码", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password_confirm)) {
                        Toast.makeText(getContext(), "请输入确认修改的密码", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(oldPassword_check)) {
                        Toast.makeText(getContext(), "请输入修改前的密码", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!oldPassword_check.equals(oldPassword)) {
                            Toast.makeText(getContext(), "密码校验错误", Toast.LENGTH_SHORT).show();
                            password_check.setText("");
                        } else if (!newPassword.equals(password_confirm)) {
                            Toast.makeText(getContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                        } else {

                            /**
                             * 向后台发送更改密码的请求
                             */

                            AlertDialogUtils.showDialog(getContext(), "修改密码", "确认修改密码号吗？", "取消",
                                    "确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.dismiss();
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            account.setQQ(person.getString("QQ", ""));

                                            String json = FastJsonUtils.objectToJsonString(account);

                                            String data;
                                            try {
                                                data = "account=" + json
                                                        + "&newPassword=" + newPassword
                                                        + "&status=" + 1;

                                                NetUtil netUtil = new NetUtil(data, NetEnum.ChangeAccountServletUrl, "POST");
                                                netUtil.start();
                                                netUtil.join();//线程同步
                                                response = netUtil.getResponse();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response,
                                                    new TypeReference<Map<String, Object>>() {
                                                    });

                                            int status = (int) map.get("status");
                                            if (status == 11) {
                                                editor.clear();
                                                editor.commit();
                                                Toast.makeText(getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getActivity(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            } else if (status == 12) {
                                                Toast.makeText(getContext(), "密码修改失败", Toast.LENGTH_SHORT).show();
                                            } else if (status == 13) {
                                                Toast.makeText(getContext(), "json格式出差", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "异常", Toast.LENGTH_SHORT).show();
                                            }


                                            dialogInterface.dismiss();
                                        }
                                    });
                        }
                    }
                }
            }
        });

        /**
         * 修改的确认layout
         *
         */
        Confirm = getView().findViewById(R.id.Confirm);
        Confirm.setVisibility(View.GONE);


        /**
         * github 按钮浏览器跳转
         */
        github = getView().findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                githubJump();
            }
        });

        /**
         * github_layout 按钮浏览器跳转
         */
        github_layout = getView().findViewById(R.id.github_layout);
        github_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                githubJump();
            }
        });


        /**
         * 注销账号的Button
         */
        logoutAccount = getView().findViewById(R.id.loginOut);
        logoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialogUtils.showDialog(getContext(), "确认注销", "确定注销个人账号吗？", "取消",
                        "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /**
                                 * 注销账号
                                 */
                                String data = "identification=" + person.getString("identification", "");

                                NetUtil netUtil = new NetUtil(data, NetEnum.LogoffServletUrl, "POST");
                                netUtil.start();
                                try {
                                    netUtil.join();//线程同步
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                response = netUtil.getResponse();

                                Map<String, Object> map = FastJsonUtils.strToJavaBeanMap(response, new TypeReference<Map<String, Object>>() {
                                });
                                int flag = (int) map.get("status");
                                if (4 == flag) {
                                    Toast.makeText(getContext(), "有书未归还，注销失败", Toast.LENGTH_SHORT).show();
                                } else if (3 == flag) {
                                    /**
                                     * 清除数据
                                     */
                                    editor.clear();
                                    editor.commit();

                                    Intent intent = new Intent(getContext(), Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        //会清除该进程空间的所有Activity
                                    startActivity(intent);
                                    Toast.makeText(getContext(), "注销账号成功", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getContext(), "异常", Toast.LENGTH_SHORT).show();

                                }
                                dialogInterface.dismiss();
                            }
                        });
            }
        });

    }

    private void changInformation() {
        if (!modifyLayoutFlag) {
            modifyLayout.setVisibility(View.VISIBLE);
            modifyInformation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand_less, 0);

        } else {
            modifyLayout.setVisibility(View.GONE);
            chang_QQ_edit.setVisibility(View.GONE);
            chang_password_edit.setVisibility(View.GONE);
            Confirm.setVisibility(View.GONE);
            check_password_edit.setVisibility(View.GONE);
            modifyInformation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
        }
        modifyLayoutFlag = !modifyLayoutFlag;
    }

    private void githubJump() {
        Uri uri = Uri.parse(NetEnum.GitHubUrl);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setInformation(){
        identification.setText("用户名：" + person.getString("identification", ""));
        studentName.setText("姓名：" + person.getString("readerName", ""));
        studentID.setText("学号：" + person.getInt("rdId", 0));
        readerType.setText("读者类别:" + person.getString("readerTypeName", ""));
        totalCanLendNum.setText("总可借书:" + person.getInt("totalCanLendNum", 0) + "本");
        CanLendDay.setText("可借天数:" + person.getInt("CanLendNumDay", 0) + "天");
    }

}
