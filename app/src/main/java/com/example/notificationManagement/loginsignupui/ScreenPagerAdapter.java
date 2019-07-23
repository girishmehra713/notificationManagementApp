package com.example.notificationManagement.loginsignupui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ScreenPagerAdapter extends PagerAdapter {
    private Context context;



    private List<ScreenObject> screenObjectList;

    public ScreenPagerAdapter(Context context, List<ScreenObject> screenObjectList) {
        this.context = context;
        this.screenObjectList = screenObjectList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        View screenView = inflater.inflate(R.layout.pager_view,null);



        ImageView imageres =  screenView.findViewById(R.id.pager_image);


        TextView title =  screenView.findViewById(R.id.pager_title);

        TextView description =  screenView.findViewById(R.id.pager_description);

        ImageView backgroundimg = screenView.findViewById(R.id.background_image);

        imageres.setImageResource(screenObjectList.get(position).getImg());



        title.setText(screenObjectList.get(position).getTitle());

        description.setText(screenObjectList.get(position).getDescription());


        backgroundimg.setImageResource(screenObjectList.get(position).getBack());


        container.addView(screenView);

        return  screenView;
    }

    @Override
    public int getCount() {
        return screenObjectList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}