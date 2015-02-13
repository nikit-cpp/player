package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by nik on 11.02.15.
 */
public class PlaybackPagerActivity extends FragmentActivity{
    private ViewPager mViewPager;
    private ArrayList<Song> mSongs;
    public final String TAG = "NIKIT_PLAYER_TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CrimePagerActivity.onCreate()");
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mSongs = SongFabric.get(this).getSongs();
        Log.d(TAG, "mSongs="+mSongs);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Song song = mSongs.get(i);
                return PlaybackFragment.newInstance(song.getId());
            }

            @Override
            public int getCount() {
                return mSongs.size();
            }
        });

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        UUID songId = (UUID) getIntent().getSerializableExtra(PlaybackFragment.EXTRA_CRIME_ID);

        for(int i = 0; i < mSongs.size(); ++i){
            if(mSongs.get(i).getId().equals(songId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
