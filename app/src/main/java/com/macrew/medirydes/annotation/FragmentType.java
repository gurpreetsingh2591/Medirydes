package com.macrew.medirydes.annotation;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FragmentType.HOME_FRAGMENT, FragmentType.CURRENT_SHIFT_FRAGMENT, FragmentType.FUTURE_SHIFT_FRAGMENT,FragmentType.ORDER_FRAGMENT,
        FragmentType.REPORTS_FRAGMENT,  FragmentType.FUEL_RECEIPT_FRAGMENT,FragmentType.NOTIFICATION_FRAGMENT, FragmentType.TRIP_DETAIL_FRAGMENT, FragmentType.REQUEST_TIME_FRAGMENT, FragmentType.PROFILE_FRAGMENT,FragmentType.FUEL_FRAGMENT,FragmentType.INSPECTION_FRAGMENT})

public @interface FragmentType {
    String HOME_FRAGMENT = "HomeFragment";
    String CURRENT_SHIFT_FRAGMENT = "TodayShiftFragment";
    String FUTURE_SHIFT_FRAGMENT = "FutureShiftFragment";
    String REPORTS_FRAGMENT = "ReportsFragment";
    String REQUEST_TIME_FRAGMENT = "RequestTimeFragment";
    String PROFILE_FRAGMENT = "ProfileFragment";
    String FUEL_FRAGMENT = "FuelFragment";
    String ORDER_FRAGMENT = "OrderFragment";
    String INSPECTION_FRAGMENT = "InspectionFragment";
    String FUEL_RECEIPT_FRAGMENT = "FuelReceiptFragment";
    String TRIP_DETAIL_FRAGMENT = "TripDetailFragment";
    String NOTIFICATION_FRAGMENT = "NotificationFragment";
}
// Declare the constants
