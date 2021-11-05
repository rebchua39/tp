package seedu.duke.model.lesson;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.duke.commons.core.DayOfTheWeek;
import seedu.duke.DukeException;
import seedu.duke.commons.core.Message;
import seedu.duke.commons.util.TimeUtil;
import seedu.duke.logic.parser.exceptions.ParseException;
import seedu.duke.model.lesson.exceptions.EmptyLinkException;
import seedu.duke.ui.Ui;

import static seedu.duke.commons.util.LinkUtil.formatLink;
import static seedu.duke.commons.util.LinkUtil.launchUrlOnLinux;
import static seedu.duke.commons.util.LinkUtil.launchUrlOnMac;
import static seedu.duke.commons.util.LinkUtil.launchUrlOnWindows;
import static seedu.duke.logic.parser.ParserUtil.parseTitle;
import static seedu.duke.logic.parser.ParserUtil.parseDayOfTheWeek;
import static seedu.duke.logic.parser.ParserUtil.parseTime;

//@@author Roycius
public class Lesson {
    private final String title;
    private final String dayOfTheWeek;
    private final String startTime;
    private final String endTime;
    private final String meetingUrl;

    public Lesson(String title, String dayOfTheWeek, String startTime, String endTime, String meetingUrl) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingUrl = meetingUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    //@@author richwill28
    /**
     * Launches meeting URL on the default browser.
     *
     * @throws IOException if an I/O error occurs while executing runtime command
     */
    public void launchUrl() throws EmptyLinkException, IOException {
        boolean isEmptyUrl = meetingUrl.isBlank() || meetingUrl.equals("-");
        if (isEmptyUrl) {
            throw new EmptyLinkException(Message.ERROR_EMPTY_MEETING_LINK);
        }

        Runtime rt = Runtime.getRuntime();
        String os = System.getProperty("os.name").toLowerCase();

        boolean isLinux = os.contains("nix") || os.contains("nux");
        boolean isMac = os.contains("mac");
        boolean isWindows = os.contains("win");

        // Linux and Mac requires HTTPS prefix
        String url = formatLink(meetingUrl);
        if (isLinux) {
            launchUrlOnLinux(rt, url);
        } else if (isMac) {
            launchUrlOnMac(rt, url);
        } else if (isWindows) {
            launchUrlOnWindows(rt, url);
        }
    }

    //@@author Roycius
    /**
     * Serializes the lesson data into the correct format for storage file.
     *
     * @return serialized lesson data
     */
    public String serialize() {
        return title + " | " + dayOfTheWeek + " | " + startTime + " | " + endTime + " | " + meetingUrl;
    }

    /**
     * Deserializes the lesson data from the storage file.
     *
     * @param line a line of string representing the serialized data
     * @return deserialized lesson data
     */
    public static Lesson deserialize(Ui ui, String line) {
        try {
            String[] params = line.split("\\s*[|]\\s*");
            String title = parseTitle(params[0]);
            String dayOfTheWeek = parseDayOfTheWeek(params[1]);
            String startTime = parseTime(params[2]);
            String endTime = parseTime(params[3]);
            String meetingUrl = params[4];
            return new Lesson(title, dayOfTheWeek, startTime, endTime, meetingUrl);
        } catch (ParseException e) {
            // Ignoring the particular line
            ui.printMessage(Message.ERROR_DESERIALIZING_LESSON);
            return null;
        }
    }

    @Override
    public String toString() {
        return title + System.lineSeparator()
                + Ui.PADDING + "   " + dayOfTheWeek + ", " + startTime + " - " + endTime + System.lineSeparator()
                + Ui.PADDING + "   " + "Meeting URL: " + meetingUrl;
    }
}
