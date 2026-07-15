package dev.jackraidenph;

import dev.jackraidenph.Constants.Endpoints;
import dev.jackraidenph.Constants.Leaderborads;
import dev.jackraidenph.Constants.Secrets;
import dev.jackraidenph.data.LevelScorePartial;
import dev.jackraidenph.data.SonicWaveInfo;
import dev.jackraidenph.data.UserObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GDWebClient {
    private final HttpClient httpClient;

    public GDWebClient() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String askGDApi(String path, String secret, Map<String, Object> params) {
        HttpRequest request = makeGDRequest(path, secret, params);

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int status = response.statusCode();

        if (status != 200) {
            throw new RuntimeException("Code: " + response.statusCode() + "\n" + response.body());
        }

        return response.body();
    }

    public SonicWaveInfo didIBeatSonicWave(String username, String password) {
        List<LevelScorePartial> friendScores = getScores(username, password, Leaderborads.FRIENDS, Constants.SONIC_WAVE_LEVEL_ID);
        Optional<LevelScorePartial> ownScore = friendScores.stream().filter(score -> score.userName().equals(username)).findAny();
        if (ownScore.isEmpty()) {
            return new SonicWaveInfo(false, 0, 0);
        }

        LevelScorePartial ownActual = ownScore.get();
        return new SonicWaveInfo(ownActual.percentage() >= 100, ownActual.percentage(), ownActual.coins(), ownActual.age());
    }

    public List<LevelScorePartial> getScores(String username, String password, Leaderborads leaderboard, int levelId) {
        UserObject basicUserInfo = getUserInfo(username);

        int accountId = basicUserInfo.accountID();
        String gjp2 = Utils.GJP2(password);

        String response = askGDApi(
                Endpoints.GET_LEVEL_SCORES,
                Secrets.COMMON,
                Map.of(
                        "accountID", accountId,
                        "gjp2", gjp2,
                        "levelID", levelId,
                        "type", leaderboard.ordinal()
                )
        );

        return Utils.splitGDMultiResponse(response).stream().map(LevelScorePartial::new).toList();
    }

    public String[] login(String username, String password) {
        String udid = Utils.UDID();
        String gjp2 = Utils.GJP2(password);
        String response = askGDApi(
                Endpoints.LOGIN,
                Secrets.ACCOUNT,
                Map.of(
                        "udid", udid,
                        "userName", username,
                        "gjp2", gjp2
                )
        );

        return response.split(",");
    }

    /**
     * CAUSES DOUBLE REQUEST!!!
     *
     * @param username Player's username whose info to acquire, queries both basic and concrete info
     * @return UserObject with the asked player's info
     */
    public UserObject getMoreExtendedUserInfo(String username) {
        UserObject basicInfo = getUserInfo(username);
        int id = basicInfo.accountID();
        UserObject extendedUserInfo = getExtendedUserInfo(id);
        return UserObject.union(basicInfo, extendedUserInfo);
    }

    public UserObject getExtendedUserInfo(int accountId) {
        String response = askGDApi(
                Endpoints.GET_USER_BY_ID,
                Secrets.COMMON,
                Map.of("targetAccountID", accountId)
        );

        Map<Integer, String> playerInfo = Utils.splitGDResponse(response);

        return new UserObject(playerInfo);
    }

    public UserObject getUserInfo(String username) {
        String response = askGDApi(
                Constants.Endpoints.GET_USER_BY_NAME,
                Secrets.COMMON,
                Map.of(
                        "gameVersion", Constants.Service.GAME_VERSION,
                        "binaryVersion", Constants.Service.BINARY_VERSION,
                        "str", username
                )
        );

        Map<Integer, String> playerInfo = Utils.splitGDResponse(response);

        return new UserObject(playerInfo);
    }

    public static HttpRequest makeGDRequest(String path, String secret, Map<String, Object> params) {
        Map<String, Object> copy = new HashMap<>(params);

        copy.putIfAbsent("secret", secret);

        String urlEncoded = Utils.mapToUrlEncoded(copy);

        return HttpRequest.newBuilder()
                .uri(Utils.makeURI(path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "")
                .POST(BodyPublishers.ofString(urlEncoded))
                .build();
    }
}
