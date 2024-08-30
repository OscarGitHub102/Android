package com.example.cinematics.Fragments.food;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cinematics.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Food extends Fragment {

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.food, container, false);

        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = v.findViewById(R.id.viewPager2);

        AdapterViewPager2 viewPagerAdapter2 = new AdapterViewPager2(getChildFragmentManager(), getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter2);

        final String [] apartados = new String[] {"APERITIVOS", "BEBIDAS", "POSTRES"};
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(apartados[position])).attach();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}