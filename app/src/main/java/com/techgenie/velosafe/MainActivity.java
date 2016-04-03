package com.techgenie.velosafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/*Class:MainActivity
  purpose:In this activity describes when user click on heat map button it will shows the heat map
  of bike thefts in that area,when user click on login button user can login and when user click on
  register user can register with all details.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /* method:HeatMapButtonOnClick
    * purpose:when clicking on heat map button it goes heat map activity
    *
    * */

    public void HeatMapButtonOnClick(View view){
        Intent intent = new Intent(this,HeatMap.class);
        startActivity(intent);
    }
    /* method:loginButtonOnClick
   * purpose:when clicking on Login button it goes login activity
   *
   * */
    public void loginButtonOnClick(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    /* method:reportButtonOnClick
  * purpose:when clicking on report button it goes heat map activity
  *
  * */
    public void reportButtonOnClick(View view) {
        Intent intent = new Intent(this,ReportActivity.class);
        startActivity(intent);
    }
    /* method:registerButtonOnClick
  * purpose:when clicking on register button it goes  Registration activity
  *
  * */
    public void registerButtonOnClick(View view) {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }
}