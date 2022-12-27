package enums;

public enum MenuOption {
    LOGIN,
    SIGN_UP,
    LOGOUT,
    SUBSCRIBE_NEW_PRODUCT,
    UNSUBSCRIBE_A_PRODUCT,
    GIVE_FEEDBACK_TO_A_PRODUCT,
    VIEW_SUBSCRIBED_PRODUCTS,
    VIEW_ALL_PRODUCTS,
    SIGN_UP_AS_CLIENT,
    SIGN_UP_AS_EMPLOYEE,
    DELETE_ACCOUNT,
    VIEW_PROFILE,
    EDIT_PROFILE,
    GO_BACK,
    CHANGE_USERNAME,
    CHANGE_PASSWORD,
    VIEW_REPORTS_RECEIVED,
    VIEW_REPORTS_SENT,
    SEND_REPORT,
    VIEW_HELPDESK_TICKETS,
    ADD_EMPLOYEE,
    ADD_DEPARTMENT,
    ADD_PRODUCT,
    REMOVE_PRODUCT,
    REMOVE_DEPARTMENT,
    VIEW_RAISED_HELPDESK_TICKETS,
    EAGLE_VIEW_DEPARTMENT,
    RELEASE_AN_UPDATE,
    VIEW_ALL_DEPARTMENTS,
    EXIT;

    @Override
    public String toString() {
        return name()
                .charAt(0)
                + name().substring(1)
                        .toLowerCase()
                        .replace("_", " ");
    }

}
