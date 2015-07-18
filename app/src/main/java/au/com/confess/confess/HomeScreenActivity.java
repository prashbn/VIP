package au.com.confess.confess;

import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;


public class HomeScreenActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "mytag";
    private EditText text;
    private String  confession;
    private CollectDetails cd =CollectDetails.getInstance();
    private JSONObject jsonObject;
    private String remail;
    private String userobjectID;

    private ValidateUserPresent vup;
    private CreateNewUser cnu;
    private HttpCreateNewLog hcnl;
    private GetAllConfession gac;

    //Various API for Get user, post confession , create New user

    private String uri_post = "https://cryptic-inlet-7699.herokuapp.com/users/postconfession";
    private String list_confession = "https://cryptic-inlet-7699.herokuapp.com/users/getconfession";
    private String uri_read = "https://cryptic-inlet-7699.herokuapp.com/users/verifyuser?emailid=";
    private String uri_createuser = "https://cryptic-inlet-7699.herokuapp.com/users/createnewuser";
    private String uri_createlog = "https://cryptic-inlet-7699.herokuapp.com/users/createlog";



    static ArrayList<Bitmap> photoImage = new ArrayList<Bitmap>();
    static ArrayList<String> dateArrayList = new ArrayList<String>();
    static ArrayList<String> timeArrayList = new ArrayList<String>();
    static ArrayList<String> confessionTxt = new ArrayList<String>();

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    Date time= new Date();

    static ListView listView;
    static RelativeLayout listViewLayout;

    static ArrayList<String> listItems = new ArrayList<String>();
    static ListViewCustomAdapter adapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        uri_read = uri_read+cd.getemailAddress();

        if(!verifyIfUserPresent())
            createNewUser();
        else
            updateuserlog();

        TextView userName=(TextView) findViewById(R.id.userName);

        ImageView post = (ImageView) findViewById(R.id.Post);

        text = (EditText) findViewById(R.id.txtConfession);
        /***Set user name in top of the screen**/
        userName.setText(cd.getUserName());
        /***Load image in the imageview */
        ImageView imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        listView = (ListView) findViewById(R.id.listView);
        listViewLayout = (RelativeLayout) findViewById(R.id.listViewLayout);
        new LoadProfileImage(imgProfilePic).execute(cd.getPhotoURL());
        post.setOnClickListener(this);

        adapter = new ListViewCustomAdapter(this);
       listView.setAdapter(adapter);
        getAllConfession();
    }

    private void getAllConfession() {
        try {


            gac = new GetAllConfession(list_confession);
            gac.join();
           // jsonObject = new JSONObject(gac.getResult().replace("[", "").replace("]", ""));
            JSONArray jsonarray = new JSONArray(gac.getResult());
            dateArrayList.clear();
            confessionTxt.clear();
            timeArrayList.clear();
            for(int i=0; i<jsonarray.length(); i++){
                jsonObject = jsonarray.getJSONObject(i);
                addItems(jsonObject.getString("Confession"), jsonObject.getString("Date"), jsonObject.getString("Time"));
             //   System.out.println(name);
               // System.out.println(url);
            }
            adapter.notifyDataSetChanged();
           // addItems(jsonObject.getString("Confession"), jsonObject.getString("Date"), jsonObject.getString("Time"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public void addItems(String confession, String date, String time) {

        confessionTxt.add(confession);

        dateArrayList.add(date);

        timeArrayList.add(time);


    }



    private void getOthersConfession() {

    }

    private Boolean updateuserlog()
    {

        JSONObject json = new JSONObject();
        try
        {
            json.put("email", cd.getemailAddress());
            json.put("Name", cd.getUserName() );
            json.put("Date", dateFormat.format(date));
            json.put("Time", timeFormat.format(time));
        }

        catch(Exception e)
        {
            Log.i(TAG, e.toString());
        }
        hcnl = new HttpCreateNewLog(uri_createlog, json);
        try {
            hcnl.join();
            if(hcnl.getResult().equals(""))
            {
                Toast.makeText(this, "User Log Updated ", Toast.LENGTH_LONG).show();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    private Boolean verifyIfUserPresent() {

        try
        {
            vup = new ValidateUserPresent(uri_read);
            vup.join();
            Log.i(TAG, "Email Address is " + vup.getResult().toString());
            jsonObject = new JSONObject(vup.getResult().replace("[","").replace("]",""));
            remail = jsonObject.getString("email");
            Log.i(TAG, "Email Address is "+remail);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(cd.getemailAddress().equals(remail)) {
            Log.i(TAG, "Match Found "+remail);

            return true;
        }
            else
            return false;

    }


    private void createNewUser() {

        JSONObject json = new JSONObject();
        try
        {
            json.put("email", cd.getemailAddress());
            json.put("Name", cd.getUserName() );
            json.put("Date", dateFormat.format(date));
            json.put("Time", timeFormat.format(time));
        }

        catch(Exception e)
        {
            Log.i(TAG, e.toString());
        }
        cnu = new CreateNewUser(uri_createuser, json);
        try {
            cnu.join();
            if(cnu.getResult()!=null)
            {
                Toast.makeText(this, "New User Added", Toast.LENGTH_LONG).show();

            }

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Post:
                // Signin button clicked

                if(text.getText().length() > 0) {
                    confession = text.getText().toString();
                    postConfession();
                    getAllConfession();
                }
                else
                    Log.i(TAG, "Length is Zero");
                break;

            default:
                // Revoke access button clicked

                break;
        }
    }

    private Boolean postConfession() {
        JSONObject json = new JSONObject();


        if(!confession.isEmpty())
        {
            try
            {
                json.put("Username", cd.getUserName());
                json.put("Confession",confession );
                json.put("Date", dateFormat.format(date));
                json.put("Time", timeFormat.format(time));
            }

            catch(Exception e)
            {
                Log.i(TAG, e.toString());
            }
           PostConfession PC = new PostConfession(uri_post, json);
            try {
                PC.join();
                Log.i(TAG, "Retruned value after confession is "+PC.getResult());
                if(PC.getResult().equals("{\"msg\":\"\"}"))
                {
                    Toast.makeText(this, "Story Posted", Toast.LENGTH_LONG).show();
                    text.setText("");
                    return true;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Your Story is Empty", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private static class ListViewCustomAdapter extends BaseAdapter implements
            View.OnClickListener {


        private LayoutInflater mInflater;
        Context ctx;

        public ListViewCustomAdapter(Activity context) {
            mInflater = LayoutInflater.from(context);
            this.ctx = context;
        }

        public int getCount() {
            return confessionTxt.size();

    }

        public Object getItem(int position) {

            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
              //  LayoutInflater inflator = context.getLayoutInflater();
                view = mInflater.inflate(R.layout.listview_singlerow, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.dateTextView = (TextView) view
                        .findViewById(R.id.dateTextView);

                viewHolder.timeTextView = (TextView) view
                        .findViewById(R.id.timeTextView);

                viewHolder.imageView = (ImageView) view
                        .findViewById(R.id.imageListView);

                viewHolder.confessTextView = (TextView) view
                        .findViewById(R.id.confess_txt);


                view.setTag(viewHolder);

            }
            else{

                view = convertView;
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.dateTextView.setText(dateArrayList.get(position));
            holder.timeTextView.setText(timeArrayList.get(position));
            holder.confessTextView.setText(confessionTxt.get(position));
            return view;
        }

        public void onClick(View v) {

            switch (v.getId()) {

            }
        }
        class ViewHolder {
            TextView dateTextView, timeTextView,confessTextView;
            ImageView imageView, cancelImageView;
            ImageView addTagImageView;

        }

    }

}


