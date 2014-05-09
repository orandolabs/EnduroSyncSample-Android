package com.orandolabs.endurosync.sample;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.orandolabs.endurosync.EnduroException;
import com.orandolabs.endurosync.EnduroModel;
import com.orandolabs.endurosync.EnduroObjectStore;
import com.orandolabs.endurosync.EnduroSync;
import com.orandolabs.endurosync.EnduroSyncClient;
import com.orandolabs.endurosync.IEnduroAsync;
import com.orandolabs.identio.IdentioParameters;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    IdentioParameters parms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        try {
            setupEnduroSync();
        } catch (Exception e) {
        }
    }

    static String kNs = "YOUR NAMESPACE i.e. http://yourdomain.com/ns/";
    static String kApp = "YOUR APP NAME";
    static String kPassword = "YOUR PASSWORD";
    static String kPassphrase = "YOUR APP PASSPHRASE";
    static String kEmail = "YOUR EMAIL";
    static String kAccount = "YOUR ACCOUNT NUMBER";
    static String kStoreName = "YOUR OBJECT STORE NAME";

    UserObject user;
    EnduroObjectStore store;

    void setupEnduroSync() throws Exception {
        parms = new IdentioParameters();
        parms.app = kApp;
        parms.username = kEmail;
        parms.account = kAccount;
        parms.password = kPassword;
        parms.passphrase = kPassphrase;
        parms.subscription = "endurosync";

        EnduroSync sync = EnduroSync.create(getApplicationContext());
        EnduroSyncClient client = sync.createClient(parms);

        EnduroModel model = new EnduroModel(kNs);
        model.declareObject(UserObject.class);
        model.declareObject(LinkObject.class);

        client.openObjectStore(kStoreName, model, new IEnduroAsync<EnduroObjectStore>() {
            @Override
            public void success(EnduroObjectStore enduroObjectStore) {
                store = enduroObjectStore;
                store.sync(new IEnduroAsync<EnduroObjectStore>() {
                    @Override
                    public void success(EnduroObjectStore enduroObjectStore) {
                        getUserObject();
                    }

                    @Override
                    public void failed(EnduroException e) {
                        Log.e("sync", e.getMessage());
                    }
                });
            }

            @Override
            public void failed(EnduroException e) {
                Log.e("sync", e.getMessage());
            }
        });
    }

    void getUserObject() {
        store.getOrCreateNamedObjectRecursiveAsync(parms.username, UserObject.class, new IEnduroAsync<UserObject>() {
            @Override
            public void success(UserObject enduroObject) {
                user = enduroObject;
                try {
                    String email = user.getEmail();
                    if (email == null || email.length() ==0)
                        initializeUserObject();
                    else
                        showUserObject();
                } catch (Exception e) {
                    Log.e("sync", e.getMessage());
                }
            }

            @Override
            public void failed(EnduroException e) {
                Log.e("sync", e.getMessage());
            }
        });
    }

    void initializeUserObject() throws Exception
    {
        user.setAge(44);
        Calendar c = Calendar.getInstance();
        c.set(1970, 5, 5);
        user.setBirthday(c.getTime());
        user.setEmail(parms.username);
        addLink("Apple", "http://www.apple.com");
        addLink("Google", "http://www.google.com");
        store.sync(new IEnduroAsync<EnduroObjectStore>() {
            @Override
            public void success(EnduroObjectStore enduroObjectStore) {
                Log.i("sync", "sync successful");
            }
            @Override
            public void failed(EnduroException e) {
                Log.e("sync", "sync failed: " + e.getMessage());
            }
        });
    }

    public void addLink(String title, String url) throws Exception
    {
        LinkObject link = store.newObject(LinkObject.class);
        link.setUrl(url);
        link.setTitle(title);
        user.addLink(link);
    }

    void showUserObject()
    {
        try {
            int age = user.getAge();
            Log.e("sync", "age: " + age);
            String email = user.getEmail();
            Log.e("sync", "email: " + email);
            LinkObject apple = null;
            for (LinkObject link : user.getLinks())
            {
                String url = link.getUrl();
                String title = link.getTitle();
                if (title.equals("Google"))
                    Log.e("sync", "google: " + url);
                else if (title.equals("Apple"))
                    Log.e("sync", "apple: " + url);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
