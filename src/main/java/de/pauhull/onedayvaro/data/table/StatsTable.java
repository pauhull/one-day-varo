package de.pauhull.onedayvaro.data.table;

import de.pauhull.onedayvaro.data.MySQL;
import de.pauhull.onedayvaro.util.UUIDFetcher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StatsTable {

    private static final String TABLE = "odv_stats";

    private MySQL mySQL;
    private ExecutorService executorService;

    public StatsTable(MySQL mySQL, ExecutorService executorService) {

        this.mySQL = mySQL;
        this.executorService = executorService;
        this.mySQL.update("CREATE TABLE IF NOT EXISTS " + TABLE + " (id INT AUTO_INCREMENT, uuid VARCHAR(36), kills INT, deaths INT, wins INT, played_games INT, PRIMARY KEY (id))");
    }

    public void getTop(int top, BiConsumer<Stats, String> consumer) {

        executorService.execute(() -> {
            try {

                ResultSet resultSet = mySQL.query("SELECT * FROM " + TABLE + " ORDER BY wins DESC LIMIT " + (top - 1) + ",1");

                if (resultSet.next()) {

                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    int kills = resultSet.getInt("kills");
                    int deaths = resultSet.getInt("deaths");
                    int wins = resultSet.getInt("wins");
                    int playedGames = resultSet.getInt("played_games");

                    Stats stats = Stats.builder()
                            .uuid(uuid)
                            .kills(kills)
                            .deaths(deaths)
                            .wins(wins)
                            .playedGames(playedGames)
                            .build();

                    String name = new UUIDFetcher().getNameSync(uuid);

                    consumer.accept(stats, name);
                    return;
                }

                consumer.accept(null, null);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void getStats(UUID uuid, Consumer<Stats> consumer) {

        executorService.execute(() -> {
            try {

                ResultSet resultSet = mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'");

                if (resultSet.next()) {

                    int kills = resultSet.getInt("kills");
                    int deaths = resultSet.getInt("deaths");
                    int wins = resultSet.getInt("wins");
                    int playedGames = resultSet.getInt("played_games");
                    double kd = (double) kills / (double) deaths;

                    Stats stats = Stats.builder()
                            .uuid(uuid)
                            .kills(kills)
                            .deaths(deaths)
                            .wins(wins)
                            .playedGames(playedGames)
                            .kd(kd)
                            .build();
                    consumer.accept(stats);

                    return;
                }

                consumer.accept(Stats.builder().uuid(uuid).build());

            } catch (SQLException e) {
                e.printStackTrace();
                consumer.accept(Stats.builder().uuid(uuid).build());
            }
        });
    }

    public void insertStats(Stats stats) {

        executorService.execute(() -> {
            try {

                ResultSet resultSet = mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + stats.getUuid() + "'");

                if (resultSet.isBeforeFirst()) {

                    mySQL.update("UPDATE " + TABLE + " SET kills=" + stats.getKills() + ", deaths=" + stats.getDeaths() + ", wins=" + stats.getPlayedGames() + ", played_games=" + stats.getPlayedGames() + " WHERE uuid='" + stats.getUuid() + "'");
                } else {

                    mySQL.update("INSERT INTO " + TABLE + " VALUES (0, '" + stats.getUuid() + "', " + stats.getKills() + ", " + stats.getDeaths() + ", " + stats.getWins() + ", " + stats.getPlayedGames() + ")");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Builder(builderClassName = "Builder")
    public static class Stats {

        @Getter
        private UUID uuid;

        @Getter
        @Setter
        private int kills, deaths, wins, playedGames;

        @Getter
        @Setter
        private double kd;

    }

}
