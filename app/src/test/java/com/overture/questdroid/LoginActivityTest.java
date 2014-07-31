package com.overture.questdroid;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.shadowOf;

@Config(manifest = "./src/main/AndroidManifest.xml",emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private ActivityController<LoginActivity> controller;
    private LoginActivity activity;

    @Before
    public void setUp(){
        ActivityController<LoginActivity> controller = buildActivity(LoginActivity.class).create();
        activity = controller.start().resume().get();
//        activity.onCreate(null);
//        activity.setContentView(R.layout.main);
    }

    @Test
    public void testActivity() throws Exception {
        assertTrue(activity != null);
    }

    @Test
    public void testApplicationName() throws Exception {
        String appName = new LoginActivity().getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Snaapiq"));
    }

    @Test
    public void testInstantiation() {
        TextView tv = new TextView(activity);
        tv.setText("e84");
        assertEquals("e84", tv.getText());
    }

    @Test
    public void testLoginListener() {
        Button btnLogin = (Button)activity.findViewById(R.id.login_button);
        assertTrue(btnLogin != null);
        btnLogin.performClick();
//
//        Intent expectedIntent = new Intent(activity, ContestsActivity.class);
//        Intent startedActivity = shadowOf(activity).getNextStartedActivity();
//        assertThat(startedActivity, equalTo(expectedIntent));
//        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}
