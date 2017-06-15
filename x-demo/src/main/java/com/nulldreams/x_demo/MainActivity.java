package com.nulldreams.x_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.boybeak.adapter.Operator;
import com.github.boybeak.adapter.Order;
import com.github.boybeak.adapter.Selector;
import com.github.boybeak.adapter.Where;

import java.util.ArrayList;
import java.util.List;

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
        int size = Selector.selector(User.class, list)
                .where("age", Operator.OPERATOR_GT, 0)
                .and("money", Operator.OPERATOR_GT, 10)
                .count();
        tv.append("\ncheck User size=" + size);
    }

    private List generateRandomList () {
        List list = new ArrayList();
        //Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.age = i;
            user.money = 5 + i;
            list.add(user);
        }
        //tv.setText("size=" + size + " Integer count=" + data[0] + " Float count=" + data[1] + " String count=" + data[2]);
        return list;
    }
}
