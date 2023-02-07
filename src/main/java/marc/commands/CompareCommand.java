package marc.commands;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Message;
import marc.queries.Match;
import reactor.core.publisher.Mono;

public class CompareCommand implements SlashCommand {
	
	@Override
	public String getName() {
		return "compare";
	}

	@Override
	public Mono<Message> handle(ChatInputInteractionEvent event) {
		/*
        Since slash command options are optional according to discord, we will wrap it into the following function
        that gets the value of our option as a String without chaining several .get() on all the optional values
        In this case, there is no fear it will return empty/null as this is marked "required: true" in our json.
		 */
		String player1 = event.getOption("name1")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.get(); //This is warning us that we didn't check if its present, we can ignore this on required options
		String player2 = event.getOption("name2")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.get(); //This is warning us that we didn't check if its present, we can ignore this on required options
		String player3 = event.getOption("name3")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.orElse("");
		String player4 = event.getOption("name4")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.orElse("");
		String player5 = event.getOption("name5")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.orElse("");
		String player6 = event.getOption("name6")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asString)
				.orElse("");
		
		
		List<String> names = new LinkedList<String>();
		for(String temp: Lists.newArrayList(player1, player2, player3, player4, player5, player6)) {
			if(!temp.isBlank()) {
				names.add(temp);
			}
		}

		//Reply to the slash command, with the name the user supplied
		return event.deferReply().then(Mono.defer(() -> reply(event, names)));
	}

	public Mono<Message> reply(ChatInputInteractionEvent event, List<String> names) {
		Match std = new Match();
		String msg = std.sortByWinrate(names);
		return event.editReply(msg);
	}
}
