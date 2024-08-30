package com.example.cinematics.Fragments.food;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdapterViewPager2 extends FragmentStateAdapter {
    public AdapterViewPager2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = new Fragment();

        switch (position){
            case 0:
                fragment = new Snacks();
                break;
            case 1:
                fragment = new Drinks();
                break;
            case 2:
                fragment = new Desserts();
                break;

        }
        return fragment;
    }

    @Override
    public int getItemCount() {return 3;}

}