package marc.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Message;
import marc.queries.Lore;
import reactor.core.publisher.Mono;

public class LoreCommand implements SlashCommand {
	
	@Override
	public String getName() {
		return "lore";
	}

	@Override
	public Mono<Message> handle(ChatInputInteractionEvent event) {
		
		
		//Reply to the slash command, with the name the user supplied
		return event.deferReply().then(Mono.defer(() -> reply(event)));
	}

	public Mono<Message> reply(ChatInputInteractionEvent event) {
		Lore lore = new Lore();
		String msg = lore.run().toString();
		return event.editReply(msg);
	}
}
