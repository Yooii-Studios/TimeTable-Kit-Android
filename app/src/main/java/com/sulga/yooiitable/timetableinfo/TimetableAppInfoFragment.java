package com.sulga.yooiitable.timetableinfo;

import java.util.*;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.*;

import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;

import com.flurry.android.*;
import com.sulga.yooiitable.R;
import com.sulga.yooiitable.constants.*;
import com.sulga.yooiitable.timetableinfo.activity.*;

public class TimetableAppInfoFragment extends Fragment {
	View yt_store;
	View yt_info;
	View yt_recommendFriends;
	View yt_credit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(
				R.layout.view_timetable_option_appinfo, 
				container, false);

		yt_store = fragmentView.findViewById(R.id.view_timetable_option_appinfo_store);
		yt_store.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(TimetableAppInfoFragment.this.getSupportActivity(), StoreActivity.class);
				TimetableAppInfoFragment.this.getSupportActivity().startActivity(intent);

				Map<String, String> info = new HashMap<String, String>();
				info.put(FlurryConstants.STORE_CLICKTYPE_KEY, FlurryConstants.STORE_CLICKTYPE_INFOPAGE);
				FlurryAgent.logEvent(FlurryConstants.STORE_CLICKED, info);
			}		
		});
		yt_info = fragmentView.findViewById(R.id.view_timetable_option_appinfo_info);
        yt_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TimetableAppInfoFragment.this.getSupportActivity(), YTInfoActivity.class);
                TimetableAppInfoFragment.this.getSupportActivity().startActivity(intent);
            }
        });
//		yt_rate = fragmentView.findViewById(R.id.view_timetable_option_appinfo_rate);
//		yt_rate.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Uri uri = Uri.parse("market://details?id="
//						+ getSupportActivity().getPackageName());
//				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//				try {
//					startActivity(goToMarket);
//				} catch (ActivityNotFoundException e) {
//					startActivity(new Intent(Intent.ACTION_VIEW,
//							Uri.parse("http://play.google.com/store/apps/details?id="
//									+ getSupportActivity().getPackageName())));
//				}
//			}
//		});

        yt_recommendFriends = fragmentView.findViewById(R.id.view_timetable_option_appinfo_recommendfriend);
        yt_recommendFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String recommendToFriendsSubject = getString(R.string.recommend_to_friends_subject);
                String recommendToFriendsBody = getString(R.string.recommend_to_friends_body)
                        + "\n"
                        + "https://play.google.com/store/apps/details?id=com.sulga.yooiitable";
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                //				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(crashLogFile));

                i.putExtra(Intent.EXTRA_SUBJECT, recommendToFriendsSubject);
                i.putExtra(Intent.EXTRA_TEXT, recommendToFriendsBody);
                //				i.putExtra(Intent.EXTRA_TEXT, "Some crash report details");

                String sendToFriendsTitle = getString(R.string.menu_recommend_friends);
                startActivity(Intent.createChooser(i, sendToFriendsTitle));
            }
        });
		yt_credit = fragmentView.findViewById(R.id.view_timetable_option_appinfo_credit);
//		yt_facebook = fragmentView.findViewById(R.id.view_timetable_option_appinfo_linkfacebook);
		//yt_yooiiStudio = fragmentView.findViewById(R.id.view_timetable_option_appinfo_yooiistudio);
		yt_credit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimetableAppInfoFragment.this.getSupportActivity(), CreditsActivity.class);
				TimetableAppInfoFragment.this.getSupportActivity().startActivity(intent);
			}
		});

//		yt_facebook.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent browserIntent =
//						new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/YooiiMooii"));
//				TimetableAppInfoFragment.this.getSupportActivity().startActivity(browserIntent);
//			}
//		});
		/*yt_yooiiStudio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = 
						new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yooiistudios.com"));
				TimetableAppInfoFragment.this.getSupportActivity().startActivity(browserIntent);
			}
		});
		 */		// TODO Auto-generated constructor stub


		return fragmentView;
	}
}
