package com.nulldreams.x_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.boybeak.adapter.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    private List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.result);

        list = generateRandomList();
    }

    public void go (View view) {
        List<String> strings = new Order(list).selector(String.class).findAll();
        int size = 0;
        if (strings != null) {
            size = strings.size();
        }
        tv.append("\ncheck String size=" + size);
    }

    private List generateRandomList () {
        List list = new ArrayList();
        Random random = new Random();
        int size = 50 + random.nextInt(50);
        int[] data = new int[3];
        for (int i = 0; i < size; i++) {
            int a = (10 + random.nextInt(10)) % 3;
            data[a]++;
            switch (a) {
                case 0:
                    list.add(new Integer(i));
                    break;
                case 1:
                    list.add(new Float(i));
                    break;
                case 2:
                    list.add(i + "");
                    break;
            }

        }
        tv.setText("size=" + size + " Integer count=" + data[0] + " Float count=" + data[1] + " String count=" + data[2]);
        return list;
    }
}
