package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 11.02.15.
 */
public class PlaybackPagerActivity extends FragmentActivity{
    private ViewPager mViewPager;
    private List<Song> mSongs;
    public static final String TAG = "NIKIT_PLAYER_TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CrimePagerActivity.onCreate()");
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mSongs = SongFabric.get(this).getCurrentPlayList().getSongs();

        FragmentManager fm = getSupportFragmentManager();
        final MyPagerAdapter pagerAdapter = new MyPagerAdapter(fm, mSongs);
        mViewPager.setAdapter(pagerAdapter);

        /**
         * Метод onPageChangeListener используется для обнаружения изменений в странице,
         которая в настоящий момент отображается экземпляром ViewPager. При изменении
         страницы заголовку CrimePagerActivity задается краткое описание Crime.
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                Song song = mSongs.get(pos);
                if(song.getName() != null){
                    setTitle(song.getArtist() + " - " + song.getName());
                }
                ((PlaybackFragment)pagerAdapter.getRegisteredFragment(pos)).stopUpdation();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Переключаем ViewPager на текущую песню
        UUID songId = (UUID) getIntent().getSerializableExtra(PlaybackFragment.EXTRA_CRIME_ID);

        for(int i = 0; i < mSongs.size(); ++i){
            if(mSongs.get(i).getId().equals(songId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

class MyPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    List<Song> songs;
    public MyPagerAdapter(FragmentManager fm, List<Song> songs) {
        super(fm);
        this.songs = songs;
    }

    @Override
    public Fragment getItem(int i) {
        Song song = songs.get(i);
        /*Fragment gettedFragment = registeredFragments.get(i);
        if(gettedFragment==null)
            gettedFragment = PlaybackFragment.newInstance(song.getId());
        return gettedFragment;*/
        return PlaybackFragment.newInstance(song.getId());
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
