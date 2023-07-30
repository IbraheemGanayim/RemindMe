
package com.example.remindme;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class PrefFragment extends PreferenceFragmentCompat
{
    private SwitchPreference switchp;
    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater,container,savedInstanceState);
        if(view!=null){
            Boolean b = sharedPreference.loadCheck();
            if(b){
                view.setBackgroundColor(Color.DKGRAY);
            }else{
                view.setBackgroundColor(Color.WHITE);
            }

        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perfscreen);
        Boolean b = sharedPreference.loadCheck();

        switchp = (SwitchPreference) findPreference("switch_preference_1");
        switchp.setChecked(b);
        switchp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    boolean ischecked = (boolean) newValue;
                    if(ischecked){
                        sharedPreference.saveCheck(true);
                        view.setBackgroundColor(Color.DKGRAY);
                    }else{
                        sharedPreference.saveCheck(false);
                        view.setBackgroundColor(Color.WHITE);
                    }

                }
                return true;
            }
        });

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
//on back button
    @Override
    public void onDetach() {
        super.onDetach();
        try{
            Boolean b = sharedPreference.loadCheck();
            ((MainActivity)getActivity()).change_theme(b);

        }catch (Exception e){

        }
    }
}