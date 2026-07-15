# Did YOU beat Sonic Wave?
A half-joke Geometry Dash Java API client for retrieving some of the user's stats, and, most importantly, whether you did beat Sonic Wave or not

## Example usage
```java
String username = "myUsername";
String password = "myPassword";

GDWebClient client = new GDWebClient();
SonicWaveInfo info = client.didIBeatSonicWave(username, password);
```
