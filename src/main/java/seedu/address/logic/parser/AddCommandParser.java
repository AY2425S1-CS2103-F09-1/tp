package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NICKNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.StudentStatus;
import seedu.address.model.contact.TelegramHandle;
import seedu.address.model.tag.Nickname;
import seedu.address.model.tag.Role;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TELEGRAM_HANDLE,
                        PREFIX_EMAIL, PREFIX_STUDENT_STATUS,
                        PREFIX_ROLE, PREFIX_NICKNAME);

        List<Prefix> compulsoryPrefixes = List.of(PREFIX_NAME, PREFIX_STUDENT_STATUS,
                PREFIX_TELEGRAM_HANDLE, PREFIX_EMAIL, PREFIX_ROLE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, "Missing fields and/or prefixes. Ensure there are no "
                            + "non-whitespace characters before the prefix as well. Command usage:\n" + AddCommand.MESSAGE_USAGE));
        }

        if (!arePrefixesPresent(argMultimap, compulsoryPrefixes)) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, stringifyAllAbsentPrefix(argMultimap,
                            compulsoryPrefixes) + " prefix(es) is/are missing. Command usage:\n" + AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_TELEGRAM_HANDLE, PREFIX_EMAIL, PREFIX_STUDENT_STATUS, PREFIX_NICKNAME);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        TelegramHandle telegramHandle = ParserUtil.parseTelegramHandle(
                argMultimap.getValue(PREFIX_TELEGRAM_HANDLE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        StudentStatus studentStatus = ParserUtil.parseStudentStatus(argMultimap.getValue(PREFIX_STUDENT_STATUS).get());
        Set<Role> roleList = ParserUtil.parseRoles(argMultimap.getAllValues(PREFIX_ROLE));
        Nickname nickname = ParserUtil.parseNickname(argMultimap.getValue(PREFIX_NICKNAME).orElse(" "));

        Contact contact = new Contact(name, telegramHandle, email, studentStatus, roleList, nickname);

        return new AddCommand(contact);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, List<Prefix> prefixes) {
        return prefixes.stream().allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static String stringifyAllAbsentPrefix(ArgumentMultimap argumentMultimap,
                                                   List<Prefix> prefixes) {
        return prefixes.stream().filter(prefix -> !argumentMultimap.getValue(prefix).isPresent())
                .map(prefix -> prefix.getPrefix())
                .reduce("", (allPrefixes, prefixString) -> allPrefixes + prefixString + "  ")
                .trim();
    }

}
