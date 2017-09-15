package limousine.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.thinh.limousine.R;

import java.util.ArrayList;

import limousine.Model.Route;
import limousine.Model.TripByDate;
import limousine.RouteListFragment;

/**
 * Created by thinh on 25/08/2017.
 */

public class ListAdapter extends ExpandableRecyclerAdapter<ListAdapter.RouterViewHolder, ListAdapter.TripViewHolder> {
    private RouteListFragment mRouteFragment;

    public ListAdapter(ArrayList<Route> arr, RouteListFragment fragment) {
        super(arr);
        mRouteFragment = fragment;
    }

    @Override
    public RouterViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.adapter_spinner, parentViewGroup, false);
        return new ListAdapter.RouterViewHolder(v);
    }

    @Override
    public TripViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.route_list_item, childViewGroup, false);
        return new ListAdapter.TripViewHolder(v);
    }

    @Override
    public void onBindParentViewHolder(@NonNull RouterViewHolder parentViewHolder, int parentPosition, @NonNull ParentListItem parent) {
        parentViewHolder.mName.setText(((Route) parent).getName());
    }

    @Override
    public void onBindChildViewHolder(TripViewHolder childViewHolder,final int position, Object childListItem) {
        final TripByDate trip = (TripByDate) childListItem;
        childViewHolder.mSeat.setText(trip.getSeat() + " / 9");
        childViewHolder.mTime.setText(trip.getTime());
        childViewHolder.mMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRouteFragment.onClickItemList(position);
            }
        });
    }

    public class RouterViewHolder extends ParentViewHolder {
        private TextView mName;

        public RouterViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public class TripViewHolder extends ChildViewHolder {
        private TextView mSeat;
        private TextView mTime;
        private RelativeLayout mMainItem;

        public TripViewHolder(View itemView) {
            super(itemView);
            mSeat = (TextView) itemView.findViewById(R.id.seat);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mMainItem = (RelativeLayout) itemView.findViewById(R.id.main_item_route_list);
        }
    }

}
