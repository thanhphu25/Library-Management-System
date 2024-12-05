package LMS;

public class TC {

    public static class MenuOption {
        public static final int LOGIN = 1;
        public static final int CREATE_NEW_ACCOUNT = 2;
        public static final int EXIT = 3;
    }

    public static class SignupOption {
        public static final int BORROWER = 1;
        public static final int LIBRARIAN = 2;
        public static final int BACK = 3;
    }
    public static class PortalOption {
        public static final int CHECK_NOTIFICATIONS = 0;
        public static final int SEARCH_BOOK = 1;
        public static final int SEARCH_BY_TITLE = 1;
        public static final int SEARCH_BY_AUTHOR = 2;
        public static final int SEARCH_BY_DESCRIPTION = 3;
        public static final int PLACE_HOLD_REQUEST = 2;
        public static final int CHECK_PERSONAL_INFO = 3;
        public static final int CHECK_TOTAL_FINE = 4;
        public static final int CHECK_HOLD_REQUEST_QUEUE = 5;
        public static final int CHECK_OUT_BOOK = 6;
        public static final int CHECK_IN_BOOK = 7;
        public static final int RENEW_BOOK = 8;
        public static final int ADD_NEW_BORROWER = 9;
        public static final int UPDATE_BORROWER_INFO = 10;
        public static final int ADD_NEW_BOOK = 11;
        public static final int REMOVE_BOOK = 12;
        public static final int CHANGE_BOOK_INFO = 13;
        public static final int VIEW_ISSUED_BOOKS_HISTORY = 14;
        public static final int VIEW_ALL_BOOKS = 15;
        public static final int L_LOG_OUT = 16;
        public static final int B_LOG_OUT = 6;
    }

    public static class Borrower {
        public static final String NAME = "BorrowerMock";
        public static final String PASS = "BorPassword";
        public static final String ADDR = "Address";
        public static final String PHONE = "111";
        public static final String EMAIL = "BorrowerMock";
    }

    public static class Librarian {
        public static final String NAME = "LibraryMock";
        public static final String PASS = "LibPassword";
        public static final String ADDR = "Address";
        public static final String PHONE = "111";
        public static final String EMAIL = "LibraryMock";
        public static final String SALARY = "1.1";
    }

    public static class BookFail {
        public static final String TITLE = "BookFail";
        public static final String AUTHOR = "AuthorFail";
        public static final String DESCRIPTION = "DescriptionFail";
    }

    public static class Book {
        public static final String TITLE = "Harry Potter and the Chamber of Secrets";
        public static final String AUTHOR = "J.K Rowling";
        public static final String DESCRIPTION = "Fiction";
    }

    public static final String PRESS_ANY_KEY = "Q";
    public static final String LIBRARY_PASSWORD = "LMS_Password";
}