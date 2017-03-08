package com.android.slangify.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.slangify.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.IntentUtils;

/**
 * Created by limormekaiten on 3/8/17.
 */

public class CreateMemeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_submit) Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_meme);

        ButterKnife.bind(this);

        setListeners();

    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                //todo - add metadata about the chosen vocabulary
                IntentUtils.startLoginActivity(CreateMemeActivity.this);
                break;
        }
    }
}
