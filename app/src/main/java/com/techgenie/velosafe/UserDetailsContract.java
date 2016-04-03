package com.techgenie.velosafe;

/*Class:UserDetailsContract
  purpose:This class defines the user details
 */
public class UserDetailsContract {
         /* method:UserDetailsInfo
         * purpose:This method declares all user detal information
         *
         * */
    public static abstract class UserDetailsInfo {
        public static final String USER_FNAME = "user_fname";
        public static final String USER_LNAME = "user_lname";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_CONTACTNO = "user_contactno";
        public static final String USER_AREA = "user_area";
        public static final String TABLE_NAME = "user_details";
    }
}
