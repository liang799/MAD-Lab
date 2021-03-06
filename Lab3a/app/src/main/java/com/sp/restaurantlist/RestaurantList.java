package com.sp.restaurantlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RestaurantList extends AppCompatActivity { // extends = son of
    private EditText restaurantName;
    private Button buttonSave;
    private EditText restaurantAddress;
    private EditText restaurantTel;
    private RadioGroup restaurantTypes;

    private List<Restaurant> model = new ArrayList<Restaurant>();
    private RestaurantAdapter adapter = null;
    private ListView list;
    private TabHost host;

    private boolean showMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate is used to start an activity
        super.onCreate(savedInstanceState);             //super is used to call the **parent class constructor**
        setContentView(R.layout.main);                  //setContentView is used to set the xml
        restaurantName = findViewById(R.id.restaurant_name);
        restaurantTel = findViewById(R.id.restaurant_tel);
        restaurantAddress = findViewById(R.id.restaurant_address);
        restaurantTypes = findViewById(R.id.restaurant_types);
        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(onSave);

        list = findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);

        host = findViewById(R.id.tabHost);
        host.setup();

        // Tab 1
        TabHost.TabSpec spec = host.newTabSpec("List");
        spec.setContent(R.id.restaurants_tab);
        spec.setIndicator("List");
        host.addTab(spec);

        // Tab 2
        spec = host.newTabSpec("Details");
        spec.setContent(R.id.details_tab);
        spec.setIndicator("Details");
        host.addTab(spec);

        host.setCurrentTab(1);      // it will set second tab as default selected tab
        list.setOnItemClickListener(onListClick);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onStart() {
        invalidateOptionsMenu();
        super.onStart();
    }

    @Override
    public void invalidateOptionsMenu() {
        if (host.getCurrentTab() == 0) {
            showMenu = false;
        } else if (host.getCurrentTab() == 1) {
            showMenu = true;
        }
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        if (showMenu == true)
            return true;
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       switch (item.getItemId()) {
           case (R.id.about):
               Toast.makeText (this,"Restaurant List - version 1.0", Toast.LENGTH_LONG).show();
               break;
       }
       return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // To read data from restaurantName EditText
            String nameStr = restaurantName.getText().toString();
            String telStr = restaurantTel.getText().toString();
            if (telStr.length() < 8) {
                Toast error = Toast.makeText(getApplicationContext(), "Invalid telephone number", Toast.LENGTH_LONG);
                error.show();
                return;
            }
            String addrStr = restaurantAddress.getText().toString();
            String restType = "";
            // To read selection of restarantTypes RadioGroup
            switch (restaurantTypes.getCheckedRadioButtonId()) {
                case R.id.chinese:
                    restType = "Chinese";
                    break;
                case R.id.western:
                    restType = "Western";
                    break;
                case R.id.indian:
                    restType = "Indian";
                    break;
                case R.id.indonesian:
                    restType = "Indonesian";
                    break;
                case R.id.korean:
                    restType = "Korean";
                    break;
                case R.id.japanese:
                    restType = "Japanese";
                    break;
                case R.id.thai:
                    restType = "Thai";
                    break;
            }
            // Create Restaurant Data model
            Restaurant restaurant = new Restaurant();
            // Update the Data model
            restaurant.setName(nameStr);
            restaurant.setAddress(addrStr);
            restaurant.setTelephone(telStr);
            restaurant.setRestaurantType(restType);
            // Pass the record to ArrayAdapter.
            // It will update the ListArray and the ListView
            adapter.add(restaurant);
        }
    };

    AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Restaurant r = model.get(position);

            restaurantName.setText(r.getName());
            restaurantAddress.setText(r.getAddress());
            restaurantTel.setText(r.getTelephone());

            switch (r.getRestaurantType()) {
                case "Chinese":
                    restaurantTypes.check(R.id.chinese);
                    break;
                case "Western":
                    restaurantTypes.check(R.id.western);
                    break;
                case "Indian":
                    restaurantTypes.check(R.id.indian);
                    break;
                case "Indonesian":
                    restaurantTypes.check(R.id.indonesian);
                    break;
                case "Korean":
                    restaurantTypes.check(R.id.korean);
                    break;
                case "Japanese":
                    restaurantTypes.check(R.id.japanese);
                    break;
                default:
                    restaurantTypes.check(R.id.thai);
                    break;
            }
            host.setCurrentTab(1);
        }
    };

    // string class api
    // manipulate with user input
    static class RestaurantHolder {
        private TextView restName = null;
        private TextView addr = null;
        private ImageView icon = null;
        RestaurantHolder(View row) {
            restName = row.findViewById(R.id.restName);
            addr = row.findViewById(R.id.restAddr);
            icon = row.findViewById(R.id.icon);
        }
        void populateFrom(Restaurant r) {
            restName.setText(r.getName());
            String address = r.getAddress().toString();
            String telefono = r.getTelephone().toString();
            String finalFlash = address + ", " + telefono;
            addr.setText(finalFlash);
            if (r.getRestaurantType().equals("Chinese")) {
                icon.setImageResource(R.drawable.ball_red);
            } else if (r.getRestaurantType().equals("Western")) {
                icon.setImageResource(R.drawable.ball_yellow);
            } else {
                icon.setImageResource(R.drawable.ball_green);
            }
        }
    }

    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        RestaurantAdapter() { super(RestaurantList.this,R.layout.row, model); } // super ==> parent constructor

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            RestaurantHolder holder;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, parent, false);
                holder = new RestaurantHolder(row);
                row.setTag(holder);
            } else {
                holder = (RestaurantHolder)row.getTag();
            }
            holder.populateFrom(model.get(position));
            return (row);
        }
    }

}