package com.example.trendingtrails.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.trendingtrails.Database;
import com.example.trendingtrails.R;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.trendingtrails.Models.Global.AccountInfo;
import static com.example.trendingtrails.Models.Global.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment {
    Connection conn;
    ArrayAdapter<String> dataAdapter;
    EditText nameText;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        nameText = view.findViewById(R.id.nameText);
        nameText.setText(UserProfile.displayName);
        TextView emailTxt = view.findViewById(R.id.emailText);
        emailTxt.setText(AccountInfo.personEmail);
        spinner = view.findViewById(R.id.experienceSpinner);
        List<String> options = new ArrayList<String>();
        options.add("");
        options.add("Beginner");
        options.add("Intermediate");
        options.add("Advanced");
        dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(UserProfile.rank);
        view.findViewById(R.id.submitProfileChangesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        return view;
    }

    private void updateProfile() {
        try
        {
            conn = Database.connect();        // Connect to database
            if (conn == null)
            {
                return;
            }
            else
            {
                String newName = nameText.getText().toString();
                int newExp = spinner.getSelectedItemPosition();
                if(UserProfile.displayName == newName && UserProfile.rank == newExp){
                    conn.close();
                    return;
                }
                String query = "Update [dbo].[Profile] SET name = '" + newName + "', experience = " + newExp + " where email= '" + AccountInfo.personEmail + "' ";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
                UserProfile.displayName = newName;
                UserProfile.rank = newExp;
            }
            conn.close();
        }
        catch (Exception ex)
        {
            return;
        }
    }
}