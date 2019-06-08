package com.advance.miui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import miui.app.AlertDialog;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import miui.preference.PreferenceActivity;
import miui.R;

/**
 *
 * */

public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Light_Settings);super.onCreate(savedInstanceState);
        addPreferencesFromResource(com.advance.miui.R.xml.miui);

        setsummary();status();sign();checkApk();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, android.preference.Preference preference) {

        all_mount();

        CheckBoxPreference clock = (CheckBoxPreference) findPreference("clock");
        CheckBoxPreference brevent = (CheckBoxPreference) findPreference("brevent");
        CheckBoxPreference charging = (CheckBoxPreference) findPreference("charging");
        CheckBoxPreference hide = (CheckBoxPreference) findPreference("hide");

        try{
        if (preference.getKey() != null && preference.getKey().startsWith("sh /")) {
            ShellUtils.execCommand(preference.getKey(), true);
        }

        if (preference.getKey().equals("baidu")){
            try {
                Uri uri = Uri.parse("https://pan.baidu.com/s/16T_2sdTHVXSDBYf8koxKew");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (preference.getKey().equals("weiyun")){
            try {
                Uri uri = Uri.parse("https://share.weiyun.com/5X2iTEo");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (preference.getKey().equals("github")){
            try {
                Uri uri = Uri.parse("https://github.com/xianwan1314/com.advance.miui");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (preference.getKey().equals("hide")){
            hide.setChecked(false);
            if ((new File("/tmp/hide_off").exists())) {
                String[] commands = new String[] { "mkdir /tmp", "chmod 777 /tmp","rm -rf /tmp/hide_off","echo 1 >/tmp/hide_on","settings put global policy_control immersive.status=*","sync" };
                ShellUtils.execCommand(commands, true);
                status();
            } else {
                if ((new File("/tmp/hide_on").exists())) {
                    String[] commands = new String[] { "mkdir /tmp", "chmod 777 /tmp","rm -rf /tmp/hide_on","echo 1 >/tmp/hide_off","settings put global policy_control null","sync" };
                    ShellUtils.execCommand(commands, true);
                    status();
                } else {
                    String[] commands = new String[] { "mkdir /tmp", "chmod 777 /tmp","echo 1 >/tmp/hide_on","settings put global policy_control immersive.status=*","sync" };
                    ShellUtils.execCommand(commands, true);
                    status();
                }
            }
        }

        if (preference.getKey().equals("charging")){
            charging.setChecked(false);
            if ((new File("/tmp/charging_on").exists())) {
                String[] commands = new String[]{"mkdir /tmp", "chmod 777 /tmp","rm -rf /tmp/charging_on","echo 1 >/tmp/charging_off", "dumpsys battery reset", "echo 1 > /sys/class/power_supply/battery/charging_enabled", "echo 0 > /sys/class/power_supply/battery/input_supend"};
                ShellUtils.execCommand(commands, true);
                status();
            } else {
                if ((new File("/tmp/charging_off").exists())) {
                    //
                    String[] commands = new String[]{"mkdir /tmp", "chmod 777 /tmp","rm -rf /tmp/charging_off","echo 1 >/tmp/charging_on", "dumpsys battery reset", "echo 0 > /sys/class/power_supply/battery/charging_enabled", "echo 1 > /sys/class/power_supply/battery/input_supend"};
                    ShellUtils.execCommand(commands, true);
                    status();
                } else {
                    String[] commands = new String[]{"mkdir /tmp", "chmod 777 /tmp","echo 1 >/tmp/charging_on","dumpsys battery reset", "echo 0 > /sys/class/power_supply/battery/charging_enabled", "echo 1 > /sys/class/power_supply/battery/input_supend"};
                    ShellUtils.execCommand(commands, true);
                    status();
                }
            }
        }

        if (preference.getKey().equals("author")){
            joinQQGroup("JDIH7JS1kUthKQMFY3vbOMVYFGUkS4dW");
        }

        if (preference.getKey().equals("clock")){
            clock.setChecked(false);
                if ((new File("/system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.center").exists())) {
                    String[] a = new String[] { "mv /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.offical","mv /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.center /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk","chmod -R 0644 /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk","/system/tools/busybox killall com.android.systemui" };
                    ShellUtils.execCommand(a, true);
                    clock.setChecked(false);
                    status();
                } else {
                    if ((new File("/system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.offical").exists())) {
                        String[] a = new String[] { "mv /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.center","mv /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.offical /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk","chmod -R 0644 /system/priv-app/MiuiSystemUI/MiuiSystemUI.apk","/system/tools/busybox killall com.android.systemui" };
                        ShellUtils.execCommand(a, true);
                        clock.setChecked(true);
                        status();
                    } else {
                        status();
                        sunday();
                    }
                }
        }

        if (preference.getKey().equals("brevent")){
            brevent.setChecked(false);
            if ((new File("/system/framework/services.jar.brevent").exists()) && (new File("/system/app/Brevent/Brevent.apk.bak").exists())) {
                String[] a = new String[] { "mv /system/framework/services.jar /system/framework/services.jar.offical","mv /system/framework/services.jar.brevent /system/framework/services.jar","mv /system/app/Brevent/Brevent.apk.bak /system/app/Brevent/Brevent.apk" };
                ShellUtils.execCommand(a, true);
                clock.setChecked(false);
                status();reboot();
            } else {
                if ((new File("/system/framework/services.jar.offical").exists()) && (new File("/system/app/Brevent/Brevent.apk").exists())) {
                    String[] a = new String[] { "mv /system/framework/services.jar /system/framework/services.jar.brevent","mv /system/framework/services.jar.offical /system/framework/services.jar","mv /system/app/Brevent/Brevent.apk /system/app/Brevent/Brevent.apk.bak" };
                    ShellUtils.execCommand(a, true);
                    clock.setChecked(true);
                    status();reboot();
                } else {
                    status();
                    sunday();
                }
            }
        }
        }catch(Exception e){
            // 异常
        }
        return false;
    }
    private void sunday() {
        AlertDialog.Builder dialog = new AlertDialog.
                Builder(MainActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage(
                "配置文件不存在，请联系小宛解决");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
    private void setsummary() {

        findPreference("hide").setTitle("去通知栏");
        ((CheckBoxPreference) findPreference("hide")).setSummaryOff("当前状态：关");
        ((CheckBoxPreference) findPreference("hide")).setSummaryOn("当前状态：开");

        findPreference("charging").setTitle("禁止充电");
        ((CheckBoxPreference) findPreference("charging")).setSummaryOff("当前状态：关");
        ((CheckBoxPreference) findPreference("charging")).setSummaryOn("当前状态：开");

        findPreference("clock").setTitle("时间居中");
        ((CheckBoxPreference) findPreference("clock")).setSummaryOff("当前状态：关");
        ((CheckBoxPreference) findPreference("clock")).setSummaryOn("当前状态：开");

        findPreference("brevent").setTitle("全局黑域");
        ((CheckBoxPreference) findPreference("brevent")).setSummaryOff("当前状态：关");
        ((CheckBoxPreference) findPreference("brevent")).setSummaryOn("当前状态：开");

        findPreference("author").setTitle("小宛制作");
        findPreference("author").setSummary("点击加入讨论群");

        findPreference("github").setTitle("开源代码");
        findPreference("github").setSummary("自由定制功能");

        findPreference("weiyun").setTitle("微云网盘");
        findPreference("weiyun").setSummary("获取历史刷机包");

        findPreference("baidu").setTitle("百度网盘");
        findPreference("baidu").setSummary("小宛的私人网盘");

    }
    private void all_mount() {
        String[] commands = new String[] { "mount -o rw,remount /","mount -o rw,remount /system","mount -o rw,remount /vendor","mount -o rw,remount /vendor/etc","mount -o rw,remount /system/vendor/etc","mount -o rw,remount /system/system","mount -o rw,remount /system/etc","mount -o rw,remount /system_root/system","echo sunday 137451824@qq.com >/system/test","echo sunday 137451824@qq.com >/test","rm -rf /system/res/res_center/layout/readme.txt","sync" };
        ShellUtils.execCommand(commands, true);
    }
    private void status() {

        CheckBoxPreference clock = (CheckBoxPreference) findPreference("clock");
        CheckBoxPreference brevent = (CheckBoxPreference) findPreference("brevent");
        CheckBoxPreference charging = (CheckBoxPreference) findPreference("charging");
        CheckBoxPreference hide = (CheckBoxPreference) findPreference("hide");

        if ((new File("/tmp/charging_on").exists())) {
            charging.setChecked(true);
        } else {
            if ((new File("/tmp/charging_off").exists())) {
                charging.setChecked(false);
            } else {
                charging.setChecked(false);
            }
        }

        if ((new File("/tmp/hide_on").exists())) {
            hide.setChecked(true);
        } else {
            if ((new File("/tmp/hide_off").exists())) {
                hide.setChecked(false);
            } else {
                hide.setChecked(false);
            }
        }

        if ((new File("/system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.offical").exists())) {
            clock.setChecked(true);
        } else {
            if ((new File("/system/priv-app/MiuiSystemUI/MiuiSystemUI.apk.center").exists())) {
                clock.setChecked(false);
            }
        }

        if ((new File("/system/framework/services.jar.offical").exists())) {
            brevent.setChecked(true);
        } else {
            if ((new File("/system/framework/services.jar.brevent").exists())) {
                brevent.setChecked(false);
            }
        }
    }
    private void reboot() {
        AlertDialog.Builder dialog = new AlertDialog.
                Builder(MainActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("重启手机后生效");
        dialog.setCancelable(false);
        dialog.setPositiveButton("重启", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] a = new String[] { "reboot" };
                ShellUtils.execCommand(a, true);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "重启手机后生效", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
    }
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void sign(){
        SignCheck signCheck = new SignCheck(this, "2F:09:1D:A0:D4:79:22:52:8F:0B:85:DA:63:B2:64:B1:AD:41:FB:A6");
        if (!signCheck.check()) {
            AlertDialog.Builder dialog = new AlertDialog.
                    Builder(MainActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("签名异常");
            dialog.setCancelable(false);
            dialog.setPositiveButton("退出", new DialogInterface.
                    OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            dialog.show();
        }
    }
    public class SignCheck {
        private Context context;
        private String cer;
        private String realCer;

        public SignCheck(Context context, String realCer) {
            this.context = context;
            this.realCer = realCer;
            cer = null;
            this.cer = getCertificateSHA1Fingerprint();
        }

        public String getCertificateSHA1Fingerprint() {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            int flags = PackageManager.GET_SIGNATURES;
            PackageInfo packageInfo = null;
            try {
                packageInfo = pm.getPackageInfo(packageName, flags);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Signature[] signatures = packageInfo.signatures;
            byte[] cert = signatures[0].toByteArray();
            InputStream input = new ByteArrayInputStream(cert);
            CertificateFactory cf = null;
            try {
                cf = CertificateFactory.getInstance("X509");
            } catch (Exception e) {
                e.printStackTrace();
            }
            X509Certificate c = null;

            try {
                c = (X509Certificate) cf.generateCertificate(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String hexString = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] publicKey = md.digest(c.getEncoded());
                hexString = byte2HexFormatted(publicKey);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            }
            return hexString;
        }
        private String byte2HexFormatted(byte[] arr) {

            StringBuilder str = new StringBuilder(arr.length * 2);

            for (int i = 0; i < arr.length; i++) {
                String h = Integer.toHexString(arr[i]);
                int l = h.length();
                if (l == 1)
                    h = "0" + h;
                if (l > 2)
                    h = h.substring(l - 2, l);
                str.append(h.toUpperCase());
                if (i < (arr.length - 1))
                    str.append(':');
            }
            return str.toString();
        }
        public boolean check() {

            if (this.realCer != null) {
                cer = cer.trim();
                realCer = realCer.trim();
                return this.cer.equals(this.realCer);
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
    }
    public void checkApk(){
        if (!checkApkExist(MainActivity.this, "com.advance.miui")) {
            AlertDialog.Builder dialog = new AlertDialog.
                    Builder(MainActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("包名异常");
            dialog.setCancelable(false);
            dialog.setPositiveButton("退出", new DialogInterface.
                    OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            dialog.show();
        }
    }
}