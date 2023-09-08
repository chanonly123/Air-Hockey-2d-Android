package com.chanonly123.airhockey2d;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SplashActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView lvMenu;
    ArrayList<MenuItem> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lvMenu = (ListView) findViewById(R.id.lv_menu);
        menuList.add(new MenuItem("Play  ", R.drawable.ic_play));
        menuList.add(new MenuItem("Share ", R.drawable.ic_share));
        menuList.add(new MenuItem("Github", R.drawable.ic_github));
        lvMenu.setAdapter(new MenuAdapter());
        lvMenu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: // play
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1: // share
                sharePlayStoreLink();
                break;
            case 2: // github
                goToGithub();
                break;
            default:
                break;
        }
    }

    private void goToGithub() {
        Uri uriUrl = Uri.parse("https://github.com/chanonly123/Air-Hockey-2d-Android");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    private void sharePlayStoreLink() {
        String shareBody = "Come lets play Air Hockey 2D. Download today from https://play.google.com/store/apps/details?id=" + getPackageName() + ". Source available at https://github.com/chanonly123/Air-Hockey-2d-Android";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Air Hockey 2d");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public MenuItem getItem(int position) {
            return menuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.menu_item, parent, false);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
            iv.setImageResource(getItem(position).imgResId);
            tvTitle.setText(getItem(position).title);
            return convertView;
        }
    }

    class MenuItem {
        int imgResId;
        String title;

        public MenuItem(String title, int imgResId) {
            this.imgResId = imgResId;
            this.title = title;
        }
    }
}
