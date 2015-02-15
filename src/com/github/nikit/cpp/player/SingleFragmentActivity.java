package com.github.nikit.cpp.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by nik on 07.02.15.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment);

        /**
         * Может показаться странным, что FragmentManager идентифицирует CrimeFragment
         по идентификатору ресурса FrameLayout. Однако идентификация UI-фрагмента по
         идентификатору ресурса его контейнерного представления встроена в механизм
         работы FragmentManager.
         */
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(android.R.id.list);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(android.R.id.list, fragment)
                    .commit();
        }
    }

}
