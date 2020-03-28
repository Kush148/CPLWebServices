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

@Path("leagueManager")
public class LeagueManager {

    private final long timeStamp = System.currentTimeMillis() / 1000;
    private final String conPath = "jdbc:mysql://198.71.227.97:3306/cpl";
    private final String userName = "mahesh";
    private final String password = "eQa2j#78";
    private final String classPath = "com.mysql.cj.jdbc.Driver";

    @GET
    @Path("createSeason&{seasonTitle}&{startDate}&{endDate}&{description}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createSeason(@PathParam("seasonTitle") String seasonTitle,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("description") String description) {

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "insert into Season (seasonTitle,startDate,endDate,description) values(?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, seasonTitle);
            stm.setString(2, startDate);
            stm.setString(3, endDate);
            stm.setString(4, description);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Season Created";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

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
        return jsonObj.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addteammanager&{userName}&{email}&{password}&{dob}&{contactNumber}")
    public String addTeamManager(@PathParam("userName") String userName,
            @PathParam("email") String email, @PathParam("password") String password, @PathParam("dob") String dob,
            @PathParam("contactNumber") String contactNumber) {

        JSONObject jsonObject = null;
        PreparedStatement stmt = null;
        String sql;
        String sql1;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "insert into User (userName,email,password,dob,contactNumber) values(?,?,?,?,?)";
            sql1 = "insert  into TeamManager (userId)  select max(userId) from User ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, dob);
            stmt.setString(5, contactNumber);

            int rs = stmt.executeUpdate();

            if (rs > 0) {
                message = "Team Manager Created";
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
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createTeam&{teamName}&{teamColor}&{teamManagerId}")
    public String createTeam(@PathParam("teamName") String teamName,
            @PathParam("teamColor") String teamColor,
            @PathParam("teamManagerId") int teamManagerId) {

        JSONObject jsonObject = null;
        PreparedStatement stmt = null, stmt1 = null;
        String sql, sql1;
        String status = "OK";
        String message = null;
        String imgpath = "http://stallionsmultiservices.com/CPL/colors/";
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "insert into Team (teamName,teamColor,teamManagerId) values(?,?,?)";
            sql1 = "select max(teamId) as tId from Team";
            stmt = con.prepareStatement(sql);
            stmt1 = con.prepareStatement(sql1);
            stmt.setString(1, teamName);
            stmt.setString(2, imgpath + teamColor + ".png");
            stmt.setInt(3, teamManagerId);
            jsonObject = new JSONObject();

            int rs = stmt.executeUpdate();
            ResultSet rs1 = stmt1.executeQuery();

            if (rs > 0) {
                message = "Team Created";
            } else {
                message = "Errors";
            }

            while (rs1.next()) {
                jsonObject.accumulate("TeamID", rs1.getInt("tId"));

            }
        } catch (Exception ex) {
            status = "Error";
            message = ex.getMessage();

        } finally {

            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteTeam&{teamId}")
    public String deleteTeam(@PathParam("teamId") int teamId) {

        PreparedStatement stm = null;
        String sql = null;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "Delete from Team where teamId=?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, teamId);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Team Deleted";
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("viewFeedback")
    public String viewFeedback() {

        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject jsonObject = null;
        String status = "OK";
        String message = null;
        JSONObject singleObject = null;
        JSONArray list = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "Select * from Feedback";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            list = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("feedbackId", rs.getInt("feedbackId"));
                singleObject.accumulate("title", rs.getString("title"));
                singleObject.accumulate("description", rs.getString("description"));
                singleObject.accumulate("email", rs.getString("email"));
                System.out.println(singleObject);
                list.add(singleObject);
                singleObject.clear();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Cpl.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
            status = "Error";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObject = new JSONObject();
            jsonObject.accumulate("Status", status);
            jsonObject.accumulate("TimeStamp", timeStamp);
            jsonObject.accumulate("Message", message);
            jsonObject.accumulate("Feedbacks", list);

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
    @Path("createMatch&{teamA}&{teamB}&{date}&{venue}&{result}&{resultDescription}&{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createMatch(
            @PathParam("teamA") String teamA,
            @PathParam("teamB") String teamB,
            @PathParam("date") String date,
            @PathParam("venue") String venue,
            @PathParam("result") String result,
            @PathParam("resultDescription") String resultDescription,
            @PathParam("seasonId") int seasonId) {

        PreparedStatement stm = null, stm1 = null;
        JSONObject jsonObj = null;
        String sql, sql1;
        String status = "OK";
        String message = null;
        int matchId = -1;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "insert into Matches (teamA,teamB,date,venue,result,resultDescription,seasonId) values(?,?,?,?,?,?,?)";
            sql1 = "Select max(matchId) as newMatchId from Matches";
            stm = con.prepareStatement(sql);
            stm1 = con.prepareStatement(sql1);

            stm.setString(1, teamA);
            stm.setString(2, teamB);
            stm.setString(3, date);
            stm.setString(4, venue);
            stm.setString(5, result);
            stm.setString(6, resultDescription);
            stm.setInt(7, seasonId);

            int rs = stm.executeUpdate();
            ResultSet currentMatchId = stm1.executeQuery();
            while (currentMatchId.next()) {
                matchId = currentMatchId.getInt("newMatchId");
            }
            if (rs > 0) {
                message = "Match Created";
            } else {
                message = "Error";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);
            jsonObj.accumulate("MatchId", matchId);

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
        return jsonObj.toString();
    }

    @GET
    @Path("insertIntoPointTable&{TeamName}&{TeamName2}&{play}&{Win}&{Lose}&{Points}&{seasonId}&{matchId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertIntoPointTable(
            @PathParam("TeamName") String team1,
            @PathParam("TeamName2") String team2,
            @PathParam("play") int play,
            @PathParam("Win") int win,
            @PathParam("Lose") int lose,
            @PathParam("Points") int point,
            @PathParam("seasonId") int seasonId,
            @PathParam("matchId") int matchId) {

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "insert into PointTable (TeamName,play,Win,Lose,Points,seasonId,matchId) values(?,?,?,?,?,?,?),(?,?,?,?,?,?,?)";
            stm = con.prepareStatement(sql);

            stm.setString(1, team1);
            stm.setInt(2, 0);
            stm.setInt(3, 0);
            stm.setInt(4, 0);
            stm.setInt(5, 0);
            stm.setInt(6, seasonId);
            stm.setInt(7, matchId);

            stm.setString(8, team2);
            stm.setInt(9, 0);
            stm.setInt(10, 0);
            stm.setInt(11, 0);
            stm.setInt(12, 0);
            stm.setInt(13, seasonId);
            stm.setInt(14, matchId);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "Points Inserted";
            } else {
                message = "Error";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

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
        return jsonObj.toString();
    }

    @GET
    @Path("updatePointTable&{TeamName}&{seasonId}&{matchId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePointTable(
            @PathParam("TeamName") String team1,
            @PathParam("seasonId") int seasonId,
            @PathParam("matchId") int matchId) {

        PreparedStatement stm = null, stm1 = null;
        JSONObject jsonObj = null;
        String sql, sql1;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "update PointTable set  Win = Win + 1 ,Points = Points + 2  where TeamName  = ? and matchId = ? and seasonId=?";
            sql1 = "update PointTable set  Win = Win + 1 , Lose = Lose + 1  where TeamName  != ? and matchId = ? and seasonId=?";

            stm = con.prepareStatement(sql);
            stm1 = con.prepareStatement(sql1);

            stm.setString(1, team1);
            stm.setInt(2, matchId);
            stm.setInt(3, seasonId);

            stm1.setString(1, team1);
            stm1.setInt(2, matchId);
            stm1.setInt(3, seasonId);

            int rs = stm.executeUpdate();
            int rs1 = stm1.executeUpdate();

            if (rs > 0 && rs1 > 0) {
                message = "Point Table Updated";
            } else {
                message = "Error";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

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
        return jsonObj.toString();
    }

    @GET
    @Path("playerList")
    @Produces("application/json")
    public String playerList() {
        Connection con = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs;
        JSONObject singleObject = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String status = "OK";
        String message = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "select * from Player where teamId IS NULL;";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            singleObject = new JSONObject();
            jsonArray = new JSONArray();
            while (rs.next()) {
                message = "Available";
                singleObject.accumulate("playerId", rs.getInt("playerId"));
                singleObject.accumulate("playerName", rs.getString("playerName"));
                singleObject.accumulate("dob", rs.getString("dob"));
                singleObject.accumulate("role", rs.getString("role"));
                singleObject.accumulate("birthPlace", rs.getString("birthPlace"));
                singleObject.accumulate("url", rs.getString("url"));
                singleObject.accumulate("teamId", rs.getString("teamId"));

                jsonArray.add(singleObject);
                singleObject.clear();
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
            jsonObject.accumulate("Players", jsonArray);

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

    @GET
    @Path("updateMatches&{matchId}&{result}&{resultDescription}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateMatches(
            @PathParam("matchId") int matchId,
            @PathParam("result") String result,
            @PathParam("resultDescription") String resultDescription) {

        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "update Matches set  result = ? ,resultDescription = ?   where matchId  = ? ";
            stm = con.prepareStatement(sql);

            stm.setString(1, result);
            stm.setString(2, resultDescription);
            stm.setInt(3, matchId);
            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = " Result Entered";
            } else {
                message = "Error";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

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
        return jsonObj.toString();
    }

    @GET
    @Path("teamManagerUpdateInfo&{contactNumber}&{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String teamManagerUpdateInfo(
            @PathParam("contactNumber") String contactNumber,
            @PathParam("userId") int userId) {
        PreparedStatement stm = null;
        JSONObject jsonObj = null;
        String sql;
        String status = "OK";
        String message = null;
        Connection con = null;

        try {
            Class.forName(classPath);
            con = DriverManager.getConnection(conPath, userName, password);

            sql = "update TeamManager as tm join User as u on tm.userId=u.userId set u.contactNumber=? where tm.userId=?;";
            stm = con.prepareStatement(sql);

            stm.setString(1, contactNumber);
            stm.setInt(2, userId);

            int rs = stm.executeUpdate();

            if (rs > 0) {
                message = "updated";
                status = "ok";
            } else {
                message = "No info updated ";
            }
        } catch (SQLException ex) {
            status = "Error";
            message = ex.getMessage();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LeagueManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            jsonObj = new JSONObject();
            jsonObj.accumulate("Status", status);
            jsonObj.accumulate("TimeStamp", timeStamp);
            jsonObj.accumulate("Message", message);

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
        return jsonObj.toString();
    }
}
