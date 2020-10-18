package com.hackgt7.portablecheckout.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hackgt7.portablecheckout.CaptureAct;
import com.hackgt7.portablecheckout.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    Button scanButton;
//    int scannerOut = -1;

    public static ArrayList<Integer> scans = new ArrayList<>();

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        this.scanButton = (Button) root.findViewById(R.id.scanBtn);
        if (scanButton == null) {
            Log.d("HOME", "scan btn is null");
        }
        scanButton.setOnClickListener(this::onClick);
        return root;

    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        barScan();
    }

    private void barScan() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Scanning Bar Code");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                scans.add(resultCode);
                Log.d("SCAN", "" + result);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again",
                        (DialogInterface dialog, int which) -> barScan())
                        .setNegativeButton("finish",
                                (DialogInterface dialog, int which) -> {});
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "no results", Toast.LENGTH_SHORT);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}