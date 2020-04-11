package cpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("main")
public class Cpl {

    private final long timeStamp = System.currentTimeMillis() / 1000;
    private final String conPath = "jdbc:mysql://198.71.227.97:3306/cpl";
    private final String userName = "mahesh";
    private final String password = "eQa2j#78";
    private final String classPath = "com.mysql.cj.jdbc.Driver";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("userLogin&{userType}&{userEmail}&{userPass}")
    public String userLogin(@PathParam("userType") String userType, @PathParam("userEmail") String userEmail, @PathParam("userPass") String userPass) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId = -1;
        String message = "Login Failed";
        JSONObject jsonObject = null;
        String status = "OK";
        Connection con = null;

        try {
            if (userType.equalsIgnoreCase("LeagueManager")) {
                sql = "Select lm.userId from LeagueManager lm join User u ON lm.userId = u.userId where u.email=? && u.password=?";
            } else {
                sql = "Select tm.userId from TeamManager tm join User u ON tm.userId = u.userId where u.email=? && u.password=?";
            }
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setString(1, userEmail);
            stm.setString(2, userPass);
            rs = stm.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("userId");
                message = "Login Successfull";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("UserId", userId);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verifyUser&{userEmail}&{dob}")
    public String verifyUser(@PathParam("userEmail") String userEmail, @PathParam("dob") String dob) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        int userId = -1;
        String message = "Invalid User";
        JSONObject jsonObject = null;
        String status = "OK";
        Connection con = null;

        try {

            sql = "Select userId from User where email=? && dob=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setString(1, userEmail);
            stm.setString(2, dob);
            rs = stm.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("userId");
                message = "Valid User";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("UserId", userId);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changePassword&{newPassword}&{userId}")
    public String changePassword(@PathParam("newPassword") String newPassword,
            @PathParam("userId") int userId) {

        PreparedStatement stm = null;
        String sql = null;
        int rs;
        String message = null;
        JSONObject jsonObject = null;
        String status = "OK";
        Connection con = null;
        try {
            sql = "update User set password=? where userId=?";
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setString(1, newPassword);
            stm.setInt(2, userId);
            rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Password Updated";

            } else {
                message = "Failed";
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("sendFeedback&{title}&{description}&{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendFeedback(@PathParam("title") String title, @PathParam("description") String description,
            @PathParam("email") String email) {
        PreparedStatement stm = null;
        String sql = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            sql = "insert into Feedback (title, description, email) values(?,?,?)";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            stm = con.prepareStatement(sql);
            stm.setString(1, title);
            stm.setString(2, description);
            stm.setString(3, email);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Feedback Sent";
            } else {
                message = "Failed";
                status = "Error";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("browsePlayers&{playerName}")
    public String browsePlayers(@PathParam("playerName") String playerName) {
        PreparedStatement stm = null;
        JSONObject jsonObject = null;
        JSONArray list = null;
        String sql = null;
        String message = null;
        String status = "OK";
        JSONObject singleObject = null;
        Connection con = null;

        try {

            sql = "Select * from Player where playerName LIKE ? ";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setString(1, playerName + "%");
            ResultSet rs = stm.executeQuery();

            singleObject = new JSONObject();
            list = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getInt("playerId"));
                singleObject.accumulate("playerName", rs.getString("playerName"));
                singleObject.accumulate("dob", rs.getString("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthPlace"));
                singleObject.accumulate("url", rs.getString("url"));
                singleObject.accumulate("teamId", rs.getInt("teamId"));
                list.add(singleObject);
                singleObject.clear();
            }
        } catch (Exception e) {
            status = "Error";
            message = e.getMessage();
        } finally {

            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Player Details", list);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("viewPlayerDetails&{playerId}")
    @Produces("application/json")
    public String viewPlayerDetails(@PathParam("playerId") int playerId) {

        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        PreparedStatement stm = null;
        String status = "OK";
        String message = null;
        String sql;
        Connection con = null;
        ResultSet rs;

        try {
            sql = "Select * from Player where playerId=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setInt(1, playerId);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getInt("playerId"));
                singleObject.accumulate("PlayerName", rs.getString("playerName"));
                singleObject.accumulate("BirthDate", rs.getString("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthPlace"));
                singleObject.accumulate("teamId", rs.getString("teamId"));
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Player Details", singleObject);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateTeam&{teamId}&{teamName}&{teamColor}&{teamManagerId}")
    public String updateTeam(@PathParam("teamId") int teamId,
            @PathParam("teamName") String teamName,
            @PathParam("teamColor") String teamColor,
            @PathParam("teamManagerId") int teamManagerId) {

        String msg = null;
        String status = "OK";
        JSONObject jsonObject = null;
        PreparedStatement stmt = null;
        Connection con = null;

        try {
            String sql;
            sql = "Update Team set teamName=?,teamColor=?,teamManagerId=? where teamId=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stmt = con.prepareStatement(sql);
            stmt.setString(1, teamName);
            stmt.setString(2, teamColor);
            stmt.setInt(3, teamManagerId);
            stmt.setInt(4, teamId);
            int rs = stmt.executeUpdate();

            if (rs > 0) {
                msg = rs + "Team updated";
            } else {
                msg = rs + "Failed";
            }

        } catch (Exception ex) {
            status = "Error";
            msg = ex.getMessage();
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp ", timeStamp);
            jsonObject.accumulate("Message :", msg);
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewTeams")
    public String viewTeams() {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonarry = null;
        String status = "OK";
        String message = null;
        Connection con = null;
        try {
            sql = "Select * from Team";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonarry = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("teamId", rs.getString("teamId"));
                singleObject.accumulate("teamName", rs.getString("teamName"));
                singleObject.accumulate("teamColor", rs.getString("teamColor"));
                singleObject.accumulate("teamManagerId", rs.getInt("teamManagerId"));
                jsonarry.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Teams", jsonarry);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("viewMatch&{seasonId}")
    @Produces("application/json")
    public String viewMatch(@PathParam("seasonId") int seasonId) {
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {

            sql = "select * from Matches where seasonId=? order by date asc";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setInt(1, seasonId);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonArray = new JSONArray();

            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("Match Id", rs.getInt("matchId"));
                singleObject.accumulate("TeamA", rs.getString("teamA"));
                singleObject.accumulate("TeamB", rs.getString("TeamB"));
                singleObject.accumulate("Date", rs.getString("date"));
                singleObject.accumulate("Venue", rs.getString("venue"));
                singleObject.accumulate("Result", rs.getString("result"));
                singleObject.accumulate("Result Description", rs.getString("resultDescription"));
                singleObject.accumulate("seasonId", rs.getInt("seasonId"));

                jsonArray.add(singleObject);
                singleObject.clear();
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Matches", jsonArray);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("viewSeason")
    @Produces("application/json")
    public String viewSeason() {
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            sql = "Select * from Season order by startDate desc";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonArray = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("SeasonId", rs.getInt("seasonId"));
                singleObject.accumulate("Season Title", rs.getString("seasonTitle"));
                singleObject.accumulate("Description", rs.getString("description"));
                singleObject.accumulate("Start Date", rs.getString("startDate"));
                singleObject.accumulate("End Date", rs.getString("endDate"));
                jsonArray.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Seasons", jsonArray);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("viewPlayersByTeam&{teamId}")
    @Produces("application/json")
    public String viewPlayersByTeam(@PathParam("teamId") int teamId) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        JSONObject singleObject = null;
        JSONArray list = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            sql = "Select * from Player where teamId=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setInt(1, teamId);
            rs = stm.executeQuery();
            singleObject = new JSONObject();
            list = new JSONArray();

            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getString("playerId"));
                singleObject.accumulate("playerName", rs.getString("playerName"));
                singleObject.accumulate("dob", rs.getString("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthPlace"));
                singleObject.accumulate("url", rs.getString("url"));
                singleObject.accumulate("teamId", rs.getString("teamId"));

                list.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Players", list);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return jsonObject.toString();
    }

    @GET
    @Path("viewTeamManager")
    @Produces("application/json")
    public String viewTeamManager() {

        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonarray = null;
        PreparedStatement stm = null;
        String status = "OK";
        String message = null;
        String sql;
        ResultSet rs;
        Connection con = null;

        try {
            sql = "select UserName,teamManagerId from TeamManager t ,User u WHERE t.userId = u.userId";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery(sql);

            singleObject = new JSONObject();
            jsonarray = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("UserName", rs.getString("UserName"));
                singleObject.accumulate("teamManagerId", rs.getString("teamManagerId"));
                jsonarray.add(singleObject);
                singleObject.clear();
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("TeamManagers", jsonarray);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Path("viewPointsTable&{seasonId}")
    @Produces("application/json")
    public String viewPointsTable(@PathParam("seasonId") int seasonId) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        JSONObject singleObject = null;
        JSONArray list = null;
        String status = "OK";
        String message = null;
        Connection con = null;
        try {
            sql = "SELECT  TeamName,sum(play)AS play ,sum(Win) as Win ,sum(Lose) as Lose,sum(Points) as Points FROM PointTable where seasonId = ? Group by TeamName order by points desc;";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setInt(1, seasonId);
            rs = stm.executeQuery();
            singleObject = new JSONObject();
            list = new JSONArray();

            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("TeamName", rs.getString("TeamName"));
                singleObject.accumulate("play", rs.getInt("play"));
                singleObject.accumulate("Win", rs.getInt("Win"));
                singleObject.accumulate("Lose", rs.getInt("Lose"));
                singleObject.accumulate("Points", rs.getInt("Points"));
                list.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("PointsTable", list);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return jsonObject.toString();
    }

    @GET
    @Path("updatePlayerTeams&{teamId}&{player1}&{player2}&{player3}&{player4}&{player5}&{player6}&{player7}&{player8}&{player9}&{player10}&{player11}&{player12}&{player13}&{player14}&{player15}")
    @Produces("application/json")
    public String updatePlayerTeams(@PathParam("teamId") int teamId, @PathParam("player1") int player1, @PathParam("player2") int player2,
            @PathParam("player3") int player3, @PathParam("player4") int player4, @PathParam("player5") int player5, @PathParam("player6") int player6,
            @PathParam("player7") int player7, @PathParam("player8") int player8, @PathParam("player9") int player9, @PathParam("player10") int player10,
            @PathParam("player11") int player11, @PathParam("player12") int player12, @PathParam("player13") int player13, @PathParam("player14") int player14,
            @PathParam("player15") int player15) {

        JSONObject jsonObject;
        PreparedStatement stmt = null;
        String status = "OK";
        String message = null;
        String sql;
        Connection con = null;

        try {

            sql = "UPDATE Player set teamId=? where playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=? OR playerId=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stmt = con.prepareStatement(sql);

            stmt.setInt(1, teamId);
            stmt.setInt(2, player1);
            stmt.setInt(3, player2);
            stmt.setInt(4, player3);
            stmt.setInt(5, player4);
            stmt.setInt(6, player5);
            stmt.setInt(7, player6);
            stmt.setInt(8, player7);
            stmt.setInt(9, player8);
            stmt.setInt(10, player9);
            stmt.setInt(11, player10);
            stmt.setInt(12, player11);
            stmt.setInt(13, player12);
            stmt.setInt(14, player13);
            stmt.setInt(15, player14);
            stmt.setInt(16, player15);

            int rs = stmt.executeUpdate();

            if (rs > 0) {
                message = "Players Added";

            } else {
                message = "Error";
            }

        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {

            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewSingleTeamInfo&{teamId}")
    public String viewSingleTeamInfo(@PathParam("teamId") int teamId) {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonarray = null;
        String status = "OK";
        String message = null;
        Connection con = null;
        try {
            sql = "select tm.teamManagerId,tm.userId,t.teamName,t.teamColor,u.userName,u.contactNumber from Team as t join TeamManager as tm on t.teamManagerId=tm.teamManagerId join User as u on tm.userId=u.userId where teamId=?";

            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);
            stm.setInt(1, teamId);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonarray = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("teamManagerUserId", rs.getInt("teamManagerId"));
                singleObject.accumulate("teamManagerId", rs.getInt("userId"));
                singleObject.accumulate("teamName", rs.getString("teamName"));
                singleObject.accumulate("teamColor", rs.getString("teamColor"));
                singleObject.accumulate("userName", rs.getString("userName"));
                singleObject.accumulate("contactNumber", rs.getString("contactNumber"));
                jsonarray.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Teams", jsonarray);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getCount")
    public String getCount() {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonarry = null;
        String status = "OK";
        String message = null;
        Connection con = null;
        try {
            sql = "select (select count(*) from Season) as totalSeason,\n"
                    + "  (select count(*) from Matches) as totalMatches,\n"
                    + "  (select count(*) from Team) as totalTeam;";
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);
            stm = con.prepareStatement(sql);

            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonarry = new JSONArray();
            while (rs.next()) {
                message = "Available";

                singleObject.accumulate("totalSeason", rs.getString("totalSeason"));
                singleObject.accumulate("totalMatches", rs.getString("totalMatches"));
                singleObject.accumulate("totalTeam", rs.getString("totalTeam"));
                jsonarry.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Counts", jsonarry);

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("removePlayer&{playerId}")
    public String removePlayer(@PathParam("playerId") int playerId) {

        PreparedStatement stm = null;
        String sql = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "Update Player set teamId=? where playerId=?";
            stm = con.prepareStatement(sql);
            stm.setString(1, null);
            stm.setInt(2,playerId);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Player Removed";
            } else {
                message = "Error";
            }

        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

}
