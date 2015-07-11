package com.github.nikit.cpp.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 11.02.15.
 */
public class PlaybackActivity extends FragmentActivity implements SeekReceiver.Receiver{
    private ViewPager mViewPager;
    private List<Song> mSongs;

    private Fragment buttonsFragment = null;
    private int currentPosition;
    private int duration;
    private SeekReceiver resultReceiver = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.LOG_TAG, "CrimePagerActivity.onCreate()");
        super.onCreate(savedInstanceState);


        FragmentManager fragmentManager = getSupportFragmentManager();
        buttonsFragment = fragmentManager.findFragmentById(R.id.buttonsFragmentContainer);
        if (null == buttonsFragment) {
            buttonsFragment = new PlaybackButtonsFragment();

            fragmentManager.beginTransaction().add(R.id.buttonsFragmentContainer, buttonsFragment).commit();
        }
        Intent start = getIntent();
        buttonsFragment.getArguments().putSerializable(Tags.SONG_ID, start.getSerializableExtra(Tags.SONG_ID));

        setContentView(R.layout.activity_playback);
        mViewPager = (ViewPager) findViewById(R.id.pager00);



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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Переключаем ViewPager на текущую песню
        UUID songId = (UUID) getIntent().getSerializableExtra(Tags.SONG_ID);

        for(int i = 0; i < mSongs.size(); ++i){
            if(mSongs.get(i).getId().equals(songId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        resultReceiver.setReceiver(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resultReceiver = new SeekReceiver(new Handler());
        resultReceiver.setReceiver(this);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case Tags.SONG_CURRENT_INFO_CODE:
                setCurrentPosition(data.getInt(Tags.SONG_CURRENT_POSITION_KEY));
                setDuration(data.getInt(Tags.SONG_DURATION_KEY));
                break;
        }
        //Log.d(Tags.LOG_TAG, "received " + resultCode);
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public SeekReceiver getReceiver() {
        return resultReceiver;
    }

    public void updateSeek() {

        if (null != resultReceiver) {
            Intent intent = new Intent(this, PlayerService.class);

            intent.putExtra(Tags.PLAYER_SERVICE_ACTION, PlayerService.Action.GET_CURRENT_INFO);
            intent.putExtra(Tags.SEEK_RECEIVER, resultReceiver);
            startService(intent);
            //Log.d(Tags.LOG_TAG, "Seek update intent sent");
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
        return PlaybackViewPagerFragment.newInstance(song.getId());
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
