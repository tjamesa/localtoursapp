package domain.finalproject;

import android.app.Fragment;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by tjamesa on 12/5/2017.
 */

public class customFragment extends Fragment {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

}
