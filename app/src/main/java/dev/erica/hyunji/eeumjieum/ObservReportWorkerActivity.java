package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class ObservReportWorkerActivity extends FragmentActivity {

    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn, roombtn1, roombtn2, roombtn3;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown, roomup, roomdown;
    private int selected_menu = 0;

    private String savedID;
    private int savedMode;
    boolean room_opened = false;
    String selected_room = "기쁨방";

    private int selected_day = -1;   //1:sun, 2:mon, 3:tue, 4:wed, 5:thu, 6:fri, 7:sat
    private int selected_date = 0;

    ArrayList<ObservArticleItem> data = new ArrayList<>();
    ObservArticleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observ_report_worker);


        //user mode and ID setting
        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }

        notice_btn = (Button) findViewById(R.id.notice_btn);
        schedule_btn = (Button) findViewById(R.id.schedule_btn);
        program_btn = (Button) findViewById(R.id.program_btn);
        observ_report_btn = (Button) findViewById(R.id.observ_report_btn);
        work_report_btn = (Button) findViewById(R.id.work_report_btn);

        notice_lb = (TextView) findViewById(R.id.notice_label);
        schedule_lb = (TextView) findViewById(R.id.schedule_label);
        program_lb = (TextView) findViewById(R.id.program_label);
        observ_report_lb = (TextView) findViewById(R.id.observ_report_label);
        work_report_lb = (TextView) findViewById(R.id.work_report_label);

        roombtn1 = (Button) findViewById(R.id.tfdbtn1);
        roombtn2 = (Button) findViewById(R.id.tfdbtn2);
        roombtn3 = (Button) findViewById(R.id.tfdbtn3);

        roombtn1.setText("기쁨방");
        roombtn2.setText("믿음방");
        roombtn3.setText("은혜방");

        //bottom pop-menu
        menuup = AnimationUtils.loadAnimation(this, R.anim.note_up_animation);
        menudown = AnimationUtils.loadAnimation(this, R.anim.note_down_animation);
        roomdown = AnimationUtils.loadAnimation(this, R.anim.roomlist_down_animation);
        roomup = AnimationUtils.loadAnimation(this, R.anim.roomlist_up_animation);


        initCalenderDate();
        listinit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormalResId(R.color.colorRedOrange);
        fab.setColorPressedResId(R.color.colorBgDefault);

    }

    private void initCalenderDate(){

        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        //cal.set(Calendar.YEAR, 2016);
        //cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        //cal.set(Calendar.DATE, 30);


        int[] weekdate = {0,0,0,0,0,0,0,0};

        //System.out.println("오늘 날짜 : " + cal.getTime());

        for(int i=0; i<7 ; i++) {
            cal.setTime(date);
            cal.add(Calendar.DATE, (i+1) - cal.get(Calendar.DAY_OF_WEEK));
            //System.out.println(i + "번째 날짜 : " +    cal.getTime());
            weekdate[i] = cal.get(Calendar.DATE);
        }

        //cal.setTime(date);
        //cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
        //System.out.println("마지막 요일(토요일) 날짜 : " + cal.getTime());


        Button tmp_btn = (Button) findViewById(R.id.cal_year);
        tmp_btn.setText(Integer.toString(cal.get(Calendar.YEAR)));
        //System.out.println("올해 : " + cal.get(Calendar.YEAR));

        tmp_btn = (Button) findViewById(R.id.cal_month);
        if(cal.get(Calendar.MONTH)+1 < 10){
            tmp_btn.setText("0"+Integer.toString(cal.get(Calendar.MONTH)+1));
        }else {
            tmp_btn.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        }


        cal.setTime(date);
        selected_day = cal.get(Calendar.DAY_OF_WEEK);          //day of the week
        selected_date = cal.get(Calendar.DATE);

        tmp_btn = (Button) findViewById(R.id.cal_sun_3);
        tmp_btn.setText(Integer.toString(weekdate[0]));
        tmp_btn = (Button) findViewById(R.id.cal_mon_3);
        tmp_btn.setText(Integer.toString(weekdate[1]));
        tmp_btn = (Button) findViewById(R.id.cal_tue_3);
        tmp_btn.setText(Integer.toString(weekdate[2]));
        tmp_btn = (Button) findViewById(R.id.cal_wed_3);
        tmp_btn.setText(Integer.toString(weekdate[3]));
        tmp_btn = (Button) findViewById(R.id.cal_thu_3);
        tmp_btn.setText(Integer.toString(weekdate[4]));
        tmp_btn = (Button) findViewById(R.id.cal_fri_3);
        tmp_btn.setText(Integer.toString(weekdate[5]));
        tmp_btn = (Button) findViewById(R.id.cal_sat_3);
        tmp_btn.setText(Integer.toString(weekdate[6]));


        switch (selected_day) {
            case 1:
                tmp_btn = (Button) findViewById(R.id.cal_sun_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_sun_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_sun_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 2:
                tmp_btn = (Button) findViewById(R.id.cal_mon_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_mon_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_mon_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 3:
                tmp_btn = (Button) findViewById(R.id.cal_tue_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_tue_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_tue_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 4:
                tmp_btn = (Button) findViewById(R.id.cal_wed_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_wed_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_wed_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 5:
                tmp_btn = (Button) findViewById(R.id.cal_thu_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_thu_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_thu_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 6:
                tmp_btn = (Button) findViewById(R.id.cal_fri_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_fri_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_fri_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
            case 7:
                tmp_btn = (Button) findViewById(R.id.cal_sat_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_sat_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_sat_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                break;
        }
    }

    //fab button (wirte btn)
    public void onClick_fabbtn(View v){
        Intent intent = new Intent(this, WriteObservReportActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "write");
        intent.putExtra("selected_date", selected_date);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:     //observereport registration complete
                setResult(1);
                setArticleSelected(selected_date);
                break;
            default:
                setResult(-1);
                finish();
                break;
        }
    }


    //back button click listner
    public void onClick_backbtn(View v){
        setResult(1);
        finish();
    }

    //roomlist tfd click listner
    public void onClick_roomlist(View v){

        if(v.getId() == R.id.tfdbtn1 && room_opened == true){
            roombtn1.bringToFront();
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn1 && room_opened == false){
            roombtn2.setVisibility(View.VISIBLE);
            roombtn3.setVisibility(View.VISIBLE);
            setAnimation(View.VISIBLE, roomdown);
            room_opened = true;
            return;
        }else if(v.getId() == R.id.tfdbtn2){
            roombtn1.bringToFront();
            selected_room = (String)roombtn2.getText();
            roombtn2.setText(roombtn1.getText());
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }else if(v.getId() == R.id.tfdbtn3){
            roombtn1.bringToFront();
            selected_room = (String)roombtn3.getText();
            roombtn3.setText(roombtn1.getText());
            roombtn1.setText(selected_room);
            setAnimation(View.GONE, roomup);
            room_opened = false;
        }
        setArticleSelected(selected_date);
    }

    //bottom menu click listener
    public void onClick_messagebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1

                selected_menu = 0;
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);

                return;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                temp_btn = (Button) findViewById(R.id.notebtn);
                temp_btn.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        notice_btn.setVisibility(View.VISIBLE);
        schedule_btn.setVisibility(View.VISIBLE);
        notice_lb.setVisibility(View.VISIBLE);
        schedule_lb.setVisibility(View.VISIBLE);

        setAnimation(1, View.VISIBLE, menuup);          //messagebtn menu param = 1

        selected_menu = 1;
        temp_btn = (Button) findViewById(R.id.messagebtn);
        temp_btn.setBackgroundResource(R.drawable.message_click);

    }
    public void onClick_foodbtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:

                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 2;
        temp_btn = (Button) findViewById(R.id.foodbtn);
        temp_btn.setBackgroundResource(R.drawable.food_click);

    }
    public void onClick_homebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:

                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 3;
        temp_btn = (Button) findViewById(R.id.homebtn);
        temp_btn.setBackgroundResource(R.drawable.home_click);
    }
    public void onClick_notebtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn mode param = 2

                selected_menu = 0;
                temp_btn = (Button) findViewById(R.id.notebtn);
                temp_btn.setBackgroundResource(R.drawable.note);

                return;
            case 5:
                temp_btn = (Button) findViewById(R.id.albumbtn);
                temp_btn.setBackgroundResource(R.drawable.album);
                break;
        }

        selected_menu = 4;
        temp_btn = (Button) findViewById(R.id.notebtn);
        temp_btn.setBackgroundResource(R.drawable.note_click);

        if(savedMode == 1){ //parent
            program_btn.setVisibility(View.VISIBLE);
            observ_report_btn.setVisibility(View.VISIBLE);
            program_lb.setVisibility(View.VISIBLE);
            observ_report_lb.setVisibility(View.VISIBLE);
            setAnimation(2, View.VISIBLE, menuup);          //notebtn mode param = 2

        }else{  //worker
            program_btn.setVisibility(View.VISIBLE);
            observ_report_btn.setVisibility(View.VISIBLE);
            work_report_btn.setVisibility(View.VISIBLE);
            program_lb.setVisibility(View.VISIBLE);
            observ_report_lb.setVisibility(View.VISIBLE);
            work_report_lb.setVisibility(View.VISIBLE);
            setAnimation(2, View.VISIBLE, menuup);         //notebtn mode param = 2
        }

    }
    public void onClick_albumbtn(View v) {
        Button temp_btn;
        switch (selected_menu) {
            case 0:
                break;
            case 1:
                setAnimation(1, View.GONE, menudown);       //messagebtn menu param = 1
                temp_btn = (Button) findViewById(R.id.messagebtn);
                temp_btn.setBackgroundResource(R.drawable.message);
                break;
            case 2:
                temp_btn = (Button) findViewById(R.id.foodbtn);
                temp_btn.setBackgroundResource(R.drawable.food);
                break;
            case 3:
                temp_btn = (Button) findViewById(R.id.homebtn);
                temp_btn.setBackgroundResource(R.drawable.home);
                break;
            case 4:
                setAnimation(2, View.GONE, menudown);       //notebtn menu param = 2
                Button temp_bt = (Button) findViewById(R.id.notebtn);
                temp_bt.setBackgroundResource(R.drawable.note);
                break;
            case 5:
                break;
        }

        selected_menu = 5;
        temp_btn = (Button) findViewById(R.id.albumbtn);
        temp_btn.setBackgroundResource(R.drawable.album_click);


    }

    //bottom pop-menu click listener
    public void onClick_noticebtn(View v){
        Toast.makeText(getApplicationContext(), "open44", Toast.LENGTH_SHORT).show();

        Button tmp_btn = (Button) findViewById(R.id.notice_btn);
        tmp_btn.setSelected(true);
    }
    public void onClick_scheduletbtn(View v){
        Toast.makeText(getApplicationContext(), "open55", Toast.LENGTH_SHORT).show();


    }
    public void onClick_programbtn(View v){
        Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
    }
    public void onClick_observreportbtn(View v){
        Toast.makeText(getApplicationContext(), "open22", Toast.LENGTH_SHORT).show();
        Button tmp_btn = (Button) findViewById(R.id.observ_report_btn);
        tmp_btn.setSelected(true);
    }
    public void onClick_workreportbtn(View v){
        Toast.makeText(getApplicationContext(), "open33", Toast.LENGTH_SHORT).show();

    }

    //room list animation
    private void setAnimation(final int btnStatus, final Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(btnStatus == View.GONE) {
                    roombtn2.setVisibility(btnStatus);
                    roombtn3.setVisibility(btnStatus);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        roombtn2.startAnimation(animation);
        roombtn3.startAnimation(animation);
    }

    //bottom pop-menu animation
    private void setAnimation(final int menuNum, final int btnStatus, Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                /*if(btnStatus == View.VISIBLE) {
                    program_btn.setVisibility(btnStatus);
                    observ_report_btn.setVisibility(btnStatus);
                    work_report_btn.setVisibility(btnStatus);
                }*/
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(menuNum == 1 && btnStatus == View.GONE){
                    notice_btn.setVisibility(btnStatus);
                    schedule_btn.setVisibility(btnStatus);
                    notice_lb.setVisibility(btnStatus);
                    schedule_lb.setVisibility(btnStatus);
                }
                if(menuNum == 2 && btnStatus == View.GONE) {
                    program_btn.setVisibility(btnStatus);
                    observ_report_btn.setVisibility(btnStatus);
                    work_report_btn.setVisibility(btnStatus);
                    program_lb.setVisibility(btnStatus);
                    observ_report_lb.setVisibility(btnStatus);
                    work_report_lb.setVisibility(btnStatus);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(menuNum == 1){
            notice_btn.startAnimation(animation);
            schedule_btn.startAnimation(animation);
            notice_lb.startAnimation(animation);
            schedule_lb.startAnimation(animation);

        }else if(menuNum ==2){
            if (savedMode == 2) {    //worker need work report menu
                work_report_btn.startAnimation(animation);
                work_report_lb.startAnimation(animation);
            }
            program_btn.startAnimation(animation);
            observ_report_btn.startAnimation(animation);
            program_lb.startAnimation(animation);
            observ_report_lb.startAnimation(animation);
        }
    }

    private void setCalenderSelected(int prev_day, int cur_day){
        Button tmp_btn;
        switch (prev_day) {
            case 1:
                tmp_btn = (Button) findViewById(R.id.cal_sun_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_sun_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorAccent));
                tmp_btn = (Button) findViewById(R.id.cal_sun_3);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorAccent));
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 2:
                tmp_btn = (Button) findViewById(R.id.cal_mon_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_mon_2);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn = (Button) findViewById(R.id.cal_mon_3);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 3:
                tmp_btn = (Button) findViewById(R.id.cal_tue_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_tue_2);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn = (Button) findViewById(R.id.cal_tue_3);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 4:
                tmp_btn = (Button) findViewById(R.id.cal_wed_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_wed_2);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn = (Button) findViewById(R.id.cal_wed_3);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 5:
                tmp_btn = (Button) findViewById(R.id.cal_thu_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_thu_2);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn = (Button) findViewById(R.id.cal_thu_3);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 6:
                tmp_btn = (Button) findViewById(R.id.cal_fri_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_fri_2);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn = (Button) findViewById(R.id.cal_fri_3);
                tmp_btn.setTextColor(Color.BLACK);
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
            case 7:
                tmp_btn = (Button) findViewById(R.id.cal_sat_1);
                tmp_btn.setBackgroundResource(R.color.colorBackgroundGray);
                tmp_btn = (Button) findViewById(R.id.cal_sat_2);
                tmp_btn.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                tmp_btn = (Button) findViewById(R.id.cal_sat_3);
                tmp_btn.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                tmp_btn.setBackgroundResource(android.R.color.white);
                break;
        }

        switch (cur_day) {
            case 1:
                tmp_btn = (Button) findViewById(R.id.cal_sun_1);
                tmp_btn.setBackgroundResource(R.color.colorAccent);
                tmp_btn = (Button) findViewById(R.id.cal_sun_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorAccent));
                tmp_btn = (Button) findViewById(R.id.cal_sun_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 2:
                tmp_btn = (Button) findViewById(R.id.cal_mon_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_mon_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_mon_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 3:
                tmp_btn = (Button) findViewById(R.id.cal_tue_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_tue_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_tue_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 4:
                tmp_btn = (Button) findViewById(R.id.cal_wed_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_wed_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_wed_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 5:
                tmp_btn = (Button) findViewById(R.id.cal_thu_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_thu_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_thu_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 6:
                tmp_btn = (Button) findViewById(R.id.cal_fri_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_fri_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_fri_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
            case 7:
                tmp_btn = (Button) findViewById(R.id.cal_sat_1);
                tmp_btn.setBackgroundResource(R.color.colorRedOrange);
                tmp_btn = (Button) findViewById(R.id.cal_sat_2);
                tmp_btn.setTextColor(getResources().getColor(R.color.colorRedOrange));
                tmp_btn = (Button) findViewById(R.id.cal_sat_3);
                tmp_btn.setTextColor(Color.WHITE);
                tmp_btn.setBackgroundResource(R.drawable.shape_oval_date);
                selected_date = Integer.parseInt((String)tmp_btn.getText());
                break;
        }
        selected_day = cur_day;
    }

    private AdapterView.OnItemClickListener itemClickListenerOfArticleList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = data.get(position).getArticleid();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(getApplicationContext(), DetailObservReportViewActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("selected_day", selected_day);
            intent.putExtra("selected_date", selected_date);

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete
        }
    };

    public void listinit(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ObservArticleItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month + "/" + selected_date;

        articlelist = handler.getObservArticlebyRoomDay(selected_room, day);
        ListView listView = (ListView) findViewById(R.id.articlelistview);


        Iterator iterator = articlelist.iterator();
        int i = 0;
        while (iterator.hasNext()){
            data.add((ObservArticleItem) iterator.next());
        }

        adapter = new ObservArticleListAdapter(this, R.layout.article_item, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfArticleList);

    }


    private void setArticleSelected(int cur_date){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<ObservArticleItem> articlelist;

        data.clear();

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month + "/" + cur_date;

        articlelist = handler.getObservArticlebyRoomDay(selected_room, day);
        Iterator iterator = articlelist.iterator();


        while (iterator.hasNext()){
            data.add((ObservArticleItem) iterator.next());
        }

        adapter.notifyDataSetChanged();

    }

    public void onClick_calendar(View v){
        switch (v.getId()){
            case R.id.cal_sun_3:
                setCalenderSelected(selected_day, 1);
                break;
            case R.id.cal_mon_3:
                setCalenderSelected(selected_day, 2);
                break;
            case R.id.cal_tue_3:
                setCalenderSelected(selected_day, 3);
                break;
            case R.id.cal_wed_3:
                setCalenderSelected(selected_day, 4);
                break;
            case R.id.cal_thu_3:
                setCalenderSelected(selected_day, 5);
                break;
            case R.id.cal_fri_3:
                setCalenderSelected(selected_day, 6);
                break;
            case R.id.cal_sat_3:
                setCalenderSelected(selected_day, 7);
                break;
        }
        setArticleSelected(selected_date);
    }

    public void onClick_serach(View v){
        Intent intent = new Intent(this, WriteObservReportActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "search");
        intent.putExtra("selected_date", selected_date);
        startActivity(intent);
        overridePendingTransition(0,0);     //activity transition animation delete

    }
}
