package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment{

    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        updateUI();
//    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != getActivity().RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CRIME){
            if(data == null){
                return;
            }
            UUID crimeId = (UUID) data.getSerializableExtra(CrimePagerActivity.EXTRA_CRIME_ID);
            int position =  CrimeLab.get(getContext()).getPosition(crimeId);
            mAdapter.notifyItemChanged(position);
        }
    }

    private abstract class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutID){
            super(inflater.inflate(layoutID, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());

            DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);

            mDateTextView.setText(format.format(crime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved()? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v){
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }

        protected Crime getCrime(){
            return mCrime;
        }
    }

    private class NormalCrimeHolder extends CrimeHolder{
        public NormalCrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater, parent, R.layout.list_item_crime);
        }
    }

    private class SeriousCrimeHolder extends CrimeHolder{
        private Button mContactPoliceButton;

        public SeriousCrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater, parent, R.layout.list_item_serious_crime);

            mContactPoliceButton = (Button) itemView.findViewById(R.id.crime_police_button);
            mContactPoliceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getActivity(), "Calling the police for " + getCrime().getTitle(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, toast.getXOffset(), toast.getYOffset());
                    toast.show();
                }
            });
        }

        @Override
        public void bind(Crime crime){
            super.bind(crime);
            mContactPoliceButton.setEnabled(!crime.isSolved());
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            return mCrimes.get(position).isRequiresPolice()?1:0;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if(viewType == 0){
                return new NormalCrimeHolder(layoutInflater, parent);
            }else{
                return new SeriousCrimeHolder(layoutInflater, parent);
            }
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
