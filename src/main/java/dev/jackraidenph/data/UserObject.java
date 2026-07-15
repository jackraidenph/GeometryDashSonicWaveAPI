package dev.jackraidenph.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import static dev.jackraidenph.Utils.parseBooleanSafe;
import static dev.jackraidenph.Utils.parseIntSafe;

public record UserObject(
        String userName,             // 1
        Integer userID,              // 2
        Integer stars,               // 3
        Integer demonCount,          // 4
        Integer ranking,             // 6
        Integer accountHighlight,    // 7
        Integer creatorPoints,       // 8
        Integer iconID,              // 9
        Integer color,               // 10
        Integer color2,              // 11
        Integer shipID,              // 12 - deprecated
        Integer secretCoins,         // 13
        Integer iconType,            // 14
        Integer special,             // 15
        Integer accountID,           // 16
        Integer userCoins,           // 17
        Integer messageState,        // 18*
        Integer friendsState,        // 19*
        String youTube,              // 20*
        Integer accIcon,             // 21*
        Integer accShip,             // 22*
        Integer accBall,             // 23*
        Integer accBird,             // 24*
        Integer accDart,             // 25*
        Integer accRobot,            // 26*
        Integer accStreak,           // 27 - never actually sent by the server
        Boolean accGlow,             // 28*
        Boolean isRegistered,        // 29*
        Integer globalRank,          // 30*
        Integer friendState,         // 31*
        Integer messages,            // 38*
        Integer friendRequests,      // 39*
        Integer newFriends,          // 40*
        Boolean newFriendRequest,    // 41
        String age,                  // 42*
        Integer accSpider,           // 43*
        String twitter,              // 44*
        String twitch,               // 45*
        Integer diamonds,            // 46
        Integer accExplosion,        // 48*
        Integer mod,                 // 49*
        Integer commentHistoryState, // 50*
        Integer color3,              // 51
        Integer moons,               // 52
        Integer accSwing,            // 53*
        Integer accJetpack,          // 54*
        String demons,               // 55* - "easy,medium,hard.insane,extreme,easyPlatformer,mediumPlatformer,hardPlatformer,insanePlatformer,extremePlatformer,weekly,gauntlet"
        String classicLevels,        // 56* - "auto,easy,normal,hard,harder,insane,daily,gauntlet"
        String platformerLevels,     // 57* - "auto,easy,normal,hard,harder,insane,theMap"
        String discord,              // 58*
        String instagram,            // 59*
        String tiktok,               // 60*
        String custom                // 61*
) {

    public UserObject(Map<Integer, String> data) {
        this(
                data.get(1),
                parseIntSafe(data.get(2)),
                parseIntSafe(data.get(3)),
                parseIntSafe(data.get(4).split("#")[0]),
                parseIntSafe(data.get(6)),
                parseIntSafe(data.get(7)),
                parseIntSafe(data.get(8)),
                parseIntSafe(data.get(9)),
                parseIntSafe(data.get(10)),
                parseIntSafe(data.get(11)),
                parseIntSafe(data.get(12)),
                parseIntSafe(data.get(13)),
                parseIntSafe(data.get(14)),
                parseIntSafe(data.get(15)),
                parseIntSafe(data.get(16)),
                parseIntSafe(data.get(17)),
                parseIntSafe(data.get(18)),
                parseIntSafe(data.get(19)),
                data.get(20),
                parseIntSafe(data.get(21)),
                parseIntSafe(data.get(22)),
                parseIntSafe(data.get(23)),
                parseIntSafe(data.get(24)),
                parseIntSafe(data.get(25)),
                parseIntSafe(data.get(26)),
                parseIntSafe(data.get(27)),
                parseBooleanSafe(data.get(28)),
                parseBooleanSafe(data.get(29)),
                parseIntSafe(data.get(30)),
                parseIntSafe(data.get(31)),
                parseIntSafe(data.get(38)),
                parseIntSafe(data.get(39)),
                parseIntSafe(data.get(40)),
                parseBooleanSafe(data.get(41)),
                data.get(42),
                parseIntSafe(data.get(43)),
                data.get(44),
                data.get(45),
                parseIntSafe(data.get(46)),
                parseIntSafe(data.get(48)),
                parseIntSafe(data.get(49)),
                parseIntSafe(data.get(50)),
                parseIntSafe(data.get(51)),
                parseIntSafe(data.get(52)),
                parseIntSafe(data.get(53)),
                parseIntSafe(data.get(54)),
                data.get(55),
                data.get(56),
                data.get(57),
                data.get(58),
                data.get(59),
                data.get(60),
                data.get(61)
        );
    }

    public static UserObject union(UserObject first, UserObject second) {
        Field[] fields = UserObject.class.getDeclaredFields();
        Object[] paramsUnion = new Object[fields.length];
        int i = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            Object firstValue = getFieldValue(field, first);
            Object secondValue = getFieldValue(field, second);
            if (firstValue != null && secondValue != null && !firstValue.equals(secondValue)) {
                throw new IllegalStateException("[%s] differ between two UserObjects: [%s] != [%s]".formatted(field.getName(), firstValue, secondValue));
            }

            paramsUnion[i++] = firstValue != null ? firstValue : secondValue;
        }

        Constructor<?> constructor = UserObject.class.getConstructors()[1];
        try {
            return (UserObject) constructor.newInstance(paramsUnion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getFieldValue(Field field, Object context) {
        try {
            return field.get(context);
        } catch (IllegalAccessException supressed) {
            return null;
        }
    }
}