package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NICKNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;

/**
 * Finds and lists all contacts in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_NO_CONTACTS_FOUND = "No contacts with the specified field found.\n"
            + "If you are finding by name and can't find what you are looking for, perhaps you can find by "
            + "nickname.";
    public static final String MESSAGE_FUNCTION = COMMAND_WORD + ": Finds all contacts whose details contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.";
    public static final String MESSAGE_COMMAND_FORMAT = COMMAND_WORD + " "
            + "[" + PREFIX_NAME + "NAME KEYWORDS] "
            + "[" + PREFIX_TELEGRAM_HANDLE + "TELEGRAM HANDLE KEYWORDS] "
            + "[" + PREFIX_EMAIL + "EMAIL KEYWORDS] "
            + "[" + PREFIX_STUDENT_STATUS + "STUDENT STATUS KEYWORDS] "
            + "[" + PREFIX_ROLE + "ROLE KEYWORDS]... "
            + "[" + PREFIX_NICKNAME + "NICKNAME KEYWORDS]"
            + "\nformat in short: `" + COMMAND_WORD + " [PREFIX] [new description]`";
    public static final String MESSAGE_COMMAND_EXAMPLE = "Example 1: " + COMMAND_WORD + " "
            + PREFIX_NAME + " Jane Doe" + "\n"
            + "Example 2: " + COMMAND_WORD + " "
            + PREFIX_NAME + "alice bob charlie "
            + PREFIX_ROLE + "President " + PREFIX_ROLE + "Admin";

    private final Predicate<Contact> predicate;

    public FindCommand(Predicate<Contact> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredContactList(predicate);
        if (model.getFilteredContactList().size() == 0) {
            return new CommandResult(MESSAGE_NO_CONTACTS_FOUND);
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_CONTACTS_LISTED_OVERVIEW, model.getFilteredContactList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
