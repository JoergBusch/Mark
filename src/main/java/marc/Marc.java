package marc;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import marc.listeners.SlashCommandListener;
import marc.utils.Secret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Marc {
    private static final Logger LOGGER = LoggerFactory.getLogger(Marc.class);

    public static void main(String[] args) {
    	
        //Creates the gateway client and connects to the gateway
        final GatewayDiscordClient client = DiscordClientBuilder.create(Secret.token).build()
            .login()
            .block();
  

        /* Call our code to handle creating/deleting/editing our global slash commands.
        We have to hard code our list of command files since iterating over a list of files in a resource directory
         is overly complicated for such a simple demo and requires handling for both IDE and .jar packaging.
         Using SpringBoot we can avoid all of this and use their resource pattern matcher to do this for us.
         */
        List<String> commands = List.of("compare.json", "overall.json", "recent.json", "lore.json");
        try {
            new GlobalCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            LOGGER.error("Error trying to register global slash commands", e);
        }

        //Register presence
        client.updatePresence(ClientPresence.online(ClientActivity.watching("in deine Seele \uD83D\uDC80"))).subscribe();
        
        //Register our slash command listener
        client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle)
        .then(client.onDisconnect())
        .block();

    
        // We use .block() as there is not another non-daemon thread and the jvm would close otherwise.
    }
}