package com.techgenie.velosafe;

/**
 * Class Name : RegionContract
 * Purpose : Declaring region name,region cordinate x,region cordinate y,region weight,table name,region is safe
 */
public class RegionContract {
    public static abstract class RegionBinInfo
    {
        public static final String REGION_NAME = "region_name";
        public static final String REGION_CORD_X = "region_cord_x";
        public static final String REGION_CORD_Y = "region_cord_y";
        public static final String REGION_WEIGHT = "region_weight";
        public static final String TABLE_NAME = "region_bins";
        public static final String REGION_ISSAFE = "region_isSafe";

    }
}
