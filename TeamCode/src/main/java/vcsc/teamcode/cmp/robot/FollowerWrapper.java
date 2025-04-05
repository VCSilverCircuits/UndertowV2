package vcsc.teamcode.cmp.robot;

import com.pedropathing.follower.Follower;

public class FollowerWrapper {
    static Follower follower;

    public static void setFollower(Follower follower) {
        FollowerWrapper.follower = follower;
    }

    public static Follower getFollower() {
        return follower;
    }
}
