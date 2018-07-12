package tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FileoutputUtil {

    public static final String Acc_Stuck = "Log_AccountStuck.rtf",
            Login_Error = "Log_Login_Error.rtf",
//          IP_Log = "Log_AccountIP.rtf",
//          GMCommand_Log = "Log_GMCommand.rtf",
//          Zakum_Log = "Log_Zakum.rtf",
//          Horntail_Log = "Log_Horntail.rtf",
            Pinkbean_Log = "Log_Pinkbean.rtf",
            ScriptEx_Log = "Log_Script_Except.rtf",
            PacketEx_Log = "Log_Packet_Except.rtf", // I cba looking for every error, adding this back in.
            Donator_Log = "Log_Donator.rtf",
            Hacker_Log = "Log_Hacker.rtf",
            Movement_Log = "Log_Movement.rtf",
            CommandEx_Log = "Log_Command_Except.rtf";
//	    PQ_Log = "Log_PQ.rtf"

    // End
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");

    static {
	sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static void log(final String file, final String msg) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, true);
            out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
            out.write(msg.getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void outputFileError(final String file, final Throwable t) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, true);
            out.write(("\n------------------------ " + CurrentReadable_Time() + " ------------------------\n").getBytes());
            out.write(getString(t).getBytes());
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static String CurrentReadable_Date() {
        return sdf_.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_Time() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_TimeGMT() {
        return sdfGMT.format(new Date());
    }

    public static String getString(final Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException ignore) {
            }
        }
        return retValue;
    }
}
