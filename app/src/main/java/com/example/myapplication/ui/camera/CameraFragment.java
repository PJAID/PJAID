package com.example.myapplication.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.databinding.FragmentCameraBinding;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import java.util.List;


public class CameraFragment extends Fragment {
    private @NonNull FragmentCameraBinding binding;
    private BarcodeView barcodeView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        CameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);
        binding = FragmentCameraBinding.inflate(inflater, container,false);
        View root =binding.getRoot();
        barcodeView=binding.barcodeScanner;
        barcodeView.decodeContinuous(callback);
        binding.btnSubmitManual.setOnClickListener(v ->{
            String manualText=binding.editManualInput.getText().toString();
            if(!manualText.isEmpty()){
                binding.textCamera.setText("Ręczne zgłoszenie: "+ manualText);
            }
        });
        //final TextView textView= binding.textCamera;
        //cameraViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    private final BarcodeCallback callback=new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText()!=null){
                binding.textCamera.setText("Kod QR: "+result.getText());
            }
        }
        @Override
        public void possibleResultPoints(List resultPoints){}
    };
    @Override
    public void onResume(){
        super.onResume();
        barcodeView.resume();
    }
    @Override
    public void onPause(){
        super.onPause();
        barcodeView.pause();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding=null;
    }
}
