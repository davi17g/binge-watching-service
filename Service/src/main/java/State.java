import request.ResponseType;

import java.util.concurrent.TimeUnit;

public interface State {
    void doAction(Context context);
    String getPrompt();
    User getUser();
    ResponseType getType();

}

class GetUserIDState implements State {
    private final ResponseType type = ResponseType.GET_USER_ID;
    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return "enter user-id";
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}

class GetUserOptionsState implements State {

    private User user = null;
    private final ResponseType type = ResponseType.GET_OPTION;

    public GetUserOptionsState(String userid) {
        user = new User(userid);
    }

    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return " S - switch user \n C - watch content \n H - show history \n E - exit";
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}

class UserContentTypeState implements State {

    private User user = null;
    private final ResponseType type = ResponseType.GET_CONTENT_TYPE;
    UserContentTypeState(State state) {
        this.user = state.getUser();
    }

    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return "Choose content: \n 1 - TV Show \n 2 - Movie \n 3 - Any";
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}

class DeliverNewContentState implements State{

    private User user = null;
    private final ResponseType type = ResponseType.NEW_CONTENT;
    DeliverNewContentState(State state){
        this.user = state.getUser();
    }
    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return user.getNewContent().toString();
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}

class ShowUserHistory implements State {
    private User user = null;
    private final ResponseType type = ResponseType.GET_OPTION;

    ShowUserHistory(State state) {
        user = state.getUser();
    }

    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return user.getHistory();
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}

class IsUserFinished implements State {
    private final ResponseType type = ResponseType.GET_STATUS;
    private User user = null;

    IsUserFinished(State state) {
        this.user = state.getUser();
    }
    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return "finished watching? (Y/N)";
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public ResponseType getType() {
        return type;
    }


}

class UserRecommendationState implements State {
    private final ResponseType type = ResponseType.GET_RATE;
    private User user = null;
    UserRecommendationState(State state) {
        user = state.getUser();
    }
    @Override
    public void doAction(Context context) {
        context.setState(this);
    }

    @Override
    public String getPrompt() {
        return "rate the content: 1-10";
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ResponseType getType() {
        return type;
    }
}



