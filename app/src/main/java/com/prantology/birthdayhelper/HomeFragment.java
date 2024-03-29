package com.prantology.birthdayhelper;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ContactAdapter  contactAdapter;
    FloatingActionButton addBtn, addConBtn;
    BirthDatabase  birthDatabase;
    Context context;
    ArrayList<ContactModel> arrayList= new ArrayList<>();
    SearchView searchView;
    TextView noData;


    public interface drawerLayoutChange{
        void sendDrawerInfo(Boolean b);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        arrayList = birthDatabase.getAllContact();

        if(DefaultSettings.getSortByName(context)){
            Collections.sort(arrayList, new Comparator<ContactModel>() {
                @Override
                public int compare(ContactModel o1, ContactModel o2) {
                    int x = o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    return x;
                }
            });
        }
        else{
            Collections.sort(arrayList, new Comparator<ContactModel>() {
                @Override
                public int compare(ContactModel o1, ContactModel o2) {
                    int x ;
                    x = o1.getMonth() - o2.getMonth();
                    if(x==0)x = o1.getDay() - o2.getDay();
                    return x;
                }
            });
        }


        loadRecyclerContent(arrayList);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_area,new AddFragment()).commit();

                //Toast.makeText(context,"Add Fragment", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.birthday_rv);
        addBtn = v.findViewById(R.id.add_btn);
        noData = v.findViewById(R.id.no_data);

        birthDatabase = new BirthDatabase(context);

    }


    private void loadRecyclerContent(ArrayList<ContactModel> array) {
        if(array != null){
            contactAdapter = new ContactAdapter(context,array);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(contactAdapter);
            noData.setVisibility(View.GONE);
        }
        else{
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);

        MenuItem item = menu.findItem(R.id.search_bar);
        if(item != null){
            searchView = (SearchView) item.getActionView();
        }

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText,arrayList);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_bar:
               // Toast.makeText(context,"SearchBar: "+item.getItemId(),Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.clear:
                setAllClear();

                //Toast.makeText(context,"Cleared",Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.exit:
                setExit();

                //Toast.makeText(context,"Exit ",Toast.LENGTH_SHORT).show();
                return true;

            default:
                Toast.makeText(context,"Wrong Click!!!",Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    private void setExit() {
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Do you really want to exit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getActivity().finish();
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        b.create().show();
    }

    private void setAllClear() {
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Do you really want to delete all Birthday?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // delete all
                                birthDatabase.deleteAll();
                                loadRecyclerContent(null);
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        b.create().show();

    }

    private void filter(String str, ArrayList<ContactModel> allContact) {
        ArrayList<ContactModel> contacts = new ArrayList<>();
        for(ContactModel contactModel: allContact){
            if(contactModel.getName().toLowerCase().contains(str.toLowerCase())  || contactModel.getContact().toLowerCase().contains(str.toLowerCase())){
                contacts.add(contactModel);
            }
        }
        contactAdapter.filterList(contacts);
    }
}
