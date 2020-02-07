package com.example.peng.nfcreadwrite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ViewFlipper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    TextView tvNFCContent;
    TextView message;

    ImageButton btnImageCat_Red;
    ImageButton btnImageCat_Green;
    ImageButton btnImageCat_Blue;

    ImageButton btnImageDog_Red;
    ImageButton btnImageDog_Green;
    ImageButton btnImageDog_Blue;

    ImageButton btnImageCow_Red;
    ImageButton btnImageCow_Green;
    ImageButton btnImageCow_Blue;

    ImageButton btnImageCat;
    ImageButton btnImageDog;
    ImageButton btnImageCow;
    ViewFlipper viewFlipper;

    String nfc_write_tag;
    String nfc_animal_tag;
    String nfc_color_tag;

    String write_log;
    String msg_log;
    String popup_msg;
    String finish_msg;

    int select_animal;
    int select_color;
    int to_do_write_nfc;

    AlertDialog nfc_dialog = null;
    AlertDialog wait_dialog = null;
    Dialog custom_dialog = null;
    Dialog finish_dialog = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        //message = (TextView) findViewById(R.id.edit_message);
        btnImageCat = (ImageButton) findViewById(R.id.button_cat);
        btnImageDog = (ImageButton) findViewById(R.id.button_dog);
        btnImageCow = (ImageButton) findViewById(R.id.button_cow);

        viewFlipper = (ViewFlipper) findViewById(R.id.nfcFlipper);

        btnImageCat_Red = (ImageButton) findViewById(R.id.button_cat_red);
        btnImageCat_Green = (ImageButton) findViewById(R.id.button_cat_green);
        btnImageCat_Blue = (ImageButton) findViewById(R.id.button_cat_blue);

        btnImageDog_Red = (ImageButton) findViewById(R.id.button_dog_red);
        btnImageDog_Green = (ImageButton) findViewById(R.id.button_dog_green);
        btnImageDog_Blue = (ImageButton) findViewById(R.id.button_dog_blue);

        btnImageCow_Red = (ImageButton) findViewById(R.id.button_cow_red);
        btnImageCow_Green = (ImageButton) findViewById(R.id.button_cow_green);
        btnImageCow_Blue = (ImageButton) findViewById(R.id.button_cow_blue);

        custom_dialog = new Dialog(this);
        finish_dialog = new Dialog(this);

        popup_msg = "Waitting for NFC to contact...";
        finish_msg = "Successfully!";

        btnImageCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(1);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.secondLayout)));
                select_animal = 0;
            }
        });

        btnImageDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(2);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.thirdLayout)));
                select_animal = 1;
            }
        });

        btnImageCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(3);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.fourthLayout)));
                select_animal = 2;
            }
        });

        btnImageCat_Red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 0;
                openCustomDialog(this, R.drawable.cat_red);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageCat_Green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 1;
                openCustomDialog(this, R.drawable.cat_green);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageCat_Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 2;
                openCustomDialog(this, R.drawable.cat_blue);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageDog_Red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 0;
                openCustomDialog(this, R.drawable.dog_red);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageDog_Green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 1;
                openCustomDialog(this, R.drawable.dog_green);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageDog_Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 2;
                openCustomDialog(this, R.drawable.dog_blue);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageCow_Red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 0;
                openCustomDialog(this, R.drawable.cow_red);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageCow_Green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 1;
                openCustomDialog(this, R.drawable.cow_green);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

        btnImageCow_Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // or you can switch selecting the layout that you want to display
                viewFlipper.setDisplayedChild(0);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
                select_color = 2;
                openCustomDialog(this, R.drawable.cow_blue);
                FromUIWriteNFC(select_animal,select_color);
            }
        });

/*
        btnWriteDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        //write(message.getText().toString(), myTag);
                        write("dog,red", myTag);
                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });
 */

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }
        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    // Show Write NTag Message Box On Screen
    private void openWaitNFCDialog()
    {
        wait_dialog = new AlertDialog.Builder(this)
                .setMessage(msg_log)
                .setNegativeButton("Exit",null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if(to_do_write_nfc == 1)
                        {
                            try {
                                if (myTag == null) {
                                    Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                                } else {
                                    //write(message.getText().toString(), myTag);
                                    write(nfc_write_tag, myTag);
                                    //Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                                    write_log = WRITE_SUCCESS;
                                    msg_log = nfc_write_tag + " written to the NFC tag successfully!";
                                    if(nfc_dialog == null)
                                        openOptionsDialog();
                                    else {
                                        nfc_dialog.setMessage(msg_log);
                                        nfc_dialog.show();
                                    }
                                }
                            } catch (IOException e) {
                                Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                                e.printStackTrace();
                            } catch (FormatException e) {
                                Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setPositiveButton("",null)
                .create();
        wait_dialog.show();

        TextView msgTxt = (TextView) wait_dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(64.0f);
        Button negativeButton = ((AlertDialog)wait_dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextSize(64.0f);
        negativeButton.setTextColor(Color.rgb(0,0, 0));
        negativeButton.setBackgroundColor(Color.rgb(128,128, 128));

    }

    // Show Message Box On Screen
    private void openOptionsDialog()
    {
        nfc_dialog = new AlertDialog.Builder(this)
                .setMessage(msg_log)
                .setPositiveButton("OK", null)
                .create();
        nfc_dialog.show();

        TextView msgTxt = (TextView) nfc_dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(64.0f);
        Button positiveButton = ((AlertDialog)nfc_dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextSize(64.0f);
        positiveButton.setTextColor(Color.rgb(0,0, 0));
        positiveButton.setBackgroundColor(Color.rgb(128,128, 128));

    }

    //Custom Pop Up Window
    @SuppressLint("ResourceType")
    private void openCustomDialog(final View.OnClickListener v, final int image_id)
    {
        TextView txtmessage;
        Button btnFollow;
        ImageView choiceAnimal;

        custom_dialog.setContentView(R.layout.custompopup);
        txtmessage =(TextView) custom_dialog.findViewById(R.id.msgtext);
        txtmessage.setTextSize(64.0f);
        txtmessage.setText(popup_msg);
        btnFollow = (Button) custom_dialog.findViewById(R.id.btncancel);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });
        btnFollow.setTextSize(48.0f);
        custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        choiceAnimal = (ImageView) custom_dialog.findViewById(R.id.iv_animal);
        if(choiceAnimal != null)
            choiceAnimal.setImageResource(image_id);

        custom_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(to_do_write_nfc == 1)
                {
                    try {
                        if (myTag == null) {
                            Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                        } else {
                            //write(message.getText().toString(), myTag);
                            if(write(nfc_write_tag, myTag)) {
                                //Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                                write_log = WRITE_SUCCESS;
                                msg_log = nfc_write_tag + " written to the NFC tag successfully!";
                                openFinishDialog(v, image_id);
                            } else {
                                openCustomDialog(v, image_id);
                                FromUIWriteNFC(select_animal,select_color);
                            }
                        }
                    } catch (IOException e) {
                        //Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                        //e.printStackTrace();
                        openCustomDialog(v, image_id);
                        FromUIWriteNFC(select_animal,select_color);
                    } catch (FormatException e) {
                        //Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                        //e.printStackTrace();
                        openCustomDialog(v, image_id);
                        FromUIWriteNFC(select_animal,select_color);
                    }
                }
            }
        });
        custom_dialog.show();
    }

    //Custom Pop Up Finish Window
    @SuppressLint("ResourceType")
    private void openFinishDialog(View.OnClickListener v, int image_id)
    {
        TextView txt_finish;
        Button btn_finish;
        ImageView finish_Animal;

        finish_dialog.setContentView(R.layout.customfinish);
        txt_finish =(TextView) finish_dialog.findViewById(R.id.finishtext);
        txt_finish.setTextSize(64.0f);
        txt_finish.setText(finish_msg);
        btn_finish = (Button) finish_dialog.findViewById(R.id.finish_cancel);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_dialog.dismiss();
            }
        });
        btn_finish.setTextSize(48.0f);
        finish_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        finish_Animal = (ImageView) finish_dialog.findViewById(R.id.finish_animal);
        if(finish_Animal != null)
            finish_Animal.setImageResource(image_id);

        finish_dialog.show();
    }


    // Write label to NFC tag
    private void FromUIWriteNFC(int nfc_animal, int nfc_color) {
        switch(nfc_animal)
        {
            case 0:
                nfc_animal_tag = "cat";
                break;
            case 1:
                nfc_animal_tag = "dog";
                break;
            case 2:
                nfc_animal_tag = "cow";
                break;
        }

        switch(nfc_color)
        {
            case 0:
                nfc_color_tag = "red";
                break;
            case 1:
                nfc_color_tag = "green";
                break;
            case 2:
                nfc_color_tag = "blue";
                break;
        }

        nfc_write_tag = nfc_animal_tag + "," + nfc_color_tag;
        /*
        try {
            if (myTag == null) {
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {
                //write(message.getText().toString(), myTag);
                write(nfc_write_tag, myTag);
                //Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                write_log = WRITE_SUCCESS;
                openOptionsDialog();
            }
        } catch (IOException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } catch (FormatException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }

         */
        /*
        if(nfc_dialog == null)
            openWaitNFCDialog();
        else
            nfc_dialog.show();

         */

        try {
            if (myTag != null) {
                if(write(nfc_write_tag, myTag) == false) {
                    myTag = null;
                }
            }
        } catch (IOException e) {
            myTag = null;
        } catch (FormatException e) {
            myTag = null;
        }

        to_do_write_nfc = 0;

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(myTag == null) {
                        sleep(500);
                    }
                    to_do_write_nfc = 1;
                    custom_dialog.dismiss();
                    //myTag = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

        /******************************************************************************
         **********************************Read From NFC Tag***************************
         ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        //tvNFCContent.setText("NFC Content: " + text);
        //Toast.makeText(context, text, Toast.LENGTH_LONG ).show();
    }


    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private boolean write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);

        if(ndef != null) {
            // Enable I/O
            ndef.connect();
            // Write the message
            ndef.writeNdefMessage(message);
            // Close the connection
            ndef.close();
        }
        else {
            return  false;
        }
        return  true;
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }



    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}
