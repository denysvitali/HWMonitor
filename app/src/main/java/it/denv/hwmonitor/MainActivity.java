package it.denv.hwmonitor;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.CpuUsageInfo;
import android.os.HardwarePropertiesManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout ll = (LinearLayout) findViewById(R.id.top_ll);
        final DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if(!dpm.isDeviceOwnerApp(getPackageName()))
        {
            TextView status = new TextView(this);
            status.setText("App is not device owner, set it with `dpm set-device-owner it.denv.hwmonitor/.AdmRcvr` by adb shell and restart the app");
            ll.addView(status);
            return;
        }

        ListView lv = (ListView) findViewById(R.id.listView);

        HardwarePropertiesManager hwpm = (HardwarePropertiesManager) getSystemService(this.HARDWARE_PROPERTIES_SERVICE);
        CpuUsageInfo cpuUsage[] = hwpm.getCpuUsages();

        TextView tv_tmp = (TextView) new TextView(this);
        tv_tmp.setId(0);
        tv_tmp.setText(String.format("cpuUsage length: %d", cpuUsage.length));
        Log.i("mainActivity", String.format("length: %d", cpuUsage.length));
        ll.addView(tv_tmp);

        for (CpuUsageInfo coreUsage : cpuUsage)
        {
            tv_tmp = (TextView) new TextView(this);
            String cpu_tmp = String.format("%d / %d", coreUsage.getActive(), coreUsage.getTotal());
            Log.i("mainActivity",cpu_tmp);
            tv_tmp.setText(cpu_tmp);
            lv.addView(tv_tmp);
        }
        Button resetOwner = (Button) new Button(this);
        resetOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dpm.isDeviceOwnerApp(getPackageName()))
                {
                    dpm.clearDeviceOwnerApp(getPackageName());
                }
            }
        });
        resetOwner.setText("Reset owner");
        ll.addView(resetOwner);
    }
}
