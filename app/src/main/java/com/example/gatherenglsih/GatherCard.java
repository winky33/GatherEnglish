package com.example.gatherenglsih;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gatherenglsih.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;

public class GatherCard extends AppCompatActivity {
    DBHelper db = new DBHelper(GatherCard.this);
    private AlertDialog dialog;

    //widgets
    TextView result, closeBtn;
    ImageView imageView,cardDiagramView, backBtn;
    ImageButton picture;

    //vars
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_card);

        cardDiagramView = findViewById(R.id.popup_obtained_card);
        imageView = findViewById(R.id.camera_preview);
        picture = findViewById(R.id.capture_btn);
        result = findViewById(R.id.result);
        backBtn = findViewById(R.id.gathercard_exitBtn);

        backBtn.setOnClickListener(view -> startActivity(new Intent(GatherCard.this, Homepage.class)));

        picture.setOnClickListener(view -> {
            //launch camera if have permission
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            } else{
                //Request camera permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = null;
            if (data != null) {
                image = (Bitmap) data.getExtras().get("data");
            }
            int dimension = 0;
            if (image != null) {
                dimension = Math.min(image.getWidth(), image.getHeight());
            }
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            String cardObj = classifyImage(image);
            displayCard(cardObj);
        }
    }

    public String classifyImage(Bitmap image){
        String object;
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 999;
            float maxConfidence = 0.8f;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            if(maxPos!=999){
                String[] classes = {"Sofa", "Lamp", "Table", "Chair", "Bed", "Clock", "Door", "Television"};
                object = classes[maxPos];
                result.setText(object);
                return object;
            }

            // Releases model resources if no longer used.
            model.close();

            return "invalid";
        } catch (IOException e) {
            return "error";
        }
    }

    public void displayCard (String imgClass){
        int diagram = db.getCardDiagram(imgClass);

        if (diagram != 0){
            String currentTime = Calendar.getInstance().getTime().toString();

            int cardID = db.getCardID(imgClass);
            boolean storeCard = db.storeObtainCard(cardID,currentTime);

            if (storeCard){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                final View cardPopupView = getLayoutInflater().inflate(R.layout.gather_card_popup, null);
                final MediaPlayer cardAudio = MediaPlayer.create(this, db.getCardAudio(cardID));

                cardDiagramView = cardPopupView.findViewById(R.id.popup_obtained_card);
                closeBtn = cardPopupView.findViewById(R.id.popup_close_btn);

                dialogBuilder.setView(cardPopupView);
                dialog = dialogBuilder.create();
                dialog.show();

                cardAudio.start();

                cardDiagramView.setImageResource(diagram);

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
                dialog.getWindow().setLayout(550, 1200);


                closeBtn.setOnClickListener(view -> dialog.dismiss());
            }else{
                Toast errorToast = Toast.makeText(GatherCard.this, "Flashcard Collected, Try On Other Object!", Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }else{
            Toast errorToast = Toast.makeText(GatherCard.this, "Invalid Object, Try Again!", Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
}