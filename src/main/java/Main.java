import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NTE3NDQyMTk2MDE0NjI4ODY4.DuCRiQ.dTVEXQLPAkKxwe974O-7eEDQng8";
        builder.setToken(token);
        builder.addEventListener(new Main());
        builder.buildAsync();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot())return;

        System.out.println("We received a message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        if(event.getMessage().getContentRaw().equals("!ping")){
            event.getChannel().sendMessage("Pong!").queue();
        }

        if(event.getMessage().getContentRaw().equals("!cat")){
            URL url = null;
            try {
                url = new URL("http://aws.random.cat/meow");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            StringBuffer content = new StringBuffer();
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader((con.getInputStream())));
                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject result = new JSONObject(content.toString());
            String response = result.getString("file");
            System.out.println(response);
            event.getChannel().sendMessage(response).queue();
        }

        if(event.getMessage().getContentRaw().startsWith("!decide ")){
            String req = event.getMessage().getContentRaw().substring(8);
            String[] choices = req.split("\\s*,\\s*");

            Random gen = new Random();
            int randomIndex = gen.nextInt(choices.length);
            event.getChannel().sendMessage(choices[randomIndex]).queue();

        }
    }
}
