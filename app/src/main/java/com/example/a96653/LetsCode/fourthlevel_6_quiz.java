package com.example.a96653.LetsCode;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class fourthlevel_6_quiz extends AppCompatActivity
        implements View.OnDragListener, View.OnLongClickListener {
    public static MySQLliteHelper sqLiteHelper;
    public static MySQLliteHelper m;
    private static final String TAG = fourthlevel_6_quiz.class.getSimpleName();

    private ImageButton btn_true1;
    private ImageButton btn_true2;
    private ImageButton btn_false;
    private ImageButton btn_true3;

    private static final String BUTTON_VIEW_TAG1 = "True";
    private static final String BUTTON_VIEW_TAG2 = "True";
    private static final String BUTTON_VIEW_TAG3 = "False";
    private static final String BUTTON_VIEW_TAG4 = "True";

    int counter = 0;
    int result = 0;

    private static final String TEXT_VIEW_TAG = "DRAG TEXT";

    View mview;

    ImageView iv_next;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourthlevel_6);
        m = new MySQLliteHelper(this);
        sqLiteHelper = new MySQLliteHelper(this);
        //TO VIEW SCORE ON BOX
        TextView textView = (TextView) findViewById(R.id.ScoreBox_fourthlevel_5);
        textView.setText(m.getChildScore() + "");


        //HOME BUTTON
        ImageButton homebtn3 = (ImageButton) findViewById(R.id.homebtn_fourthlevel5);
        homebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomePage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(HomePage);
            }
        });

        btn_true1 = (ImageButton) findViewById(R.id.button101);
        btn_true1.setTag(BUTTON_VIEW_TAG1);
        btn_true2 = (ImageButton) findViewById(R.id.button88);
        btn_true2.setTag(BUTTON_VIEW_TAG2);
        btn_false = (ImageButton) findViewById(R.id.button99);
        btn_false.setTag(BUTTON_VIEW_TAG3);
        btn_true3=(ImageButton)findViewById(R.id.button111);
        btn_true3.setTag(BUTTON_VIEW_TAG4);


        iv_next = (ImageView) findViewById(R.id.fourthlevel_5_next);
        iv_next.setVisibility(View.INVISIBLE);

        implementEvents();

        myDialog = new Dialog(this);

        ImageView next3 = (ImageView) findViewById(R.id.fourthlevel_5_next);
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata();
                GoNext();
            }
        });
    }


    //Implement long click and drag listener
    private void implementEvents() {
        //add or remove any view that you don't want to be dragged
        btn_true1.setOnLongClickListener(this);
        btn_true2.setOnLongClickListener(this);
        btn_false.setOnLongClickListener(this);
        btn_true3.setOnLongClickListener(this);

        //add or remove any layout view that you don't want to accept dragged view
        findViewById(R.id.false_answer).setOnDragListener(this);
        findViewById(R.id.true_answer).setOnDragListener(this);
        findViewById(R.id.true_answer1).setOnDragListener(this);
        findViewById(R.id.true_answer2).setOnDragListener(this);

    }


    @Override
    public boolean onLongClick(View view) {

        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);


        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        view.setVisibility(View.INVISIBLE);
        mview = view;

        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {

        int linear_id = view.getId();
        int action = event.getAction();

        switch (linear_id) {
            case R.id.false_answer:

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            return true;

                        }

                        return false;


                    case DragEvent.ACTION_DRAG_EXITED:

                        view.getBackground().clearColorFilter();

                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        String mimType = event.getClipDescription().toString();
                        // String mimType = item.getText().toString();
                        Log.d(TAG, mimType);

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = item.getText().toString();

                        // Displays a message containing the dragged data.
                        Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Turns off any color tints
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        View v = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) v.getParent();
                        owner.removeView(v);//remove the dragged view
                        LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                        container.addView(v);//Add the dragged view
                        v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                        if (mimType.contains(BUTTON_VIEW_TAG2)) {
                            result++;
                            if (result >= 4)
                                Toast.makeText(fourthlevel_6_quiz.this, "all answers right", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Result: " + result);
                        }

                        counter++;
                        if (counter == 4) {
                            iv_next.setVisibility(View.VISIBLE);
                            Toast.makeText(fourthlevel_6_quiz.this, "neeext", Toast.LENGTH_SHORT).show();
                            // set next view visible
                        }


                        return true;


                    // Returns true. DragEvent.getResult() will return true.

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Turns off any color tinting
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        // Does a getResult(), and displays what happened.
                        if (event.getResult())
                            Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                            mview.setVisibility(View.VISIBLE);
                        }
                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.

                }

                break;


            case R.id.true_answer1:
                // Handles each of the expected events

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            return true;

                        }

                        return false;

                    case DragEvent.ACTION_DRAG_EXITED:

                        view.getBackground().clearColorFilter();

                        // Invalidate the view to force a redraw in the new tint
                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        String mimType = event.getClipDescription().toString();
                        // String mimType = item.getText().toString();
                        Log.d(TAG, mimType);

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = item.getText().toString();

                        // Displays a message containing the dragged data.
                        Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Turns off any color tints
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        View v = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) v.getParent();
                        owner.removeView(v);//remove the dragged view
                        LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                        container.addView(v);//Add the dragged view
                        v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                        if ( mimType.contains(BUTTON_VIEW_TAG1)) {
                            result ++;
                            if (result >= 4) Toast.makeText(fourthlevel_6_quiz.this, "all answers right", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Result: "+result);
                        }

                        counter ++;
                        if(counter == 4) {
                            iv_next.setVisibility(View.VISIBLE);
                            Toast.makeText(fourthlevel_6_quiz.this, "neeext", Toast.LENGTH_SHORT).show();
                            // set next view visible
                        }


                        return true;


                    // Returns true. DragEvent.getResult() will return true.

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Turns off any color tinting
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        // Does a getResult(), and displays what happened.
                        if (event.getResult())
                            Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                            mview.setVisibility(View.VISIBLE);
                        }
                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.

                }

                break;

            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;


            case R.id.true_answer:
                // Handles each of the expected events

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            return true;

                        }

                        return false;

                    case DragEvent.ACTION_DRAG_EXITED:

                        view.getBackground().clearColorFilter();

                        // Invalidate the view to force a redraw in the new tint
                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        String mimType = event.getClipDescription().toString();
                        // String mimType = item.getText().toString();
                        Log.d(TAG, mimType);

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = item.getText().toString();

                        // Displays a message containing the dragged data.
                        Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Turns off any color tints
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        View v = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) v.getParent();
                        owner.removeView(v);//remove the dragged view
                        LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                        container.addView(v);//Add the dragged view
                        v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                        if (mimType.contains(BUTTON_VIEW_TAG3) ) {
                            result ++;
                            if (result >= 4) Toast.makeText(fourthlevel_6_quiz.this, "all answers right", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Result: "+result);
                        }

                        counter ++;
                        if(counter == 4) {
                            iv_next.setVisibility(View.VISIBLE);
                            Toast.makeText(fourthlevel_6_quiz.this, "neeext", Toast.LENGTH_SHORT).show();
                            // set next view visible
                        }


                        return true;


                    // Returns true. DragEvent.getResult() will return true.

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Turns off any color tinting
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        // Does a getResult(), and displays what happened.
                        if (event.getResult())
                            Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                            mview.setVisibility(View.VISIBLE);
                        }
                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.

                }

                break;


            case R.id.true_answer2:
                // Handles each of the expected events

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            return true;

                        }

                        return false;


                    case DragEvent.ACTION_DRAG_EXITED:
                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        String mimType = event.getClipDescription().toString();
                        // String mimType = item.getText().toString();
                        Log.d(TAG, mimType);

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = item.getText().toString();

                        // Displays a message containing the dragged data.
                        Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                        // Turns off any color tints
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        View v = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) v.getParent();
                        owner.removeView(v);//remove the dragged view
                        LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                        container.addView(v);//Add the dragged view
                        v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                        if (mimType.contains(BUTTON_VIEW_TAG4)) {
                            result++;
                            if (result >= 4)
                                Toast.makeText(fourthlevel_6_quiz.this, "all answers right", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Result: " + result);
                        }

                        //*    v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                        counter++;
                        if (counter == 4) {
                            iv_next.setVisibility(View.VISIBLE);
                            Toast.makeText(fourthlevel_6_quiz.this, "neeext", Toast.LENGTH_SHORT).show();
                            // set next view visible
                        }

                        return true;

                    // Returns true. DragEvent.getResult() will return true.

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Turns off any color tinting
                        view.getBackground().clearColorFilter();

                        // Invalidates the view to force a redraw
                        view.invalidate();

                        // Does a getResult(), and displays what happened.
                        if (event.getResult())
                            Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                            mview.setVisibility(View.VISIBLE);
                        }
                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.

                }

                break;
        }

        return false;

    }





    public void updatedata() {

        // sqLiteHelper.UpdateQuestionAnswer(1,1);
        if (result == 4) {
            sqLiteHelper.UpdateQuestionAnswer(15,1);
        }
        else
            sqLiteHelper.UpdateQuestionAnswer(15,0);


    }
    public void ShowPopup(View v) {

        Button btnClose;
        myDialog.setContentView(R.layout.hint4_3);
        btnClose =(Button) myDialog.findViewById(R.id.okaybtn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void GoNext(){
       // Intent intent = new Intent(this,welcome_1.class);

       // startActivity(intent);

    }
}
