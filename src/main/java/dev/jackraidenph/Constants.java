package dev.jackraidenph;

public class Constants {
    public static final String GD_BASE_URL = "https://www.boomlings.com/database/";
    public static final Integer SONIC_WAVE_LEVEL_ID = 26681070;

    public static final class Salts {
        public static final String GJP2 = "mI29fmAnxgTs";
    }

    public static final class Secrets {
        public static final String COMMON = "Wmfd2893gb7";
        public static final String ACCOUNT = "Wmfv3899gc9";
    }


    public static final class Endpoints {
        public static final String LOGIN = "accounts/loginGJAccount.php";
        public static final String GET_USER_BY_NAME = "getGJUsers20.php";
        public static final String GET_USER_BY_ID = "getGJUserInfo20.php";
        public static final String GET_LEVEL_SCORES = "getGJLevelScores211.php";
    }

    public static final class Service {
        public static final Integer GAME_VERSION = 22;
        public static final Integer BINARY_VERSION = 47;
    }

    public enum Leaderborads {
        FRIENDS,
        TOP,
        WEEK
    }
}
