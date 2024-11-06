package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String preamble = argMultimap.getPreamble().trim();
            if (!preamble.isEmpty()) {
                throw new ParseException(DeleteCommand.MESSAGE_DELETE_MULTIPLE_WAYS_FORBIDDEN);
            }
            String str = argMultimap.getValue(PREFIX_NAME).get();

            try {
                Name name = ParserUtil.parseName(str);
                return new DeleteCommand(name);
            } catch (Exception ex) {
                throw ex; // why it became successfull
                // did not use the 2 parameter exception
            }
        }

        assert argMultimap.getValue(PREFIX_NAME).isEmpty();

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(DeleteCommand.MESSAGE_MISSING_INDEX_OR_FULL_NAME);
        }

        try { // afford to SLAP better
            Index index = ParserUtil.parseIndex(trimmedArgs);
            return new DeleteCommand(index);
        } catch (Exception exp) {
            if (isInteger(trimmedArgs)) {
                throw new ParseException(exp.getMessage()); // no invalid command format
                // -1, 0
            }
            return createDeleteCommandByName(trimmedArgs); // no invalid command format
        }
    }

    private DeleteCommand createDeleteCommandByName(String args) throws ParseException {
        try {
            Name name = ParserUtil.parseName(args);
            return new DeleteCommand(name);
        } catch (Exception exp) { // to try
            throw new ParseException(exp.getMessage());
            // considered invalid name if it isn't an Integer
        }
    }

    private boolean isInteger(String args) {
        try {
            Integer.parseInt(args); // can pass negative integers as well
            return true;
        } catch (Exception exp) {
            return false;
        }
    }
}
