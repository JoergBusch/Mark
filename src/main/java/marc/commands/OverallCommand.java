package marc.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Message;
import marc.queries.Match;
import reactor.core.publisher.Mono;

public class OverallCommand implements SlashCommand {
	
	@Override
	public String getName() {
		return "overall";
	}

	@Override
	public Mono<Message> handle(ChatInputInteractionEvent event) {
		/*
        Since slash command options are optional according to discord, we will wrap it into the following function
        that gets the value of our option as a String without chaining several .get() on all the optional values
        In this case, there is no fear it will return empty/null as this is marked "required: true" in our json.
		 */
		String name = event.getOption("name")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.get(); //This is warning us that we didn't check if its present, we can ignore this on required options
		
		//Reply to the slash command, with the name the user supplied
		return event.deferReply().then(Mono.defer(() -> reply(event, name)));
	}

	public Mono<Message> reply(ChatInputInteractionEvent event, String name) {
		Match std = new Match();
		String msg = std.getTotal(name);
		return event.editReply(msg);
	}
}

