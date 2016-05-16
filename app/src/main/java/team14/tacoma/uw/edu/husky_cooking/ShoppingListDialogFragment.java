package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import team14.tacoma.uw.edu.husky_cooking.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User mUser;


    public ShoppingListDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListDialogFragment newInstance(String param1, String param2) {
        ShoppingListDialogFragment fragment = new ShoppingListDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState){
//        final List mSelectedItems = new ArrayList();//For tracking the select ingredients
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle(R.string.shopping_list_title)
//                  TODO retrieve list from DB
//                .setMultiChoiceItems(LIST TO PULL FROM, null,
//                        new DialogInterface.OnMultiChoiceClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which, boolean isChecked){
//                                if(isChecked){
//                                    mSelectedItems.add(which);
//                                }else if(mSelectedItems.contains(which)){
//                                    mSelectedItems.remove(Integer.valueOf(which));
//                                }
//                            }
//                        })
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                //TODO dictate functionality to remove item from user_shopping list table
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //Closes fragment
//                            }
//                        });
//        return builder.create();
//
//    }

}
