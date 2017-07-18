package com.foursquare.takehome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.foursquare.takehome.Constants.KEY_NO_VISITORS;
import static com.foursquare.takehome.Constants.LONG_INITIALIZER;
import static com.foursquare.takehome.Constants.NO_VISITOR_ID;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvRecyclerView;
    private PersonAdapter personAdapter;
    private LinearLayoutManager layoutManager;

    private static ArrayList<Person> personDetails = new ArrayList<>();
    private ArrayList<Person> noVisitorDetails = new ArrayList<>();

    Person noVisitorsStart = new Person();
    Person noVisitorBetween = new Person();
    Person noVisitorsEnd = new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRecyclerView = (RecyclerView) findViewById(R.id.rvRecyclerView);

        //TODO hook up your adapter and any additional logic here

        layoutManager = new LinearLayoutManager(MainActivity.this);

        parseVenueFromResponse();
    }


    /**
     * Parsing a fake json response from assets/people.json
     */
    private void parseVenueFromResponse() {
        new AsyncTask<Void, Void, Venue>() {
            @Override
            protected Venue doInBackground(Void... params) {
                try {
                    InputStream is = getAssets().open("people.json");
                    InputStreamReader inputStreamReader = new InputStreamReader(is);

                    final PeopleHereJsonResponse response = new Gson().fromJson(inputStreamReader, PeopleHereJsonResponse.class);

                    // Add the visitors list unsorted into a Arraylist of Person

                    personDetails.addAll(response.getVenue().getVisitors());

                    // Calling the adapter class with person Details

                    personAdapter = new PersonAdapter(MainActivity.this, personDetails);

                    // This is the method call with response from Gson sent for implementing the algorithm for displaying idle times and visitor times

                    addNoVisitorsIntervalsToArray(response);

                    return response.getVenue();
                } catch (Exception e) {}

                return null;
            }

            @Override
            protected void onPostExecute(Venue venue) {
                //TODO use the venue object to populate your recyclerview

                // Populating the recycler with sorted data and also displaying divider for each item in recycler view as per requirement screenshot

                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rvRecyclerView.setLayoutManager(layoutManager);
                rvRecyclerView.setAdapter(personAdapter);
                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST);
                rvRecyclerView.addItemDecoration(itemDecoration);
            }
        }.execute();
    }

    /**
     *
     * @param response
     * @return
     *
     * The algorithm followed here is similar to the Merge overlapping intervals problem.
     * This algorithm is implemented as a time complexity of O(nLogn)
     *
     *  Explanation :
     *
     *  Steps followed leading to O(nLogn) Algorithm
     *  1. Sorting the people Details Array by ArrivalTimes (This leads to O(nLogn)
     *  2. Conditional check with the minimum arrival time and the venue opening time.
     *     If difference found, add the difference from opening time of venue till arrival time of first customer with "No Visitors" name tag
     *  3. Make note of the arrival time and leave time of first visitor
     *  4. Now we only loop through the people Details once (1 to N) . Since we already have the likely first idle time from first visitor and before. which will be O(n).
     *  5. We check if the next visitor arrival Time is less than the first / previous visitor leave time,
     *     and assign the leave time as max of next visitor leave time and previous visitor leave time
     *  6. During this if there is no visitor present , we take the difference of the times between visitors and add the "No Visitors" name tag to noVisitors Detail Array
     *  7. In the end, we check for last visitor and the closing time by comparing the closing time of venue with leaving time of the last visitor if it greater.
     *  8. And then add the subsequent entire duration where no visitors present after last visitor will be marked as "No Visitors"
     *  9. Have considered two arrays for storing peopleDetails which have all the visitors details and noVisitorDetails for storing the idle time with No visitor tags
     *  10. Both arrays are merged and sorted again O(nLogn) time and displaying the results to recyclerView
     *
     */

    private ArrayList<Person> addNoVisitorsIntervalsToArray(PeopleHereJsonResponse response){

        // Condition checks for if arraylist is empty of has one item

        int count = personDetails.size();

        if(count == 0)
            return personDetails;

        if(count == 1)
            return personDetails;

        // Sorting based on the arrival time of each Visitor
        // One condition check added to prioritize the visitor with minimum of duration as higher over longer duration if both visitors started at same time

        Collections.sort(personDetails, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {

                Long arrivalTimeA = LONG_INITIALIZER;
                Long arrivalTimeB = LONG_INITIALIZER;
                Long visitorStayTimeA;
                Long visitorStayTimeB;

                try {

                    arrivalTimeA = o1.getArriveTime();
                    arrivalTimeB = o2.getArriveTime();

                    if(arrivalTimeA.equals(arrivalTimeB)) {
                        visitorStayTimeA = o1.getLeaveTime() - arrivalTimeA;
                        visitorStayTimeB = o2.getLeaveTime() - arrivalTimeB;

                        return visitorStayTimeA.compareTo(visitorStayTimeB);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exception: " +e.getMessage());
                }
                return arrivalTimeA.compareTo(arrivalTimeB);
            }
        });

        // First visitor time check with opening time of the venue

        Person firstVisitor = personDetails.get(0);
        long openingTime = response.getVenue().getOpenTime();
        if(openingTime < firstVisitor.getArriveTime()) {
            noVisitorsStart.setId(NO_VISITOR_ID);
            noVisitorsStart.setName(KEY_NO_VISITORS);
            noVisitorsStart.setArriveTime(openingTime);
            noVisitorsStart.setLeaveTime(firstVisitor.getArriveTime());
            noVisitorDetails.add(noVisitorsStart);
        }

        // Then we head into the rest of the array of people Details / visitor details for comparing the leave time and arrival time for idle intervals

        long arrivalTime = firstVisitor.getArriveTime();
        long leaveTime = firstVisitor.getLeaveTime();

        for(int index = 1; index < count; ++index) {
            Person nextVisitor = personDetails.get(index);

            if(nextVisitor.getArriveTime() <= leaveTime) {
                leaveTime = Math.max(nextVisitor.getLeaveTime(), leaveTime);
            } else {
                noVisitorBetween.setId(NO_VISITOR_ID);
                noVisitorBetween.setName(KEY_NO_VISITORS);
                noVisitorBetween.setArriveTime(leaveTime);
                noVisitorBetween.setLeaveTime(nextVisitor.getArriveTime());
                noVisitorDetails.add(noVisitorBetween);

                arrivalTime = nextVisitor.getArriveTime();
                leaveTime = nextVisitor.getLeaveTime();
            }
        }

        // Last visitor is compared with the closing time of venue and idle time is stored

        Person lastVisitor = personDetails.get(count-1);
        long closingTime = response.getVenue().getCloseTime();
        if(closingTime > lastVisitor.getLeaveTime()){
            noVisitorsEnd.setId(NO_VISITOR_ID);
            noVisitorsEnd.setName(KEY_NO_VISITORS);
            noVisitorsEnd.setArriveTime(lastVisitor.getLeaveTime());
            noVisitorsEnd.setLeaveTime(closingTime);
            noVisitorDetails.add(noVisitorsEnd);
        }

        // Merging idle time array into visitor details array

        personDetails.addAll(noVisitorDetails);

        // Sorting the visitor details array for view

        Collections.sort(personDetails, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {

                Long arrivalTimeA = LONG_INITIALIZER;
                Long arrivalTimeB = LONG_INITIALIZER;
                Long visitorStayTimeA;
                Long visitorStayTimeB;

                try {

                    arrivalTimeA = o1.getArriveTime();
                    arrivalTimeB = o2.getArriveTime();

                    if(arrivalTimeA.equals(arrivalTimeB)) {
                        visitorStayTimeA = o1.getLeaveTime() - arrivalTimeA;
                        visitorStayTimeB = o2.getLeaveTime() - arrivalTimeB;

                        return visitorStayTimeA.compareTo(visitorStayTimeB);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Exception: " +e.getMessage());
                }
                return arrivalTimeA.compareTo(arrivalTimeB);
            }
        });

        return personDetails;
    }
}
