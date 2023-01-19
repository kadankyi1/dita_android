package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fishpott.dita.R;
import com.fishpott.dita.Util.Config;

public class EbookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackImageview, mReloadImageview, mBookCoverImageView, mChooseBuyBookFullImageView, mChooseBuyBookSummaryImageView;
    private TextView mBookTitleTextView, mBookAuthorTextView, mBookPriceTextView, mBookLongDescriptionTextView, mChooseBuyBookFullTextView, mChooseBuyBookSummaryTextView;
    private RadioButton mMaleGenderRadioButton, mFemaleGenderRadioButton;
    private ConstraintLayout mPaymentItemHolderConstraintLayout;
    private Button mReadFullButton, mReadSummaryButton, mReadPaidFullBookButton, mReadPaidSummaryButton;
    private ProgressBar mLoadingProgressbar;
    int getting = 0;
    private String bookOrSummary = "";
    private Thread network_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_details);

        mBackImageview = findViewById(R.id.activity_ebookdetails_fp_back_imageview);
        mReloadImageview = findViewById(R.id.reloadbook_imageview);
        mLoadingProgressbar = findViewById(R.id.loading_progressbar);
        mBookCoverImageView = findViewById(R.id.list_item_book_image_imageview);
        mBookTitleTextView = findViewById(R.id.list_item_book_title_textview);
        mBookAuthorTextView = findViewById(R.id.list_item_book_author_textview);
        mBookLongDescriptionTextView = findViewById(R.id.list_item_book_short_description_textview);
        mBookPriceTextView = findViewById(R.id.list_item_book_price_textview);
        mReadPaidFullBookButton = findViewById(R.id.activity_ebookdetails_readpaidfullbook_button);
        mReadPaidSummaryButton = findViewById(R.id.activity_ebookdetails_readpaidsummarybook_button);
        mPaymentItemHolderConstraintLayout = findViewById(R.id.buy_items_holder_constraintlayout);
        mReadFullButton = findViewById(R.id.activity_ebookdetails_readfull_button);
        mReadFullButton = findViewById(R.id.activity_ebookdetails_readfull_button);
        mReadSummaryButton = findViewById(R.id.activity_ebookdetails_readsummary_button);
        mChooseBuyBookFullImageView = findViewById(R.id.fragment_signup_personalstage1_gender_male_imageview);
        mChooseBuyBookFullTextView = findViewById(R.id.fragment_signup_personalstage1_gender_male_textview);
        mMaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_male_radiobutton);
        mChooseBuyBookSummaryImageView = findViewById(R.id.fragment_signup_personalstage1_gender_female_imageview);
        mChooseBuyBookSummaryTextView = findViewById(R.id.fragment_signup_personalstage1_gender_female_textview);
        mFemaleGenderRadioButton = findViewById(R.id.fragment_signup_personalstage1_gender_female_radiobutton);

        if(!EbookDetailsActivity.this.isFinishing() && !Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim().equalsIgnoreCase("")
        ){
            Config.loadImageView(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_COVER_URL).trim(), mBookCoverImageView, null);
        }
        mBookTitleTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE).trim());
        mBookLongDescriptionTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_DESCRIPTION).trim());
        mBookAuthorTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_AUTHOR).trim());
        mBookPriceTextView.setText(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_PRICE).trim());


        mChooseBuyBookFullImageView.setOnClickListener(this);
        mChooseBuyBookFullTextView.setOnClickListener(this);
        mChooseBuyBookSummaryImageView.setOnClickListener(this);
        mChooseBuyBookSummaryTextView.setOnClickListener(this);
        mBackImageview.setOnClickListener(this);
        mReadPaidFullBookButton.setOnClickListener(this);
        mReadPaidSummaryButton.setOnClickListener(this);
        mReadFullButton.setOnClickListener(this);
        mReadSummaryButton.setOnClickListener(this);
        mMaleGenderRadioButton.setOnClickListener(this);
        mFemaleGenderRadioButton.setOnClickListener(this);

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL).trim().equalsIgnoreCase("")){
            mChooseBuyBookFullImageView.setVisibility(View.INVISIBLE);
            mChooseBuyBookFullTextView.setVisibility(View.INVISIBLE);
            mMaleGenderRadioButton.setVisibility(View.INVISIBLE);
        } else {
            mChooseBuyBookFullImageView.setVisibility(View.VISIBLE);
            mChooseBuyBookFullTextView.setVisibility(View.VISIBLE);
            mFemaleGenderRadioButton.setVisibility(View.VISIBLE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL).trim().equalsIgnoreCase("")){
            mChooseBuyBookSummaryImageView.setVisibility(View.INVISIBLE);
            mChooseBuyBookSummaryTextView.setVisibility(View.INVISIBLE);
            mFemaleGenderRadioButton.setVisibility(View.INVISIBLE);
        } else {
            mChooseBuyBookSummaryImageView.setVisibility(View.VISIBLE);
            mChooseBuyBookSummaryTextView.setVisibility(View.VISIBLE);
            mFemaleGenderRadioButton.setVisibility(View.VISIBLE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED).trim().equalsIgnoreCase("yes")){
            mReadPaidFullBookButton.setVisibility(View.VISIBLE);
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "EBOOKDETAILS_PURCHASED_PAGE");
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_URL));
        } else {
            mReadPaidFullBookButton.setVisibility(View.GONE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED).trim().equalsIgnoreCase("yes")){
            mReadPaidSummaryButton.setVisibility(View.VISIBLE);
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FROM, "EBOOKDETAILS_PURCHASED_PAGE");
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_TITLE));
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL, Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_URL));
        } else {
            mReadPaidSummaryButton.setVisibility(View.GONE);
        }

        if(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_SUMMARY_PURCHASED).trim().equalsIgnoreCase("yes") && Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_FULL_PURCHASED).trim().equalsIgnoreCase("yes")){
            mPaymentItemHolderConstraintLayout.setVisibility(View.GONE);
        } else {
            mPaymentItemHolderConstraintLayout.setVisibility(View.VISIBLE);
        }

    }


    public void setMaleClicked(){
        bookOrSummary = "book_full";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "book_full");
        mFemaleGenderRadioButton.setChecked(false);
    }

    public void setFemaleClicked(){
        bookOrSummary = "book_summary";
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_BOOK_OR_SUMMARY_TO_BE_PURCHASED, "book_summary");
        mMaleGenderRadioButton.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageview.getId()){
            onBackPressed();
        } else if(view.getId() == mReadPaidFullBookButton.getId()){
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK, "book_full");
            Intent intent = new Intent(getApplicationContext(), BookTextReaderActivity.class);
            startActivity(intent);
        } else if(view.getId() == mReadPaidSummaryButton.getId()){
            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_READING_FULLBOOK_OR_SUMMARYBOOK, "book_summary");
            Intent intent = new Intent(getApplicationContext(), BookTextReaderActivity.class);
            startActivity(intent);
        } else if(view.getId() == mReadFullButton.getId()){
            // USING PDF VIEWER
            if(!bookOrSummary.trim().equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), "bookOrSummary: " + bookOrSummary, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MobileMoneyPaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Choose if you are buying a full book or a summary", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == mReadSummaryButton.getId()){
            if(!bookOrSummary.trim().equalsIgnoreCase("")){
                Intent intent = new Intent(EbookDetailsActivity.this, MobileMoneyPaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Choose if you are buying a full book or a summary", Toast.LENGTH_LONG).show();
            }
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_radiobutton){
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_textview){
            mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_male_imageview){
            mMaleGenderRadioButton.performClick();
            setMaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_radiobutton){
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_textview){
            mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        } else if(view.getId() == R.id.fragment_signup_personalstage1_gender_female_imageview){
            mFemaleGenderRadioButton.performClick();
            setFemaleClicked();
        }
    }
}